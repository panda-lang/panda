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

package org.panda_lang.panda.language.syntax.type;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.architecture.module.Imports;
import org.panda_lang.framework.architecture.type.Reference;
import org.panda_lang.framework.architecture.type.signature.GenericSignature;
import org.panda_lang.framework.architecture.type.signature.Relation;
import org.panda_lang.framework.architecture.type.signature.Signature;
import org.panda_lang.framework.architecture.type.signature.TypedSignature;
import org.panda_lang.framework.interpreter.parser.Context;
import org.panda_lang.framework.interpreter.parser.Contextual;
import org.panda_lang.framework.interpreter.parser.PandaParserFailure;
import org.panda_lang.framework.interpreter.parser.Parser;
import org.panda_lang.framework.interpreter.token.PandaSourceStream;
import org.panda_lang.framework.interpreter.token.Snippet;
import org.panda_lang.framework.interpreter.token.SourceStream;
import org.panda_lang.framework.resource.syntax.separator.Separators;
import org.panda_lang.panda.language.syntax.PandaSourceReader;
import org.panda_lang.utilities.commons.collection.Pair;
import panda.std.Option;
import panda.std.Result;

import java.util.ArrayList;
import java.util.List;

public final class SignatureParser implements Parser {

    @Override
    public String name() {
        return "signature";
    }

    public Signature parse(Contextual<?> contextual, SignatureSource signatureSource, boolean root, @Nullable Signature signatureContextValue) {
        Context<?> context = contextual.toContext();
        Imports imports = context.getImports();

        Option<Signature> signatureContext = Option.of(signatureContextValue);
        String name = signatureSource.getName().getValue();

        Result<Reference, Signature> type = imports.forType(name)
                .map(importedType -> {
                    //noinspection Convert2MethodRef
                    return Result.<Reference, Signature> ok(importedType);
                })
                .orElseGet(() -> {
                    if (!name.equals(name.toUpperCase())) {
                        throw new PandaParserFailure(context, signatureSource.getName(), "Abstract generic name has to be declared uppercase");
                    }

                    Signature signature = signatureContext
                            .flatMap(ctx -> ctx.findGeneric(name))
                            .map(Pair::getValue)
                            .orElseGet(() -> new GenericSignature(context.getTypeLoader(), signatureContextValue, name, null, new Signature[0], Relation.DIRECT, signatureSource.getName()));

                    return Result.error(signature);
                });

        Signature subParent = (!root && signatureContext.isEmpty() && type.isOk())
                ? type.get().fetchType().getSignature()
                : signatureContextValue;

        Signature[] generics = signatureSource.getGenerics().stream()
                .map(genericSignature -> parse(context, genericSignature, root, signatureContextValue))
                .toArray(Signature[]::new);

        return type
                .map(reference -> (Signature) new TypedSignature(subParent, reference, generics, Relation.DIRECT, signatureSource.getName()))
                .orElseGet(genericSignature -> genericSignature);
    }

    public List<SignatureSource> readSignatures(Snippet source) {
        Snippet[] sources = source.split(Separators.COMMA);
        List<SignatureSource> signatures = new ArrayList<>((source.size() / 3) + 1);

        for (Snippet signatureSource : sources) {
            SourceStream signatureSourceStream = new PandaSourceStream(signatureSource);
            PandaSourceReader signatureReader = new PandaSourceReader(signatureSourceStream);

            signatures.add(signatureReader.readSignature().orThrow(() -> {
                throw new PandaParserFailure(source, signatureSource, "Invalid signature");
            }));
        }

        return signatures;
    }



}
