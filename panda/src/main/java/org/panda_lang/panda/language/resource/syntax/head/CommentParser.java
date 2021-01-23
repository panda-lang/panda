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
import org.panda_lang.language.interpreter.parser.SourceReader;
import org.panda_lang.language.interpreter.parser.pool.Targets;
import org.panda_lang.language.resource.syntax.sequence.SequencesUtils;
import org.panda_lang.utilities.commons.ArrayUtils;
import org.panda_lang.language.interpreter.parser.Component;
import org.panda_lang.utilities.commons.function.Completable;
import org.panda_lang.utilities.commons.function.Option;

public final class CommentParser implements ContextParser<Object, CommentStatement> {

    @Override
    public String name() {
        return "comment";
    }

    @Override
    public Component<?>[] targets() {
        return ArrayUtils.of(Targets.ALL);
    }

    @Override
    public Option<Completable<CommentStatement>> parse(Context<?> context) {
        return new SourceReader(context.getStream())
                .read(SequencesUtils::isComment)
                .map(CommentStatement::new)
                .map(Completable::completed);
    }

}
