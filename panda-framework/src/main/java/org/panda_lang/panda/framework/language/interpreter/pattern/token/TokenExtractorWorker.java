package org.panda_lang.panda.framework.language.interpreter.pattern.token;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternElement;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternNode;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternUnit;
import org.panda_lang.panda.framework.language.interpreter.token.PandaTokenizedSource;

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
            return new TokenExtractorResult();
        }

        LexicalPatternNode node = element.toNode();

        if (node.isVariant()) {
            return this.matchVariant(node, distributor);
        }

        List<LexicalPatternElement> elements = node.getElements();
        TokenizedSource[] dynamics = this.matchUnits(elements, distributor);

        if (dynamics == null) {
            return new TokenExtractorResult();
        }

        return this.matchDynamics(elements, dynamics);
    }

    private @Nullable TokenizedSource[] matchUnits(List<LexicalPatternElement> elements, TokenDistributor distributor) {
        TokenizedSource[] dynamics = new TokenizedSource[elements.size()];

        for (int i = 0; i < elements.size(); i++) {
            LexicalPatternElement element = elements.get(i);

            if (!element.isUnit()) {
                continue;
            }

            LexicalPatternUnit unit = element.toUnit();
            boolean found = false;
            int dynamic = 0;

            for (TokenRepresentation representation : distributor) {
                if (representation.getTokenValue().equals(unit.getValue())) {
                    found = true;
                    break;
                }

                dynamic++;
            }

            if (!found && unit.isOptional()) {
                continue;
            }

            if (!found && (distributor.getIndex() + dynamic != distributor.length()) && !unit.isOptional()) {
                return null;
            }

            if (dynamic == 0) {
                distributor.next();
                continue;
            }

            if ((found) || (distributor.getIndex() + dynamic == distributor.length() && unit.isOptional())) {
                dynamics[getLastDynamicIndex(dynamics)] = new PandaTokenizedSource(distributor.next(dynamic));
                distributor.next();
            }
        }

        return dynamics;
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

            TokenDistributor content = new TokenDistributor(nodeContent);
            TokenExtractorResult nodeElementResult = this.extract(nodeElement, content);

            if (!nodeElementResult.isMatched()) {
                return new TokenExtractorResult();
            }

            if (content.hasNext()) {
                int nextIndex = i + 1;

                if (nextIndex >= dynamics.length || dynamics[nextIndex] != null) {
                    return new TokenExtractorResult();
                }

                dynamics[nextIndex] = new PandaTokenizedSource(content.next(content.length() - content.getIndex()));
            }

            result.addIdentifier(nodeElement.getIdentifier());
            result.merge(nodeElementResult);
        }

        for (TokenizedSource dynamicContent : dynamics) {
            if (dynamicContent != null) {
                return new TokenExtractorResult();
            }
        }

        return result;
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

    private int getLastDynamicIndex(TokenizedSource[] dynamics) {
        for (int i = dynamics.length - 1; i > -1; i--) {
            if (dynamics[i] == null) {
                continue;
            }

            return i + 1;
        }

        return 0;
    }

}
