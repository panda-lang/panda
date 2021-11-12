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

package panda.interpreter.architecture.statement;

import panda.interpreter.architecture.type.signature.Signature;
import panda.interpreter.parser.PandaParserException;
import panda.std.reactive.Completable;

public class PandaVariableData implements VariableData {

    private final String name;
    private final boolean mutable;
    private final boolean nillable;
    private final Completable<Signature> signature;

    public PandaVariableData(Completable<Signature> signature, String name, boolean mutable, boolean nillable) {
        if (name == null) {
            throw new IllegalArgumentException("Variable name cannot be null");
        }

        this.name = name;
        this.mutable = mutable;
        this.nillable = nillable;
        this.signature = signature;
    }

    public PandaVariableData(Signature signature, String name, boolean mutable, boolean nillable) {
        this(Completable.completed(signature), name, mutable, nillable);

        if (signature == null) {
            throw new IllegalArgumentException("Variable type cannot be null");
        }
    }

    public PandaVariableData(Signature signature, String name) {
        this(Completable.completed(signature), name, true, true);
    }

    public PandaVariableData(String name, boolean mutable, boolean nillable) {
        this(new Completable<>(), name, mutable, nillable);
    }

    @Override
    public boolean awaitsSignature() {
        return !signature.isReady();
    }

    @Override
    public boolean interfereSignature(Signature signature) {
        if (this.signature.isReady()) {
            return false;
        }

        this.signature.complete(signature);
        return true;
    }

    @Override
    public Completable<Signature> getSignatureReference() {
        return signature;
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
        return signature.orThrow(() -> {
            throw new PandaParserException("Signature of " + name + " variable cannot be interfered");
        });
    }

    @Override
    public String getName() {
        return name;
    }

}
