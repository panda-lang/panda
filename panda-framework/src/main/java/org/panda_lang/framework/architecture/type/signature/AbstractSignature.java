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

package org.panda_lang.framework.architecture.type.signature;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.architecture.type.Type;
import org.panda_lang.framework.interpreter.token.Snippet;
import org.panda_lang.framework.interpreter.token.Snippetable;
import org.panda_lang.utilities.commons.collection.Pair;
import org.panda_lang.utilities.commons.function.Option;

import java.util.Arrays;

abstract class AbstractSignature<V> implements Signature {

    private final Option<Signature> parent;
    private final V subject;
    private final Signature[] generics;
    private final Relation relation;
    private final Snippetable source;

    protected AbstractSignature(@Nullable Signature parent, V subject, Signature[] generics, Relation relation, Snippetable source) {
        this.parent = Option.of(parent);
        this.subject = subject;
        this.generics = generics;
        this.relation = relation;
        this.source = source;
    }

    @Override
    public boolean isGeneric() {
        return false;
    }

    @Override
    public GenericSignature toGeneric() {
        throw new UnsupportedOperationException("Signature " + toString() + " is not generic");
    }

    @Override
    public boolean isTyped() {
        return false;
    }

    @Override
    public TypedSignature toTyped() {
        throw new UnsupportedOperationException("Signature " + toString() + " is not typed");
    }

    @Override
    public Option<Pair<GenericSignature, Signature>> findGeneric(String identifier) {
        Option<Signature> currentSignature = Option.of(this);

        do {
            Signature signature = currentSignature.get();

            for (int index = 0; index < generics.length; index++) {
                Signature parameter = signature.getGenerics()[index];

                if (parameter.isGeneric()) {
                    GenericSignature genericSignature = parameter.toGeneric();

                    if (identifier.equals(genericSignature.getLocalIdentifier())) {
                        return Option.of(new Pair<>(genericSignature, getGenerics()[index]));
                    }
                }
            }

            currentSignature = signature.getParent();
        } while (currentSignature.isPresent());

        return Option.none();
    }

    protected Signature[] applyGenerics(Signed context) {
        Signature contextSignature = context.getSignature();
        Signature[] appliedGenerics = new Signature[generics.length];

        for (int index = 0; index < appliedGenerics.length; index++) {
            Signature currentGeneric = generics[index];

            if (currentGeneric.isTyped()) {
                appliedGenerics[index] = currentGeneric;
            }
            else if (currentGeneric.isGeneric()) {
                appliedGenerics[index] = contextSignature.findGeneric(currentGeneric.toGeneric())
                        .map(Pair::getValue)
                        .orElseGet(currentGeneric);
            }
        }

        return appliedGenerics;
    }

    @Override
    public boolean isAssignableFrom(Signed from) {
        if (from == null) {
            return true; // TODO: Remove nulls
        }
        
        return SignatureUtils.isAssignable(from.getSignature(), this).isOk();
    }

    @Override
    public boolean equals(Object to) {
        if (this == to) {
            return true;
        }

        if (to == null || getClass() != to.getClass()) {
            return false;
        }

        Signature signature = (Signature) to;

        if (!subject.equals(signature.getSubject())) {
            return false;
        }

        if (!Arrays.equals(generics, signature.getGenerics())) {
            return false;
        }

        return relation == signature.getRelation();
    }

    @Override
    public int hashCode() {
        int result = subject.hashCode();
        result = 31 * result + Arrays.hashCode(generics);
        result = 31 * result + relation.hashCode();
        return result;
    }

    @Override
    public Type getKnownType() {
        return toType();
    }

    @Override
    public V getSubject() {
        return subject;
    }

    @Override
    public Snippet getSource() {
        return source.toSnippet();
    }

    public Relation getRelation() {
        return relation;
    }

    public Signature[] getGenerics() {
        return generics;
    }

    @Override
    public Option<Signature> getParent() {
        return parent;
    }

}
