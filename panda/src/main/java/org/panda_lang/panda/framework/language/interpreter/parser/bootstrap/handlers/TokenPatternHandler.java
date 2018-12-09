package org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.handlers;

import org.panda_lang.panda.framework.design.interpreter.token.stream.TokenReader;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.PandaParserBootstrap;
import org.panda_lang.panda.framework.language.interpreter.pattern.PandaTokenPattern;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.TokenPattern;

public class TokenPatternHandler implements BootstrapHandler {

    private TokenPattern pattern;

    @Override
    public void initialize(PandaParserBootstrap bootstrap) {
        this.pattern = PandaTokenPattern.builder()
                .compile(bootstrap.getPattern())
                .build();
    }

    @Override
    public boolean handle(TokenReader reader) {
        return pattern.extract(reader.getTokenizedSource()).isMatched();
    }

}
