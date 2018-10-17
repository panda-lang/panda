package org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.handlers;

import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.design.interpreter.token.stream.TokenReader;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.utils.AbyssPatternBuilder;
import org.panda_lang.panda.framework.language.resource.PandaSyntax;

public class AbyssPatternHandler implements ParserHandler {

    private final AbyssPattern pattern;

    public AbyssPatternHandler(String pattern) {
        this.pattern = new AbyssPatternBuilder()
                .compile(PandaSyntax.getInstance(), pattern)
                .build();
    }

    @Override
    public boolean handle(TokenReader reader) {
        return pattern.extractor().extract(reader) != null;
    }

}
