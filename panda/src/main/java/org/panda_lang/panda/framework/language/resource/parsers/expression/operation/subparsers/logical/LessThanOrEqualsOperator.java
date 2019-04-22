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

package org.panda_lang.panda.framework.language.resource.parsers.expression.operation.subparsers.logical;

import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.resource.parsers.expression.operation.rpn.RPNOperationAction;

public class LessThanOrEqualsOperator extends ComparisonOperator {

    @Override
    public RPNOperationAction of(int compared) {
        return new ComparisonOperatorAction() {
            @Override
            protected Object get(Number a, Number b) {
                switch (compared) {
                    case BYTE:
                        return a.byteValue() <= b.byteValue();
                    case SHORT:
                        return a.shortValue() <= b.shortValue();
                    case INT:
                        return a.intValue() <= b.intValue();
                    case LONG:
                        return a.longValue() < b.longValue();
                    case FLOAT:
                        return a.floatValue() <= b.floatValue();
                    case DOUBLE:
                        return a.doubleValue() <= b.doubleValue();
                    default:
                        throw new PandaParserException("Unknown type " + compared);
                }
            }
        };
    }

}
