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

package org.panda_lang.framework.language.architecture.statement;

import org.panda_lang.framework.design.architecture.statement.Scope;
import org.panda_lang.framework.design.architecture.statement.VariableData;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.Snippetable;
import org.panda_lang.framework.language.architecture.type.utils.VisibilityComparator;
import org.panda_lang.framework.language.interpreter.parser.PandaParserFailure;

import java.util.Objects;

public final class PandaVariableDataInitializer {

    private final Context context;
    private final Scope scope;

    public PandaVariableDataInitializer(Context context, Scope scope) {
        this.context = context;
        this.scope = scope;
    }

    public VariableData createVariableData(Snippetable declaration, boolean mutable, boolean nillable) {
        Snippet declarationSource = declaration.toSnippet();

        if (declarationSource.size() < 2) {
            throw new PandaParserFailure(context, declarationSource, "Lack of data");
        }

        Snippet type = declarationSource.subSource(0, declarationSource.size() - 1);
        Snippetable name = Objects.requireNonNull(declarationSource.getLast());

        return createVariableData(type, name, mutable, nillable);
    }

    public VariableData createVariableData(Snippetable typeName, Snippetable name, boolean mutable, boolean nillable) {
        Snippet nameSource = name.toSnippet();

        if (nameSource.size() > 1) {
            throw new PandaParserFailure(context, name, "Variable name has to be singe word");
        }

        String variableName = nameSource.asSource();

        if (scope.getVariable(variableName).isDefined()) {
            throw new PandaParserFailure(context, name, "Variable name is already used in the scope '" + variableName + "'");
        }

        return context.getComponent(Components.IMPORTS)
                .forName(typeName.toSnippet().asSource())
                .map(type -> {
                    VisibilityComparator.requireAccess(type, context, typeName);
                    return new PandaVariableData(type, nameSource.asSource(), mutable, nillable);
                })
                .getOrElseThrow(() -> {
                    throw new PandaParserFailure(context, typeName, "Cannot recognize variable type: " + typeName);
                });
    }

}
