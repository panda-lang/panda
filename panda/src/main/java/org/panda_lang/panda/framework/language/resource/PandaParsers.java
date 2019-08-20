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

package org.panda_lang.panda.framework.language.resource;

import org.panda_lang.panda.framework.design.interpreter.parser.ApplicationParser;
import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.Parsers;
import org.panda_lang.panda.framework.language.interpreter.parser.block.BlockParser;
import org.panda_lang.panda.framework.language.resource.container.LateDeclarationParser;
import org.panda_lang.panda.framework.language.resource.container.StandaloneExpressionParser;
import org.panda_lang.panda.framework.language.resource.container.TryCatchParser;
import org.panda_lang.panda.framework.language.resource.container.block.conditional.ConditionalBlockParser;
import org.panda_lang.panda.framework.language.resource.container.block.looping.ForEachParser;
import org.panda_lang.panda.framework.language.resource.container.block.looping.ForParser;
import org.panda_lang.panda.framework.language.resource.container.block.looping.LoopParser;
import org.panda_lang.panda.framework.language.resource.container.block.looping.WhileParser;
import org.panda_lang.panda.framework.language.resource.container.branching.BreakParser;
import org.panda_lang.panda.framework.language.resource.container.branching.ContinueParser;
import org.panda_lang.panda.framework.language.resource.container.branching.ReturnParser;
import org.panda_lang.panda.framework.language.resource.container.branching.ThrowParser;
import org.panda_lang.panda.framework.language.resource.expression.subparsers.number.NumberParser;
import org.panda_lang.panda.framework.language.resource.head.CommentParser;
import org.panda_lang.panda.framework.language.resource.head.ImportParser;
import org.panda_lang.panda.framework.language.resource.head.MainParser;
import org.panda_lang.panda.framework.language.resource.head.ModuleParser;
import org.panda_lang.panda.framework.language.resource.head.RequireParser;
import org.panda_lang.panda.framework.language.resource.parsers.ContainerParser;
import org.panda_lang.panda.framework.language.resource.parsers.ScopeParser;
import org.panda_lang.panda.framework.language.resource.prototype.ClassPrototypeParser;
import org.panda_lang.panda.framework.language.resource.prototype.constructor.ConstructorParser;
import org.panda_lang.panda.framework.language.resource.prototype.field.FieldParser;
import org.panda_lang.panda.framework.language.resource.prototype.method.MethodParser;
import org.panda_lang.panda.framework.language.resource.prototype.parameter.ArgumentsParser;
import org.panda_lang.panda.framework.language.resource.prototype.parameter.ParameterParser;

public final class PandaParsers extends Parsers {

    public static final Class<? extends Parser>[] PARSERS = of(
            // lead
            ApplicationParser.class,
            ContainerParser.class,
            ScopeParser.class,

            // common
            CommentParser.class,
            ArgumentsParser.class,
            NumberParser.class,

            // overall
            ImportParser.class,
            RequireParser.class,
            MainParser.class,
            ModuleParser.class,

            // prototype
            ClassPrototypeParser.class,
            ConstructorParser.class,
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

    @Override
    public Class<? extends Parser>[] getParsers() {
        return PARSERS;
    }

}
