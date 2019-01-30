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

package org.panda_lang.panda.framework.design.architecture.module;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;

import java.util.Optional;
import java.util.function.Function;

public class ModuleLoaderUtils {

    public static @Nullable ClassPrototypeReference getReferenceOrNull(ParserData data, String className) {
        return getReferenceOrOptional(data, className).orElse(null);
    }

    public static Optional<ClassPrototypeReference> getReferenceOrOptional(ParserData data, String className) {
        return data.getComponent(UniversalComponents.MODULE_LOADER).forClass(className);
    }

    public static ClassPrototypeReference getReferenceOrThrow(ParserData data, String className, @Nullable Tokens source) {
        return getReferenceOrThrow(data, className, "Unknown type " + className, source);
    }

    public static ClassPrototypeReference getReferenceOrThrow(ParserData data, String className, String message, @Nullable Tokens source) {
        return getReferenceOrThrow(data, loader -> loader.forClass(className), "Unknown type " + className, source);
    }

    public static ClassPrototypeReference getReferenceOrThrow(ParserData data, Class<?> type, @Nullable Tokens source) {
        return getReferenceOrThrow(data, type, "Unknown type " + type, source);
    }

    public static ClassPrototypeReference getReferenceOrThrow(ParserData data, Class<?> type, String message, @Nullable Tokens source) {
        return getReferenceOrThrow(data, loader -> loader.forClass(type), message, source);
    }

    static ClassPrototypeReference getReferenceOrThrow(ParserData data, Function<ModuleLoader, Optional<ClassPrototypeReference>> mapper, String message, Tokens source) {
        Optional<ClassPrototypeReference> reference = mapper.apply(data.getComponent(UniversalComponents.MODULE_LOADER));

        if (!reference.isPresent()) {
            throw new PandaParserFailure(message, data, source);
        }

        return reference.get();
    }

}
