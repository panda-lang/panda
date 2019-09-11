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

package org.panda_lang.panda.language.resource.expression.subparsers.assignation.array;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.language.architecture.dynamic.assigner.Assigner;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.language.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Component;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.language.interpreter.parser.loader.Registrable;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.Channel;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.SourceStream;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.language.interpreter.token.PandaSourceStream;
import org.panda_lang.panda.language.resource.expression.subparsers.assignation.AssignationComponents;
import org.panda_lang.panda.language.resource.expression.subparsers.assignation.AssignationPriorities;
import org.panda_lang.panda.language.resource.expression.subparsers.assignation.AssignationSubparserBootstrap;
import org.panda_lang.panda.language.resource.syntax.auxiliary.Section;
import org.panda_lang.panda.language.resource.syntax.separator.Separators;

import java.util.Optional;

@Registrable(pipeline = PandaPipelines.ASSIGNER_LABEL, priority = AssignationPriorities.ARRAY_ASSIGNATION)
public class ArrayValueAssignationSubparser extends AssignationSubparserBootstrap {

    private static final ArrayValueAccessorParser PARSER = new ArrayValueAccessorParser();

    @Override
    protected BootstrapInitializer<@Nullable Assigner<?>> initialize(Context context, BootstrapInitializer<@Nullable Assigner<?>> initializer) {
        return initializer;
    }

    @Override
    protected Boolean customHandle(ParserHandler handler, Context context, Channel channel, Snippet source) {
        TokenRepresentation sectionRepresentation = source.getLast();

        if (sectionRepresentation == null || sectionRepresentation.getType() != TokenType.SECTION) {
            return false;
        }

        Section section = sectionRepresentation.toToken();

        if (!section.getSeparator().equals(Separators.SQUARE_BRACKET_LEFT)) {
            return false;
        }

        SourceStream expressionSource = new PandaSourceStream(source.subSource(0, source.size() - 1));
        Optional<Expression> expression = context.getComponent(UniversalComponents.EXPRESSION).parseSilently(context, expressionSource);

        if (expressionSource.hasUnreadSource() || !expression.isPresent()) {
            return false;
        }

        channel.put("array-instance", expression.get());
        return true;
    }

    @Autowired
    Assigner<?> parse(Context context, @Component SourceStream source, @Component Channel channel, @Component(AssignationComponents.EXPRESSION_LABEL) Expression value) {
        Snippet snippet = source.toSnippet();
        return PARSER.parse(context, snippet, channel.get("array-instance", Expression.class), snippet.getLast().toToken(), value);
    }

}
