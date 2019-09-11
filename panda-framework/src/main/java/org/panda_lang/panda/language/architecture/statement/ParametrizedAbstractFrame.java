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

package org.panda_lang.panda.language.architecture.statement;

import org.panda_lang.panda.framework.design.architecture.prototype.parameter.PrototypeParameter;
import org.panda_lang.panda.framework.design.architecture.statement.Variable;

import java.util.ArrayList;
import java.util.List;

public abstract class ParametrizedAbstractFrame extends AbstractFrame {

    protected final List<PrototypeParameter> parameters;

    public ParametrizedAbstractFrame(List<PrototypeParameter> parameters) {
        this.parameters = parameters;
        addParameters(parameters);
    }

    public List<? extends Variable> addParameters(List<? extends PrototypeParameter> parameters) {
        List<Variable> variables = new ArrayList<>();

        for (PrototypeParameter parameter : parameters) {
            variables.add(createVariable(parameter));
        }

        return variables;
    }

    public List<? extends PrototypeParameter> getParameters() {
        return parameters;
    }

}
