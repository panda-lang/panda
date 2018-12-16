package org.panda_lang.panda.framework.language.interpreter.parser.implementation.statement.invoker;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.statement.Container;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.language.architecture.prototype.method.MethodInvoker;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPriorities;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.BootstrapParser;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Src;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.handlers.TokenPatternHandler;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.interceptor.TokenPatternInterceptor;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.layer.Delegation;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.layer.LocalData;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.old.callbacks.invoker.MethodInvokerExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.ParserRegistration;

@ParserRegistration(target = PandaPipelines.SCOPE, priority = PandaPriorities.SCOPE_METHOD_INVOKER_PARSER)
public class NewMethodInvokerParser extends BootstrapParser {

    {
        parserBuilder = builder()
                .handler(new TokenPatternHandler())
                .interceptor(new TokenPatternInterceptor())
                .pattern("[<instance:reader expression exclude method, field> .] <name> `( [<*args>] `) [;]");
    }

    @Autowired(order = 1)
    public void parse(ParserData data, LocalData localData) {
        localData.allocateInstance(data.getComponent(PandaComponents.CONTAINER));
    }

    @Autowired(order = 2, delegation = Delegation.NEXT_AFTER)
    public void parse(ParserData data, LocalData localData, @Src("instance") @Nullable Tokens instance, @Src("name") Tokens name, @Src("*args") @Nullable Tokens arguments) {
        MethodInvokerExpressionParser methodInvokerParser = new MethodInvokerExpressionParser(instance, name, arguments);

        methodInvokerParser.setVoids(true);
        methodInvokerParser.parse(null, data);

        MethodInvoker invoker = methodInvokerParser.getInvoker();
        localData.getValue(Container.class).addStatement(invoker);
    }

}
