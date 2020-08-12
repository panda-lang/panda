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

package org.panda_lang.language.interpreter.source;

import org.panda_lang.language.interpreter.Interpreter;

/**
 * Set of {@link Source} used by {@link Interpreter}
 */
public interface SourceSet extends Iterable<Source> {

    /**
     * Add a new source to set
     *
     * @param source the source to add
     */
    void addSource(Source source);

    /**
     * @return true if set is empty
     */
    boolean isEmpty();

}
