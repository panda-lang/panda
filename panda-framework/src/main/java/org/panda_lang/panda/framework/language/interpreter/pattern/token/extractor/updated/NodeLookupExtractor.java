package org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor.updated;

import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternElement;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.TokenDistributor;

import java.util.List;

class NodeLookupExtractor  {

    protected final NodeExtractor nodeExtractor;
    private final NodeElementLookupExtractor elementLookupExtractor = new NodeElementLookupExtractor(this);

    NodeLookupExtractor(NodeExtractor nodeExtractor) {
        this.nodeExtractor = nodeExtractor;
    }

    protected LookupResult extractNode(List<LexicalPatternElement> nextElements, TokenDistributor distributor) {
        int skip = 0;

        for (LexicalPatternElement nextElement : nextElements) {
            if (!nextElement.isWildcard()) {
                break;
            }

            skip++;
        }

        int indexBackup = distributor.getIndex();

        for (int i = skip; i < nextElements.size(); i++) {
            LexicalPatternElement element = nextElements.get(i);
            distributor.setIndex(indexBackup);

            // consider exclusion of wildcards here

            LookupResult result = elementLookupExtractor.extractNode(nextElements.subList(0, skip), element, distributor);
            result.matchedIndex = i;

            if (result.getMergedResults().isMatched()) {
                return result;
            }

            if (element.isOptional()) {
                continue;
            }

            break;
        }

        return new LookupResult();
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
