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

package org.panda_lang.framework.language.resource.internal.java;

import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.type.Autocast;
import org.panda_lang.framework.design.architecture.type.Type;
import org.panda_lang.framework.language.architecture.type.PandaTypeUtils;
import org.panda_lang.framework.language.resource.internal.InternalModuleInfo;
import org.panda_lang.utilities.commons.ClassUtils;

public final class JavaModule implements InternalModuleInfo {

    @Override
    public void initialize(Module module) {
        PandaTypeUtils.of(module, void.class);
        PandaTypeUtils.of(module, Object.class);
        type(module, "Int", int.class);
        type(module, "Bool", boolean.class);
        type(module, "Char", char.class);
        type(module, "Byte", byte.class);
        type(module, "Short", short.class);
        type(module, "Long", long.class);
        type(module, "Float", float.class);
        type(module, "Double", double.class);

        PandaTypeUtils.generateOf(module, Object.class);
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

    private void type(Module module, String name, Class<?> primitiveClass) {
        PandaTypeUtils.of(module, "Primitive" + name, primitiveClass);
        PandaTypeUtils.of(module, name, ClassUtils.getNonPrimitiveClass(primitiveClass));
    }

    private Type generate(Module module, Class<?> primitiveClass, String name) {
        Type primitiveType = PandaTypeUtils.generateOf(module, "Primitive" + name, primitiveClass).fetch();

        Type type = PandaTypeUtils.generateOf(module, name, ClassUtils.getNonPrimitiveClass(primitiveClass)).fetch();
        type.addBase(primitiveType);

        return type;
    }

    @Override
    public String[] getNames() {
        return new String[] {
                "String",
                "Number",
                "Iterable"
        };
    }

    @Override
    public String getPackageName() {
        return "java.lang";
    }

    @Override
    public String getModule() {
        return "java";
    }

}
