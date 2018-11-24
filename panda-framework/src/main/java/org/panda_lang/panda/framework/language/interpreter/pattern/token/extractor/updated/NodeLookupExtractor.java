package org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor.updated;

import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternElement;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.TokenDistributor;
import org.panda_lang.panda.utilities.commons.StringUtils;

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

            return result;
        }

        distributor.setIndex(indexBackup);
        ExtractorResult result = elementLookupExtractor.extractWildcards(nextElements.subList(0, skip), distributor);

        LookupResult lookupResult = new LookupResult();
        lookupResult.precedingResult = result;
        lookupResult.currentResult = new ExtractorResult();
        lookupResult.matchedIndex = nextElements.size();

        return lookupResult;
    }

    static class LookupResult {

        protected ExtractorResult precedingResult;
        protected ExtractorResult currentResult;
        protected int matchedIndex;

        protected ExtractorResult getMergedResults() {
            if (precedingResult == null || currentResult == null) {
                return new ExtractorResult("Undefined result (" + getMergedErrorMessage() + ")");
            }

            if (!precedingResult.isMatched() || !currentResult.isMatched()) {
                return new ExtractorResult("Lookup result not matched (" + getMergedErrorMessage() + ")");
            }

            return precedingResult.merge(currentResult);
        }

        private String getMergedErrorMessage() {
            String error = "";

            if (currentResult != null) {
                error += currentResult.getErrorMessage();
            }

            if (precedingResult != null) {
                error += precedingResult.getErrorMessage();
            }

            if (StringUtils.isEmpty(error)) {
                error = "<unknown>";
            }

            return error;
        }

    }

}
