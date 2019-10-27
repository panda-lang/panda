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
import org.panda_lang.framework.design.architecture.module.Imports;
import org.panda_lang.framework.design.architecture.prototype.Reference;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.language.interpreter.parser.PandaParserFailure;

import java.util.Optional;
import java.util.function.Function;

public final class PandaImportsUtils {

    private PandaImportsUtils() { }

    public static Reference getReferenceThrow(Context context, String className, @Nullable Snippet source) {
        return getReferenceThrow(context, imports -> imports.forName(className), "Unknown type " + className, source);
    }

    static Reference getReferenceThrow(Context context, Function<Imports, Optional<Reference>> mapper, String message, Snippet source) {
        Optional<Reference> reference = mapper.apply(context.getComponent(Components.IMPORTS));

        if (!reference.isPresent()) {
            throw new PandaParserFailure(context, source, message);
        }

        return reference.get();
    }

}
