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

package org.panda_lang.panda.language.syntax.head;

import org.panda_lang.framework.architecture.module.Module;
import org.panda_lang.framework.architecture.type.Reference;
import org.panda_lang.framework.interpreter.parser.Component;
import org.panda_lang.framework.interpreter.parser.Context;
import org.panda_lang.framework.interpreter.parser.ContextParser;
import org.panda_lang.framework.interpreter.parser.pool.Targets;
import org.panda_lang.framework.interpreter.token.Snippet;
import org.panda_lang.framework.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.syntax.PandaSourceReader;
import org.panda_lang.utilities.commons.ArrayUtils;
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
