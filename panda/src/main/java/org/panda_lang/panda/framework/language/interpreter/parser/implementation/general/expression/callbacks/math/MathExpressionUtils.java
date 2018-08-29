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

package org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.callbacks.math;

import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.language.interpreter.pattern.vague.VagueElement;
import org.panda_lang.panda.framework.language.interpreter.pattern.vague.VagueResult;

public class MathExpressionUtils {

    public static boolean isMathExpression(TokenizedSource source) {
        VagueResult result = MathParser.EXTRACTOR.extract(source);

        if (result.size() % 2 == 0) {
            return false;
        }

        int expression = 0;
        int operators = 0;

        for (VagueElement element : result.getElements()) {
            if (element.isExpression()) {
                expression++;
            }

            if (element.isOperator()) {
                operators++;
            }

            if (operators > 0 && expression > 1) {
                return true;
            }
        }

        return false;
    }

}
