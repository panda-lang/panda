package org.panda_lang.panda.implementation.element.parameter;

import org.panda_lang.core.interpreter.parser.Parser;
import org.panda_lang.core.interpreter.parser.ParserInfo;
import org.panda_lang.core.interpreter.token.Token;
import org.panda_lang.core.interpreter.token.TokensSet;

public class ParameterParser implements Parser {

    public Parameter parse(ParserInfo parserInfo, TokensSet tokensSet) {
        Token parameterType = tokensSet.getToken(0);
        Token parameterName = tokensSet.getToken(1);

        return new Parameter(parameterType.getToken(), parameterName.getName());
    }

}
