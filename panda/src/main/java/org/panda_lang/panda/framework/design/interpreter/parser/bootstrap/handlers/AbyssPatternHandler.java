package org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.handlers;

import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.PandaParserBootstrap;
import org.panda_lang.panda.framework.design.interpreter.pattern.AbyssPatternData;
import org.panda_lang.panda.framework.design.interpreter.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.framework.design.interpreter.pattern.abyss.utils.AbyssPatternBuilder;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.language.resource.PandaSyntax;

import java.util.List;

public class AbyssPatternHandler implements BootstrapHandler {

    private AbyssPattern pattern;

    @Override
    public void initialize(PandaParserBootstrap bootstrap) {
        AbyssPatternData data = (AbyssPatternData) bootstrap.getPattern();

        this.pattern = new AbyssPatternBuilder()
                .compile(PandaSyntax.getInstance(), data.getPattern())
                .build();
    }

    @Override
    public boolean handle(ParserData data, Tokens source) {
        List<Tokens> result = pattern.extractor().extract(source);
        return result != null && result.size() == pattern.getAmountOfHollows();
    }

}
