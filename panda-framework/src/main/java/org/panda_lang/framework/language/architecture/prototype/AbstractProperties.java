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

package org.panda_lang.framework.language.architecture.prototype;

import org.panda_lang.framework.design.architecture.prototype.ExecutableProperty;
import org.panda_lang.framework.design.architecture.prototype.Properties;
import org.panda_lang.framework.design.architecture.prototype.Prototype;

abstract class AbstractProperties<T extends ExecutableProperty> implements Properties<T> {

    private final Prototype prototype;

    protected AbstractProperties(Prototype prototype) {
        this.prototype = prototype;
    }

    @Override
    public Prototype getPrototype() {
        return prototype;
    }

}
