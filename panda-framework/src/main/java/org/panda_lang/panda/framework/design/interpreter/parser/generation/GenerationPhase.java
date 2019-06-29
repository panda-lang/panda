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

package org.panda_lang.panda.framework.design.interpreter.parser.generation;

import org.panda_lang.panda.framework.design.interpreter.parser.Context;

public interface GenerationPhase {

    /**
     * Call all tasks delegated to the phase
     *
     * @throws Exception allows you to handle exception that may occur in tasks
     */
    void callTasks() throws Exception;

    /**
     * Delegate task
     *
     * @param priority the priority of task (the lowest is called first)
     * @param task the task to delegate
     * @param delegated the data to associate with task
     * @return current phase
     */
    GenerationPhase delegate(GenerationTaskPriority priority, GenerationTask task, Context delegated);

    /**
     * Delegate task using {@link GenerationTaskPriority#DEFAULT}
     *
     * @see #delegate(GenerationTaskPriority, GenerationTask, org.panda_lang.panda.framework.design.interpreter.parser.Context)
     */
    default GenerationPhase delegate(GenerationTask task, Context delegated) {
        return delegate(GenerationTaskPriority.DEFAULT, task, delegated);
    }

    /**
     * Delegate task using {@link GenerationTaskPriority#BEFORE}
     *
     * @see #delegate(GenerationTaskPriority, GenerationTask, org.panda_lang.panda.framework.design.interpreter.parser.Context)
     */
    default GenerationPhase delegateBefore(GenerationTask task, Context delegated) {
        return delegate(GenerationTaskPriority.BEFORE, task, delegated);
    }

    /**
     * Delegate task using {@link GenerationTaskPriority#AFTER}
     *
     * @see #delegate(GenerationTaskPriority, GenerationTask, org.panda_lang.panda.framework.design.interpreter.parser.Context)
     */
    default GenerationPhase delegateAfter(GenerationTask task, Context delegated) {
        return delegate(GenerationTaskPriority.AFTER, task, delegated);
    }

    /***
     * @return the amount of delegated tasks
     */
    int countTasks();

}
