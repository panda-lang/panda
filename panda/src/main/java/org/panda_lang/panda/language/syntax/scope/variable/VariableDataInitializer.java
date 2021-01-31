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

package org.panda_lang.panda.language.syntax.scope.variable;

import org.panda_lang.framework.architecture.statement.PandaVariableData;
import org.panda_lang.framework.architecture.statement.Scope;
import org.panda_lang.framework.architecture.statement.VariableData;
import org.panda_lang.framework.architecture.type.signature.Signature;
import org.panda_lang.framework.architecture.type.VisibilityComparator;
import org.panda_lang.framework.interpreter.parser.Context;
import org.panda_lang.framework.interpreter.parser.PandaParserFailure;
import org.panda_lang.framework.interpreter.token.Snippet;
import org.panda_lang.framework.interpreter.token.Snippetable;
import org.panda_lang.framework.interpreter.token.TokenInfo;
import org.panda_lang.panda.language.syntax.type.SignatureParser;
import org.panda_lang.panda.language.syntax.type.SignatureSource;

import java.util.List;

public final class VariableDataInitializer {

    private static final SignatureParser SIGNATURE_PARSER = new SignatureParser();

    private final Context<?> context;
    private final Scope scope;

    public VariableDataInitializer(Context<?> context, Scope scope) {
        this.context = context;
        this.scope = scope;
    }

    public VariableData createVariableDataByDeclaration(Snippetable declaration, boolean mutable, boolean nillable) {
        Snippet source = declaration.toSnippet();
        TokenInfo name = source.getLast();

        Snippet signatureSource = source.subSource(0, source.size() - 1);
        List<SignatureSource> signatures = SIGNATURE_PARSER.readSignatures(signatureSource);

        if (signatures.size() != 1) {
            throw new PandaParserFailure(context, signatureSource, "Invalid signature");
        }

        return createVariableData(signatures.get(0), name, mutable, nillable);
    }

    public VariableData createVariableData(SignatureSource signatureSource, Snippetable name, boolean mutable, boolean nillable) {
        Signature signature = SIGNATURE_PARSER.parse(context, signatureSource, false, null);
        return createVariableData(signature, name, mutable, nillable);
    }

    public VariableData createVariableData(Signature signature, Snippetable name, boolean mutable, boolean nillable) {
        // TODO: parent signature
        VisibilityComparator.requireAccess(signature.toTyped().fetchType(), context, name);

        VariableData variableData = createVariableData(name, mutable, nillable);
        variableData.interfereSignature(signature);
        return variableData;
    }

    public VariableData createVariableData(Snippetable name, boolean mutable, boolean nillable) {
        Snippet nameSource = name.toSnippet();

        if (nameSource.size() > 1) {
            throw new PandaParserFailure(context, name, "Variable name has to be singe word");
        }

        String variableName = nameSource.asSource();

        if (scope.getVariable(variableName).isDefined()) {
            throw new PandaParserFailure(context, name, "Variable name is already used in the scope '" + variableName + "'");
        }

        return new PandaVariableData(variableName, mutable, nillable);
    }

}
