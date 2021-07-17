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

package panda.interpreter.architecture.dynamic;

import panda.interpreter.architecture.statement.FramedScope;
import panda.interpreter.runtime.MemoryContainer;

/**
 * Specific type of memory container, associated with the {@link panda.interpreter.architecture.statement.StandardizedFramedScope}.
 *
 */
public interface Frame extends MemoryContainer, Frameable {

    /**
     * Get associated scope with the frame
     *
     * @return the frame scope
     */
    FramedScope getFramedScope();

    @Override
    default Frame __panda__to_frame() {
        return this;
    }

}
