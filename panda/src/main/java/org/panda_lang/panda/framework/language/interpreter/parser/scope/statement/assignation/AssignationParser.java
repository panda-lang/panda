package org.panda_lang.panda.framework.language.interpreter.parser.scope.statement.assignation;

import org.panda_lang.panda.framework.design.architecture.statement.Statement;
import org.panda_lang.panda.framework.design.architecture.statement.StatementCell;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaPriorities;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapParserBuilder;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.UnifiedParserBootstrap;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Component;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Src;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.layer.LocalData;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.PipelineRegistry;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;
import org.panda_lang.panda.utilities.commons.ObjectUtils;

@ParserRegistration(target = PandaPipelines.SCOPE_LABEL, priority = PandaPriorities.SCOPE_ASSIGNATION_PARSER)
public class AssignationParser extends UnifiedParserBootstrap {

    @Override
    public BootstrapParserBuilder initialize(ParserData data, BootstrapParserBuilder defaultBuilder) {
        return defaultBuilder.pattern("<*declaration> (=|+=|-=|`*=|/=) <assignation:reader expression> [;]");
    }

    @Override
    public boolean handle(ParserData data, Tokens source) {
        return ObjectUtils.isNotNull(data
                .getComponent(UniversalComponents.PIPELINE)
                .getPipeline(PandaPipelines.ASSIGNER)
                .handle(data, new PandaSourceStream(source)));
    }

    @Autowired
    public void parse(ParserData data, LocalData local, @Component PipelineRegistry registry, @Src("*declaration") Tokens declaration, @Src("assignation") Tokens assignation) throws Throwable {
        ParserData delegatedData = data.fork();
        delegatedData.setComponent(AssignationComponents.SCOPE, delegatedData.getComponent(PandaComponents.SCOPE_LINKER).getCurrentScope());

        Expression assignationExpression = delegatedData.getComponent(PandaComponents.EXPRESSION).parse(delegatedData, assignation);
        AssignationSubparser subparser = registry.getPipeline(PandaPipelines.ASSIGNER).handle(data, new PandaSourceStream(declaration));

        StatementCell cell = delegatedData.getComponent(PandaComponents.CONTAINER).reserveCell();
        Statement statement = subparser.parseVariable(delegatedData, declaration, assignationExpression);

        if (statement == null) {
            throw new PandaParserFailure("Cannot parse assignment", delegatedData);
        }

        cell.setStatement(statement);
    }

}