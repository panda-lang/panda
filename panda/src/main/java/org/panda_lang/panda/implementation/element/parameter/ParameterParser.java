package org.panda_lang.panda.implementation.element.parameter;

import org.panda_lang.core.interpreter.parser.Parser;
import org.panda_lang.core.interpreter.parser.ParserInfo;
import org.panda_lang.core.interpreter.token.Token;
import org.panda_lang.core.interpreter.token.util.TokensSet;

import java.util.List;

public class ParameterParser implements Parser {

    public Parameter parse(ParserInfo parserInfo, TokensSet tokensSet) {
        List<Token> tokens = tokensSet.getTokens();
        Token parameterType = tokens.get(0);
        Token parameterName = tokens.get(1);

        return new Parameter(parameterType.getToken(), parameterName.getName());
    }

}
