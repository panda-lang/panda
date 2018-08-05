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

package org.panda_lang.panda.utilities.commons.pattern.text;

import org.panda_lang.panda.utilities.commons.objects.StringUtils;
import org.panda_lang.panda.utilities.commons.pattern.PatternMatcher;

import java.util.ArrayList;
import java.util.List;

public class TextHollowPattern implements PatternMatcher {

    private final List<TextHollowSymbol> hollowSymbols;
    private final List<String> hollows;

    protected TextHollowPattern(List<TextHollowSymbol> hollowSymbols) {
        this.hollowSymbols = hollowSymbols;
        this.hollows = new ArrayList<>();
    }

    @Override
    public boolean match(String expression) {
        hollows.clear();

        for (int f = 0; f < hollowSymbols.size(); f++) {
            TextHollowSymbol hollowSymbol = hollowSymbols.get(f);

            loop:
            if (hollowSymbol.isBasis()) {
                for (String variant : hollowSymbol.getVariants()) {
                    String part = getPart(expression, 0, variant.length());

                    if (variant.equals(part)) {
                        expression = expression.substring(variant.length(), expression.length());
                        break loop;
                    }
                }

                return false;
            }
            else if (hollowSymbol.isHollow()) {
                boolean inRange = hollowSymbols.size() > hollowSymbol.getIndex() + 1;
                TextHollowSymbol nextHollowSymbol = inRange ? hollowSymbols.get(hollowSymbol.getIndex() + 1) : hollowSymbol;

                for (String variant : nextHollowSymbol.getVariants()) {
                    int partIndex = expression.indexOf(variant);

                    if (partIndex < 0) {
                        return false;
                    }

                    String hollow = expression.substring(0, partIndex);
                    expression = expression.substring(partIndex, expression.length());

                    hollows.add(hollow);
                    break loop;
                }

                hollows.add(expression);
                expression = StringUtils.EMPTY;
            }
        }

        return expression.length() < 1;
    }

    public void clear() {
        hollows.clear();
    }

    public List<String> cloneHollows() {
        return new ArrayList<>(hollows);
    }

    public String getPart(String string, int beginIndex, int endIndex) {
        return string.length() >= endIndex ? string.substring(beginIndex, endIndex) : string.substring(beginIndex, string.length());
    }

    public List<String> getHollows() {
        return hollows;
    }

    public List<TextHollowSymbol> getHollowSymbols() {
        return hollowSymbols;
    }

    @Override
    public String toString() {
        return hollowSymbols.toString();
    }

    public static TextHollowPatternBuilder builder() {
        return new TextHollowPatternBuilder();
    }

}
