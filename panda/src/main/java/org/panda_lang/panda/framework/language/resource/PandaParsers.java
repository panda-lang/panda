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
import org.panda_lang.panda.framework.language.resource.parsers.common.ArgumentsParser;
import org.panda_lang.panda.framework.language.resource.parsers.common.CommentParser;
import org.panda_lang.panda.framework.language.resource.parsers.common.number.NumberParser;
import org.panda_lang.panda.framework.language.resource.parsers.overall.ImportParser;
import org.panda_lang.panda.framework.language.resource.parsers.overall.MainParser;
import org.panda_lang.panda.framework.language.resource.parsers.overall.ModuleParser;
import org.panda_lang.panda.framework.language.resource.parsers.prototype.ClassPrototypeParser;
import org.panda_lang.panda.framework.language.resource.parsers.prototype.constructor.ConstructorParser;
import org.panda_lang.panda.framework.language.resource.parsers.prototype.field.FieldParser;
import org.panda_lang.panda.framework.language.resource.parsers.prototype.method.MethodParser;
import org.panda_lang.panda.framework.language.resource.parsers.prototype.parameter.ParameterParser;
import org.panda_lang.panda.framework.language.resource.parsers.scope.LateDeclarationParser;
import org.panda_lang.panda.framework.language.resource.parsers.scope.StandaloneExpressionParser;
import org.panda_lang.panda.framework.language.resource.parsers.scope.block.BlockParser;
import org.panda_lang.panda.framework.language.resource.parsers.scope.block.conditional.ConditionalBlockParser;
import org.panda_lang.panda.framework.language.resource.parsers.scope.block.looping.ForEachParser;
import org.panda_lang.panda.framework.language.resource.parsers.scope.block.looping.LoopParser;
import org.panda_lang.panda.framework.language.resource.parsers.scope.block.looping.WhileParser;
import org.panda_lang.panda.framework.language.resource.parsers.scope.branching.BreakParser;
import org.panda_lang.panda.framework.language.resource.parsers.scope.branching.ContinueParser;
import org.panda_lang.panda.framework.language.resource.parsers.scope.branching.ReturnParser;

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
            MainParser.class,
            ModuleParser.class,

            // prototype
            ClassPrototypeParser.class,
            ConstructorParser.class,
            FieldParser.class,
            MethodParser.class,
            ParameterParser.class,

            // scope
            LateDeclarationParser.class,
            StandaloneExpressionParser.class,

            // block
            BlockParser.class,
            ConditionalBlockParser.class,
            ForEachParser.class,
            LoopParser.class,
            WhileParser.class,

            // branching
            BreakParser.class,
            ContinueParser.class,
            ReturnParser.class
    );

    @Override
    public Class<? extends Parser>[] getParsers() {
        return PARSERS;
    }

}
