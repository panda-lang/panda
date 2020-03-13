/*
 * Copyright (c) 2015-2020 Dzikoysk
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

package org.panda_lang.framework.language.resource.syntax.sequence;

import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.Token;
import org.panda_lang.framework.design.interpreter.token.TokenInfo;
import org.panda_lang.framework.language.interpreter.token.PandaSnippet;
import org.panda_lang.framework.language.resource.syntax.TokenTypes;

import java.util.ArrayList;
import java.util.List;

public final class SequencesUtils {

    private SequencesUtils() { }

    public static Snippet uncommented(Snippet source) {
        List<TokenInfo> uncommentedSource = new ArrayList<>(source.size());

        for (TokenInfo tokenInfo : source) {
            Token token = tokenInfo.getToken();

            if (SequencesUtils.isComment(token)) {
                continue;
            }

            uncommentedSource.add(tokenInfo);
        }

        return new PandaSnippet(uncommentedSource);
    }

    public static boolean isComment(Token token) {
        return token.getType() == TokenTypes.SEQUENCE && token.getName().isPresent() && token.getName().get().equals("Comment");
    }

}
