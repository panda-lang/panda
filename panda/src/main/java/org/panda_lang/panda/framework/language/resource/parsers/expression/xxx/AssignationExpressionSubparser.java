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

package org.panda_lang.panda.framework.language.resource.parsers.expression.xxx;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.statement.Statement;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserPipeline;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.PipelinePath;
import org.panda_lang.panda.framework.design.interpreter.pattern.PandaTokenPattern;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.TokenPattern;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.extractor.ExtractorResult;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.resource.parsers.expression.xxx.ExpressionParserOld;
import org.panda_lang.panda.framework.design.resource.parsers.expression.xxx.ExpressionSubparser;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.resource.parsers.expression.xxx.assignation.AssignationComponents;
import org.panda_lang.panda.framework.language.resource.parsers.expression.xxx.assignation.AssignationSubparser;

public class AssignationExpressionSubparser implements ExpressionSubparser {

    private static final String PATTERN = "<*declaration> (=|+=|-=|`*=|/=) <assignation:reader expression> [;]";

    private TokenPattern pattern;
    private ParserPipeline<AssignationSubparser> pipelinePath;

    @Override
    public void initialize(ParserData data) {
        this.pattern = PandaTokenPattern.builder()
                .compile(PATTERN)
                .build(data);
    }

    @Override
    public @Nullable Tokens read(ExpressionParserOld parent, Tokens source) {
        return null;

        /*
        SourceStream sourceStream = new PandaSourceStream(source);
        ExtractorResult result = pattern.extract(sourceStream);

        if (!result.isMatched()) {
            return null;
        }

        return source.subSource(0, source.size() - sourceStream.getUnreadLength());
        */
    }

    @Override
    public @Nullable Expression parse(ExpressionParserOld parent, ParserData data, Tokens source) {
        System.out.println(":O -> " + source);
        ExtractorResult result = pattern.extract(source);

        ParserData delegatedData = data.fork();
        delegatedData.setComponent(AssignationComponents.SCOPE, delegatedData.getComponent(UniversalComponents.SCOPE_LINKER).getCurrentScope());

        Tokens assignation = result.getWildcard("assignation");
        Expression assignationExpression = delegatedData.getComponent(PandaComponents.EXPRESSION).parse(delegatedData, assignation);

        PipelinePath path = data.getComponent(UniversalComponents.PIPELINE);
        Tokens declaration = result.getWildcard("*declaration");
        AssignationSubparser subparser = path.getPipeline(PandaPipelines.ASSIGNER).handle(data, declaration);

        try {
            Statement statement = subparser.parseAssignment(delegatedData, declaration, assignationExpression); // TODO
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return null;
    }

    @Override
    public double getPriority() {
        return DefaultSubparsers.Priorities.DYNAMIC;
    }

    @Override
    public String getName() {
        return DefaultSubparsers.Names.ASSIGNATION;
    }

}
