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

package org.panda_lang.panda.framework.design.interpreter.parser;

import org.panda_lang.panda.framework.design.architecture.dynamic.ScopeFrame;
import org.panda_lang.panda.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.panda.framework.design.architecture.module.ModulePath;
import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.linker.ScopeLinker;
import org.panda_lang.panda.framework.design.runtime.ExecutableBranch;
import org.panda_lang.panda.framework.language.architecture.module.PandaModuleLoader;
import org.panda_lang.panda.framework.language.architecture.module.PandaModulePath;
import org.panda_lang.panda.framework.language.architecture.statement.AbstractScope;
import org.panda_lang.panda.framework.language.architecture.value.PandaVariable;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserData;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionSubparsersLoader;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.PandaExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.parser.linker.PandaScopeLinker;
import org.panda_lang.panda.framework.language.resource.PandaTypes;
import org.panda_lang.panda.framework.language.resource.parsers.expression.PandaExpressionUtils;

public class PandaParserDataUtils {

    /**
     * Create the fake parser data, which contains:
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
    public static ParserData createFakeData() throws Exception {
        ParserData data = new PandaParserData();

        Scope scope = new AbstractScope() {
            @Override
            public ScopeFrame createInstance(ExecutableBranch branch) {
                return null;
            }
        };

        ScopeLinker linker = new PandaScopeLinker(scope);
        data.setComponent(UniversalComponents.SCOPE_LINKER, linker);

        ModulePath path = new PandaModulePath();
        ModuleLoader loader = new PandaModuleLoader(new PandaTypes().fill(path));

        loader.include(path.getDefaultModule());
        data.setComponent(UniversalComponents.MODULE_LOADER, loader);

        scope.addVariable(new PandaVariable(PandaTypes.STRING.getReference(), "variable"));
        scope.addVariable(new PandaVariable(PandaTypes.STRING.toArray(), "array"));
        scope.addVariable(new PandaVariable(PandaTypes.INT.getReference(), "i"));

        ExpressionSubparsersLoader subparsersLoader = new ExpressionSubparsersLoader();
        data.setComponent(UniversalComponents.EXPRESSION, new PandaExpressionParser(PandaExpressionUtils.collectSubparsers()));

        return data;
    }

}
