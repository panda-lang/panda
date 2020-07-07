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

package org.panda_lang.panda.language.resource.syntax.type;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.expression.ExpressionUtils;
import org.panda_lang.framework.design.architecture.type.Type;
import org.panda_lang.framework.design.architecture.type.TypeField;
import org.panda_lang.framework.design.architecture.type.Visibility;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.LocalChannel;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionTransaction;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Pipelines;
import org.panda_lang.framework.design.interpreter.source.Location;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.TokenInfo;
import org.panda_lang.framework.language.architecture.module.PandaImportsUtils;
import org.panda_lang.framework.language.architecture.type.PandaField;
import org.panda_lang.framework.language.architecture.type.TypeComponents;
import org.panda_lang.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.framework.language.interpreter.parser.generation.GenerationCycles;
import org.panda_lang.framework.language.interpreter.pattern.Mappings;
import org.panda_lang.framework.language.resource.syntax.TokenTypes;
import org.panda_lang.framework.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.interpreter.parser.RegistrableParser;
import org.panda_lang.panda.language.interpreter.parser.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.parser.context.ParserBootstrap;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Channel;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Src;
import org.panda_lang.panda.language.resource.syntax.PandaPriorities;

@RegistrableParser(pipeline = Pipelines.TYPE_LABEL, priority = PandaPriorities.PROTOTYPE_FIELD)
public final class FieldParser extends ParserBootstrap<Void> {

    @Override
    protected BootstrapInitializer<Void> initialize(Context context, BootstrapInitializer<Void> initializer) {
        return initializer.functional(builder -> builder
                .variant("visibility").consume(variant -> variant.content(Keywords.PUBLIC, Keywords.SHARED, Keywords.INTERNAL))
                .keyword(Keywords.STATIC).optional()
                .keyword(Keywords.MUT).optional()
                .keyword(Keywords.NIL).optional()
                .type("type").verifyNextType(TokenTypes.UNKNOWN)
                .wildcard("name").verifyType(TokenTypes.UNKNOWN)
                .subPattern("assign", sub -> sub
                        .unit("operator", "=")
                        .expression("assignation").map(ExpressionTransaction::getExpression)
                ).optional());
    }

    @Autowired(order = 1, cycle = GenerationCycles.TYPES_LABEL)
    public void parse(Context context, LocalChannel channel, @Channel Mappings mappings, @Channel Location location, @Src("type") Snippet typeName, @Src("name") TokenInfo name) {
        Type returnType = PandaImportsUtils.getTypeOrThrow(context, typeName.asSource(), typeName);
        Visibility visibility = Visibility.valueOf(mappings.get("visibility").get().toString().toUpperCase());

        Type type = context.getComponent(TypeComponents.PROTOTYPE);
        int fieldIndex = type.getFields().getDeclaredProperties().size();

        TypeField field = PandaField.builder()
                .name(name.getValue())
                .type(type)
                .returnType(returnType)
                .fieldIndex(fieldIndex)
                .location(location)
                .visibility(visibility)
                .isStatic(mappings.has(Keywords.STATIC))
                .mutable(mappings.has(Keywords.MUT))
                .nillable(mappings.has(Keywords.NIL))
                .build();

        type.getFields().declare(field);
        channel.allocated("field", field);
    }

    @Autowired(order = 2, cycle = GenerationCycles.CONTENT_LABEL)
    public void parseAssignation(Context context, @Channel Snippet source, @Channel TypeField field, @Src("assignation") @Nullable Expression assignationValue) {
        if (assignationValue == null) {
            //throw new PandaParserFailure("Cannot parse expression '" + assignationValue + "'", context, name);
            return;
        }

        if (!field.getReturnType().isAssignableFrom(assignationValue.getType())) {
            throw new PandaParserFailure(context, source, "Cannot assign type " + assignationValue.getType().getName() + " to " + field.getReturnType().getName());
        }

        field.setDefaultValue(ExpressionUtils.equalize(assignationValue, field.getReturnType()));
        field.initialize();
    }

}
