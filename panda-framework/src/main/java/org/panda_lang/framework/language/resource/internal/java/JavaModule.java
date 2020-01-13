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
import org.panda_lang.framework.design.architecture.prototype.Autocast;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.language.architecture.prototype.PandaPrototypeUtils;
import org.panda_lang.framework.language.resource.internal.InternalModuleInfo;
import org.panda_lang.utilities.commons.ClassUtils;

public final class JavaModule implements InternalModuleInfo {

    @Override
    public void initialize(Module module) {
        PandaPrototypeUtils.of(module, void.class);
        PandaPrototypeUtils.of(module, Object.class);
        prototype(module, "Int", int.class);
        prototype(module, "Bool", boolean.class);
        prototype(module, "Char", char.class);
        prototype(module, "Byte", byte.class);
        prototype(module, "Short", short.class);
        prototype(module, "Long", long.class);
        prototype(module, "Float", float.class);
        prototype(module, "Double", double.class);

        PandaPrototypeUtils.generateOf(module, Object.class);
        Prototype intType = generate(module, int.class, "Int");
        Prototype boolType = generate(module, boolean.class, "Bool");
        Prototype charType = generate(module, char.class, "Char");
        Prototype byteType = generate(module, byte.class, "Byte");
        Prototype shortType = generate(module, short.class, "Short");
        Prototype longType = generate(module, long.class, "Long");
        Prototype floatType = generate(module, float.class, "Float");
        Prototype doubleType = generate(module, double.class, "Double");

        intType.addAutocast(longType, (Autocast<Number, Long>) (originalType, object, resultType) -> object.longValue());
        intType.addAutocast(doubleType, (Autocast<Number, Double>) (originalType, object, resultType) -> object.doubleValue());
        intType.addAutocast(floatType, (Autocast<Number, Float>) (originalType, object, resultType) -> object.floatValue());
        floatType.addAutocast(doubleType, (Autocast<Number, Double>) (originalType, object, resultType) -> object.doubleValue());

        charType.addAutocast(intType, (Autocast<Character, Integer>) (originalType, object, resultType) -> Character.getNumericValue(object));
        byteType.addAutocast(intType, (Autocast<Number, Integer>) (originalType, object, resultType) -> object.intValue());
        shortType.addAutocast(intType, (Autocast<Number, Integer>) (originalType, object, resultType) -> object.intValue());
    }

    private void prototype(Module module, String name, Class<?> primitiveClass) {
        PandaPrototypeUtils.of(module, "Primitive" + name, primitiveClass);
        PandaPrototypeUtils.of(module, name, ClassUtils.getNonPrimitiveClass(primitiveClass));
    }

    private Prototype generate(Module module, Class<?> primitiveClass, String name) {
        Prototype primitiveType = PandaPrototypeUtils.generateOf(module, "Primitive" + name, primitiveClass).fetch();

        Prototype type = PandaPrototypeUtils.generateOf(module, name, ClassUtils.getNonPrimitiveClass(primitiveClass)).fetch();
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
