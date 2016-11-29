/*
 * Copyright (c) 2015-2016 Dzikoysk
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

package org.panda_lang.panda.implementation.runtime;

import org.panda_lang.framework.runtime.ExecutableBridge;
import org.panda_lang.framework.structure.Application;
import org.panda_lang.framework.structure.Executable;
import org.panda_lang.framework.structure.Value;
import org.panda_lang.framework.structure.Wrapper;

public class PandaExecutableBridge implements ExecutableBridge {

    private final Application application;
    private final Wrapper wrapper;
    private final Value[] parametersValues;

    public PandaExecutableBridge(Application application, Wrapper wrapper, Value... parametersValues) {
        this.application = application;
        this.wrapper = wrapper;
        this.parametersValues = parametersValues;
    }

    @Override
    public void call(Executable executable) {

    }

    @Override
    public void returnValue(Value value) {

    }

    @Override
    public Value[] getParameters() {
        return parametersValues;
    }

}
