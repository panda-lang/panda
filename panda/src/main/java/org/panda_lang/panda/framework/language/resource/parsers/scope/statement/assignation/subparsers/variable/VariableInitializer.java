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

package org.panda_lang.panda.framework.language.resource.parsers.scope.statement.assignation.subparsers.variable;

import org.panda_lang.panda.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.architecture.value.Variable;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.language.architecture.value.PandaVariable;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.utilities.commons.StringUtils;

public class VariableInitializer {

    public static final String DECLARATION_PARSER = "mutable:[mutable] nullable:[nullable] <type:reader type> <name:condition token {type:unknown}>";

    public Variable createVariable(ParserData data, ModuleLoader loader, Scope scope, boolean mutable, boolean nullable, String type, String name) {
        ClassPrototype prototype = loader.forClass(type).fetch();

        if (!StringUtils.isEmpty(type) && prototype == null) {
            throw new PandaParserFailure("Cannot recognize variable type: " + type, data);
        }

        Variable variable = new PandaVariable(prototype, name, 0, mutable, nullable);
        scope.addVariable(variable);

        return variable;
    }

}
