/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.framework.language.interpreter.pattern.abyss.extractor;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.AbyssPatternUnit;

/**
 * Utils for {@link TokenizedSource} based on {@link AbyssExtractorSource}
 */
public class AbyssExtractorSourceUtils {

    public static int countHardTypedUnits(AbyssPatternUnit[] units) {
        int i = 0;

        for (AbyssPatternUnit unit : units) {
            if (unit.isOptional()) {
                continue;
            }

            if (unit.isAbyss()) {
                continue;
            }

            i++;
        }

        return i;
    }

    public static int countGaps(AbyssPatternUnit[] units) {
        int i = 0;

        for (AbyssPatternUnit unit : units) {
            if (unit.isAbyss()) {
                i++;
            }
        }

        return i;
    }

    /**
     * @return index of the specified token in array, returns -1 if the token was not found
     */
    public static int indexOf(AbyssExtractorSource source, Token search) {
        return indexOf(source, search, 0, 0);
    }

    /**
     * @return index of the specified token in array with a defined start-index, returns -1 if the token was not found
     */
    public static int indexOf(AbyssExtractorSource source, Token search, int minIndex, int maxNestingLevel) {
        return indexOf(source, search, minIndex, maxNestingLevel, null);
    }

    /**
     * @return index of the specified token in array with a defined start-index, returns -1 if the token was not found or a specified before-token was reached
     */
    public static int indexOf(AbyssExtractorSource source, Token search, int minIndex, int maxNestingLevel, @Nullable Token before) {
        AbyssTokenRepresentation[] representations = source.getAbyssRepresentations();

        for (int i = minIndex; i < representations.length; i++) {
            AbyssTokenRepresentation representation = representations[i];

            if (representation.getNestingLevel() > maxNestingLevel) {
                continue;
            }

            Token token = representation.getTokenRepresentation().getToken();

            if (before != null && before.equals(token) && !before.equals(search)) {
                break;
            }

            if (search.equals(token)) {
                return i;
            }
        }

        return -1;
    }

    public static int lastIndexOf(AbyssExtractorSource source, Token search) {
        return lastIndexOf(source, search, 0, 0, null);
    }

    /**
     * @return last index of the specified token in array, returns -1 if the token was not found
     */
    public static int lastIndexOf(AbyssExtractorSource source, Token search, int minIndex, int maxNestingLevel, @Nullable Token before) {
        AbyssTokenRepresentation[] representations = source.getAbyssRepresentations();

        int indexOfBefore = before != null ? indexOf(source, before) : -1;
        int startIndex = indexOfBefore != -1 ? indexOfBefore - 1 : representations.length - 1;

        for (int i = startIndex; i > minIndex - 1; i--) {
            AbyssTokenRepresentation representation = representations[i];

            if (representation.getNestingLevel() > maxNestingLevel) {
                continue;
            }

            Token token = representation.getTokenRepresentation().getToken();

            if (search.equals(token)) {
                return i;
            }
        }

        return -1;
    }

}
