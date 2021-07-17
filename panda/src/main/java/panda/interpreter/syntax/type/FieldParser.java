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

package panda.interpreter.syntax.type;

import panda.interpreter.architecture.expression.Expression;
import panda.interpreter.architecture.expression.ExpressionUtils;
import panda.interpreter.architecture.type.Type;
import panda.interpreter.architecture.type.TypeContext;
import panda.interpreter.architecture.type.Visibility;
import panda.interpreter.architecture.type.member.field.PandaField;
import panda.interpreter.architecture.type.member.field.TypeField;
import panda.interpreter.parser.Context;
import panda.interpreter.parser.ContextParser;
import panda.interpreter.parser.PandaParserFailure;
import panda.interpreter.parser.pool.Targets;
import panda.interpreter.parser.stage.Layer;
import panda.interpreter.parser.stage.Phases;
import panda.interpreter.token.TokenInfo;
import panda.interpreter.resource.syntax.TokenTypes;
import panda.interpreter.resource.syntax.keyword.Keywords;
import panda.interpreter.resource.syntax.operator.Operators;
import panda.interpreter.syntax.PandaSourceReader;
import panda.interpreter.syntax.PandaPriorities;
import panda.utilities.ArrayUtils;
import panda.interpreter.parser.Component;
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
