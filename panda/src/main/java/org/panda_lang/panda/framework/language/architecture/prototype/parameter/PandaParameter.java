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

package org.panda_lang.panda.framework.language.architecture.prototype.parameter;

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.parameter.Parameter;
import org.panda_lang.panda.framework.design.architecture.value.Variable;
import org.panda_lang.panda.framework.language.architecture.value.PandaVariable;

public class PandaParameter implements Parameter {

    private final String parameterName;
    private final ClassPrototype parameterType;

    public PandaParameter(ClassPrototype parameterType, String parameterName) {
        this.parameterName = parameterName;
        this.parameterType = parameterType;
    }

    @Override
    public Variable toVariable(int nestingLevel) {
        return new PandaVariable(parameterType, parameterName, nestingLevel, false, false);
    }

    @Override
    public ClassPrototype getParameterType() {
        return parameterType;
    }

    @Override
    public String getParameterName() {
        return parameterName;
    }

}
