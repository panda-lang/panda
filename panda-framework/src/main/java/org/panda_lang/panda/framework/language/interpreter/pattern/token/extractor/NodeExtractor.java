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

        for (int i = 0; i < elements.size(); i++) {
            LexicalPatternElement element = elements.get(i);

            ExtractorResult workerResult;
            int index = distributor.getIndex();

            if (!element.isWildcard()) {
                workerResult = extract(element, distributor);

                if (workerResult == null) {
                    break;
                }
            }
            else {
                NodeLookupExtractor.LookupResult lookupResult = nodeLookupExtractor.extractNode(elements.subList(i, elements.size()), distributor);
                workerResult = lookupResult.getMergedResults();
                i += lookupResult.matchedIndex;
            }

            if (!workerResult.isMatched()) {
                distributor.setIndex(index);

                if (element.isOptional()) {
                    continue;
                }

                return new ExtractorResult("Could not extract element, caused by: " + workerResult.getErrorMessage());
            }

            result.merge(workerResult);
        }

        return result;
    }

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
