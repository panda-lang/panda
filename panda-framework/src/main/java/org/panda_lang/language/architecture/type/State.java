/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.language.architecture.type;

import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.language.interpreter.token.Snippetable;
import org.panda_lang.language.resource.syntax.keyword.Keywords;

import java.lang.reflect.Modifier;

/**
 * State of type
 */
public enum State {

    /**
     * Type cannot be instantiated but can be extended
     */
    ABSTRACT(false, true),
    /**
     * Type can be instantiated and can be extended
     */
    DEFAULT(true, true),
    /**
     * Type can be instantiated but can't be extended
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
     * Utility method to compare state with {@link org.panda_lang.language.architecture.type.State#ABSTRACT}
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

    /**
     * Get state based on the given string ('class' -> {@link #DEFAULT}, any other -> {@link #ABSTRACT}
     *
     * @param state the string representation of state
     * @return the state
     */
    public static State of(String state) {
        return state.equals(Keywords.CLASS.getValue()) ? State.DEFAULT : State.ABSTRACT;
    }

    public static void requireInstantiation(Context<?> context, Type type, Snippetable source) {
        if (!type.getState().canBeInstantiated()) {
            throw new PandaParserFailure(context, source, "Cannot create instance of abstract type");
        }
    }

    public static void requireInheritance(Context<?> context, Type type, Snippetable source) {
        if (!type.getState().canBeInherited()) {
            throw new PandaParserFailure(context, source, "Cannot inherit final type");
        }
    }

}
