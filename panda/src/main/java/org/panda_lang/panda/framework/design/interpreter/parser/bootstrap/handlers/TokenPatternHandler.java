package org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.handlers;

import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.PandaParserBootstrap;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.design.interpreter.pattern.PandaTokenPattern;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.TokenPattern;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;

public class TokenPatternHandler implements BootstrapHandler, ParserHandler {

    private TokenPattern pattern;

    @Override
    public void initialize(PandaParserBootstrap bootstrap) {
        this.pattern = PandaTokenPattern.builder()
                .compile(bootstrap.getPattern().toString())
                .build();
    }

    @Override
    public boolean handle(ParserData data, SourceStream source) {
        return pattern.extract(source).isMatched();
    }

}
