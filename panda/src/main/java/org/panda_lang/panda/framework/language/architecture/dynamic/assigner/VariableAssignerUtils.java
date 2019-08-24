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

package org.panda_lang.panda.framework.language.architecture.dynamic.assigner;

import org.panda_lang.panda.framework.design.architecture.statement.Variable;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.dynamic.accessor.VariableAccessor;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;

public class VariableAssignerUtils {

    public static Assigner<Variable> of(Context context, Variable variable, Expression expression) {
        if (!variable.getType().isAssignableFrom(expression.getReturnType())) {
            String message = "Cannot assign " + expression.getReturnType().getName() + " to " + variable.getType().getName() + " variable";

            throw PandaParserFailure.builder(message, context)
                    .withSourceFragment()
                        .ofOriginals(context)
                        .create()
                    .withNote("Change variable type or ensure the expression has compatible return type")
                    .build();
        }

        return new VariableAccessor(variable).toAssigner(expression);
    }

}
