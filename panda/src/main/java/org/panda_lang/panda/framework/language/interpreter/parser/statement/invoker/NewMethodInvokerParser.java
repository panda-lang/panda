package org.panda_lang.panda.framework.language.interpreter.parser.statement.invoker;

import org.panda_lang.panda.framework.design.architecture.statement.StatementCell;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.language.architecture.prototype.method.MethodInvoker;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaPriorities;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.UnifiedParserBootstrap;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Local;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Src;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.handlers.TokenPatternHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.interceptor.TokenPatternInterceptor;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.layer.Delegation;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.layer.LocalData;
import org.panda_lang.panda.framework.language.interpreter.parser.general.expression.old.callbacks.invoker.MethodInvokerExpressionParser;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRegistration;

@ParserRegistration(target = PandaPipelines.SCOPE_LABEL, priority = PandaPriorities.SCOPE_METHOD_INVOKER_PARSER)
public class NewMethodInvokerParser extends UnifiedParserBootstrap {

    {
        super.builder()
                .handler(new TokenPatternHandler())
                .interceptor(new TokenPatternInterceptor())
                .pattern("[<instance:reader expression exclude method, field> .] <name> `( [<*args>] `) [;]");
    }

    @Autowired
    public void parse(ParserData data, LocalData localData) {
        localData.allocateInstance(data.getComponent(PandaComponents.CONTAINER).reserveCell());
    }

    @Autowired(order = 1, delegation = Delegation.NEXT_AFTER)
    public void parse(ParserData data, @Local StatementCell cell, @Src("instance") Tokens instance, @Src("name") Tokens name, @Src("*args") Tokens arguments) {
        MethodInvokerExpressionParser methodInvokerParser = new MethodInvokerExpressionParser(instance, name, arguments);
        methodInvokerParser.setVoids(true);

        methodInvokerParser.parse(null, data);
        MethodInvoker invoker = methodInvokerParser.getInvoker();

        cell.setStatement(methodInvokerParser.getInvoker());
    }

}
