package org.panda_lang.panda.framework.language.interpreter.pattern.token;

import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.interpreter.token.stream.TokenReader;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternElement;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternNode;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaTokenReader;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TokenExtractorWorker {

    private final TokenPattern pattern;
    private final Map<String, TokenizedSource> results = new LinkedHashMap<>();

    public TokenExtractorWorker(TokenPattern pattern) {
        this.pattern = pattern;
    }

    public TokenExtractorResult extract(TokenizedSource source) {
        LexicalPatternElement content = pattern.getPatternContent();
        return extract(pattern.getPatternContent(), new PandaTokenReader(source));
    }

    protected TokenExtractorResult extract(LexicalPatternElement element, TokenReader reader) {
        if (!reader.hasNext()) {
            return new TokenExtractorResult();
        }

        if (element.isUnit()) {
            return new TokenExtractorResult(element.toUnit().getValue().equals(reader.next().getTokenValue()));
        }

        if (element.isWildcard()) {
            return null;
        }

        LexicalPatternNode node = element.toNode();

        if (node.isVariant()) {
            return this.matchVariant(node, reader);
        }

        List<LexicalPatternElement> elements = node.getElements();
        TokenizedSource[] dynamics = this.matchUnits(elements, reader);

        if (dynamics == null) {
            return new TokenExtractorResult();
        }

        return this.matchDynamics(elements, dynamics);
    }

    private TokenizedSource[] matchUnits(List<LexicalPatternElement> elements, TokenReader reader) {
        TokenizedSource[] dynamics = new TokenizedSource[elements.size()];
        return null;
    }

    private TokenExtractorResult matchDynamics(List<LexicalPatternElement> elements, TokenizedSource[] dynamics) {
        TokenExtractorResult result = new TokenExtractorResult(true);

        for (int i = 0; i < elements.size(); i++) {
            LexicalPatternElement nodeElement = elements.get(i);

            if (nodeElement.isUnit()) {
                continue;
            }

            if (dynamics.length == 0 && nodeElement.isOptional()) {
                continue;
            }

            TokenizedSource nodeContent = dynamics[i];
            dynamics[i] = null;

            if (nodeContent == null) {
                return new TokenExtractorResult();
            }

            TokenExtractorResult nodeElementResult = this.extract(nodeElement, new PandaTokenReader(nodeContent));

            if (!nodeElementResult.isMatched()) {
                return new TokenExtractorResult();
            }

            result.addIdentifier(nodeElement.getIdentifier());
            result.merge(nodeElementResult);
        }

        for (TokenizedSource dynamicContent : dynamics) {
            if (dynamicContent != null) {
                return new TokenExtractorResult(false);
            }
        }

        return result;
    }

    private TokenExtractorResult matchVariant(LexicalPatternNode variantNode, TokenReader reader) {
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
