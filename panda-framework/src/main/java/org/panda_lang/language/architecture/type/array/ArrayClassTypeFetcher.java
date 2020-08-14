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

package org.panda_lang.language.architecture.type.array;

import org.panda_lang.language.PandaFrameworkException;
import org.panda_lang.language.architecture.module.Module;
import org.panda_lang.language.architecture.module.PandaModule;
import org.panda_lang.language.architecture.module.TypeLoader;
import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.utilities.commons.ArrayUtils;
import org.panda_lang.utilities.commons.StringUtils;
import org.panda_lang.utilities.commons.function.Option;

import java.util.HashMap;
import java.util.Map;

public final class ArrayClassTypeFetcher {

    private static final Module ARRAY_MODULE = new PandaModule(ArrayClassTypeFetcher.class.getName());
    private static final Map<String, Type> ARRAY_PROTOTYPES = new HashMap<>();

    public static Option<Type> fetch(TypeLoader typeLoader, Class<?> type) {
        Class<?> baseClass = ArrayUtils.getBaseClass(type);
        Type baseType = typeLoader.forClass(baseClass).orElseGet(() -> {
           return typeLoader.load(ARRAY_MODULE, baseClass, baseClass.getName());
        });
        return fetch(typeLoader, baseType.getSimpleName() + type.getSimpleName().replace(baseClass.getSimpleName(), StringUtils.EMPTY));
    }

    public static Option<Type> fetch(TypeLoader typeLoader, String typeName) {
        return Option.of(ARRAY_PROTOTYPES.get(typeName))
                .orElse(() -> {
                    String arrayType = StringUtils.replace(typeName, PandaArray.IDENTIFIER, StringUtils.EMPTY);
                    return typeLoader.forName(arrayType);
                })
                .map(type -> {
                    int dimensions = StringUtils.countOccurrences(typeName, PandaArray.IDENTIFIER);
                    return getArrayOf(typeLoader, type, dimensions);
                });
    }

    public static Type getArrayOf(TypeLoader typeLoader, Type baseType, int dimensions) {
        Class<?> componentType = ArrayUtils.getDimensionalArrayType(baseType.getAssociatedClass().fetchStructure(), dimensions);
        Class<?> arrayClass = ArrayUtils.getArrayClass(componentType);

        Module module = baseType.getModule();
        Type componentReference;

        if (componentType.isArray()) {
            componentReference = fetch(typeLoader, componentType).orThrow(() -> {
                throw new PandaFrameworkException("Cannot fetch array class for array type " + componentType);
            });
        }
        else {
            componentReference = module.forClass(componentType).orThrow(() -> {
                throw new PandaFrameworkException("Cannot fetch array class for " + componentType);
            });
        }

        ArrayType arrayType = new ArrayType(module, arrayClass, componentReference);
        ARRAY_PROTOTYPES.put(baseType.getName() + dimensions, arrayType);

        arrayType.getMethods().declare("size", () -> ArrayClassTypeConstants.SIZE.apply(typeLoader));
        arrayType.getMethods().declare("asString", () -> ArrayClassTypeConstants.AS_STRING.apply(typeLoader));

        module.add(arrayType);
        return arrayType;
    }

}
