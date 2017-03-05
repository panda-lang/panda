/*
 * Copyright (c) 2015-2017 Dzikoysk
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

package org.panda_lang.panda.language.runtime;

import org.panda_lang.panda.implementation.structure.Application;
import org.panda_lang.panda.implementation.structure.dynamic.Executable;
import org.panda_lang.panda.implementation.structure.Value;
import org.panda_lang.panda.implementation.structure.Scope;

public class PandaExecutableBridge implements ExecutableBridge {

    private final Application application;
    private final Scope wrapper;

    public PandaExecutableBridge(Application application, Scope headWrapper) {
        this.application = application;
        this.wrapper = headWrapper;
    }

    @Override
    public void call(Executable executable) {

    }

    @Override
    public void returnValue(Value value) {

    }

}
