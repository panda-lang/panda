package org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor;

import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;

public interface WildcardCondition {

    boolean accept(TokenRepresentation representation);

}
