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

package org.panda_lang.panda.language.resource.syntax.head;

import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.ContextParser;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Channel;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Handler;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Pipelines;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.Token;
import org.panda_lang.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.framework.language.interpreter.token.PandaSnippet;
import org.panda_lang.framework.language.resource.syntax.TokenTypes;
import org.panda_lang.panda.language.interpreter.parser.RegistrableParser;

import java.util.ArrayList;
import java.util.List;

@RegistrableParser(pipeline = Pipelines.ALL_LABEL)
public final class CommentParser implements ContextParser<CommentStatement>, Handler {

    @Override
    public Boolean handle(Context context, Channel channel, Snippet source) {
        return isComment(source.getFirst().getToken());
    }

    @Override
    public CommentStatement parse(Context context) {
        return new CommentStatement(context.getComponent(Components.STREAM).read());
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
        return token.getType() == TokenTypes.SEQUENCE && token.getName().isPresent() && token.getName().get().equals("Comment");
    }

}
