package org.panda_lang.panda.language.structure.general.comment;

import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.language.interpreter.token.PandaTokenizedSource;

import java.util.ArrayList;
import java.util.List;

public class CommentAssistant {

    public TokenizedSource uncomment(TokenizedSource source) {
        List<TokenRepresentation> uncommentedSource = new ArrayList<>(source.size());

        for (TokenRepresentation tokenRepresentation : source.getTokensRepresentations()) {
            Token token = tokenRepresentation.getToken();

            if (token != null && token.getType() == TokenType.SEQUENCE && token.getName().equals("Comment")) {
                continue;
            }

            uncommentedSource.add(tokenRepresentation);
        }
        
        return new PandaTokenizedSource(uncommentedSource);
    }

}
