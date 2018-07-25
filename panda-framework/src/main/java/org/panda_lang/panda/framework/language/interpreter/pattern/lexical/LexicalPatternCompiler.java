/*
 * Copyright (c) 2016-2018 Dzikoysk
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

package org.panda_lang.panda.framework.language.interpreter.pattern.lexical;

import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.*;
import org.panda_lang.panda.utilities.commons.arrays.*;
import org.panda_lang.panda.utilities.commons.arrays.character.*;
import org.panda_lang.panda.utilities.commons.objects.*;

import java.util.*;

public class LexicalPatternCompiler {

    private static final char[] IDENTIFIER_CHARACTERS = CharacterUtils.mergeArrays(CharacterUtils.LETTERS, CharacterUtils.DIGITS, CharacterUtils.arrayOf('-'));

    public LexicalPatternElement compile(String pattern) {
        List<LexicalPatternElement> elements = new ArrayList<>();
        StringBuilder unitBuilder = new StringBuilder();

        CharArrayDistributor distributor = new CharArrayDistributor(pattern.toCharArray());
        BracketContentReader contentReader = new BracketContentReader(distributor);

        while (distributor.hasNext()) {
            char currentChar = distributor.next();
            String identifier = null;

            if ((currentChar == '[' || currentChar == '<' || currentChar == '(' || currentChar == '*') && unitBuilder.length() > 0) {
                String unitContent = unitBuilder.toString();
                unitBuilder.setLength(0);

                if (!StringUtils.isEmpty(unitContent)) {
                    identifier = this.compileIdentifier(unitContent);
                    boolean current = false;

                    if (identifier != null) {
                        int identifierIndex = unitContent.trim().indexOf(identifier);

                        if (identifierIndex == 0) {
                            current = true;
                            unitContent = unitContent.substring(unitContent.indexOf(':') + 1);
                        }
                        else {
                            unitContent = unitContent.substring(0, unitContent.lastIndexOf(':') - identifier.length());
                        }
                    }

                    if (!StringUtils.isEmpty(unitContent)) {
                        LexicalPatternUnit unit = new LexicalPatternUnit(unitContent);

                        if (current) {
                            unit.setIdentifier(identifier);
                            identifier = null;
                        }

                        elements.add(unit);
                    }
                }
            }

            char previousChar = distributor.getPrevious();
            LexicalPatternElement element = null;

            if (currentChar == '[') {
                element = this.compileOptional(contentReader.readCurrent());
            }
            else if (currentChar == '(') {
                element = this.compileVariant(contentReader.readCurrent());
            }
            else if (currentChar == '<') {
                element = new LexicalPatternWildcard(contentReader.readCurrent());
            }
            else if (currentChar == '*') {
                element = new LexicalPatternWildcard();
            }
            else {
                unitBuilder.append(currentChar);
            }

            if (element != null) {
                LexicalPatternElement.Isolation parentIsolation = LexicalPatternElement.Isolation.of(previousChar, distributor.getNext());
                LexicalPatternElement.Isolation commonIsolation = LexicalPatternElement.Isolation.merge(parentIsolation, element.getIsolationType());
                element.setIsolationType(commonIsolation);

                if (identifier != null) {
                    element.setIdentifier(identifier);
                }

                elements.add(element);
            }
        }

        if (unitBuilder.length() > 0) {
            LexicalPatternUnit unit = new LexicalPatternUnit(unitBuilder.toString());
            elements.add(unit);
        }

        if (elements.size() == 0) {
            throw new RuntimeException("Empty element");
        }

        return elements.size() == 1 ? elements.get(0) : new LexicalPatternNode(elements);
    }

    private LexicalPatternElement compileOptional(String pattern) {
        LexicalPatternElement element = this.compile(pattern);
        element.setOptional(true);
        return element;
    }

    private LexicalPatternElement compileVariant(String pattern) {
        AttentiveContentReader contentReader = new AttentiveContentReader(pattern);

        List<String> variants = contentReader.select('|');
        List<LexicalPatternElement> elements = new ArrayList<>(variants.size());

        for (String variant : variants) {
            elements.add(this.compile(variant));
        }

        return new LexicalPatternNode(elements, true);
    }

    private String compileIdentifier(String pattern) {
        pattern = pattern.trim();

        if (pattern.length() < 2 || !pattern.contains(":")) {
            return null;
        }

        String identifier;

        if (pattern.endsWith(":")) {
            int lastIndex = pattern.lastIndexOf(" ");
            identifier = pattern.substring(lastIndex == -1 ? 0 : lastIndex + 1, pattern.length() - 1);
        }
        else {
            AttentiveContentReader contentReader = new AttentiveContentReader(pattern);
            List<String> variants = contentReader.select(':');

            if (variants.size() < 2) {
                return null;
            }

            identifier = variants.get(0).trim();

            if (identifier.contains(" ")) {
                return null;
            }
        }

        if (StringUtils.isEmpty(identifier)) {
            return null;
        }

        if (StringUtils.containsOtherCharacters(identifier, IDENTIFIER_CHARACTERS)) {
            return null;
        }

        return identifier;
    }

}
