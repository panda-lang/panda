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

package org.panda_lang.framework.design.interpreter.token;

import org.jetbrains.annotations.Nullable;

/**
 * Snippet utilities
 */
public final class SnippetUtils {

    private SnippetUtils() { }

    /**
     * {@link Snippet#isEmpty()} with null support
     *
     * @param snippet nullable snippet to check
     * @return returns true if the given snippet is null or content of snippet is empty
     */
    public static boolean isEmpty(@Nullable Snippet snippet) {
        return snippet == null || snippet.isEmpty();
    }

}
