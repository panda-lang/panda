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

package org.panda_lang.panda.util.spcl.value;

import org.panda_lang.panda.util.spcl.SPCLVariables;

public class SPCLVar extends SPCLSimpleValue {

    private final SPCLVariables variables;
    private final String variableName;

    public SPCLVar(SPCLVariables variables, String variableName) {
        super(variableName);

        this.variables = variables;
        this.variableName = variableName;
    }

    @Override
    public String getValue() {
        return variables.get(variableName).getValue().toString();
    }

}
