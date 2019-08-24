/*
 * Copyright (c) 2015-2019 Dzikoysk
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

package org.panda_lang.panda.framework.language.resource.head;

import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.ContextParser;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.language.interpreter.parser.loader.Registrable;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.Channel;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.UniversalPipelines;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.language.interpreter.token.PandaSnippet;

import java.util.ArrayList;
import java.util.List;

@Registrable(pipeline = UniversalPipelines.ALL_LABEL)
public class CommentParser implements ContextParser<CommentStatement>, ParserHandler {

    @Override
    public Boolean handle(Context context, Channel channel, Snippet source) {
        return isComment(source.getFirst().getToken());
    }

    @Override
    public CommentStatement parse(Context context) {
        return new CommentStatement(context.getComponent(UniversalComponents.STREAM).read().getValue());
    }

    public static Snippet uncommented(Snippet source) {
        List<TokenRepresentation> uncommentedSource = new ArrayList<>(source.size());

        for (TokenRepresentation tokenRepresentation : source) {
            Token token = tokenRepresentation.getToken();

            if (isComment(token)) {
                continue;
            }

            uncommentedSource.add(tokenRepresentation);
        }

        return new PandaSnippet(uncommentedSource);
    }

    private static boolean isComment(Token token) {
        return token.getType() == TokenType.SEQUENCE && token.getName().isPresent() && token.getName().get().equals("Comment");
    }

}
