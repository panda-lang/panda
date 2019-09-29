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

package org.panda_lang.framework.design.architecture.prototype;

import java.util.List;

public interface Declarations<T extends PrototypeExecutable> {

    /**
     * Declare a new property
     *
     * @param property the property to add
     */
    void declare(T property);

    /**
     * Get all properties of the T type
     *
     * @return list of properties
     */
    List<? extends T> getProperties();

    /**
     * Get amount of properties
     *
     * @return the amount of properties
     */
    int size();

}
