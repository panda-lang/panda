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

package org.panda_lang.panda.language.resource.expression.subparsers.assignation.variable;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.Snippet;
import org.panda_lang.panda.language.interpreter.token.PandaSnippet;
import org.panda_lang.panda.language.resource.syntax.auxiliary.Section;
import org.panda_lang.panda.language.resource.syntax.separator.Separators;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class VariableDeclarationUtils {

    public static Optional<Snippet> readType(Snippet source) {
        Snippet type = new PandaSnippet();
        TokenRepresentation candidate = Objects.requireNonNull(source.get(0));

        if (candidate.getType() != TokenType.UNKNOWN) {
            return Optional.empty();
        }

        type.addToken(candidate);

        if (!source.hasElement(1)) {
            return Optional.of(type);
        }

        candidate = Objects.requireNonNull(source.get(1));

        // read dimensions
        if (isArraySeparator(candidate)) {
            int firstIndex = 1;

            do {
                type.addToken(candidate);
                candidate = source.hasElement(++firstIndex) ? source.get(firstIndex) : null;
            } while (isArraySeparator(candidate));
        }

        return Optional.of(type);
    }

    public static Optional<Snippet> readTypeBackwards(Snippet source) {
        Snippet type = new PandaSnippet();
        TokenRepresentation candidate = Objects.requireNonNull(source.getLast());

        // read dimensions
        if (isArraySeparator(candidate)) {
            int lastIndex = 0;

            do {
                type.addToken(candidate);
                candidate = source.getLast(++lastIndex);
            } while (isArraySeparator(candidate));
        }

        type.addToken(candidate);

        if (candidate == null || candidate.getType() != TokenType.UNKNOWN) {
            return Optional.empty();
        }

        return Optional.of(type.reversed());
    }

    public static List<Section> getArraySections(Snippet type) {
        List<Section> sections = new ArrayList<>();

        for (int index = type.size() - 1; index >= 0; index--) {
            TokenRepresentation representation = type.get(index);

            if (representation.getType() != TokenType.SECTION) {
                break;
            }

            sections.add(representation.toToken());
        }

        return sections;
    }

    public static boolean isArray(Snippet type) {
        return isArraySeparator(type.getLast());
    }

    public static boolean isArraySeparator(@Nullable TokenRepresentation token) {
        return token != null && token.getType() == TokenType.SECTION && token.toToken(Section.class).getSeparator().equals(Separators.SQUARE_BRACKET_LEFT);
    }

}
