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

import org.junit.jupiter.api.Test;
import org.panda_lang.language.architecture.module.ModuleLoaderUtils;
import org.panda_lang.language.architecture.statement.PandaVariableData;
import org.panda_lang.language.architecture.type.member.method.PandaMethod;
import org.panda_lang.language.architecture.type.member.parameter.PropertyParameterImpl;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.source.PandaClassSource;
import org.panda_lang.panda.Panda;
import org.panda_lang.panda.util.PandaUtils;

class ReplTest {

    @Test
    void test() throws Exception {
        Panda panda = PandaUtils.defaultInstance();
        ReplConsole console = new ReplConsole(panda, System.in, true);
        ReplCreator creator = Repl.creator(console);
        Context context = creator.getContext();

        Repl repl = creator
                .variable(new PandaVariableData(ModuleLoaderUtils.requireType(context, int.class), "i"), 5)
                .define(PandaMethod.builder()
                        .name("sqrt")
                        .parameters(new PropertyParameterImpl(0, ModuleLoaderUtils.requireType(context, Double.class), "i"))
                        .customBody((method, stack, instance, arguments) -> Math.sqrt(((Number) arguments[0]).doubleValue()))
                        .returnType(ModuleLoaderUtils.requireType(context, Double.class))
                        .location(new PandaClassSource(ReplTest.class).toLocation())
                        .build())
                .addVariableChangeListener((variable, previous, current) -> {
                    panda.getLogger().info("// variable change :: " + variable.getName() + " = " + previous + " -> " + current);
                })
                .create();

        ReplUtils.print(panda, repl.evaluate("i"));
        ReplUtils.print(panda, repl.evaluate("i = 4"));

        repl.regenerate();

        ReplUtils.print(panda, repl.evaluate("i"));
        ReplUtils.print(panda, repl.evaluate("String text = 'hello'; 'second expression'"));

        ReplUtils.print(panda, repl.evaluate("vars"));
        ReplUtils.print(panda, repl.evaluate("history"));

        ReplUtils.print(panda, repl.evaluate("Double double = 9.86960440109"));
        ReplUtils.print(panda, repl.evaluate("sqrt(double)"));
    }

}