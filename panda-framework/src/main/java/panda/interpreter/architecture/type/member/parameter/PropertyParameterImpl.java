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

package panda.interpreter.architecture.type.member.parameter;

import panda.interpreter.architecture.statement.PandaVariable;
import panda.interpreter.architecture.type.signature.Signature;

public final class PropertyParameterImpl extends PandaVariable implements PropertyParameter {

    public PropertyParameterImpl(int parameterIndex, Signature signature, String name, boolean mutable, boolean nillable) {
        super(parameterIndex, signature, name, mutable, nillable);
    }

    public PropertyParameterImpl(int parameterIndex, Signature signature, String name) {
        this(parameterIndex, signature, name, false, false);
    }

}
