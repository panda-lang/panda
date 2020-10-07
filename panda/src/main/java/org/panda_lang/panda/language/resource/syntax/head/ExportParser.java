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

import org.panda_lang.language.architecture.module.Module;
import org.panda_lang.language.architecture.type.Reference;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.ContextParser;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.language.interpreter.parser.pool.Targets;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.interpreter.parser.PandaSourceReader;
import org.panda_lang.utilities.commons.ArrayUtils;
import org.panda_lang.utilities.commons.collection.Component;
import org.panda_lang.utilities.commons.function.Option;

import java.util.concurrent.CompletableFuture;

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
    public Option<CompletableFuture<Reference>> parse(Context<Object> context) {
        PandaSourceReader sourceReader = new PandaSourceReader(context.getStream());

        if (sourceReader.read(Keywords.EXPORT).isEmpty()) {
            return Option.none();
        }

        Option<Snippet> javaQualifier = sourceReader.readPandaQualifier();

        if (javaQualifier.isEmpty()) {
            return Option.none();
        }

        Module module = context.getScript().getModule().orThrow(() -> {
           throw new PandaParserFailure(context, context.getSource(), "Script does not declare module");
        });

        Reference reference = module.add(new Reference(ConveyanceUtils.fetchType(context, javaQualifier.get())));
        return Option.of(CompletableFuture.completedFuture(reference));
    }

}
