/*
 * Copyright (c) 2021 dzikoysk
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

package panda.repl;

import org.jetbrains.annotations.Nullable;
import panda.interpreter.architecture.module.Module;
import panda.interpreter.architecture.module.ModuleSource;
import panda.interpreter.architecture.module.PandaModule;
import panda.interpreter.architecture.packages.Package;
import panda.interpreter.architecture.statement.PandaVariableData;
import panda.interpreter.architecture.type.Kind;
import panda.interpreter.architecture.type.PandaType;
import panda.interpreter.architecture.type.Reference;
import panda.interpreter.architecture.type.State;
import panda.interpreter.architecture.type.Type;
import panda.interpreter.architecture.type.TypeContext;
import panda.interpreter.architecture.type.TypeScope;
import panda.interpreter.architecture.type.Visibility;
import panda.interpreter.architecture.type.generator.ClassGenerator;
import panda.interpreter.architecture.type.member.constructor.PandaConstructor;
import panda.interpreter.architecture.type.member.method.PandaMethod;
import panda.interpreter.architecture.type.signature.Relation;
import panda.interpreter.architecture.type.signature.Signature;
import panda.interpreter.architecture.type.signature.TypedSignature;
import panda.interpreter.parser.Context;
import panda.interpreter.source.ClassSource;
import panda.interpreter.token.PandaLocation;
import panda.interpreter.token.PandaSnippet;
import panda.interpreter.runtime.PandaProcess;
import panda.interpreter.runtime.Process;
import panda.interpreter.runtime.ProcessStack;
import panda.interpreter.PandaException;
import panda.interpreter.utils.PandaContextUtils;
import panda.std.Completable;
import panda.std.function.ThrowingFunction;

import java.io.File;
import java.util.Collections;
import java.util.function.Supplier;

/**
 * REPL creator
 */
public final class ReplCreator {

    protected final ReplConsole console;
    protected final Context<?> context;
    protected final Module module;
    protected final TypeScope typeScope;
    protected final ReplScope replScope;
    protected ReplExceptionListener exceptionListener;
    protected Supplier<Process> processSupplier;
    protected ThrowingFunction<ProcessStack, Object, Exception> instanceSupplier;

    ReplCreator(ReplConsole console) {
        this.console = console;

        Completable<Type> futureType = new Completable<>();
        Context<?> context = PandaContextUtils.createStubContext(console.getFrameworkController()).toContext();

        Package replPackage = new Package("repl", "repl", "1.0.0", new File("repl"));
        context.getEnvironment().getPackages().registerPackage(replPackage);

        this.module = new PandaModule(replPackage, Package.DEFAULT_MODULE);
        replPackage.addModule(new ModuleSource(module, Collections.emptyList()));
        replPackage.forMainModule(context.getEnvironment().getSources());

        Reference reference = new Reference(futureType, module, "ShellType", Visibility.OPEN, Kind.TYPE, new ClassSource(module, ReplCreator.class).toLocation());
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

        this.typeScope = new TypeScope(PandaLocation.unknownLocation(reference.getModule(), "repl"), reference);

        type.getConstructors().declare(PandaConstructor.builder()
                .type(type)
                .invoker((typeConstructor, stack, instance, arguments) -> typeScope.revive(stack))
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

        this.context = context.forkCreator()
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
        // Type type = typeScope.getReference().fetchType();
        this.instanceSupplier = typeScope::revive;

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
