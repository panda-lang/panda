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

package org.panda_lang.panda.language.interpreter.parser;

import org.panda_lang.framework.design.interpreter.parser.Parser;
import org.panda_lang.framework.language.interpreter.parser.PandaParsersUtils;
import org.panda_lang.framework.language.interpreter.parser.ScopeParser;
import org.panda_lang.panda.language.interpreter.bootstraps.block.BlockParser;
import org.panda_lang.panda.language.interpreter.parser.expression.subparsers.ArgumentsParser;
import org.panda_lang.panda.language.interpreter.parser.expression.subparsers.number.NumberParser;
import org.panda_lang.panda.language.interpreter.parser.head.CommentParser;
import org.panda_lang.panda.language.interpreter.parser.head.ExportParser;
import org.panda_lang.panda.language.interpreter.parser.head.ImportParser;
import org.panda_lang.panda.language.interpreter.parser.head.MainParser;
import org.panda_lang.panda.language.interpreter.parser.head.ModuleParser;
import org.panda_lang.panda.language.interpreter.parser.head.RequireParser;
import org.panda_lang.panda.language.interpreter.parser.prototype.BaseConstructorParser;
import org.panda_lang.panda.language.interpreter.parser.prototype.ConstructorParser;
import org.panda_lang.panda.language.interpreter.parser.prototype.FieldParser;
import org.panda_lang.panda.language.interpreter.parser.prototype.MethodParser;
import org.panda_lang.panda.language.interpreter.parser.prototype.ParameterParser;
import org.panda_lang.panda.language.interpreter.parser.prototype.PrototypeParser;
import org.panda_lang.panda.language.interpreter.parser.scope.LateDeclarationParser;
import org.panda_lang.panda.language.interpreter.parser.scope.StandaloneExpressionParser;
import org.panda_lang.panda.language.interpreter.parser.scope.TryCatchParser;
import org.panda_lang.panda.language.interpreter.parser.scope.block.conditional.ConditionalBlockParser;
import org.panda_lang.panda.language.interpreter.parser.scope.block.looping.ForEachParser;
import org.panda_lang.panda.language.interpreter.parser.scope.block.looping.ForParser;
import org.panda_lang.panda.language.interpreter.parser.scope.block.looping.LoopParser;
import org.panda_lang.panda.language.interpreter.parser.scope.block.looping.WhileParser;
import org.panda_lang.panda.language.interpreter.parser.scope.branching.BreakParser;
import org.panda_lang.panda.language.interpreter.parser.scope.branching.ContinueParser;
import org.panda_lang.panda.language.interpreter.parser.scope.branching.ReturnParser;
import org.panda_lang.panda.language.interpreter.parser.scope.branching.ThrowParser;

public final class PandaParsers {

    public static final Class<? extends Parser>[] PARSERS = PandaParsersUtils.of(
            // lead
            ApplicationParser.class,
            ScopeParser.class,

            // common
            CommentParser.class,
            ArgumentsParser.class,
            NumberParser.class,

            // overall
            ExportParser.class,
            ImportParser.class,
            RequireParser.class,
            MainParser.class,
            ModuleParser.class,

            // prototype
            PrototypeParser.class,
            ConstructorParser.class,
            BaseConstructorParser.class,
            FieldParser.class,
            MethodParser.class,
            ParameterParser.class,

            // scope
            // AssignationParser.class, off
            LateDeclarationParser.class,
            StandaloneExpressionParser.class,
            TryCatchParser.class,

            // block
            BlockParser.class,
            ConditionalBlockParser.class,
            ForEachParser.class,
            ForParser.class,
            LoopParser.class,
            WhileParser.class,

            // branching
            BreakParser.class,
            ContinueParser.class,
            ReturnParser.class,
            ThrowParser.class
    );

    private PandaParsers() { }

}
