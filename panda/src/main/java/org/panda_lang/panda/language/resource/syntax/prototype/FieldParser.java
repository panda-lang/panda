/*
 * Copyright (c) 2015-2020 Dzikoysk
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

package org.panda_lang.panda.language.resource.syntax.prototype;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.expression.ExpressionUtils;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.prototype.PrototypeField;
import org.panda_lang.framework.design.architecture.prototype.Visibility;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionTransaction;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Pipelines;
import org.panda_lang.framework.design.interpreter.source.SourceLocation;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.framework.language.architecture.module.PandaImportsUtils;
import org.panda_lang.framework.language.architecture.prototype.PandaPrototypeField;
import org.panda_lang.framework.language.architecture.prototype.PrototypeComponents;
import org.panda_lang.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.framework.language.interpreter.parser.generation.GenerationCycles;
import org.panda_lang.framework.language.interpreter.pattern.custom.CustomPattern;
import org.panda_lang.framework.language.interpreter.pattern.custom.Result;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.ExpressionElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.KeywordElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.SubPatternElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.TypeElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.UnitElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.VariantElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.WildcardElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.verifiers.NextTokenTypeVerifier;
import org.panda_lang.framework.language.interpreter.pattern.custom.verifiers.TokenTypeVerifier;
import org.panda_lang.framework.language.resource.syntax.TokenTypes;
import org.panda_lang.framework.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.interpreter.parser.RegistrableParser;
import org.panda_lang.panda.language.interpreter.parser.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.parser.context.ParserBootstrap;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Inter;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Local;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Src;
import org.panda_lang.panda.language.interpreter.parser.context.data.LocalData;
import org.panda_lang.panda.language.interpreter.parser.context.handlers.CustomPatternHandler;
import org.panda_lang.panda.language.interpreter.parser.context.interceptors.CustomPatternInterceptor;
import org.panda_lang.panda.language.resource.syntax.PandaPriorities;

@RegistrableParser(pipeline = Pipelines.PROTOTYPE_LABEL, priority = PandaPriorities.PROTOTYPE_FIELD)
public final class FieldParser extends ParserBootstrap<Void> {

    @Override
    protected BootstrapInitializer<Void> initialize(Context context, BootstrapInitializer<Void> initializer) {
        return initializer
                .handler(new CustomPatternHandler())
                .interceptor(new CustomPatternInterceptor())
                .pattern(CustomPattern.of(
                        VariantElement.create("visibility").content(Keywords.PUBLIC.getValue(), Keywords.SHARED.getValue(), Keywords.INTERNAL.getValue()),
                        KeywordElement.create(Keywords.STATIC).optional(),
                        KeywordElement.create(Keywords.MUT).optional(),
                        KeywordElement.create(Keywords.NIL).optional(),
                        TypeElement.create("type").optional().verify(new NextTokenTypeVerifier(TokenTypes.UNKNOWN)),
                        WildcardElement.create("name").verify(new TokenTypeVerifier(TokenTypes.UNKNOWN)),
                        SubPatternElement.create("assign").optional().of(
                                UnitElement.create("operator").content("="),
                                ExpressionElement.create("assignation").map(ExpressionTransaction::getExpression)
                        )
                ));
    }

    @Autowired(order = 1, cycle = GenerationCycles.TYPES_LABEL)
    void parse(Context context, LocalData local, @Inter Result result, @Inter SourceLocation location, @Src("type") Snippet type, @Src("name") TokenRepresentation name) {
        Prototype returnType = PandaImportsUtils.getReferenceThrow(context, type.asSource(), type).fetch();
        Visibility visibility = Visibility.valueOf(result.get("visibility").toString().toUpperCase());

        Prototype prototype = context.getComponent(PrototypeComponents.PROTOTYPE);
        int fieldIndex = prototype.getFields().getDeclaredProperties().size();

        PrototypeField field = PandaPrototypeField.builder()
                .name(name.getValue())
                .prototype(prototype)
                .returnType(returnType)
                .fieldIndex(fieldIndex)
                .location(location)
                .visibility(visibility)
                .isStatic(result.has("static"))
                .mutable(result.has("mut"))
                .nillable(result.has("nil"))
                .build();

        prototype.getFields().declare(field);
        local.allocated(field);
    }

    @Autowired(order = 2, cycle = GenerationCycles.CONTENT_LABEL)
    void parseAssignation(Context context, @Inter Snippet source, @Local PrototypeField field, @Src("assignation") @Nullable Expression assignationValue) {
        if (assignationValue == null) {
            //throw new PandaParserFailure("Cannot parse expression '" + assignationValue + "'", context, name);
            return;
        }

        if (!field.getType().isAssignableFrom(assignationValue.getType())) {
            throw new PandaParserFailure(context, source, "Cannot assign type " + assignationValue.getType().getName() + " to " + field.getType().getName());
        }

        field.setDefaultValue(ExpressionUtils.equalize(assignationValue, field.getType()));
        field.initialize();
    }

}
