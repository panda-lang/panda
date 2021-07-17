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

package panda.interpreter.syntax.expressions;

import panda.interpreter.parser.expression.ExpressionSubparser;
import panda.interpreter.parser.expression.ExpressionSubparsers;
import panda.interpreter.syntax.expressions.subparsers.CastParser;
import panda.interpreter.syntax.expressions.subparsers.InstanceCreationParser;
import panda.interpreter.syntax.expressions.subparsers.CreaseParser;
import panda.interpreter.syntax.expressions.subparsers.DeclarationParser;
import panda.interpreter.syntax.expressions.subparsers.IsParser;
import panda.interpreter.syntax.expressions.subparsers.LiteralParser;
import panda.interpreter.syntax.expressions.subparsers.MethodCallParser;
import panda.interpreter.syntax.expressions.subparsers.NegateParser;
import panda.interpreter.syntax.expressions.subparsers.number.NegativeParser;
import panda.interpreter.syntax.expressions.subparsers.number.NotBitwiseParser;
import panda.interpreter.syntax.expressions.subparsers.number.NumberParser;
import panda.interpreter.syntax.expressions.subparsers.operation.OperationExpressionParser;
import panda.interpreter.syntax.expressions.subparsers.SectionParser;
import panda.interpreter.syntax.expressions.subparsers.SequenceParser;
import panda.interpreter.syntax.expressions.subparsers.StaticParser;
import panda.interpreter.syntax.expressions.subparsers.VariableParser;
import panda.interpreter.syntax.expressions.subparsers.AssignationPrarser;

import java.util.Arrays;
import java.util.Collection;

public final class PandaExpressions {

    public static Collection<ExpressionSubparser> createSubparsers() {
        return Arrays.asList(
                new AssignationPrarser(),
                new CastParser(),
                new InstanceCreationParser(),
                new CreaseParser(),
                new DeclarationParser(),
                new IsParser(),
                new LiteralParser(),
                new MethodCallParser(),
                new NegateParser(),
                new NegativeParser(),
                new NotBitwiseParser(),
                new NumberParser(),
                new OperationExpressionParser(),
                new SectionParser(),
                new SequenceParser(),
                new StaticParser(),
                new VariableParser()
        );
    }

    public static ExpressionSubparsers createExpressionSubparsers() {
        return new ExpressionSubparsers(createSubparsers());
    }

}
