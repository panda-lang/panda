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

package org.panda_lang.language.architecture.statement;

import org.panda_lang.language.architecture.type.Signature;

public class PandaVariableData implements VariableData {

    private final String name;
    private final Signature signature;
    private final boolean mutable;
    private final boolean nillable;

    public PandaVariableData(Signature signature, String name, boolean mutable, boolean nillable) {
        if (signature == null) {
            throw new IllegalArgumentException("Variable type cannot be null");
        }

        if (name == null) {
            throw new IllegalArgumentException("Variable name cannot be null");
        }

        this.name = name;
        this.signature = signature;
        this.mutable = mutable;
        this.nillable = nillable;
    }

    public PandaVariableData(Signature signature, String name) {
        this(signature, name, true, true);
    }

    @Override
    public boolean isNillable() {
        return nillable;
    }

    @Override
    public boolean isMutable() {
        return mutable;
    }

    @Override
    public Signature getSignature() {
        return signature;
    }

    @Override
    public String getName() {
        return name;
    }

}
