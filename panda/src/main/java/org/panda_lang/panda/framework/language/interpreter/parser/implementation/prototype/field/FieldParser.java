/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.framework.language.interpreter.parser.implementation.prototype.field;

import org.panda_lang.panda.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.field.FieldVisibility;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeField;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGeneration;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.GenerationLayer;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.util.LocalCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.interpreter.token.stream.TokenReader;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.PandaScript;
import org.panda_lang.panda.framework.language.architecture.prototype.field.PandaPrototypeField;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPriorities;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.ExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.prototype.ClassPrototypeComponents;
import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.redactor.AbyssRedactor;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.redactor.AbyssRedactorHollows;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.utils.AbyssPatternAssistant;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.utils.AbyssPatternBuilder;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;
import org.panda_lang.panda.framework.language.resource.PandaSyntax;

import java.util.List;

@ParserRegistration(target = PandaPipelines.PROTOTYPE, priority = PandaPriorities.PROTOTYPE_FIELD_PARSER)
public class FieldParser implements UnifiedParser, ParserHandler {

    protected static final AbyssPattern PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "+** ;")
            .build();

    protected static final AbyssPattern ASSIGNATION_PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "+** = +* ;")
            .build();

    @Override
    public boolean handle(TokenReader reader) {
        return FieldParser.PATTERN.match(reader) != null;
    }

    @Override
    public boolean parse(ParserData data, GenerationLayer nextLayer) {
        CasualParserGeneration generation = data.getComponent(UniversalComponents.GENERATION);
        CasualParserGenerationCallback callback;

        SourceStream stream = data.getComponent(UniversalComponents.SOURCE_STREAM);
        SourceStream copyOfStream = new PandaSourceStream(stream.toTokenizedSource());

        List<TokenizedSource> hollows = FieldParser.ASSIGNATION_PATTERN
                .extractor()
                .extract(copyOfStream.toTokenReader());

        if (hollows == null || hollows.size() < 2) {
            callback = new FieldDeclarationCasualParserCallback(false);
        }
        else {
            callback = new FieldDeclarationCasualParserCallback(true);
        }

        callback.call(data, nextLayer);
        return true;
    }

    private static class FieldDeclarationCasualParserCallback implements CasualParserGenerationCallback {

        private final boolean assignation;

        private FieldDeclarationCasualParserCallback(boolean assignation) {
            this.assignation = assignation;
        }

        @Override
        public void call(ParserData delegatedData, GenerationLayer nextLayer) {
            AbyssRedactorHollows hollows = AbyssPatternAssistant.extract(assignation ? ASSIGNATION_PATTERN : PATTERN, delegatedData);
            AbyssRedactor redactor = new AbyssRedactor(hollows);

            if (assignation) {
                redactor.map("left", "right");
            }
            else {
                redactor.map("left");
            }

            TokenizedSource left = redactor.get("left");
            ClassPrototype prototype = delegatedData.getComponent(ClassPrototypeComponents.CLASS_PROTOTYPE);

            String name = null;
            FieldVisibility visibility = null;
            ClassPrototype type = null;
            boolean isStatic = false;
            boolean mutable = false;
            boolean nullable = false;

            for (int i = 0; i < left.size(); i++) {
                TokenRepresentation representation = left.get(i);
                Token token = representation.getToken();

                if (token.getType() == TokenType.UNKNOWN && i == left.size() - 1) {
                    name = token.getTokenValue();
                    continue;
                }

                if (token.getType() == TokenType.UNKNOWN && i == left.size() - 2) {
                    String returnTypeName = token.getTokenValue();

                    PandaScript script = delegatedData.getComponent(PandaComponents.PANDA_SCRIPT);
                    ModuleLoader registry = script.getModuleLoader();

                    type = registry.forClass(returnTypeName);
                    continue;
                }

                switch (token.getTokenValue()) {
                    case "public":
                        visibility = FieldVisibility.PUBLIC;
                        continue;
                    case "local":
                        visibility = FieldVisibility.LOCAL;
                        continue;
                    case "hidden":
                        visibility = FieldVisibility.HIDDEN;
                        continue;
                    case "static":
                        isStatic = true;
                        visibility = visibility != null ? visibility : FieldVisibility.PUBLIC;
                        continue;
                    case "mutable":
                        mutable = true;
                        continue;
                    case "nullable":
                        nullable = true;
                        continue;
                    default:
                        throw new PandaParserException("Unexpected token at line " + (representation.getLine() + 1) + ": " + token.getTokenValue());
                }
            }

            int fieldIndex = prototype.getFields().getAmountOfFields();

            if (visibility == null) {
                visibility = FieldVisibility.LOCAL;
            }

            PrototypeField field = PandaPrototypeField.builder()
                    .fieldIndex(fieldIndex)
                    .type(type)
                    .name(name)
                    .visibility(visibility)
                    .isStatic(isStatic)
                    .mutable(mutable)
                    .nullable(nullable)
                    .build();

            prototype.getFields().addField(field);

            // int fieldIndex = prototype.getListOfFields().indexOf(field);
            // FieldStatement statement = new FieldStatement(fieldIndex, field);

            // ScopeLinker linker = delegatedInfo.getComponent(Components.SCOPE_LINKER);
            // linker.getCurrentScope().addStatement(statement); class scope [without statements]

            if (assignation) {
                nextLayer.delegate(new FieldAssignationCasualParserCallback(field, redactor), delegatedData);
            }
        }

    }

    @LocalCallback
    private static class FieldAssignationCasualParserCallback implements CasualParserGenerationCallback {

        private final PrototypeField field;
        private final AbyssRedactor redactor;

        public FieldAssignationCasualParserCallback(PrototypeField field, AbyssRedactor redactor) {
            this.field = field;
            this.redactor = redactor;
        }

        @Override
        public void call(ParserData delegatedData, GenerationLayer nextLayer) {
            TokenizedSource right = redactor.get("right");

            ExpressionParser expressionParser = new ExpressionParser();
            Expression expressionValue = expressionParser.parse(delegatedData, right);

            if (expressionValue == null) {
                throw new PandaParserException("Cannot parse expression '" + right + "'");
            }

            field.setDefaultValue(expressionValue);
        }

    }

}
