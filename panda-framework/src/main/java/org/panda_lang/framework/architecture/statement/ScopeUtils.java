/*
 * Copyright (c) 2021 dzikoysk
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

package org.panda_lang.framework.architecture.statement;

public final class ScopeUtils {

    private ScopeUtils() { }

    /**
     * Check if one of a parent scopes inherits from the given type
     *
     * @param currentScope the current scope
     * @param expectedParent the type of parent scope to look for
     * @return true if parent scope of the given type was found, otherwise false
     */
    public static boolean lookFor(Scope currentScope, Class<? extends Scope> expectedParent) {
        Scope parent = currentScope;

        do {
            if (expectedParent.isAssignableFrom(parent.getClass())) {
                return true;
            }

            parent = parent.getParentScope().getOrNull();
        } while (parent != null);

        return false;
    }

}
