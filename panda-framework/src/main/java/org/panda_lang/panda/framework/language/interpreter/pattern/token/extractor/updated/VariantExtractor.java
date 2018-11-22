package org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor.updated;

import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternNode;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.TokenDistributor;

public class VariantExtractor extends AbstractElementExtractor<LexicalPatternNode> {

    protected VariantExtractor(ExtractorWorker worker) {
        super(worker);
    }

    @Override
    public ExtractorResult extract(LexicalPatternNode element, TokenDistributor distributor) {
        return null;
    }

}
