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

import java.util.ArrayList;
import java.util.List;

public class TextHollowPatternUtils {

    public static List<String> toFragments(String lightLine) {
        List<String> fragments = new ArrayList<>();
        StringBuilder fragmentBuilder = new StringBuilder();
        boolean string = false;

        for (char c : lightLine.trim().toCharArray()) {
            if (c == '"') {
                string = !string;
            }
            else if (c == '*' && !string) {
                if (fragmentBuilder.length() != 0) {
                    String fragment = fragmentBuilder.toString();
                    fragments.add(fragment);
                    fragmentBuilder.setLength(0);
                }
                fragments.add("*");
                continue;
            }
            fragmentBuilder.append(c);
        }

        if (fragmentBuilder.length() != 0) {
            String fragment = fragmentBuilder.toString();
            fragments.add(fragment);
        }

        return fragments;
    }

}
