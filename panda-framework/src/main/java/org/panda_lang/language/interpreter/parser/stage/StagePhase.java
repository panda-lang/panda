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

package org.panda_lang.language.interpreter.parser.stage;

import org.panda_lang.language.interpreter.parser.Context;

/**
 * Represents a group of tasks delegated to the same phase (layer) in the cycle
 */
public interface StagePhase {

    /**
     * Call all tasks delegated to the phase
     */
    void callTasks();

    /**
     * Delegate task
     *
     * @param priority the priority of task (the lowest is called first)
     * @param task the task to delegate
     * @param delegated the data to associate with task
     * @return current phase
     */
    StagePhase delegate(StageOrder priority, StageTask<?> task, Context delegated);

    /**
     * Delegate task using {@link StageOrder#DEFAULT}
     *
     * @see #delegate(StageOrder, StageTask, org.panda_lang.language.interpreter.parser.Context)
     */
    default StagePhase delegate(StageTask<?> task, Context delegated) {
        return delegate(StageOrder.DEFAULT, task, delegated);
    }

    /**
     * Delegate task using {@link StageOrder#BEFORE}
     *
     * @see #delegate(StageOrder, StageTask, org.panda_lang.language.interpreter.parser.Context)
     */
    default StagePhase delegateBefore(StageTask<?> task, Context delegated) {
        return delegate(StageOrder.BEFORE, task, delegated);
    }

    /**
     * Delegate task using {@link StageOrder#AFTER}
     *
     * @see #delegate(StageOrder, StageTask, org.panda_lang.language.interpreter.parser.Context)
     */
    default StagePhase delegateAfter(StageTask<?> task, Context delegated) {
        return delegate(StageOrder.AFTER, task, delegated);
    }

    /***
     * @return the amount of delegated tasks
     */
    int countTasks();

}
