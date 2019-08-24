/*
 * Copyright (c) 2015-2019 Dzikoysk
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

package org.panda_lang.panda.framework.language.architecture.statement;

import org.panda_lang.panda.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.architecture.statement.VariableData;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.extractor.ExtractorResult;
import org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.extractor.ExtractorUtils;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippetable;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.resource.syntax.keyword.Keywords;

import java.util.Objects;
import java.util.Optional;

public class VariableDataInitializer {

    private final Context context;
    private final Scope scope;

    public VariableDataInitializer(Context context, Scope scope) {
        this.context = context;
        this.scope = scope;
    }

    public VariableData createVariableData(Snippetable source) {
        return createVariableData(VariableConstants.DECLARATION_PATTERN.extract(context, source.toSnippet()));
    }

    public VariableData createVariableData(ExtractorResult result) {
        Snippet type = ExtractorUtils.getWildcard(result, "type");
        Snippet name = ExtractorUtils.getWildcard(result, "name");

        boolean mutable = result.hasIdentifier(Keywords.MUT.getValue());
        boolean nillable = result.hasIdentifier(Keywords.NIL.getValue());

        return createVariableData(type, name, mutable, nillable);
    }

    public VariableData createVariableData(Snippetable declaration, boolean mutable, boolean nillable) {
        Snippet declarationSource = declaration.toSnippet();

        if (declarationSource.size() < 2) {
            throw PandaParserFailure.builder("Lack of data", context)
                    .withStreamOrigin(declarationSource)
                    .build();
        }

        Snippet type = declarationSource.subSource(0, declarationSource.size() - 1);
        Snippetable name = Objects.requireNonNull(declarationSource.getLast());

        return createVariableData(type, name, mutable, nillable);
    }

    public VariableData createVariableData(Snippetable type, Snippetable name, boolean mutable, boolean nillable) {
        Snippet nameSource = name.toSnippet();

        if (nameSource.size() > 1) {
            throw PandaParserFailure.builder("Variable name has to be singe word", context)
                    .withStreamOrigin(name)
                    .build();
        }

        String variableName = nameSource.asSource();

        if (scope.getVariable(variableName).isPresent()) {
            throw PandaParserFailure.builder("Variable name is already used in the scope '" + variableName + "'", context)
                    .withStreamOrigin(name)
                    .build();
        }

        ModuleLoader loader = context.getComponent(UniversalComponents.MODULE_LOADER);
        Optional<ClassPrototypeReference> prototype = loader.forName(type.toSnippet().asSource());

        if (!prototype.isPresent()) {
            throw PandaParserFailure.builder("Cannot recognize variable type: " + type, context)
                    .withStreamOrigin(type)
                    .build();
        }

        return new PandaVariableData(prototype.get(), nameSource.asSource(), mutable, nillable);
    }

}
