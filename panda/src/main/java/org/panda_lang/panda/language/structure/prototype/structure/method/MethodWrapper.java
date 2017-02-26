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

package org.panda_lang.panda.language.structure.prototype.structure.method;

import org.panda_lang.framework.structure.Wrapper;
import org.panda_lang.framework.structure.dynamic.WrapperInstance;
import org.panda_lang.panda.language.structure.prototype.structure.field.FieldStatement;
import org.panda_lang.panda.language.structure.prototype.structure.method.parameter.Parameter;
import org.panda_lang.panda.implementation.structure.AbstractContainer;

import java.util.List;

public class MethodWrapper extends AbstractContainer implements Wrapper {

    private final int wrapperID;
    private final String methodName;
    private final List<Parameter> parameters;
    private final FieldStatement[] fieldStatements;

    public MethodWrapper(int wrapperID, String methodName, List<Parameter> parameters, List<FieldStatement> fieldStatements) {
        this.wrapperID = wrapperID;
        this.methodName = methodName;
        this.parameters = parameters;
        this.fieldStatements = fieldStatements.toArray(new FieldStatement[fieldStatements.size()]);
    }

    @Override
    public WrapperInstance createInstance() {
        return new MethodWrapperInstance(this);
    }

    public FieldStatement[] getFieldStatements() {
        return fieldStatements;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

}
