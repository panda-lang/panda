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

package org.panda_lang.panda.language.resource.syntax.scope;

import org.panda_lang.language.architecture.statement.PandaVariableData;
import org.panda_lang.language.architecture.statement.Variable;
import org.panda_lang.language.architecture.statement.VariableData;
import org.panda_lang.language.architecture.type.VisibilityComparator;
import org.panda_lang.language.architecture.type.signature.Signature;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.ContextParser;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.language.interpreter.parser.pool.Targets;
import org.panda_lang.language.interpreter.token.TokenInfo;
import org.panda_lang.language.resource.syntax.TokenTypes;
import org.panda_lang.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.interpreter.parser.PandaSourceReader;
import org.panda_lang.panda.language.resource.syntax.PandaPriorities;
import org.panda_lang.panda.language.resource.syntax.type.SignatureParser;
import org.panda_lang.panda.language.resource.syntax.type.SignatureSource;
import org.panda_lang.utilities.commons.ArrayUtils;
import org.panda_lang.utilities.commons.collection.Component;
import org.panda_lang.utilities.commons.function.Completable;
import org.panda_lang.utilities.commons.function.Option;

public final class LateDeclarationParser implements ContextParser<Object, Variable> {

    private static final SignatureParser SIGNATURE_PARSER = new SignatureParser();

    @Override
    public String name() {
        return "late declaration";
    }

    @Override
    public Component<?>[] targets() {
        return ArrayUtils.of(Targets.SCOPE);
    }

    @Override
    public double priority() {
        return PandaPriorities.SCOPE_LATE_DECLARATION;
    }

    @Override
    public Option<Completable<Variable>> parse(Context<?> context) {
        PandaSourceReader sourceReader = new PandaSourceReader(context.getStream());

        if (sourceReader.read(Keywords.LATE).isEmpty()) {
            return Option.none();
        }

        boolean mut = sourceReader.optionalRead(() -> sourceReader.read(Keywords.MUT)).isPresent();
        boolean nil = sourceReader.optionalRead(() -> sourceReader.read(Keywords.NIL)).isPresent();

        Option<SignatureSource> variableSignature = sourceReader.readSignature();

        if (variableSignature.isEmpty()) {
            throw new PandaParserFailure(context, context.getSource(), "Missing valid variable signature");
        }

        Option<TokenInfo> name = sourceReader.read(TokenTypes.UNKNOWN);

        if (name.isEmpty()) {
            throw new PandaParserFailure(context, context.getSource(), "Missing variable name");
        }

        // TODO: Parent signature
        Signature signature = SIGNATURE_PARSER.parse(null, context, variableSignature.get());
        VisibilityComparator.requireAccess(signature.toTyped().fetchType(), context, variableSignature.get().getName());

        VariableData variableData = new PandaVariableData(signature, name.get().getValue(), mut, nil);
        Variable variable = context.getScope().createVariable(variableData);

        return Option.ofCompleted(variable);
    }

}
