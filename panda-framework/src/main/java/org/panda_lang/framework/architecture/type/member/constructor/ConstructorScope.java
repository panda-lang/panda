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

package org.panda_lang.framework.architecture.type.member.constructor;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.PandaFrameworkException;
import org.panda_lang.framework.architecture.dynamic.Frameable;
import org.panda_lang.framework.architecture.expression.Expression;
import org.panda_lang.framework.architecture.statement.AbstractPropertyFramedScope;
import org.panda_lang.framework.architecture.type.Type;
import org.panda_lang.framework.architecture.type.TypeFrame;
import org.panda_lang.framework.architecture.type.TypeInstance;
import org.panda_lang.framework.architecture.type.member.MemberFrameImpl;
import org.panda_lang.framework.architecture.type.member.MemberInvoker;
import org.panda_lang.framework.architecture.type.member.field.TypeField;
import org.panda_lang.framework.architecture.type.member.parameter.ParameterUtils;
import org.panda_lang.framework.architecture.type.member.parameter.PropertyParameter;
import org.panda_lang.framework.interpreter.source.Localizable;
import org.panda_lang.framework.runtime.ProcessStack;
import org.panda_lang.framework.runtime.Result;
import org.panda_lang.framework.runtime.Status;
import panda.utilities.ArrayUtils;
import panda.utilities.UnsafeUtils;
import panda.utilities.collection.Lists;
import panda.std.Option;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class ConstructorScope extends AbstractPropertyFramedScope implements MemberInvoker<TypeConstructor, Frameable, Object> {

    private static final Object[] EMPTY = new Object[0];

    public ConstructorScope(Localizable localizable, List<PropertyParameter> parameters) {
        super(localizable, parameters);
    }

    @Override
    public ConstructorFrame revive(ProcessStack stack, Object typeInstance) {
        return new ConstructorFrame(this, (TypeFrame) typeInstance);
    }

    @Override
    public Object invoke(TypeConstructor constructor, ProcessStack stack, @Nullable Frameable frameable, Object[] arguments) throws Exception {
        TypeFrame typeFrame = (TypeFrame) Objects.requireNonNull(frameable).__panda__to_frame();

        ConstructorFrame constructorFrame = revive(stack, typeFrame);
        ParameterUtils.assignValues(constructorFrame, arguments);

        constructor.getBaseCall()
                .map(call -> {
                    try {
                        return (Object[]) stack.callCustomFrame(frameable, typeFrame, () -> {
                            return stack.callCustomFrame(typeFrame, constructorFrame, () -> {
                                return new Result<>(Status.RETURN, call.evaluate(stack, constructorFrame));
                            });
                        }).getResult();
                    } catch (Exception exception) {
                        return UnsafeUtils.throwException(exception);
                    }
                })
                .orElse(EMPTY)
                .peek(typeFrame::setBaseArguments);

        TypeInstance typeInstance;

        try {
            Constructor<? extends TypeInstance> nativeConstructor = getConstructor(constructor.getType(), constructor.getParameters());
            typeInstance = nativeConstructor.newInstance(ArrayUtils.merge(typeFrame, arguments, Object[]::new));
        } catch (InvocationTargetException targetException) {
            return UnsafeUtils.throwException(targetException.getTargetException());
        }

        typeFrame.setTypeInstance(typeInstance);

        for (TypeField field : constructor.getType().getFields().getDeclaredProperties()) {
            if (!field.hasDefaultValue()) {
                continue;
            }

            if (field.isStatic()) {
                field.fetchStaticValue(); // just init
                continue;
            }

            Expression expression = field.getDefaultValue();
            typeInstance.__panda__get_frame().set(field.getPointer(), expression.evaluate(stack, typeInstance));
        }

        stack.callFrame(typeFrame, constructorFrame);

        return typeInstance;
    }

    @SuppressWarnings("unchecked")
    private Constructor<? extends TypeInstance> getConstructor(Type type, List<? extends PropertyParameter> parameters) {
        Class<?>[] parameterTypes = ArrayUtils.merge(TypeFrame.class, ParameterUtils.parametersToClasses(parameters), Class[]::new);

        try {
            return (Constructor<? extends TypeInstance>) type.getAssociated().get().getConstructor(parameterTypes);
        } catch (NoSuchMethodException noSuchMethodException) {
            throw new PandaFrameworkException("Associated class does not implement " + Arrays.toString(parameterTypes) + " constructor");
        }
    }

    public Option<BaseCall> getBaseCall() {
        return Option.of(this)
                .map(scope -> Lists.get(scope.getStatements(), 0))
                .filter(statement -> statement instanceof BaseCall)
                .map(statement -> ((BaseCall) statement));
    }

    public List<PropertyParameter> getParameters() {
        return parameters;
    }

    public static final class ConstructorFrame extends MemberFrameImpl<ConstructorScope> {

        private final TypeFrame typeFrame;

        public ConstructorFrame(ConstructorScope scope, TypeFrame frame) {
            super(scope, frame);
            this.typeFrame = frame;
        }

        public TypeFrame getTypeFrame() {
            return typeFrame;
        }

    }

}
