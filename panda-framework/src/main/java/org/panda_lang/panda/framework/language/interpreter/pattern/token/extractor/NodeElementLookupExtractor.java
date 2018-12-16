package org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor;

import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternElement;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.MatchableDistributor;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.TokenDistributor;

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
        int index = startIndex;

        MatchableDistributor matchable = new MatchableDistributor(distributor);
        matchable.verifyBefore();

        while (true) {
            // Verify current token
            matchable.verify();

            if (!distributor.hasNext()) {
                break;
            }

            // Move to the next position if distributor is locked
            if (!matchable.isMatchable()) {
                distributor.next();
                continue;
            }

            // backup index and extract current source
            index = distributor.getIndex();
            currentResult = lookupExtractor.nodeExtractor.getWorker().extract(distributor, element);

            if (currentResult.isMatched()) {
                break;
            }

            // restore index and keep looking
            distributor.setIndex(index);
            distributor.next();
        }

        // Match content before matched element
        if (currentResult != null && currentResult.isMatched()) {
            Tokens before = distributor.getSource().subSource(startIndex, index);
            precedingResult = extractWildcards(elementsBefore, new TokenDistributor(before));

            // Compare content instead of length?
            if (precedingResult.isMatched() && before.size() != precedingResult.contentLength()) {
                precedingResult = new ExtractorResult("The content is still unused");
            }

            // Check if source before can be empty (elements before has to be optional)
            else if (precedingResult.isMatched() && before.isEmpty()) {
                for (LexicalPatternElement elementBefore : elementsBefore) {
                    if (elementBefore.isOptional()) {
                        continue;
                    }

                    precedingResult = new ExtractorResult("Cannot match properly the content before");
                    break;
                }
            }
        }

        NodeLookupExtractor.LookupResult result = new NodeLookupExtractor.LookupResult();
        result.precedingResult = precedingResult;
        result.currentResult = currentResult;
        return result;
    }

    protected ExtractorResult extractWildcards(List<LexicalPatternElement> elements,  TokenDistributor distributor) {
        ExtractorResult precedingResult = new ExtractorResult();

        for (LexicalPatternElement element : elements) {
            ExtractorResult result = lookupExtractor.nodeExtractor.getWorker().extract(distributor, element);

            if (!result.isMatched()) {
                precedingResult = new ExtractorResult("Could not match element before, caused by: " + result.getErrorMessage());
                break;
            }

            precedingResult.merge(result);
        }

        return precedingResult;
    }

}
