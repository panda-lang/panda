package org.panda_lang.panda.framework.language.interpreter.pattern.token;

import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.mapping.PatternMapping;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor.TokenExtractorResult;

public class TokenPatternMapping implements PatternMapping {

    private final TokenExtractorResult result;

    public TokenPatternMapping(TokenExtractorResult result) {
        this.result = result;
    }

    @Override
    public Tokens get(String name) {
        return result.getWildcards().get(name);
    }

}
