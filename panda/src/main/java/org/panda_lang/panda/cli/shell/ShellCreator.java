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

package org.panda_lang.panda.cli.shell;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.prototype.Visibility;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.runtime.Process;
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.framework.language.architecture.prototype.PandaMethod;
import org.panda_lang.framework.language.architecture.prototype.PandaPrototype;
import org.panda_lang.framework.language.architecture.prototype.PrototypeComponents;
import org.panda_lang.framework.language.architecture.prototype.PrototypeScope;
import org.panda_lang.framework.language.architecture.statement.PandaVariableData;
import org.panda_lang.framework.language.interpreter.token.PandaSourceLocationUtils;
import org.panda_lang.framework.language.runtime.PandaProcess;
import org.panda_lang.panda.Panda;
import org.panda_lang.panda.language.interpreter.parser.PandaContextUtils;
import org.panda_lang.utilities.commons.function.ThrowingFunction;

import java.util.Collections;
import java.util.function.Supplier;

public final class ShellCreator {

    private final Panda panda;
    private final Context context;
    private final PrototypeScope prototypeScope;
    private final ShellScope shellScope;

    ShellCreator(Panda panda) {
        this.panda = panda;
        this.context = PandaContextUtils.createStubContext(panda);

        Prototype prototype = PandaPrototype.builder()
                .module(context.getComponent(Components.SCRIPT).getModule())
                .name("ShellPrototype")
                .visibility(Visibility.PUBLIC)
                .build();
        context.withComponent(PrototypeComponents.PROTOTYPE, prototype);
        this.prototypeScope = new PrototypeScope(PandaSourceLocationUtils.unknownLocation("shell"), prototype);

        this.shellScope = new ShellScope(prototypeScope.getSourceLocation(), Collections.emptyList());
        context.withComponent(Components.SCOPE, shellScope);
    }

    public ShellCreator define(PandaMethod method) {
        prototypeScope.getPrototype().getMethods().declare(method);
        return this;
    }

    public ShellCreator variable(PandaVariableData variableData, @Nullable Object defaultValue) {
        shellScope.setDefaultValue(shellScope.createVariable(variableData), defaultValue);
        return this;
    }

    public Shell create() throws Exception {
        Supplier<Process> processSupplier = () -> new PandaProcess(context.getComponent(Components.APPLICATION), shellScope);
        ThrowingFunction<ProcessStack, Object, Exception> instanceSupplier = stack -> prototypeScope.revive(stack, null);

        return new Shell(context, context.getComponent(Components.EXPRESSION), processSupplier, instanceSupplier);
    }

}