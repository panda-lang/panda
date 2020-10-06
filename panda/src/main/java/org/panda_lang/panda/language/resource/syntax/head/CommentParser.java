/*
 * Copyright (c) 2020 Dzikoysk
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

import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.ContextParser;
import org.panda_lang.language.interpreter.parser.Parser;
import org.panda_lang.language.interpreter.parser.pool.Targets;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.resource.syntax.sequence.SequencesUtils;
import org.panda_lang.utilities.commons.ArrayUtils;

public final class CommentParser implements ContextParser<CommentStatement>, Handler {

    @Override
    public Target<? extends Parser>[] pipeline() {
        return ArrayUtils.of(Targets.ALL);
    }

    @Override
    public Boolean handle(Context context, LocalChannel channel, Snippet source) {
        return SequencesUtils.isComment(source.getFirst().getToken());
    }

    @Override
    public CommentStatement parse(Context context) {
        return new CommentStatement(context.getComponent(Components.STREAM).read());
    }

}
