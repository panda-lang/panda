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

package org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks.operation;

import org.panda_lang.panda.framework.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.pattern.vague.VagueResult;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.runtime.expression.ExpressionCallback;

public class NewOperationParser implements Parser {

    public ExpressionCallback parse(ParserData data, Tokens source) {
        return parse(data, OperationExpressionUtils.OPERATION_EXTRACTOR.extract(source));
    }

    public ExpressionCallback parse(ParserData data, VagueResult source) {
        Operation operation = Operation.of(data.getComponent(PandaComponents.EXPRESSION), data, source);

        if (OperationUtils.isNumeric(operation)) {
            return null; // math parser
        }

        return null; // other like concatenation etc.
    }

}