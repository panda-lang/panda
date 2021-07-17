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

import panda.interpreter.architecture.module.Module;
import panda.interpreter.architecture.type.Reference;
import panda.interpreter.parser.Component;
import panda.interpreter.parser.Context;
import panda.interpreter.parser.ContextParser;
import panda.interpreter.parser.pool.Targets;
import panda.interpreter.token.Snippet;
import panda.interpreter.resource.syntax.keyword.Keywords;
import panda.interpreter.syntax.PandaSourceReader;
import panda.utilities.ArrayUtils;
import panda.std.Completable;
import panda.std.Option;

public final class ExportParser implements ContextParser<Object, Reference> {

    @Override
    public String name() {
        return "export";
    }

    @Override
    public Component<?>[] targets() {
        return ArrayUtils.of(Targets.HEAD);
    }

    @Override
    public Option<Completable<Reference>> parse(Context<?> context) {
        PandaSourceReader sourceReader = new PandaSourceReader(context.getStream());

        if (sourceReader.read(Keywords.EXPORT).isEmpty()) {
            return Option.none();
        }

        Option<Snippet> javaQualifier = sourceReader.readPandaQualifier();

        if (javaQualifier.isEmpty()) {
            return Option.none();
        }

        Module module = context.getScript().getModule();
        Reference reference = module.add(new Reference(ConveyanceUtils.fetchType(context, javaQualifier.get())));
        return Option.ofCompleted(reference);
    }

}
