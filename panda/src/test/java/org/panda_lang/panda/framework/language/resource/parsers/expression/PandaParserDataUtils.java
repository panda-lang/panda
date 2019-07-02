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

package org.panda_lang.panda.framework.language.resource.parsers.expression;

import org.panda_lang.panda.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.panda.framework.design.architecture.module.ModulePath;
import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.architecture.value.Variable;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.linker.ScopeLinker;
import org.panda_lang.panda.framework.language.architecture.module.PandaModuleLoader;
import org.panda_lang.panda.framework.language.architecture.module.PandaModulePath;
import org.panda_lang.panda.framework.language.architecture.statement.StaticScope;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaContext;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.PandaExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.parser.linker.PandaScopeLinker;
import org.panda_lang.panda.framework.language.resource.PandaTypes;

import java.util.Map;

public class PandaParserDataUtils {

    /**
     * Create the fake parser context, which contains:
     * - expression parser
     * - variables:
     * > string variable
     * > string[] array
     * > int i
     * - scope linker
     * - abstract scope
     * - module path & loader
     *
     * @return the fake data
     */
    public static Context createFakeData(Map<Variable, Object> variables) {
        Context context = new PandaContext();
        context.withComponent(UniversalComponents.EXPRESSION, new PandaExpressionParser(PandaExpressionUtils.collectSubparsers()));

        ModulePath path = new PandaModulePath();
        ModuleLoader loader = new PandaModuleLoader(new PandaTypes().fill(path));
        loader.load(path.getDefaultModule());
        context.withComponent(UniversalComponents.MODULE_LOADER, loader);

        Scope scope = new StaticScope(variables);
        ScopeLinker linker = new PandaScopeLinker(scope);
        context.withComponent(UniversalComponents.LINKER, linker);

        return context;
    }

}
