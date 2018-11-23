package org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor.updated;

import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternElement;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.TokenDistributor;

import java.util.List;

class NodeLookupExtractor  {

    private final NodeExtractor nodeExtractor;

    NodeLookupExtractor(NodeExtractor nodeExtractor) {
        this.nodeExtractor = nodeExtractor;
    }

    protected LookupResult extractNode(List<LexicalPatternElement> nextElements, TokenDistributor distributor) {
        return null;
    }

    static class LookupResult {

        protected ExtractorResult precedingResult;
        protected ExtractorResult currentResult;
        protected int matchedIndex;

        protected ExtractorResult getMergedResults() {
            if (precedingResult == null || currentResult == null) {
                return new ExtractorResult("Undefined result");
            }

            if (precedingResult.isMatched() && currentResult.isMatched()) {
                return new ExtractorResult("Lookup result not matched");
            }

            return precedingResult.merge(currentResult);
        }

    }

}
