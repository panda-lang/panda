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

package org.panda_lang.framework.design.architecture.prototype;

import java.lang.reflect.Modifier;

/**
 * State of prototype
 */
public enum State {

    /**
     * Prototype cannot be instantiated but can be extended
     */
    ABSTRACT(false, true),
    /**
     * Prototype can be instantiated and can be extended
     */
    DEFAULT(true, true),
    /**
     * Prototype can be instantiated but can't be extended
     */
    FINAL(true, false);

    private final boolean instantiated;
    private final boolean inherited;

    State(boolean instantiated, boolean inherited) {
        this.instantiated = instantiated;
        this.inherited = inherited;
    }

    /**
     * Check if type can be inherited
     *
     * @return true if can be inherited, otherwise false
     */
    public boolean canBeInherited() {
        return inherited;
    }

    /**
     * Check if type can be instantiated
     *
     * @return true if can be instantiated, otherwise false
     */
    public boolean canBeInstantiated() {
        return instantiated;
    }

    /**
     * Utility method to compare state with {@link org.panda_lang.framework.design.architecture.prototype.State#ABSTRACT}
     *
     * @return true if the current state represents abstract state
     */
    public boolean isAbstract() {
        return this == ABSTRACT;
    }

    /**
     * Get state based on the given class (abstract/final/default)
     *
     * @param clazz the class to check
     * @return the state of the given class
     */
    public static State of(Class<?> clazz) {
        if (Modifier.isAbstract(clazz.getModifiers())) {
            return State.ABSTRACT;
        }

        if (Modifier.isFinal(clazz.getModifiers())) {
            return State.FINAL;
        }

        return State.DEFAULT;
    }

}
