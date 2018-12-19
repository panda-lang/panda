package org.panda_lang.panda.framework.design.interpreter.pattern.token.extractor;

import org.panda_lang.panda.framework.design.interpreter.pattern.lexical.elements.LexicalPatternUnit;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.TokenDistributor;

class UnitExtractor extends AbstractElementExtractor<LexicalPatternUnit> {

    protected UnitExtractor(ExtractorWorker worker) {
        super(worker);
    }

    @Override
    public ExtractorResult extract(LexicalPatternUnit element, TokenDistributor distributor) {
        String unitValue = element.getValue();
        String tokenValue = distributor.next().getTokenValue();

        if (!unitValue.equals(tokenValue)) {
            return new ExtractorResult("Unit does not match");
        }

        return new ExtractorResult().identified(element.getIdentifier());
    }

}