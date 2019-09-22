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

package org.panda_lang.framework.language.architecture.module;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.framework.design.architecture.prototype.PrototypeReference;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.language.interpreter.parser.PandaParserFailure;

import java.util.Optional;
import java.util.function.Function;

public final class ModuleLoaderUtils {

    private ModuleLoaderUtils() { }

    public static @Nullable PrototypeReference getReferenceOrNull(Context context, String className) {
        return getReferenceOrOptional(context, className).orElse(null);
    }

    public static Optional<PrototypeReference> getReferenceOrOptional(Context context, String className) {
        return context.getComponent(Components.MODULE_LOADER).forName(className);
    }

    public static PrototypeReference getReferenceOrThrow(Context context, String className, @Nullable Snippet source) {
        return getReferenceOrThrow(context, className, "Unknown type " + className, source);
    }

    public static PrototypeReference getReferenceOrThrow(Context context, String className, String message, @Nullable Snippet source) {
        return getReferenceOrThrow(context, loader -> loader.forName(className), "Unknown type " + className, source);
    }

    public static PrototypeReference getReferenceOrThrow(Context context, Class<?> type, @Nullable Snippet source) {
        return getReferenceOrThrow(context, type, "Unknown type " + type, source);
    }

    public static PrototypeReference getReferenceOrThrow(Context context, Class<?> type, String message, @Nullable Snippet source) {
        return getReferenceOrThrow(context, loader -> loader.forName(type.getCanonicalName()), message, source);
    }

    static PrototypeReference getReferenceOrThrow(Context context, Function<ModuleLoader, Optional<PrototypeReference>> mapper, String message, Snippet source) {
        Optional<PrototypeReference> reference = mapper.apply(context.getComponent(Components.MODULE_LOADER));

        if (!reference.isPresent()) {
            throw PandaParserFailure.builder(message, context)
                    .withStreamOrigin(source)
                    .build();
        }

        return reference.get();
    }

}
