package org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.handlers;

import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.interpreter.token.stream.TokenReader;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.PandaParserBootstrap;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.utils.AbyssPatternBuilder;
import org.panda_lang.panda.framework.language.resource.PandaSyntax;

import java.util.List;

public class AbyssPatternHandler implements BootstrapHandler {

    private AbyssPattern pattern;

    @Override
    public void initialize(PandaParserBootstrap bootstrap) {
        this.pattern = new AbyssPatternBuilder()
                .compile(PandaSyntax.getInstance(), bootstrap.getPattern())
                .build();
    }

    @Override
    public boolean handle(TokenReader reader) {
        List<Tokens> result = pattern.extractor().extract(reader);
        return result != null && result.size() == pattern.getAmountOfHollows();
    }

}
