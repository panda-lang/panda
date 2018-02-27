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

package org.panda_lang.panda.language.structure.prototype.parsers.field;

import org.panda_lang.panda.design.architecture.PandaScript;
import org.panda_lang.panda.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.design.architecture.prototype.field.FieldVisibility;
import org.panda_lang.panda.design.architecture.prototype.field.PrototypeField;
import org.panda_lang.panda.design.architecture.prototype.field.implementation.PandaPrototypeField;
import org.panda_lang.panda.design.interpreter.parser.pipeline.DefaultPipelines;
import org.panda_lang.panda.design.interpreter.parser.pipeline.DefaultPriorities;
import org.panda_lang.panda.design.interpreter.parser.pipeline.registry.ParserRegistration;
import org.panda_lang.panda.design.interpreter.parser.util.Components;
import org.panda_lang.panda.design.interpreter.token.AbyssPatternBuilder;
import org.panda_lang.panda.design.interpreter.token.AbyssPatternAssistant;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGeneration;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationLayer;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationType;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.util.LocalCallback;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.interpreter.token.distributor.SourceStream;
import org.panda_lang.panda.framework.design.interpreter.token.extractor.Extractor;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.token.distributor.PandaSourceStream;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.redactor.AbyssRedactor;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.AbyssPattern;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.redactor.AbyssRedactorHollows;
import org.panda_lang.panda.language.structure.general.expression.Expression;
import org.panda_lang.panda.language.structure.general.expression.ExpressionParser;
import org.panda_lang.panda.language.structure.overall.imports.ImportRegistry;
import org.panda_lang.panda.language.syntax.PandaSyntax;

import java.util.List;

@ParserRegistration(target = DefaultPipelines.PROTOTYPE, parserClass = FieldParser.class, handlerClass = FieldParserHandler.class, priority = DefaultPriorities.PROTOTYPE_FIELD_PARSER)
public class FieldParser implements UnifiedParser {

    protected static final AbyssPattern PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "+** ;")
            .build();

    protected static final AbyssPattern ASSIGNATION_PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "+** = +* ;")
            .build();

    @Override
    public void parse(ParserInfo info) {
        CasualParserGeneration generation = info.getComponent(Components.GENERATION);
        CasualParserGenerationCallback callback;

        Extractor extractor = FieldParser.ASSIGNATION_PATTERN.extractor();
        SourceStream stream = info.getComponent(Components.SOURCE_STREAM);
        SourceStream copyOfStream = new PandaSourceStream(stream.toTokenizedSource());
        List<TokenizedSource> hollows = extractor.extract(copyOfStream.toTokenReader());

        if (hollows == null || hollows.size() < 2) {
            callback = new FieldDeclarationCasualParserCallback(false);
        }
        else {
            callback = new FieldDeclarationCasualParserCallback(true);
        }

        generation.getLayer(CasualParserGenerationType.HIGHER).delegateImmediately(callback, info.fork());
    }

    private static class FieldDeclarationCasualParserCallback implements CasualParserGenerationCallback {

        private final boolean assignation;

        private FieldDeclarationCasualParserCallback(boolean assignation) {
            this.assignation = assignation;
        }

        @Override
        public void call(ParserInfo delegatedInfo, CasualParserGenerationLayer nextLayer) {
            AbyssRedactorHollows hollows = AbyssPatternAssistant.extract(assignation ? ASSIGNATION_PATTERN : PATTERN, delegatedInfo);
            AbyssRedactor redactor = new AbyssRedactor(hollows);

            if (assignation) {
                redactor.map("left", "right");
            }
            else {
                redactor.map("left");
            }

            delegatedInfo.setComponent("redactor", redactor);
            TokenizedSource left = redactor.get("left");
            ClassPrototype prototype = delegatedInfo.getComponent("class-prototype");

            FieldVisibility visibility = null;
            ClassPrototype type = null;
            String name = null;
            boolean isStatic = false;

            for (int i = 0; i < left.size(); i++) {
                TokenRepresentation representation = left.get(i);
                Token token = representation.getToken();

                if (token.getType() == TokenType.UNKNOWN && i == left.size() - 1) {
                    name = token.getTokenValue();
                    continue;
                }

                if (token.getType() == TokenType.UNKNOWN && i == left.size() - 2) {
                    String returnTypeName = token.getTokenValue();

                    PandaScript script = delegatedInfo.getComponent(Components.SCRIPT);
                    ImportRegistry registry = script.getImportRegistry();

                    type = registry.forClass(returnTypeName);
                    continue;
                }

                switch (token.getTokenValue()) {
                    case "method":
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
                    default:
                        throw new PandaParserException("Unexpected token at line " + (representation.getLine() + 1) + ": " + token.getTokenValue());
                }
            }

            if (visibility == null) {
                visibility = FieldVisibility.LOCAL;
            }

            int fieldIndex = prototype.getFields().size();
            PrototypeField field = new PandaPrototypeField(type, fieldIndex, name, visibility, isStatic, false);
            prototype.getFields().add(field);

            // int fieldIndex = prototype.getFields().indexOf(field);
            // FieldStatement statement = new FieldStatement(fieldIndex, field);

            // ScopeLinker linker = delegatedInfo.getComponent(Components.SCOPE_LINKER);
            // linker.getCurrentScope().addStatement(statement); class scope [without statements]

            if (assignation) {
                nextLayer.delegate(new FieldAssignationCasualParserCallback(field), delegatedInfo);
            }
        }

    }

    @LocalCallback
    private static class FieldAssignationCasualParserCallback implements CasualParserGenerationCallback {

        private final PrototypeField field;

        public FieldAssignationCasualParserCallback(PrototypeField field) {
            this.field = field;
        }

        @Override
        public void call(ParserInfo delegatedInfo, CasualParserGenerationLayer nextLayer) {
            AbyssRedactor redactor = delegatedInfo.getComponent("redactor");
            TokenizedSource right = redactor.get("right");

            ExpressionParser expressionParser = new ExpressionParser();
            Expression expressionValue = expressionParser.parse(delegatedInfo, right);

            if (expressionValue == null) {
                throw new PandaParserException("Cannot parse expression '" + right + "'");
            }

            field.setDefaultValue(expressionValue);
        }

    }

}
