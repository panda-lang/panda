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

package org.panda_lang.panda.framework.language.resource.expression;

import org.panda_lang.panda.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.panda.framework.design.architecture.module.ModulePath;
import org.panda_lang.panda.framework.design.architecture.statement.Frame;
import org.panda_lang.panda.framework.design.architecture.statement.VariableData;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.component.Component;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.language.architecture.module.PandaModuleLoader;
import org.panda_lang.panda.framework.language.architecture.module.PandaModulePath;
import org.panda_lang.panda.framework.language.architecture.statement.StaticFrame;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaContext;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.PandaExpressionParser;
import org.panda_lang.panda.framework.language.resource.PandaTypes;

import java.util.Map;
import java.util.function.Function;

public class PandaParserDataUtils {

    public static Context createFakeData(Function<Context, Map<VariableData, Object>> variablesSupplier) {
        return new Context() {
            @java.lang.Override
            public Context fork() {
                return null;
            }

            @java.lang.Override
            public <T> Context withComponent(Component<T> componentName, T component) {
                return null;
            }

            @java.lang.Override
            public <T> T getComponent(Component<T> componentName) {
                return null;
            }

            @java.lang.Override
            public Map<? extends Component<?>, ? extends Object> getComponents() {
                return null;
            }
        };
    }

}
