/*
 * Copyright (c) 2020 Dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.panda_lang.panda.shell.repl;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.architecture.module.PandaModule;
import org.panda_lang.language.architecture.statement.PandaVariableData;
import org.panda_lang.language.architecture.type.Kind;
import org.panda_lang.language.architecture.type.PandaType;
import org.panda_lang.language.architecture.type.Reference;
import org.panda_lang.language.architecture.type.State;
import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.language.architecture.type.TypeContext;
import org.panda_lang.language.architecture.type.TypeScope;
import org.panda_lang.language.architecture.type.Visibility;
import org.panda_lang.language.architecture.type.generator.ClassGenerator;
import org.panda_lang.language.architecture.type.member.constructor.PandaConstructor;
import org.panda_lang.language.architecture.type.member.method.PandaMethod;
import org.panda_lang.language.architecture.type.signature.Relation;
import org.panda_lang.language.architecture.type.signature.Signature;
import org.panda_lang.language.architecture.type.signature.TypedSignature;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.source.PandaClassSource;
import org.panda_lang.language.interpreter.token.PandaLocation;
import org.panda_lang.language.interpreter.token.PandaSnippet;
import org.panda_lang.language.runtime.PandaProcess;
import org.panda_lang.language.runtime.Process;
import org.panda_lang.language.runtime.ProcessStack;
import org.panda_lang.panda.PandaException;
import org.panda_lang.panda.language.interpreter.parser.PandaContextUtils;
import org.panda_lang.utilities.commons.function.Completable;
import org.panda_lang.utilities.commons.function.ThrowingFunction;

import java.util.Collections;
import java.util.function.Supplier;

/**
 * REPL creator
 */
public final class ReplCreator {

    protected final ReplConsole console;
    protected final Context<?> context;
    protected final TypeScope typeScope;
    protected final ReplScope replScope;
    protected ReplExceptionListener exceptionListener;
    protected Supplier<Process> processSupplier;
    protected ThrowingFunction<ProcessStack, Object, Exception> instanceSupplier;

    ReplCreator(ReplConsole console) {
        this.console = console;

        Completable<Type> futureType = new Completable<>();
        Reference reference = new Reference(futureType, new PandaModule("repl"), "ShellType", Visibility.OPEN, Kind.TYPE, new PandaClassSource(ReplCreator.class).toLocation());
        Completable<Class<?>> associatedClass = new Completable<>();

        Type type = PandaType.builder()
                .name("ShellType")
                .signature(new TypedSignature(null, reference, new Signature[0], Relation.DIRECT, PandaSnippet.empty()))
                .module(reference.getModule())
                .location(reference.getLocation())
                .state(State.FINAL)
                .associatedType(associatedClass)
                .build();
        futureType.complete(type);

        ClassGenerator classGenerator = new ClassGenerator();
        classGenerator.allocate(type);

        this.typeScope = new TypeScope(PandaLocation.unknownLocation("repl"), reference);

        type.getConstructors().declare(PandaConstructor.builder()
                .type(type)
                .invoker((typeConstructor, frame, instance, arguments) -> typeScope.revive(frame, instance, typeConstructor, arguments))
                .location(type.getLocation())
                .returnType(type.getSignature())
                .build());

        try {
            classGenerator.generate(type);
            associatedClass.complete(classGenerator.complete(type));
        } catch (Exception exception) {
            throw new PandaException("Cannot generate shell type", exception);
        }

        this.replScope = new ReplScope(typeScope.getSourceLocation(), Collections.emptyList());

        this.context = PandaContextUtils.createStubContext(console.getFrameworkController())
                .withSubject(new TypeContext(type, typeScope))
                .withScope(replScope)
                .toContext();
    }

    /**
     * Create REPL
     *
     * @return a REPL instance generated by the creator
     * @throws Exception if something happen
     */
    public Repl create() throws Exception {
        this.processSupplier = () -> new PandaProcess(context.getApplication(), replScope);

        Type type = typeScope.getReference().fetchType();

        this.instanceSupplier = stack -> {
            return typeScope.revive(stack, typeScope, type.getConstructors().getConstructor(Collections.emptyList()).getOrNull(), new Object[0]);
        };

        return new Repl(this);
    }

    /**
     * Define a method in the repl type
     *
     * @param method the method to register
     * @return the REPL creator instance
     */
    public ReplCreator define(PandaMethod method) {
        typeScope.getReference().fetchType().getMethods().declare(method);
        return this;
    }

    /**
     * Define variable in the repl scope
     *
     * @param variableData the variable to register
     * @param defaultValue value of variable assigned by default
     * @return the REPL creator instance
     */
    public ReplCreator variable(PandaVariableData variableData, @Nullable Object defaultValue) {
        replScope.setDefaultValue(replScope.createVariable(variableData), defaultValue);
        return this;
    }

    /**
     * Register variable change listener
     *
     * @param variableChangeListener the listener to register
     * @return the REPL creator instance
     */
    public ReplCreator addVariableChangeListener(ReplVariableChangeListener variableChangeListener) {
        replScope.addVariableChangeListener(variableChangeListener);
        return this;
    }

    /**
     * Set custom exception handler
     *
     * @param exceptionListener a new exception listener
     * @return the REPL creator instance
     */
    public ReplCreator withCustomExceptionListener(@Nullable ReplExceptionListener exceptionListener) {
        this.exceptionListener = exceptionListener;
        return this;
    }

    public Context getContext() {
        return context;
    }

}
