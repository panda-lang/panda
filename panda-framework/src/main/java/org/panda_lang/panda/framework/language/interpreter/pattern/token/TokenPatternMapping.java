package org.panda_lang.panda.framework.language.interpreter.pattern.token;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.mapping.PatternMapping;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor.updated.ExtractorResult;

public class TokenPatternMapping implements PatternMapping {

    private final ExtractorResult result;

    public TokenPatternMapping(ExtractorResult result) {
        this.result = result;
    }

    @Override
    public @Nullable Tokens get(String name) {
        return result.getWildcards() != null ? result.getWildcards().get(name) : null;
    }

}
