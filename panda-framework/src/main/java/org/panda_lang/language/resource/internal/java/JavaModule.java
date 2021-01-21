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

package org.panda_lang.language.resource.internal.java;

import org.panda_lang.language.architecture.module.Module;
import org.panda_lang.language.architecture.module.TypeLoader;
import org.panda_lang.language.architecture.type.Autocast;
import org.panda_lang.language.architecture.type.Kind;
import org.panda_lang.language.architecture.type.PandaType;
import org.panda_lang.language.architecture.type.Reference;
import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.language.architecture.type.Visibility;
import org.panda_lang.language.architecture.type.generator.TypeGenerator;
import org.panda_lang.language.architecture.type.member.method.PandaMethod;
import org.panda_lang.language.architecture.type.signature.Relation;
import org.panda_lang.language.architecture.type.signature.Signature;
import org.panda_lang.language.architecture.type.signature.TypedSignature;
import org.panda_lang.language.interpreter.source.PandaClassSource;
import org.panda_lang.language.interpreter.token.PandaSnippet;
import org.panda_lang.language.resource.internal.InternalModuleInfo;
import org.panda_lang.language.resource.internal.InternalModuleInfo.CustomInitializer;
import org.panda_lang.utilities.commons.ClassUtils;
import org.panda_lang.utilities.commons.function.Completable;

import java.util.Collections;

@InternalModuleInfo(module = "panda", pkg = "java.lang", classes = {
        "Iterable"
})
public final class JavaModule implements CustomInitializer {

    @Override
    public void initialize(Module module, TypeGenerator typeGenerator, TypeLoader typeLoader) {
        Type primitiveVoid = primitive(typeGenerator, module, "Void", void.class);
        Type voidType = associated(typeGenerator, primitiveVoid);
        typeLoader.load(primitiveVoid, voidType);

        Type primitiveInt = primitive(typeGenerator, module, "Int", int.class);
        Type primitiveBool = primitive(typeGenerator, module, "Bool", boolean.class);
        Type primitiveChar = primitive(typeGenerator, module, "Char", char.class);
        Type primitiveByte = primitive(typeGenerator, module, "Byte", byte.class);
        Type primitiveShort = primitive(typeGenerator, module, "Short", short.class);
        Type primitiveLong = primitive(typeGenerator, module, "Long", long.class);
        Type primitiveFloat = primitive(typeGenerator, module, "Float", float.class);
        Type primitiveDouble = primitive(typeGenerator, module, "Double", double.class);

        Type objectType = generate(typeGenerator, module, Object.class);
        typeLoader.load(objectType);

        Type stringType = generate(typeGenerator, module, String.class);
        typeLoader.load(stringType);

        Type numberType = generate(typeGenerator, module, Number.class);
        typeLoader.load(numberType);

        Type boolType = associated(typeGenerator, primitiveBool);
        Type charType = associated(typeGenerator, primitiveChar);
        Type byteType = associated(typeGenerator, primitiveByte);
        Type shortType = associated(typeGenerator, primitiveShort);
        Type intType = associated(typeGenerator, primitiveInt);
        Type longType = associated(typeGenerator, primitiveLong);
        Type floatType = associated(typeGenerator, primitiveFloat);
        Type doubleType = associated(typeGenerator, primitiveDouble);

        intType.addAutocast(longType.getReference(), (Autocast<Number, Long>) (originalType, object, resultType) -> object.longValue());
        intType.addAutocast(doubleType.getReference(), (Autocast<Number, Double>) (originalType, object, resultType) -> object.doubleValue());
        intType.addAutocast(floatType.getReference(), (Autocast<Number, Float>) (originalType, object, resultType) -> object.floatValue());
        floatType.addAutocast(doubleType.getReference(), (Autocast<Number, Double>) (originalType, object, resultType) -> object.doubleValue());

        charType.addAutocast(intType.getReference(), (Autocast<Character, Integer>) (originalType, object, resultType) -> Character.getNumericValue(object));
        byteType.addAutocast(intType.getReference(), (Autocast<Number, Integer>) (originalType, object, resultType) -> object.intValue());
        shortType.addAutocast(intType.getReference(), (Autocast<Number, Integer>) (originalType, object, resultType) -> object.intValue());

        typeLoader.load(
                primitiveBool, boolType,
                primitiveByte, byteType,
                primitiveShort, shortType,
                primitiveChar, charType,
                primitiveInt, intType,
                primitiveLong, longType,
                primitiveFloat, floatType,
                primitiveDouble, doubleType
        );

        Type java = generate(typeGenerator, module, Java.class);
        java.getMethods().declare(PandaMethod.builder()
                .name("null")
                .parameters(Collections.emptyList())
                .customBody((property, stack, instance, arguments) -> null)
                .visibility(Visibility.OPEN)
                .isStatic(true)
                .location(new PandaClassSource(Java.class).toLocation())
                .returnType(objectType.getSignature())
                .build());
        typeLoader.load(java);
    }

    private Type primitive(TypeGenerator typeGenerator, Module module, String name, Class<?> primitiveClass) {
        Completable<Type> futureType = new Completable<>();

        Reference reference = new Reference(
                futureType,
                module,
                "Primitive" + name,
                Visibility.OPEN,
                Kind.TYPE,
                new PandaClassSource(primitiveClass).toLocation());

        Type type = PandaType.builder()
                .name(reference.getSimpleName())
                .module(module)
                .signature(new TypedSignature(null, reference, new Signature[0], Relation.DIRECT, PandaSnippet.empty()))
                .associatedType(Completable.completed(primitiveClass))
                .kind(reference.getKind())
                .location(reference.getLocation())
                .build();

        futureType.complete(typeGenerator.allocate(primitiveClass, type));
        module.add(reference);

        return type;
    }

    private Type associated(TypeGenerator typeGenerator, Type primitive) {
        Type type = generate(typeGenerator, primitive.getModule(), primitive.getSimpleName().replace("Primitive", ""), ClassUtils.getNonPrimitiveClass(primitive.getAssociated().get()));

        type.addAutocast(primitive.getReference(), (originalType, object, resultType) -> object);
        primitive.addAutocast(type.getReference(), (originalType, object, resultType) -> object);

        return type;
    }

    public static Type generate(TypeGenerator typeGenerator, Module module, Class<?> type) {
        return generate(typeGenerator, module, type.getSimpleName(), type);
    }

    public static Type generate(TypeGenerator typeGenerator, Module module, String name, Class<?> javaType) {
        Reference reference= typeGenerator.generate(module, name, javaType);
        module.add(reference);

        return reference.fetchType();
    }

}
