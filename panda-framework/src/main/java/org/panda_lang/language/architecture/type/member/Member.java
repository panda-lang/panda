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

package org.panda_lang.language.architecture.type.member;

import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.language.architecture.type.TypedUtils;
import org.panda_lang.language.architecture.type.member.parameter.PropertyParameter;
import org.panda_lang.language.runtime.ProcessStack;

import java.util.Arrays;

/**
 * ExecutableProperty is equivalent to {@link java.lang.reflect.Executable} 
 */
public interface Member extends Property {

    /**
     * Invoke the executable
     *
     * @param stack the current process stack
     * @param instance the current instance
     * @param arguments arguments to use
     * @param <T> type of expected result
     * @return the result of executable
     * @throws Exception if something happen
     */
    <T> T invoke(ProcessStack stack, Object instance, Object... arguments) throws Exception;

    /**
     * Get parameter types as java types
     *
     * @return array of java types
     */
    default Class<?>[] getJavaParameterTypes() {
        return Arrays.stream(getParameterTypes())
                .map(parameterType -> parameterType.getAssociatedClass().fetchStructure())
                .toArray(Class[]::new);
    }

    /**
     * Get references to types of executable's parameters
     *
     * @return array of used by parameter types
     */
    Type[] getParameterTypes();

    /**
     * Get parameters used by executable
     *
     * @return array of used parameters
     */
    PropertyParameter[] getParameters();

    /**
     * Get return type of executable
     *
     * @return the return type of property
     */
    Type getReturnType();

    @Override
    default String getName() {
        return getSimpleName() + " (" + TypedUtils.toString(getParameters()) + ")";
    }

}
