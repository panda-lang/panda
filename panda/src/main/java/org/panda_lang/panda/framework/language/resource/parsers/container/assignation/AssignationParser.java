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
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapInitializer;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.ParserBootstrap;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Src;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.design.interpreter.pattern.PandaDescriptivePattern;
import org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.DescriptivePattern;
import org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.extractor.ExtractorResult;
import org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.extractor.ExtractorResultElement;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.resource.parsers.ParserRegistration;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPriorities;

import java.util.Optional;

@ParserRegistration(pipeline = PandaPipelines.CONTAINER_LABEL, priority = PandaPriorities.CONTAINER_ASSIGNATION)
public class AssignationParser extends ParserBootstrap {

    private static final String PATTERN = "<*declaration> (=|+=|-=|`*=|/=) <assignation:reader expression> [;]"; // slow

    private DescriptivePattern pattern;
    private AssignationSubparser subparser;

    @Override
    public BootstrapInitializer initialize(ParserData data, BootstrapInitializer initializer) {
        this.pattern = PandaDescriptivePattern.builder()
                .compile(PATTERN)
                .build(data);

        return initializer.pattern(PATTERN);
    }

    @Override
    public boolean customHandle(@Nullable ParserHandler handler, ParserData data, Snippet source) {
        ExtractorResult result = pattern.extract(data, source);

        if (!result.isMatched()) {
            return false;
        }

        Optional<ExtractorResultElement> declaration = result.getWildcard("*declaration");

        if (!declaration.isPresent()) {
            return false;
        }

        this.subparser = data
                .getComponent(UniversalComponents.PIPELINE)
                .getPipeline(PandaPipelines.ASSIGNER)
                .handle(data, declaration.get().getValue());

        return subparser != null;
    }

    @Autowired
    void parse(ParserData data, @Src("*declaration") Snippet declaration, @Src("assignation") Expression assignation) throws Exception {
        ParserData delegatedData = data.fork();
        delegatedData.setComponent(AssignationComponents.SCOPE, delegatedData.getComponent(UniversalComponents.SCOPE_LINKER).getCurrentScope());

        StatementCell cell = delegatedData.getComponent(PandaComponents.CONTAINER).reserveCell();
        Statement statement = subparser.parseAssignment(delegatedData, declaration, assignation);
        subparser = null;

        if (statement == null) {
            throw PandaParserFailure.builder("Cannot parse assignment", delegatedData)
                    .withStreamOrigin(declaration)
                    .build();
        }

        cell.setStatement(statement);
    }

}