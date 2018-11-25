package org.panda_lang.panda.framework.language.interpreter.pattern.token.wildcard.condition;

import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;

public interface WildcardCondition {

    WildcardConditionResult accept(TokenRepresentation representation);

}
