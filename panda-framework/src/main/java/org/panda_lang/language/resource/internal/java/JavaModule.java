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
import org.panda_lang.language.architecture.type.State;
import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.language.architecture.type.Visibility;
import org.panda_lang.language.architecture.type.signature.Relation;
import org.panda_lang.language.architecture.type.signature.Signature;
import org.panda_lang.language.architecture.type.signature.TypedSignature;
import org.panda_lang.language.interpreter.source.PandaClassSource;
import org.panda_lang.language.interpreter.token.PandaSnippet;
import org.panda_lang.language.resource.internal.InternalModuleInfo;
import org.panda_lang.language.resource.internal.InternalModuleInfo.CustomInitializer;
import org.panda_lang.utilities.commons.ClassUtils;
import org.panda_lang.utilities.commons.function.CompletableOption;

@InternalModuleInfo(module = "panda", pkg = "java.lang", classes = {
        "String",
        "Number",
        "Iterable"
})
public final class JavaModule implements CustomInitializer {

    @Override
    public void initialize(Module module, TypeLoader typeLoader) {
        of(module, void.class);
        of(module, Void.class);

        primitive(module, "Int", int.class);
        primitive(module, "Bool", boolean.class);
        primitive(module, "Char", char.class);
        primitive(module, "Byte", byte.class);
        primitive(module, "Short", short.class);
        primitive(module, "Long", long.class);
        primitive(module, "Float", float.class);
        primitive(module, "Double", double.class);

        of(module, Object.class);
        of(module, String.class);
        Type intType = generate(module, int.class, "Int");
        Type boolType = generate(module, boolean.class, "Bool");
        Type charType = generate(module, char.class, "Char");
        Type byteType = generate(module, byte.class, "Byte");
        Type shortType = generate(module, short.class, "Short");
        Type longType = generate(module, long.class, "Long");
        Type floatType = generate(module, float.class, "Float");
        Type doubleType = generate(module, double.class, "Double");

        intType.addAutocast(longType, (Autocast<Number, Long>) (originalType, object, resultType) -> object.longValue());
        intType.addAutocast(doubleType, (Autocast<Number, Double>) (originalType, object, resultType) -> object.doubleValue());
        intType.addAutocast(floatType, (Autocast<Number, Float>) (originalType, object, resultType) -> object.floatValue());
        floatType.addAutocast(doubleType, (Autocast<Number, Double>) (originalType, object, resultType) -> object.doubleValue());

        charType.addAutocast(intType, (Autocast<Character, Integer>) (originalType, object, resultType) -> Character.getNumericValue(object));
        byteType.addAutocast(intType, (Autocast<Number, Integer>) (originalType, object, resultType) -> object.intValue());
        shortType.addAutocast(intType, (Autocast<Number, Integer>) (originalType, object, resultType) -> object.intValue());
    }

    private void primitive(Module module, String name, Class<?> primitiveClass) {
        generate(module, name, ClassUtils.getNonPrimitiveClass(primitiveClass));
        // typeLoader.load(module, ClassUtils.getNonPrimitiveClass(primitiveClass), name);
    }

    private Type generate(Module module, Class<?> primitiveClass, String name) {
        return generate(module, "Primitive" + name, primitiveClass);
        // return typeLoader.load(module, ClassUtils.getNonPrimitiveClass(primitiveClass), name);
    }

    public static Type of(Module module, Class<?> type) {
        return generate(module, type.getSimpleName(), type);
    }

    public static Type generate(Module module, String name, Class<?> javaType) {
        CompletableOption<Type> futureType = new CompletableOption<>();
        Reference reference = new Reference(futureType, module, name, Visibility.OPEN, javaType.isInterface() ? Kind.INTERFACE : Kind.CLASS, new PandaClassSource(javaType).toLocation());
        Signature signature = new TypedSignature(null, reference, new Signature[0], Relation.DIRECT, PandaSnippet.empty());

        Type type = PandaType.builder()
                .name(name)
                .signature(signature)
                .module(module)
                .associatedType(CompletableOption.completed(javaType))
                .visibility(Visibility.OPEN)
                .state(State.DEFAULT)
                .kind(reference.getKind())
                .location(reference.getLocation())
                .build();
        futureType.complete(type);
        module.add(reference);

        return type;
    }

}
