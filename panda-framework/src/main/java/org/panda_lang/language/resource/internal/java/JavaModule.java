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
import org.panda_lang.language.architecture.type.Reference;
import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.language.architecture.type.generator.TypeGenerator;
import org.panda_lang.language.resource.internal.InternalModuleInfo;
import org.panda_lang.language.resource.internal.InternalModuleInfo.CustomInitializer;
import org.panda_lang.utilities.commons.ClassUtils;

@InternalModuleInfo(module = "panda", pkg = "java.lang", classes = {
        "String",
        "Number",
        "Iterable"
})
public final class JavaModule implements CustomInitializer {

    @Override
    public void initialize(Module module, TypeGenerator typeGenerator, TypeLoader typeLoader) {
        Type primitiveVoidType = of(typeGenerator, module, void.class);
        Type voidType = of(typeGenerator, module, Void.class);

        primitive(typeGenerator, module, "Int", int.class);
        primitive(typeGenerator, module, "Bool", boolean.class);
        primitive(typeGenerator, module, "Char", char.class);
        primitive(typeGenerator, module, "Byte", byte.class);
        primitive(typeGenerator, module, "Short", short.class);
        primitive(typeGenerator, module, "Long", long.class);
        primitive(typeGenerator, module, "Float", float.class);
        primitive(typeGenerator, module, "Double", double.class);

        Type intType = generate(typeGenerator, module, int.class, "Int");
        Type boolType = generate(typeGenerator, module, boolean.class, "Bool");
        Type charType = generate(typeGenerator, module, char.class, "Char");
        Type byteType = generate(typeGenerator, module, byte.class, "Byte");
        Type shortType = generate(typeGenerator, module, short.class, "Short");
        Type longType = generate(typeGenerator, module, long.class, "Long");
        Type floatType = generate(typeGenerator, module, float.class, "Float");
        Type doubleType = generate(typeGenerator, module, double.class, "Double");

        Type objectType = of(typeGenerator, module, Object.class);
        Type stringType = of(typeGenerator, module, String.class);

        intType.addAutocast(longType, (Autocast<Number, Long>) (originalType, object, resultType) -> object.longValue());
        intType.addAutocast(doubleType, (Autocast<Number, Double>) (originalType, object, resultType) -> object.doubleValue());
        intType.addAutocast(floatType, (Autocast<Number, Float>) (originalType, object, resultType) -> object.floatValue());
        floatType.addAutocast(doubleType, (Autocast<Number, Double>) (originalType, object, resultType) -> object.doubleValue());

        charType.addAutocast(intType, (Autocast<Character, Integer>) (originalType, object, resultType) -> Character.getNumericValue(object));
        byteType.addAutocast(intType, (Autocast<Number, Integer>) (originalType, object, resultType) -> object.intValue());
        shortType.addAutocast(intType, (Autocast<Number, Integer>) (originalType, object, resultType) -> object.intValue());

        typeLoader.load(primitiveVoidType);
        typeLoader.load(voidType);

        typeLoader.load(objectType);
        typeLoader.load(stringType);

        typeLoader.load(intType);
        typeLoader.load(boolType);
        typeLoader.load(charType);
        typeLoader.load(byteType);
        typeLoader.load(shortType);
        typeLoader.load(longType);
        typeLoader.load(floatType);
        typeLoader.load(doubleType);
        typeLoader.load(doubleType);
    }

    private Reference primitive(TypeGenerator typeGenerator, Module module, String name, Class<?> primitiveClass) {
        Reference reference = typeGenerator.generate(module, name, ClassUtils.getNonPrimitiveClass(primitiveClass));
        module.add(reference);

        return reference;
    }

    private Type generate(TypeGenerator typeGenerator, Module module, Class<?> primitiveClass, String name) {
        Reference reference = typeGenerator.generate(module, "Primitive" + name, primitiveClass);
        module.add(reference);

        return reference.fetchType();
        // return typeLoader.load(module, ClassUtils.getNonPrimitiveClass(primitiveClass), name);
    }

    public static Type of(TypeGenerator typeGenerator, Module module, Class<?> type) {
        Reference reference = typeGenerator.generate(module, type.getSimpleName(), type);
        module.add(reference);

        return reference.fetchType();
    }

    public static Type generate(TypeGenerator typeGenerator, Module module, String name, Class<?> javaType) {
        Reference reference= typeGenerator.generate(module, name, javaType);
        module.add(reference);

        return reference.fetchType();
    }

}
