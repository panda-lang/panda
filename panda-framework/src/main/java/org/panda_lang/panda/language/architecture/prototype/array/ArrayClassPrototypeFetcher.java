/*
 * Copyright (c) 2015-2019 Dzikoysk
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

package org.panda_lang.panda.language.architecture.prototype.array;

import org.panda_lang.panda.framework.PandaFrameworkException;
import org.panda_lang.panda.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeMetadata;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.utilities.commons.ArrayUtils;
import org.panda_lang.panda.utilities.commons.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class ArrayClassPrototypeFetcher {

    private static final Map<String, ClassPrototypeReference> ARRAY_PROTOTYPES = new HashMap<>();

    public static Optional<ClassPrototypeReference> fetch(ModuleLoader loader, Class<?> type) {
        return fetch(loader, type.getSimpleName());
    }

    public static Optional<ClassPrototypeReference> fetch(Context context, String type) {
        return fetch(context.getComponent(UniversalComponents.MODULE_LOADER), type);
    }

    public static Optional<ClassPrototypeReference> fetch(ModuleLoader loader, String type) {
        ClassPrototypeReference cached = ARRAY_PROTOTYPES.get(type);

        if (cached != null) {
            return Optional.of(cached);
        }

        Optional<ClassPrototypeReference> baseReference = loader.forName(StringUtils.replace(type, PandaArray.IDENTIFIER, StringUtils.EMPTY));

        if (!baseReference.isPresent()) {
            return Optional.empty();
        }

        int dimensions = StringUtils.countOccurrences(type, PandaArray.IDENTIFIER);
        ClassPrototypeReference array = getArrayOf(loader, baseReference.get(), dimensions);

        return Optional.ofNullable(loader.getLocalModule().add(array));
    }

    public static ClassPrototypeReference getArrayOf(ModuleLoader loader, ClassPrototypeMetadata prototype, int dimensions) {
        Class<?> arrayType = ArrayUtils.getDimensionalArrayType(prototype.getAssociatedClass(), dimensions);
        Class<?> arrayClass = ArrayUtils.getArrayClass(arrayType);

        ClassPrototypeReference type;

        if (arrayType.isArray()) {
            type = fetch(loader, arrayType)
                    .orElseThrow((Supplier<PandaFrameworkException>) () -> {
                        throw new PandaFrameworkException("Cannot fetch array class for array type " + arrayType);
                    });
        }
        else {
            type = prototype.getModule().getAssociatedWith(arrayType)
                    .orElseThrow((Supplier<PandaFrameworkException>) () -> {
                        throw new PandaFrameworkException("Cannot fetch array class for " + arrayType);
                    });
        }

        ArrayClassPrototype arrayPrototype = new ArrayClassPrototype(prototype.getModule(), arrayClass, type);
        ARRAY_PROTOTYPES.put(prototype.getName() + dimensions, arrayPrototype.getReference());

        arrayPrototype.getMethods().declare(ArrayClassPrototypeConstants.SIZE);
        arrayPrototype.getMethods().declare(ArrayClassPrototypeConstants.TO_STRING);

        return arrayPrototype.getReference();
    }

}
