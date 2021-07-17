/*
 * Copyright (c) 2021 dzikoysk
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

package panda.interpreter.syntax.expressions.subparsers.operation;

import panda.interpreter.token.Token;
import panda.interpreter.resource.syntax.operator.OperatorFamilies;
import panda.interpreter.resource.syntax.operator.Operators;
import panda.interpreter.resource.syntax.separator.Separators;
import panda.interpreter.syntax.expressions.subparsers.operation.pattern.OperationPattern;
import panda.utilities.ArrayUtils;

final class OperationExpressionUtils {

    static final Token[] OPERATORS = ArrayUtils.merge(
            Operators.getFamily(OperatorFamilies.MATH),
            Operators.getFamily(OperatorFamilies.LOGICAL)
    );

    static final OperationPattern OPERATION_PATTERN = new OperationPattern(Separators.getOpeningSeparators(), OPERATORS);

    private OperationExpressionUtils() { }

}
