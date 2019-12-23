/*
 * Copyright (c) 2015-2019 Dzikoysk
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
import org.panda_lang.framework.FrameworkController;
import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.prototype.DynamicClass;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.prototype.State;
import org.panda_lang.framework.design.architecture.prototype.Visibility;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.runtime.Process;
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.framework.language.architecture.prototype.PandaDynamicClass;
import org.panda_lang.framework.language.architecture.prototype.PandaMethod;
import org.panda_lang.framework.language.architecture.prototype.PandaPrototype;
import org.panda_lang.framework.language.architecture.prototype.PandaReference;
import org.panda_lang.framework.language.architecture.prototype.PrototypeComponents;
import org.panda_lang.framework.language.architecture.prototype.PrototypeScope;
import org.panda_lang.framework.language.architecture.statement.PandaVariableData;
import org.panda_lang.framework.language.interpreter.source.PandaClassSource;
import org.panda_lang.framework.language.interpreter.token.PandaSourceLocationUtils;
import org.panda_lang.framework.language.runtime.PandaProcess;
import org.panda_lang.panda.language.interpreter.parser.PandaContextUtils;
import org.panda_lang.utilities.commons.function.ThrowingFunction;

import java.util.Collections;
import java.util.function.Supplier;

/**
 * REPL creator
 */
public final class ReplCreator {

    protected final Context context;
    protected final PrototypeScope prototypeScope;
    protected final ReplScope replScope;
    protected ReplExceptionListener exceptionListener;
    protected Supplier<Process> processSupplier;
    protected ThrowingFunction<ProcessStack, Object, Exception> instanceSupplier;

    ReplCreator(FrameworkController frameworkController) {
        this.context = PandaContextUtils.createStubContext(frameworkController);

        Module module = context.getComponent(Components.SCRIPT).getModule();
        DynamicClass shellType = new PandaDynamicClass(module, "ShellPrototype", ShellPrototype.class);

        Prototype prototype = new PandaReference(shellType.getSimpleName(), shellType, ref -> PandaPrototype.builder()
                .name(ref.getName())
                .reference(ref)
                .module(module)
                .associated(ref.getAssociatedClass())
                .location(new PandaClassSource(ReplCreator.class).toLocation())
                .state(State.FINAL)
                .type("class")
                .visibility(Visibility.PUBLIC)
                .build()
        ).fetch();

        context.withComponent(PrototypeComponents.PROTOTYPE, prototype);
        this.prototypeScope = new PrototypeScope(PandaSourceLocationUtils.unknownLocation("repl"), prototype);

        this.replScope = new ReplScope(prototypeScope.getSourceLocation(), Collections.emptyList());
        context.withComponent(Components.SCOPE, replScope);
    }

    /**
     * Create REPL
     *
     * @return a REPL instance generated by the creator
     * @throws Exception if something happen
     */
    public Repl create() throws Exception {
        this.processSupplier = () -> new PandaProcess(context.getComponent(Components.APPLICATION), replScope);
        this.instanceSupplier = stack -> prototypeScope.revive(stack, null);

        return new Repl(this);
    }

    /**
     * Define a method in the repl prototype
     *
     * @param method the method to register
     * @return the REPL creator instance
     */
    public ReplCreator define(PandaMethod method) {
        prototypeScope.getPrototype().getMethods().declare(method);
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
     * Set custom exception handler, by default exceptions are passed through the {@link org.panda_lang.framework.design.interpreter.messenger.Messenger}
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
