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

package org.panda_lang.framework.runtime;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.architecture.dynamic.Frame;
import org.panda_lang.framework.architecture.statement.Scope;
import org.panda_lang.framework.architecture.statement.Statement;
import org.panda_lang.utilities.commons.function.ThrowingSupplier;

/**
 * Stack stores information about the active subroutines of a process
 */
public interface ProcessStack {

    /**
     * Call frame within the given instance
     *
     * @param instance the instance under which the frame has to be performed
     * @param frame the frame to call
     * @return result of invocation
     * @throws Exception if something happen
     */
    @Nullable Result<?> callFrame(Object instance, Frame frame) throws Exception;

    /**
     * Call frame within the given instance and return custom result
     *
     * @param instance the instance within the frame has to be performed
     * @param frame the frame to call
     * @param resultSupplier the supplier of result
     * @return result of invocation
     * @throws Exception if something happen
     */
    @Nullable Result<?> callCustomFrame(Object instance, Frame frame, ThrowingSupplier<Result<?>, Exception> resultSupplier) throws Exception;

    /**
     * Call statement within the given instance
     *
     * @param instance the instance within the statement
     * @param statement the statement to call
     * @return result of invocation
     * @throws Exception if something happen
     */
    @Nullable Result<?> callStatement(Object instance, Statement statement) throws Exception;

    /**
     * Call scope within the given instance
     *
     * @param instance the instance within the frame has to performed
     * @param scope the scope to call
     * @return result of invocation
     * @throws Exception if something happen
     */
    @Nullable Result<?> callScope(Object instance, Scope scope) throws Exception;

    /**
     * Get statements on stack
     *
     * @return the array of statements on stack
     */
    Statement[] getLivingFramesOnStack();

    /**
     * @return instance of the current scope
     */
    Frame getCurrentFrame();

    /**
     * Get associated process
     *
     * @return the process
     */
    Process getProcess();

}
