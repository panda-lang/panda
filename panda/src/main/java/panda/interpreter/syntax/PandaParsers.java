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

package panda.interpreter.syntax;

import panda.interpreter.parser.ContextParser;
import panda.interpreter.syntax.head.CommentParser;
import panda.interpreter.syntax.head.ExportParser;
import panda.interpreter.syntax.head.ImportParser;
import panda.interpreter.syntax.head.MainParser;
import panda.interpreter.syntax.head.RequireParser;
import panda.interpreter.syntax.scope.LateDeclarationParser;
import panda.interpreter.syntax.scope.LogParser;
import panda.interpreter.syntax.scope.StandaloneExpressionParser;
import panda.interpreter.syntax.scope.block.TryCatchParser;
import panda.interpreter.syntax.scope.block.conditional.ElseIfParser;
import panda.interpreter.syntax.scope.block.conditional.ElseParser;
import panda.interpreter.syntax.scope.block.conditional.IfParser;
import panda.interpreter.syntax.scope.block.looping.ForEachParser;
import panda.interpreter.syntax.scope.block.looping.ForParser;
import panda.interpreter.syntax.scope.block.looping.LoopParser;
import panda.interpreter.syntax.scope.block.looping.WhileParser;
import panda.interpreter.syntax.scope.branching.BreakParser;
import panda.interpreter.syntax.scope.branching.ContinueParser;
import panda.interpreter.syntax.scope.branching.ReturnParser;
import panda.interpreter.syntax.scope.branching.ThrowParser;
import panda.interpreter.syntax.type.BaseCallParser;
import panda.interpreter.syntax.type.ConstructorParser;
import panda.interpreter.syntax.type.FieldParser;
import panda.interpreter.syntax.type.MethodParser;
import panda.interpreter.syntax.type.SelfConstructorParser;
import panda.interpreter.syntax.type.TypeParser;

public final class PandaParsers {

    public static final ContextParser<?, ?>[] PARSERS = {
            new CommentParser(),

            new ExportParser(),
            new ImportParser(),
            new RequireParser(),
            new MainParser(),

            new BaseCallParser(),
            new ConstructorParser(),
            new SelfConstructorParser(),
            new FieldParser(),
            new MethodParser(),
            new TypeParser(),

            new LateDeclarationParser(),
            new LogParser(),
            new StandaloneExpressionParser(),

            new TryCatchParser(),

            new ElseIfParser(),
            new ElseParser(),
            new IfParser(),

            new ForEachParser(),
            new ForParser(),
            new LoopParser(),
            new WhileParser(),

            new BreakParser(),
            new ContinueParser(),
            new ReturnParser(),
            new ThrowParser()
    };

    private PandaParsers() { }

}
