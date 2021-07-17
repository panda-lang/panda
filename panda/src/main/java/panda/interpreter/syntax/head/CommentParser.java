/*
 * Copyright (c) 2021 dzikoysk
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

package panda.interpreter.syntax.head;

import panda.interpreter.parser.Context;
import panda.interpreter.parser.ContextParser;
import panda.interpreter.parser.SourceReader;
import panda.interpreter.parser.pool.Targets;
import panda.interpreter.resource.syntax.sequence.SequencesUtils;
import panda.utilities.ArrayUtils;
import panda.interpreter.parser.Component;
import panda.std.Completable;
import panda.std.Option;

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
