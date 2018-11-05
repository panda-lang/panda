package org.panda_lang.panda.framework.language.interpreter.pattern.token;

import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.LexicalPatternCompiler;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternElement;

class TokenPatternCompiler {

    LexicalPatternElement compile(String pattern) {
        LexicalPatternCompiler lexicalPatternCompiler = new LexicalPatternCompiler();
        return lexicalPatternCompiler.compile(pattern);
    }

}
