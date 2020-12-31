/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.panda.language.resource.syntax;

import org.panda_lang.language.interpreter.parser.ContextParser;
import org.panda_lang.panda.language.resource.syntax.head.CommentParser;
import org.panda_lang.panda.language.resource.syntax.head.ExportParser;
import org.panda_lang.panda.language.resource.syntax.head.ImportParser;
import org.panda_lang.panda.language.resource.syntax.head.MainParser;
import org.panda_lang.panda.language.resource.syntax.head.ModuleParser;
import org.panda_lang.panda.language.resource.syntax.head.RequireParser;
import org.panda_lang.panda.language.resource.syntax.scope.LateDeclarationParser;
import org.panda_lang.panda.language.resource.syntax.scope.LogParser;
import org.panda_lang.panda.language.resource.syntax.scope.StandaloneExpressionParser;
import org.panda_lang.panda.language.resource.syntax.scope.block.TryCatchParser;
import org.panda_lang.panda.language.resource.syntax.scope.block.conditional.ElseIfParser;
import org.panda_lang.panda.language.resource.syntax.scope.block.conditional.ElseParser;
import org.panda_lang.panda.language.resource.syntax.scope.block.conditional.IfParser;
import org.panda_lang.panda.language.resource.syntax.scope.block.looping.ForEachParser;
import org.panda_lang.panda.language.resource.syntax.scope.block.looping.ForParser;
import org.panda_lang.panda.language.resource.syntax.scope.block.looping.LoopParser;
import org.panda_lang.panda.language.resource.syntax.scope.block.looping.WhileParser;
import org.panda_lang.panda.language.resource.syntax.scope.branching.BreakParser;
import org.panda_lang.panda.language.resource.syntax.scope.branching.ContinueParser;
import org.panda_lang.panda.language.resource.syntax.scope.branching.ReturnParser;
import org.panda_lang.panda.language.resource.syntax.scope.branching.ThrowParser;
import org.panda_lang.panda.language.resource.syntax.type.BaseCallParser;
import org.panda_lang.panda.language.resource.syntax.type.ConstructorParser;
import org.panda_lang.panda.language.resource.syntax.type.FieldParser;
import org.panda_lang.panda.language.resource.syntax.type.MethodParser;
import org.panda_lang.panda.language.resource.syntax.type.SelfConstructorParser;
import org.panda_lang.panda.language.resource.syntax.type.TypeParser;

public final class PandaParsers {

    public static final ContextParser<?, ?>[] PARSERS = {
            new CommentParser(),

            new ExportParser(),
            new ImportParser(),
            new RequireParser(),
            new MainParser(),
            new ModuleParser(),

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
