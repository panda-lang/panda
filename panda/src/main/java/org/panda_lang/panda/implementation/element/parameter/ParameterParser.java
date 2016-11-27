package org.panda_lang.panda.implementation.element.parameter;

import org.panda_lang.framework.interpreter.lexer.TokenRepresentation;
import org.panda_lang.framework.interpreter.lexer.TokenizedSource;
import org.panda_lang.framework.interpreter.parser.Parser;
import org.panda_lang.framework.interpreter.parser.ParserInfo;
import org.panda_lang.framework.interpreter.token.Token;
import org.panda_lang.framework.interpreter.token.TokenType;
import org.panda_lang.panda.implementation.interpreter.parser.PandaParserException;

import java.util.ArrayList;
import java.util.List;

public class ParameterParser implements Parser {

    public List<Parameter> parse(ParserInfo parserInfo, TokenizedSource tokenizedSource) {
        TokenRepresentation[] tokenRepresentations = tokenizedSource.toArray();
        List<Parameter> parameters = new ArrayList<>(tokenRepresentations.length / 3 + 1);

        for (int i = 0; i < tokenRepresentations.length; i += 3) {
            TokenRepresentation parameterTypeRepresentation = tokenRepresentations[i];
            TokenRepresentation parameterNameRepresentation = tokenRepresentations[i + 1];

            String type = parameterTypeRepresentation.getToken().getTokenValue();
            String name = parameterNameRepresentation.getToken().getTokenValue();

            Parameter parameter = new Parameter(type, name);
            parameters.add(parameter);

            if (i + 2 < tokenRepresentations.length) {
                TokenRepresentation separatorRepresentation = tokenRepresentations[i + 2];
                Token separator = separatorRepresentation.getToken();

                if (separator.getType() != TokenType.SEPARATOR) {
                    throw new PandaParserException("Unexpected token " + separatorRepresentation);
                }
            }
        }

        return parameters;
    }

}
