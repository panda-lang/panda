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

package org.panda_lang.panda.framework.language.resource.parsers.container.assignation;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.statement.Statement;
import org.panda_lang.panda.framework.design.architecture.statement.StatementCell;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapInitializer;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.ParserBootstrap;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Src;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.HandleResult;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.UniversalPipelines;
import org.panda_lang.panda.framework.design.interpreter.pattern.PandaDescriptivePattern;
import org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.DescriptivePattern;
import org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.extractor.ExtractorResult;
import org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.extractor.ExtractorResultElement;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.resource.parsers.Registrable;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPriorities;

import java.util.Optional;

@Registrable(pipeline = UniversalPipelines.CONTAINER_LABEL, priority = PandaPriorities.CONTAINER_ASSIGNATION)
public class AssignationParser extends ParserBootstrap {

    private static final String PATTERN = "<*declaration> (=|+=|-=|`*=|/=) <assignation:reader expression> [;]"; // slow

    private DescriptivePattern pattern;
    private AssignationSubparser subparser;

    @Override
    protected BootstrapInitializer initialize(Context context, BootstrapInitializer initializer) {
        this.pattern = PandaDescriptivePattern.builder()
                .compile(PATTERN)
                .build(context);

        return initializer.pattern(PATTERN);
    }

    @Override
    protected Boolean customHandle(@Nullable ParserHandler handler, Context context, Snippet source) {
        ExtractorResult result = pattern.extract(context, source);

        if (!result.isMatched()) {
            return false;
        }

        Optional<ExtractorResultElement> declaration = result.getWildcard("*declaration");

        if (!declaration.isPresent()) {
            return false;
        }

        HandleResult<AssignationSubparser> handleResult = context.getComponent(UniversalComponents.PIPELINE)
                .getPipeline(PandaPipelines.ASSIGNER)
                .handle(context, declaration.get().getValue());

        handleResult.getParser().ifPresent(parser -> this.subparser = parser);
        return handleResult.isFound();
    }

    @Autowired
    void parse(Context context, @Src("*declaration") Snippet declaration, @Src("assignation") Expression assignation) throws Exception {
        Context delegatedContext = context.fork();
        delegatedContext.withComponent(AssignationComponents.SCOPE, delegatedContext.getComponent(UniversalComponents.LINKER).getCurrentScope());

        StatementCell cell = delegatedContext.getComponent(UniversalComponents.CONTAINER).reserveCell();
        Statement statement = subparser.parseAssignment(delegatedContext, declaration, assignation);
        subparser = null;

        if (statement == null) {
            throw PandaParserFailure.builder("Cannot parse assignment", delegatedContext)
                    .withStreamOrigin(declaration)
                    .build();
        }

        cell.setStatement(statement);
    }

}