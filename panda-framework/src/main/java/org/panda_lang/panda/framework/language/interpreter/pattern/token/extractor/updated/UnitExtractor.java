package org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor.updated;

import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternUnit;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.TokenDistributor;

class UnitExtractor extends AbstractElementExtractor<LexicalPatternUnit> {

    protected UnitExtractor(ExtractorWorker worker) {
        super(worker);
    }

    @Override
    public ExtractorResult extract(LexicalPatternUnit element, TokenDistributor distributor) {
        return null;
    }

}
