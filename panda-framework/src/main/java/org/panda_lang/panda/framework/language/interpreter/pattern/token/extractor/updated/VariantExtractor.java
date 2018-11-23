package org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor.updated;

import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternElement;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternNode;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.TokenDistributor;

class VariantExtractor extends AbstractElementExtractor<LexicalPatternNode> {

    protected VariantExtractor(ExtractorWorker worker) {
        super(worker);
    }

    @Override
    public ExtractorResult extract(LexicalPatternNode element, TokenDistributor distributor) {
        if (!element.isVariant()) {
            throw new RuntimeException("The specified node is not marked as a variant node");
        }

        for (LexicalPatternElement variantElement : element.getElements()) {
            ExtractorResult result = super.getWorker().extract(distributor, variantElement);

            if (result.isMatched()) {
                return result.identified(variantElement.getIdentifier());
            }
        }

        return new ExtractorResult("Variant does not matched");
    }

}
