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

package org.panda_lang.framework.architecture.type;

/**
 * Visibility of type
 */
public enum Visibility {

    /**
     * May be accessed anywhere
     */
    OPEN(0),
    /**
     * May be accessed from the associated module and its submodules
     */
    SHARED(1),
    /**
     * May be accessed only from the same source file or by inheritance
     */
    INTERNAL(2);

    private final int modifier;

    Visibility(int modifier) {
        this.modifier = modifier;
    }

    /**
     * Numeric representation of visibility
     *
     * @return get modifier
     */
    public int getModifier() {
        return modifier;
    }

    /**
     * Returns the enum constant of this type with the specified name
     *
     * @param visibility the char sequence that represents visibility
     * @return the visibility
     */
    public static Visibility of(Object visibility) {
        return valueOf(visibility.toString().toUpperCase());
    }

}
