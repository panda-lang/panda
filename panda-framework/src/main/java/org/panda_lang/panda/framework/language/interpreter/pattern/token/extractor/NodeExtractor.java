package org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternElement;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternNode;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.TokenDistributor;

import java.util.List;

class NodeExtractor extends AbstractElementExtractor<LexicalPatternNode> {

    private final NodeLookupExtractor nodeLookupExtractor = new NodeLookupExtractor(this);

    protected NodeExtractor(ExtractorWorker worker) {
        super(worker);
    }

    @Override
    public ExtractorResult extract(LexicalPatternNode node, TokenDistributor distributor) {
        List<LexicalPatternElement> elements = node.getElements();
        ExtractorResult result = new ExtractorResult();
        int matches = 0;

        for (int i = 0; i < elements.size(); i++) {
            LexicalPatternElement element = elements.get(i);
            int index = distributor.getIndex();

            ExtractorResult workerResult;

            if (!element.isWildcard()) {
                workerResult = extract(element, distributor);

                // out of source, but element was optional
                if (workerResult == null) {
                    break;
                }
            }
            else {
                NodeLookupExtractor.LookupResult lookupResult = nodeLookupExtractor.extractNode(elements.subList(i, elements.size()), distributor);
                workerResult = lookupResult.getMergedResults();

                // skip elements only if matched
                if (workerResult.isMatched()) {
                    i += lookupResult.matchedIndex;
                }
            }

            if (!workerResult.isMatched()) {
                // restore index for the next searches
                distributor.setIndex(index);

                if (element.isOptional()) {
                    continue;
                }

                return new ExtractorResult("Could not extract element, caused by: " + workerResult.getErrorMessage());
            }

            result.merge(workerResult);
            matches++;
        }

        if (matches == 0) {
            // return new ExtractorResult("Cannot match node, 0 matches");
        }

        return result;
    }

    /**
     * @param element the element to match
     * @param distributor source
     * @return subresult or null if distributor does not contains source
     */
    private @Nullable ExtractorResult extract(LexicalPatternElement element, TokenDistributor distributor) {
        if (!distributor.hasNext()) {
            if (element.isOptional()) {
                return null;
            }

            return new ExtractorResult("Out of source");
        }

        return super.getWorker().extract(distributor, element);
    }

}
