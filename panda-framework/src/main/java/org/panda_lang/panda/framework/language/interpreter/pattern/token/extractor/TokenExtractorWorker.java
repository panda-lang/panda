package org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor;

import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternElement;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternNode;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternWildcard;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.TokenDistributor;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.TokenPattern;
import org.panda_lang.panda.framework.language.interpreter.token.PandaTokenizedSource;

import java.util.LinkedHashMap;
import java.util.Map;

class TokenExtractorWorker {

    private final TokenPattern pattern;
    private final WorkerDynamicContent dynamicContent = new WorkerDynamicContent(this);
    private final Map<String, TokenizedSource> results = new LinkedHashMap<>();

    TokenExtractorWorker(TokenPattern pattern) {
        this.pattern = pattern;
    }

    TokenExtractorResult extract(TokenizedSource source) {
        LexicalPatternElement content = pattern.getPatternContent();
        return extract(pattern.getPatternContent(), new TokenDistributor(source));
    }

    protected TokenExtractorResult extract(LexicalPatternElement element, TokenDistributor distributor) {
        if (!distributor.hasNext()) {
            return new TokenExtractorResult();
        }

        if (element.isUnit()) {
            return new TokenExtractorResult(element.toUnit().getValue().equals(distributor.next().getTokenValue()));
        }

        if (element.isWildcard()) {
            return matchWildcard(element.toWildcard(), distributor);
        }

        LexicalPatternNode node = element.toNode();

        if (node.isVariant()) {
            return matchVariant(node, distributor);
        }

        return dynamicContent.matchDynamicContent(node, distributor);
    }

    private TokenExtractorResult matchWildcard(LexicalPatternWildcard wildcard, TokenDistributor distributor) {
        TokenizedSource wildcardContent;

        if (wildcard.getDetails() != null && wildcard.getDetails().startsWith("*")) {
            wildcardContent = new PandaTokenizedSource(distributor.next(distributor.length() - distributor.getIndex()));
        }
        else {
            wildcardContent = new PandaTokenizedSource(distributor.next());
        }

        return new TokenExtractorResult(true).addWildcard(wildcardContent);
    }

    private TokenExtractorResult matchVariant(LexicalPatternNode variantNode, TokenDistributor reader) {
        if (!variantNode.isVariant()) {
            throw new RuntimeException("The specified node is not marked as a variant node");
        }

        for (LexicalPatternElement variantElement : variantNode.getElements()) {
            TokenExtractorResult result = this.extract(variantElement, reader);

            if (result.isMatched()) {
                return result;
            }
        }

        return new TokenExtractorResult();
    }

}
