package org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor.updated;

import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternElement;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.TokenDistributor;

import java.util.List;

class NodeElementLookupExtractor {

    private final NodeLookupExtractor lookupExtractor;

    NodeElementLookupExtractor(NodeLookupExtractor lookupExtractor) {
        this.lookupExtractor = lookupExtractor;
    }

    NodeLookupExtractor.LookupResult extractNode(List<LexicalPatternElement> elementsBefore, LexicalPatternElement element, TokenDistributor distributor) {
        return null;
    }

}
