/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.framework.design.runtime.flow;

public interface ControlFlow {

    /**
     * Call the flow
     */
    void call();

    /**
     * Skip next statements (Most commonly used by loops)
     */
    void skip();

    /**
     * Escape the flow
     */
    void escape();

    /**
     * Reset the flow
     */
    void reset();

    /**
     * @return true if {@link ControlFlow#skip()} was called
     */
    boolean isSkipped();

    /**
     * @return true if {@link ControlFlow#escape()} was called
     */
    boolean isEscaped();

}
