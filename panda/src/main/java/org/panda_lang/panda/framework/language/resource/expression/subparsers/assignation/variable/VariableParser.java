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

package org.panda_lang.panda.framework.language.resource.expression.subparsers.assignation.variable;

import org.panda_lang.panda.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.architecture.value.Variable;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.DescriptivePattern;
import org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.extractor.ExtractorResult;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippetable;
import org.panda_lang.panda.framework.language.architecture.value.PandaVariable;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.resource.syntax.keyword.Keywords;

import java.util.Optional;

public class VariableParser {

    public static final String DECLARATION = "mut:[mut] nil:[nil] <type:reader type> <name:condition token {type:unknown}>";

    private static final DescriptivePattern DECLARATION_PATTERN = DescriptivePattern.builder()
            .compile(DECLARATION)
            .build();

    public Variable parseVariable(Context context, Scope scope, Snippetable source) {
        return parseVariable(context, scope, DECLARATION_PATTERN.extract(context, source.toSnippet()));
    }

    public Variable parseVariable(Context context, Scope scope, ExtractorResult result) {
        Snippet type = result.getWildcard("type")
                .map(resultElement -> (Snippet) resultElement.getValue())
                .orElse(null);

        Snippet name = result.getWildcard("name")
                .map(resultElement -> (Snippet) resultElement.getValue())
                .orElseThrow(null);

        boolean mutable = result.hasIdentifier(Keywords.MUT.getValue());
        boolean nillable = result.hasIdentifier(Keywords.NIL.getValue());

        return createVariable(context, scope, mutable, nillable, type, name);
    }

    public Variable parseVariable(Context context, Scope scope, boolean mutable, boolean nillable, Snippetable declaration) {
        Snippet declarationSource = declaration.toSnippet();

        if (declarationSource.size() < 2) {
            throw PandaParserFailure.builder("Lack of data", context)
                    .withStreamOrigin(declarationSource)
                    .build();
        }

        Snippet type = declarationSource.subSource(0, declarationSource.size() - 1);
        Snippetable name = declarationSource.getLast();

        return createVariable(context, scope, mutable, nillable, type, name);
    }

    public Variable createVariable(Context context, Scope scope, boolean mutable, boolean nillable, Snippetable type, Snippetable name) {
        Snippet nameSource = name.toSnippet();

        if (nameSource.size() > 1) {
            throw PandaParserFailure.builder("Variable name has to be singe word", context)
                    .withStreamOrigin(name)
                    .build();
        }

        String variableName = nameSource.asSource();

        if (scope.getVariables().stream().anyMatch(variable -> variable.getName().equals(variableName))) {
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

        Variable variable = new PandaVariable(prototype.get(), nameSource.asSource(), mutable, nillable);
        scope.addVariable(variable);

        return variable;
    }

}
