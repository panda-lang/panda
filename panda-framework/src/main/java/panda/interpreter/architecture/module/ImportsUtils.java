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

package panda.interpreter.architecture.module;

import org.jetbrains.annotations.Nullable;
import panda.interpreter.architecture.type.Reference;
import panda.interpreter.architecture.type.Type;
import panda.interpreter.parser.Context;
import panda.interpreter.parser.PandaParserFailure;
import panda.interpreter.token.Snippet;
import panda.std.Option;

import java.util.function.Function;

public final class ImportsUtils {

    private ImportsUtils() { }

    public static Type getTypeOrThrow(Context<?> context, String className, @Nullable Snippet source) {
        return getTypeOrThrow(context, imports -> imports.forType(className).map(Reference::fetchType), "Unknown type " + className, source);
    }

    private static Type getTypeOrThrow(Context<?> context, Function<Imports, Option<Type>> mapper, String message, Snippet source) {
        return mapper.apply(context.getImports()).orThrow(() -> {
            throw new PandaParserFailure(context, source, message);
        });
    }

}
