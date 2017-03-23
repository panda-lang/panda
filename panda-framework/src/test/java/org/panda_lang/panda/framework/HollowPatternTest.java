/*
 * Copyright (c) 2015-2017 Dzikoysk
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

package org.panda_lang.panda.framework;

import org.panda_lang.panda.framework.tool.match.charset.CharsetPattern;
import org.panda_lang.panda.framework.tool.match.text.TextHollowPattern;

public class HollowPatternTest {

    private static final String EXPRESSION = "instance.extractToken().method(parameter.extractToken())";

    public static void main(String[] args) {
        TextHollowPattern hollowPattern = TextHollowPattern.builder().compile("*.*(*)*").build();
        boolean matched = hollowPattern.match(EXPRESSION);

        System.out.println("[HollowPattern] Matched: " + matched);
        System.out.println("[HollowPattern] " + hollowPattern.getHollows());

        CharsetPattern charsetPattern = new CharsetPattern("*.*(*)");
        charsetPattern.setCharset(new char[]{ '.', '(', ')' });
        matched = charsetPattern.match(EXPRESSION);

        System.out.println("[CharsetPattern] Matched: " + matched);
    }

}
