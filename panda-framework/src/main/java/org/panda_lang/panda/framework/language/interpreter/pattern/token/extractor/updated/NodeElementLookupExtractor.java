package org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor.updated;

import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternElement;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.TokenDistributor;

import java.util.List;

class NodeElementLookupExtractor {

    private final NodeLookupExtractor lookupExtractor;

    NodeElementLookupExtractor(NodeLookupExtractor lookupExtractor) {
        this.lookupExtractor = lookupExtractor;
    }

    protected NodeLookupExtractor.LookupResult extractNode(List<LexicalPatternElement> elementsBefore, LexicalPatternElement element, TokenDistributor distributor) {
        ExtractorResult precedingResult = null;
        ExtractorResult currentResult = null;

        int startIndex = distributor.getIndex();
        // distributor.setIndex(startIndex - 1);

        MatchableDistributor matchable = new MatchableDistributor(distributor);
        int index = startIndex;

        while (matchable.hasNext()) {
            matchable.next();
            index = distributor.getIndex();

            if (!matchable.isMatchable()) {
                continue;
            }

            if (!distributor.hasNext()) {
                break;
            }

            currentResult = lookupExtractor.nodeExtractor.getWorker().extract(distributor, element);

            if (currentResult.isMatched()) {
                break;
            }

            distributor.setIndex(index);
        }

        if (currentResult != null && currentResult.isMatched()) {
            Tokens before = distributor.getSource().subSource(startIndex, index);
            TokenDistributor beforeDistributor = new TokenDistributor(before);
            precedingResult = new ExtractorResult();

            for (LexicalPatternElement elementBefore : elementsBefore) {
                ExtractorResult result = lookupExtractor.nodeExtractor.getWorker().extract(beforeDistributor, elementBefore);

                if (!result.isMatched()) {
                    precedingResult = new ExtractorResult("Could not match element before, caused by: " + result.getErrorMessage());
                    break;
                }

                precedingResult.merge(result);
            }
        }

        NodeLookupExtractor.LookupResult result = new NodeLookupExtractor.LookupResult();
        result.precedingResult = precedingResult;
        result.currentResult = currentResult;

        return result;
    }

}
