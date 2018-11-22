package org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor.updated;

import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternWildcard;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.TokenDistributor;

class WildcardExtractor extends AbstractElementExtractor<LexicalPatternWildcard> {

    protected WildcardExtractor(ExtractorWorker worker) {
        super(worker);
    }

    @Override
    public ExtractorResult extract(LexicalPatternWildcard element, TokenDistributor distributor) {
        return null;
    }

}
