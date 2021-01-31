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

package org.panda_lang.framework.architecture.type.member;

import org.panda_lang.framework.architecture.type.Typed;
import org.panda_lang.framework.architecture.type.TypedUtils;
import org.panda_lang.framework.architecture.type.member.parameter.PropertyParameter;
import org.panda_lang.framework.architecture.type.signature.Signature;
import org.panda_lang.framework.runtime.ProcessStack;

import java.util.List;

public interface ParametrizedMember extends Member {

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
     * Check if member can be invoked by arguments of the given type
     *
     * @param arguments
     * @return
     */
    boolean isInvokableBy(List<? extends Typed> arguments);

    /**
     * Get references to types of executable's parameters
     *
     * @return array of used by parameter types
     */
    List<? extends Signature> getParameterSignatures();

    /**
     * Get parameters used by executable
     *
     * @return array of used parameters
     */
    List<? extends PropertyParameter> getParameters();

    /**
     * Get member name
     *
     * @return the name of member
     */
    @Override
    default String getName() {
        return getSimpleName() + " (" + TypedUtils.toString(getParameters()) + ")";
    }

}
