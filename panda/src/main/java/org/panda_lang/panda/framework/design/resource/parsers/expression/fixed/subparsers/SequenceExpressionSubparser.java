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

package org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.subparsers;

import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionSubparser;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionSubparserWorker;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionUtils;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.resource.PandaTypes;

public class SequenceExpressionSubparser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createSubparser() {
        return new LiteralWorker();
    }

    static class LiteralWorker implements ExpressionSubparserWorker {

        private TokenRepresentation token;

        @Override
        public boolean next(ParserData data, TokenRepresentation representation) {
            if (token != null) {
                return false;
            }

            if (representation.getTokenName().equals("String")) {
                token = representation;
            }

            return token != null;
        }

        @Override
        public Expression parse(ParserData data) {
            return ExpressionUtils.toExpression(PandaTypes.STRING, token.getTokenValue());
        }

    }

}
