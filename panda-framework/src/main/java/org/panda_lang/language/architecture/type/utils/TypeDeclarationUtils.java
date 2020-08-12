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

package org.panda_lang.language.architecture.type.utils;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.interpreter.token.TokenInfo;
import org.panda_lang.language.interpreter.token.PandaSnippet;
import org.panda_lang.language.interpreter.token.SynchronizedSource;
import org.panda_lang.language.resource.syntax.TokenTypes;
import org.panda_lang.language.resource.syntax.auxiliary.Section;
import org.panda_lang.language.resource.syntax.separator.Separators;
import org.panda_lang.utilities.commons.function.Option;

import java.util.Objects;

public final class TypeDeclarationUtils {

    private TypeDeclarationUtils() { }

    public static Option<Snippet> readType(SynchronizedSource source) {
        Option<Snippet> type = TypeDeclarationUtils.readType(source.getAvailableSource());
        type.peek(tokenRepresentations -> source.next(tokenRepresentations.size()));
        return type;
    }

    public static Option<Snippet> readType(Snippet source) {
        Snippet type = PandaSnippet.createMutable();
        TokenInfo candidate = Objects.requireNonNull(source.get(0));

        if (candidate.getType() != TokenTypes.UNKNOWN) {
            return Option.none();
        }

        type.append(candidate);

        if (!source.hasElement(1)) {
            return Option.of(type);
        }

        candidate = Objects.requireNonNull(source.get(1));

        // read dimensions
        if (isArraySeparator(candidate)) {
            int firstIndex = 1;

            do {
                type.append(candidate);
                candidate = source.hasElement(++firstIndex) ? source.get(firstIndex) : null;
            } while (isArraySeparator(candidate));
        }

        return Option.of(type);
    }

    public static Option<Snippet> readTypeBackwards(Snippet source) {
        Snippet type = PandaSnippet.createMutable();
        TokenInfo candidate = Objects.requireNonNull(source.getLast());

        // read dimensions
        if (isArraySeparator(candidate)) {
            int lastIndex = 0;

            do {
                type.append(candidate);
                candidate = source.getLast(++lastIndex);
            } while (isArraySeparator(candidate));
        }

        type.append(candidate);

        if (candidate == null || candidate.getType() != TokenTypes.UNKNOWN) {
            return Option.none();
        }

        return Option.of(type.reversed());
    }

    public static boolean isArray(Snippet type) {
        return isArraySeparator(type.getLast());
    }

    public static boolean isArraySeparator(@Nullable TokenInfo token) {
        return token != null && token.getType() == TokenTypes.SECTION && token.toToken(Section.class).getSeparator().equals(Separators.SQUARE_BRACKET_LEFT);
    }

}
