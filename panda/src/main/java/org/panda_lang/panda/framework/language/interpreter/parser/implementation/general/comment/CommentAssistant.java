/*
 * Copyright (c) 2015-2018 Dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.comment;

import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.language.interpreter.token.PandaTokenizedSource;

import java.util.ArrayList;
import java.util.List;

public class CommentAssistant {

    public static TokenizedSource uncomment(TokenizedSource source) {
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
