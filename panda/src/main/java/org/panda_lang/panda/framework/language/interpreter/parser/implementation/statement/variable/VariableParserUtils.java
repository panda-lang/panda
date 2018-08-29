/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.framework.language.interpreter.parser.implementation.statement.variable;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.architecture.value.Variable;

public class VariableParserUtils {

    public static @Nullable Variable getVariable(Scope scope, String variableName) {
        for (Variable var : scope.getVariables()) {
            if (var.getName().equals(variableName)) {
                return var;
            }
        }

        return null;
    }

    public static boolean checkDuplicates(Scope scope, Variable variable) {
        for (Variable var : scope.getVariables()) {
            String variableName = var.getName();
            int nestingLevel = var.getNestingLevel();

            if (variableName.equals(variable.getName()) && nestingLevel <= variable.getNestingLevel()) {
                return true;
            }
        }

        return false;
    }

    public static int indexOf(Scope scope, Variable variable) {
        return scope.getVariables().indexOf(variable);
    }

}
