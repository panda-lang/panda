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

import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.Parsers;
import org.panda_lang.panda.framework.language.resource.parsers.ApplicationParser;
import org.panda_lang.panda.framework.language.resource.parsers.ContainerParser;
import org.panda_lang.panda.framework.language.resource.parsers.ScopeParser;
import org.panda_lang.panda.framework.language.resource.parsers.container.LateDeclarationParser;
import org.panda_lang.panda.framework.language.resource.parsers.container.StandaloneExpressionParser;
import org.panda_lang.panda.framework.language.resource.parsers.container.TryCatchParser;
import org.panda_lang.panda.framework.language.resource.parsers.container.assignation.AssignationParser;
import org.panda_lang.panda.framework.language.resource.parsers.container.block.BlockParser;
import org.panda_lang.panda.framework.language.resource.parsers.container.block.conditional.ConditionalBlockParser;
import org.panda_lang.panda.framework.language.resource.parsers.container.block.looping.ForEachParser;
import org.panda_lang.panda.framework.language.resource.parsers.container.block.looping.ForParser;
import org.panda_lang.panda.framework.language.resource.parsers.container.block.looping.LoopParser;
import org.panda_lang.panda.framework.language.resource.parsers.container.block.looping.WhileParser;
import org.panda_lang.panda.framework.language.resource.parsers.container.branching.BreakParser;
import org.panda_lang.panda.framework.language.resource.parsers.container.branching.ContinueParser;
import org.panda_lang.panda.framework.language.resource.parsers.container.branching.ReturnParser;
import org.panda_lang.panda.framework.language.resource.parsers.container.branching.ThrowParser;
import org.panda_lang.panda.framework.language.resource.parsers.expression.subparsers.number.NumberParser;
import org.panda_lang.panda.framework.language.resource.parsers.overall.CommentParser;
import org.panda_lang.panda.framework.language.resource.parsers.overall.ImportParser;
import org.panda_lang.panda.framework.language.resource.parsers.overall.MainParser;
import org.panda_lang.panda.framework.language.resource.parsers.overall.ModuleParser;
import org.panda_lang.panda.framework.language.resource.parsers.overall.RequireParser;
import org.panda_lang.panda.framework.language.resource.parsers.overall.prototype.ClassPrototypeParser;
import org.panda_lang.panda.framework.language.resource.parsers.overall.prototype.constructor.ConstructorParser;
import org.panda_lang.panda.framework.language.resource.parsers.overall.prototype.field.FieldParser;
import org.panda_lang.panda.framework.language.resource.parsers.overall.prototype.method.MethodParser;
import org.panda_lang.panda.framework.language.resource.parsers.overall.prototype.parameter.ArgumentsParser;
import org.panda_lang.panda.framework.language.resource.parsers.overall.prototype.parameter.ParameterParser;

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
            AssignationParser.class,
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
