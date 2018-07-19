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

package org.panda_lang.panda.utilities.configuration;

import org.jetbrains.annotations.Nullable;

import java.util.Stack;

public class ConfigurationUtils {

    public static @Nullable String getPath(Stack<String> stack) {
        StringBuilder sb = new StringBuilder();

        if (stack == null || stack.isEmpty()) {
            return null;
        }

        for (String key : stack) {
            if (key == null || key.isEmpty()) {
                continue;
            }

            sb.append(".");
            sb.append(key);
        }

        String path = sb.toString();

        if (path.length() > 0) {
            path = path.substring(1);
        }

        return path;
    }

    public static int getTabs(String s) {
        if (s.isEmpty()) {
            return 0;
        }

        String t = "\t";
        int count = 0;
        int idx = 0;

        while ((idx = s.indexOf(t, idx)) != -1) {
            count++;
            idx += t.length();
        }

        return count;
    }

}
