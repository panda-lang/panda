package org.panda_lang.panda.framework.language.interpreter.lexer;

import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.language.interpreter.source.PandaSource;
import org.panda_lang.panda.framework.language.resource.PandaSyntax;

public class PandaLexerUtils {

    public static Tokens convert(String source) {
        return new PandaLexer(PandaSyntax.getInstance(), new PandaSource("runtime::PandaLexerUtils", source)).convert();
    }

}
