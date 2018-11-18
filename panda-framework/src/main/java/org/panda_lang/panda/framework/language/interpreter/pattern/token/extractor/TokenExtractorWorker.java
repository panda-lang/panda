package org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor;

import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternElement;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternNode;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.TokenDistributor;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.TokenPattern;

import java.util.LinkedHashMap;
import java.util.Map;

class TokenExtractorWorker {

    protected final TokenPattern pattern;
    protected final Map<String, Tokens> results = new LinkedHashMap<>();

    private final WorkerDynamicContent dynamicContent = new WorkerDynamicContent(this);
    private final WorkerWildcardContent wildcardContent = new WorkerWildcardContent(this);

    TokenExtractorWorker(TokenPattern pattern) {
        this.pattern = pattern;
    }

    TokenExtractorResult extract(SourceStream source) {
        TokenDistributor distributor = new TokenDistributor(source.toTokenizedSource());
        TokenExtractorResult result = extract(pattern.getPatternContent(), distributor);

        if (result.isMatched()) {
            source.read(distributor.getIndex());
        }

        return result;
    }

    protected TokenExtractorResult extract(LexicalPatternElement element, TokenDistributor distributor) {
        if (!distributor.hasNext()) {
            return new TokenExtractorResult("Distributor does not have content");
        }

        if (element.isUnit()) {
            return new TokenExtractorResult(element.toUnit().getValue().equals(distributor.next().getTokenValue())).identified(element.getIdentifier());
        }

        if (element.isWildcard()) {
            return wildcardContent.matchWildcard(element.toWildcard(), distributor).identified(element.getIdentifier());
        }

        LexicalPatternNode node = element.toNode();

        if (node.isVariant()) {
            return matchVariant(node, distributor).identified(node.getIdentifier());
        }

        return dynamicContent.matchDynamicContent(node, distributor).identified(node.getIdentifier());
    }


    private TokenExtractorResult matchVariant(LexicalPatternNode variantNode, TokenDistributor reader) {
        if (!variantNode.isVariant()) {
            throw new RuntimeException("The specified node is not marked as a variant node");
        }

        for (LexicalPatternElement variantElement : variantNode.getElements()) {
            TokenExtractorResult result = this.extract(variantElement, reader);

            if (result.isMatched()) {
                return result.identified(variantElement.getIdentifier());
            }
        }

        return new TokenExtractorResult("Variant does not matched");
    }

}
