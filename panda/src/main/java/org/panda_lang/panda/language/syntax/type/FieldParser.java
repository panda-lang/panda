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

import org.panda_lang.framework.architecture.expression.Expression;
import org.panda_lang.framework.architecture.expression.ExpressionUtils;
import org.panda_lang.framework.architecture.type.Type;
import org.panda_lang.framework.architecture.type.TypeContext;
import org.panda_lang.framework.architecture.type.Visibility;
import org.panda_lang.framework.architecture.type.member.field.PandaField;
import org.panda_lang.framework.architecture.type.member.field.TypeField;
import org.panda_lang.framework.interpreter.parser.Context;
import org.panda_lang.framework.interpreter.parser.ContextParser;
import org.panda_lang.framework.interpreter.parser.PandaParserFailure;
import org.panda_lang.framework.interpreter.parser.pool.Targets;
import org.panda_lang.framework.interpreter.parser.stage.Layer;
import org.panda_lang.framework.interpreter.parser.stage.Phases;
import org.panda_lang.framework.interpreter.token.TokenInfo;
import org.panda_lang.framework.resource.syntax.TokenTypes;
import org.panda_lang.framework.resource.syntax.keyword.Keywords;
import org.panda_lang.framework.resource.syntax.operator.Operators;
import org.panda_lang.panda.language.syntax.PandaSourceReader;
import org.panda_lang.panda.language.syntax.PandaPriorities;
import org.panda_lang.utilities.commons.ArrayUtils;
import org.panda_lang.framework.interpreter.parser.Component;
import panda.std.Completable;
import panda.std.Option;

public final class FieldParser implements ContextParser<TypeContext, TypeField> {

    private static final SignatureParser SIGNATURE_PARSER = new SignatureParser();

    @Override
    public String name() {
        return "field";
    }

    @Override
    public Component<?>[] targets() {
        return ArrayUtils.of(Targets.TYPE);
    }

    @Override
    public double priority() {
        return PandaPriorities.PROTOTYPE_FIELD;
    }

    @Override
    public Option<Completable<TypeField>> parse(Context<? extends TypeContext> context) {
        PandaSourceReader sourceReader = new PandaSourceReader(context.getStream());
        Option<Visibility> visibility = sourceReader.readVisibility();

        if (visibility.isEmpty()) {
            return Option.none();
        }

        boolean isStatic = sourceReader.optionalRead(() -> sourceReader.read(Keywords.STATIC)).isDefined();
        boolean isMutable = sourceReader.optionalRead(() -> sourceReader.read(Keywords.MUT)).isDefined();
        boolean isNillable = sourceReader.optionalRead(() -> sourceReader.read(Keywords.NIL)).isDefined();

        Option<SignatureSource> signature = sourceReader.readSignature();

        if (signature.isEmpty()) {
            return Option.none();
        }

        Option<String> name = sourceReader.read(TokenTypes.UNKNOWN).map(TokenInfo::getValue);

        if (name.isEmpty()) {
            return Option.none();
        }

        Type type = context.getSubject().getType();

        TypeField field = PandaField.builder()
                .type(type)
                .name(name.get())
                .returnType(SIGNATURE_PARSER.parse(context, signature.get(), false, context.getSubject().getType().getSignature()))
                .fieldIndex(type.getFields().getDeclaredProperties().size())
                .location(context)
                .visibility(visibility.get())
                .isStatic(isStatic)
                .mutable(isMutable)
                .nillable(isNillable)
                .build();

        type.getFields().declare(field);

        if (sourceReader.optionalRead(() -> sourceReader.read(Operators.ASSIGNMENT)).isDefined()) {
            Expression assignedValue = context.getExpressionParser().parse(context, context.getStream());

            context.getStageService().delegate("verify field values", Phases.VERIFY, Layer.NEXT_DEFAULT, verifyPhase -> {
                if (!field.getReturnType().isAssignableFrom(assignedValue.getSignature())) {
                    throw new PandaParserFailure(context, "Cannot assign type " + assignedValue.getSignature() + " to " + field.getReturnType());
                }
            });

            context.getStageService().delegate("initialize field", Phases.INITIALIZE, Layer.NEXT_DEFAULT, initializePhase -> {
                Expression equalizedValue = ExpressionUtils.equalize(assignedValue, field.getReturnType()).orElseThrow(error -> {
                    throw new PandaParserFailure(context, "Incompatible signatures");
                });

                field.setDefaultValue(equalizedValue);
                field.initialize();
            });
        }

        return Option.ofCompleted(field);
    }

}
