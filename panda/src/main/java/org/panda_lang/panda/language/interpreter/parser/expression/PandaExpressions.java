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

package org.panda_lang.panda.language.interpreter.parser.expression;

import org.panda_lang.framework.design.interpreter.parser.Parser;
import org.panda_lang.framework.design.interpreter.parser.Parsers;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.panda.language.interpreter.parser.expression.subparsers.ArrayValueExpressionSubparser;
import org.panda_lang.panda.language.interpreter.parser.expression.subparsers.CastExpressionSubparser;
import org.panda_lang.panda.language.interpreter.parser.expression.subparsers.ConstructorExpressionSubparser;
import org.panda_lang.panda.language.interpreter.parser.expression.subparsers.CreaseExpressionSubparser;
import org.panda_lang.panda.language.interpreter.parser.expression.subparsers.IsExpressionSubparser;
import org.panda_lang.panda.language.interpreter.parser.expression.subparsers.LiteralExpressionSubparser;
import org.panda_lang.panda.language.interpreter.parser.expression.subparsers.MethodExpressionSubparser;
import org.panda_lang.panda.language.interpreter.parser.expression.subparsers.NegateExpressionSubparser;
import org.panda_lang.panda.language.interpreter.parser.expression.subparsers.NumberExpressionSubparser;
import org.panda_lang.panda.language.interpreter.parser.expression.subparsers.OperationExpressionSubparser;
import org.panda_lang.panda.language.interpreter.parser.expression.subparsers.SectionExpressionSubparser;
import org.panda_lang.panda.language.interpreter.parser.expression.subparsers.SequenceExpressionSubparser;
import org.panda_lang.panda.language.interpreter.parser.expression.subparsers.StaticExpressionSubparser;
import org.panda_lang.panda.language.interpreter.parser.expression.subparsers.VariableExpressionSubparser;
import org.panda_lang.panda.language.interpreter.parser.expression.subparsers.assignation.AssignationExpressionSubparser;

public final class PandaExpressions extends Parsers {

    public static final Class<? extends ExpressionSubparser>[] SUBPARSERS = of(
            ArrayValueExpressionSubparser.class,
            AssignationExpressionSubparser.class,
            CastExpressionSubparser.class,
            ConstructorExpressionSubparser.class,
            CreaseExpressionSubparser.class,
            IsExpressionSubparser.class,
            LiteralExpressionSubparser.class,
            MethodExpressionSubparser.class,
            NegateExpressionSubparser.class,
            NumberExpressionSubparser.class,
            OperationExpressionSubparser.class,
            SectionExpressionSubparser.class,
            SequenceExpressionSubparser.class,
            StaticExpressionSubparser.class,
            VariableExpressionSubparser.class
    );

    @Override
    public Class<? extends Parser>[] getParsers() {
        return SUBPARSERS;
    }

}
