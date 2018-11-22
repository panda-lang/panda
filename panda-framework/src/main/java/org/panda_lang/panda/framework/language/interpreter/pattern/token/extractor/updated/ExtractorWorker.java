package org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor.updated;

import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternElement;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternNode;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternUnit;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternWildcard;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.TokenDistributor;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.TokenPattern;

class ExtractorWorker {

    protected final TokenPattern pattern;

    private final UnitExtractor unitExtractor = new UnitExtractor(this);
    private final WildcardExtractor wildcardExtractor = new WildcardExtractor(this);
    private final VariantExtractor variantExtractor = new VariantExtractor(this);
    private final NodeExtractor nodeExtractor = new NodeExtractor(this);

    ExtractorWorker(TokenPattern pattern) {
        this.pattern = pattern;
    }

    protected ExtractorResult extract(SourceStream source) {
        TokenDistributor distributor = new TokenDistributor(source.toTokenizedSource());
        ExtractorResult result = extract(pattern.getPatternContent(), distributor);

        if (result.isMatched()) {
            source.read(distributor.length() - distributor.getIndex());
        }

        return result;
    }

    private ExtractorResult extract(LexicalPatternElement element, TokenDistributor distributor) {
        if (element.isUnit()) {
            return extractUnit(element.toUnit(), distributor);
        }

        if (element.isWildcard()) {
            return extractWildcard(element.toWildcard(), distributor);
        }

        if (element.isVariant()) {
            return extractVariant(element.toNode(), distributor);
        }

        if (element.isNode()) {
            return extractNode(element.toNode(), distributor);
        }

        return new ExtractorResult("Unknown element: " + element);
    }

    private ExtractorResult extractUnit(LexicalPatternUnit unit, TokenDistributor distributor) {
        return unitExtractor.extract(unit, distributor);
    }

    private ExtractorResult extractWildcard(LexicalPatternWildcard wildcard, TokenDistributor distributor) {
        return wildcardExtractor.extract(wildcard, distributor);
    }

    private ExtractorResult extractVariant(LexicalPatternNode variant, TokenDistributor distributor) {
        return variantExtractor.extract(variant, distributor);
    }

    private ExtractorResult extractNode(LexicalPatternNode node, TokenDistributor distributor) {
        return nodeExtractor.extract(node, distributor);
    }

}
