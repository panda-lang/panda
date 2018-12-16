package org.panda_lang.panda.framework.language.interpreter.parser.implementation.statement.invoker;

import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.interpreter.token.TokensUtils;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPriorities;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.BootstrapParser;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Src;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.handlers.TokenPatternHandler;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.interceptor.TokenPatternInterceptor;
import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.ParserRegistration;

@ParserRegistration(target = PandaPipelines.SCOPE, priority = PandaPriorities.SCOPE_METHOD_INVOKER_PARSER)
public class NewMethodInvokerParser extends BootstrapParser {

    {
        parserBuilder = builder()
                .handler(new TokenPatternHandler())
                .interceptor(new TokenPatternInterceptor())
                .pattern("[<instance:reader expression exclude method, field> .] <name> `( <*arguments> `) [;]");
    }

    @Autowired
    public void parse(@Src("instance") Tokens instance, @Src("name") Tokens name, @Src("*arguments") Tokens arguments) {
        System.out.println(name.getFirst().getLine() + "M: " + TokensUtils.asString(instance) + "." + TokensUtils.asString(name) + "#" + TokensUtils.asString(arguments));
    }

}
