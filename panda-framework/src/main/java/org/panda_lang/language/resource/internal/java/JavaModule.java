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
import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.language.architecture.type.PandaTypeUtils;
import org.panda_lang.language.resource.internal.InternalModuleInfo;
import org.panda_lang.language.resource.internal.InternalModuleInfo.CustomInitializer;
import org.panda_lang.utilities.commons.ClassUtils;

@InternalModuleInfo(module = "java", pkg = "java.lang", classes = {
        "String",
        "Number",
        "Iterable"
})
public final class JavaModule implements CustomInitializer {

    @Override
    public void initialize(Module module, TypeLoader typeLoader) {
        PandaTypeUtils.of(module, void.class);
        PandaTypeUtils.of(module, Void.class);

        type(module, typeLoader, "Int", int.class);
        type(module, typeLoader, "Bool", boolean.class);
        type(module, typeLoader, "Char", char.class);
        type(module, typeLoader, "Byte", byte.class);
        type(module, typeLoader, "Short", short.class);
        type(module, typeLoader, "Long", long.class);
        type(module, typeLoader, "Float", float.class);
        type(module, typeLoader, "Double", double.class);

        typeLoader.load(module, Object.class);
        Type intType = generate(module, typeLoader, int.class, "Int");
        Type boolType = generate(module, typeLoader, boolean.class, "Bool");
        Type charType = generate(module, typeLoader, char.class, "Char");
        Type byteType = generate(module, typeLoader, byte.class, "Byte");
        Type shortType = generate(module, typeLoader, short.class, "Short");
        Type longType = generate(module, typeLoader, long.class, "Long");
        Type floatType = generate(module, typeLoader, float.class, "Float");
        Type doubleType = generate(module, typeLoader, double.class, "Double");

        intType.addAutocast(longType, (Autocast<Number, Long>) (originalType, object, resultType) -> object.longValue());
        intType.addAutocast(doubleType, (Autocast<Number, Double>) (originalType, object, resultType) -> object.doubleValue());
        intType.addAutocast(floatType, (Autocast<Number, Float>) (originalType, object, resultType) -> object.floatValue());
        floatType.addAutocast(doubleType, (Autocast<Number, Double>) (originalType, object, resultType) -> object.doubleValue());

        charType.addAutocast(intType, (Autocast<Character, Integer>) (originalType, object, resultType) -> Character.getNumericValue(object));
        byteType.addAutocast(intType, (Autocast<Number, Integer>) (originalType, object, resultType) -> object.intValue());
        shortType.addAutocast(intType, (Autocast<Number, Integer>) (originalType, object, resultType) -> object.intValue());
    }

    private void type(Module module, TypeLoader typeLoader, String name, Class<?> primitiveClass) {
        PandaTypeUtils.of(module, "Primitive" + name, primitiveClass);
        typeLoader.load(module, ClassUtils.getNonPrimitiveClass(primitiveClass), name);
    }

    private Type generate(Module module, TypeLoader typeLoader, Class<?> primitiveClass, String name) {
        PandaTypeUtils.of(module, "Primitive" + name, primitiveClass);
        return typeLoader.load(module, ClassUtils.getNonPrimitiveClass(primitiveClass), name);
    }

}
