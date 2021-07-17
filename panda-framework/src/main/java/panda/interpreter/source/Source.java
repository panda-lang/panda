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

package panda.interpreter.source;

import panda.interpreter.architecture.module.Module;

/**
 * Source representation
 */
public interface Source {

    /**
     * Check if source is virtual
     *
     * @return true if virtual
     */
    boolean isVirtual();

    /**
     * Get content of source
     *
     * @return the content
     */
    String getContent();

    Module getModule();

    /**
     * Get source identifier
     *
     * @return e.g. a name of file or generated name
     */
    String getId();

}
