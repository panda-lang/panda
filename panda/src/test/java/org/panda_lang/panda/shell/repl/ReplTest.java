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

import org.junit.jupiter.api.Test;
import org.panda_lang.framework.PandaFramework;
import org.panda_lang.framework.language.architecture.prototype.PandaPropertyParameter;
import org.panda_lang.framework.language.architecture.prototype.PandaMethod;
import org.panda_lang.framework.language.architecture.statement.PandaVariableData;
import org.panda_lang.framework.language.interpreter.source.PandaClassSource;
import org.panda_lang.framework.language.resource.internal.java.JavaModule;
import org.panda_lang.panda.Panda;
import org.panda_lang.panda.PandaFactory;

class ReplTest {

    @Test
    void test() throws Exception {
        PandaFactory pandaFactory = new PandaFactory();
        Panda panda = pandaFactory.createPanda();

        Repl repl = Repl.creator(panda)
                .define(PandaMethod.builder()
                        .name("sqrt")
                        .parameters(new PandaPropertyParameter(0, JavaModule.DOUBLE, "i"))
                        .methodBody((stack, instance, arguments) -> Math.sqrt(((Number) arguments[0]).doubleValue()))
                        .returnType(JavaModule.DOUBLE)
                        .location(new PandaClassSource(ReplTest.class).toLocation())
                        .build())
                .variable(new PandaVariableData(JavaModule.INT, "i"), 5)
                .addVariableChangeListener((variable, previous, current) -> {
                    PandaFramework.getLogger().debug("// variable change :: " + variable.getName() + " = " + previous + " -> " + current);
                })
                .create();

        ReplUtils.print(repl.evaluate("i"));
        ReplUtils.print(repl.evaluate("i = 4"));

        repl.regenerate();
        ReplUtils.print(repl.evaluate("i"));
        ReplUtils.print(repl.evaluate("String text = 'hello'; 'second expression'"));

        ReplUtils.print(repl.evaluate("!! vars"));
        ReplUtils.print(repl.evaluate("!! source"));

        ReplUtils.print(repl.evaluate("Double double = 9.86960440109"));
        ReplUtils.print(repl.evaluate("sqrt(double)"));
    }

}