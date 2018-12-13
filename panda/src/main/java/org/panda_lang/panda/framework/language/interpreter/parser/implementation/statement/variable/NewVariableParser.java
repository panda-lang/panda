package org.panda_lang.panda.framework.language.interpreter.parser.implementation.statement.variable;

import org.jetbrains.annotations.Nullable;
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

@ParserRegistration(target = PandaPipelines.SCOPE, priority = PandaPriorities.SCOPE_VARIABLE_PARSER)
public class NewVariableParser extends BootstrapParser {

    {
        parserBuilder = builder()
                .handler(new TokenPatternHandler())
                .interceptor(new TokenPatternInterceptor())
                .pattern("<type> <name> [= <assignation:reader expression>][;]");
    }

    @Autowired
    public void parse(@Src("type") Tokens instance, @Src("name") Tokens name, @Src("*assignation") @Nullable Tokens arguments) {
        System.out.println("V: " + TokensUtils.asString(instance) + "." + TokensUtils.asString(name) + "#" + TokensUtils.asString(arguments));
    }

}
