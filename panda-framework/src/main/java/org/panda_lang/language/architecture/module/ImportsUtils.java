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

package org.panda_lang.language.architecture.module;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.interpreter.token.Snippetable;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.utilities.commons.function.Option;
import org.panda_lang.utilities.commons.text.MessageFormatter;

import java.util.function.Function;
import java.util.function.Supplier;

public final class ImportsUtils {

    private ImportsUtils() { }

    public static Type getTypeOrThrow(Context<?> context, Snippetable nameSource, String message, String note) {
        String name = nameSource.toSnippet().asSource();

        return context.getImports()
                .forType(name)
                .orThrow((Supplier<? extends PandaParserFailure>) () -> {
                    MessageFormatter formatter = new MessageFormatter();
                    formatter.register("{name}", name);

                    throw new PandaParserFailure(context, nameSource, formatter.format(message), formatter.format(note));
                });
    }

    public static Type getTypeOrThrow(Context<?> context, String className, @Nullable Snippet source) {
        return getTypeOrThrow(context, imports -> imports.forType(className), "Unknown type " + className, source);
    }

    private static Type getTypeOrThrow(Context<?> context, Function<Imports, Option<Type>> mapper, String message, Snippet source) {
        return mapper.apply(context.getImports()).orThrow(() -> {
            throw new PandaParserFailure(context, source, message);
        });
    }

}
