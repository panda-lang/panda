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

package org.panda_lang.framework.language.architecture.prototype.array;

import org.panda_lang.framework.PandaFrameworkException;
import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.prototype.Referencable;
import org.panda_lang.framework.design.architecture.prototype.Reference;
import org.panda_lang.utilities.commons.ArrayUtils;
import org.panda_lang.utilities.commons.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public final class ArrayClassPrototypeFetcher {

    private static final Map<String, Referencable> ARRAY_PROTOTYPES = new HashMap<>();

    public static Optional<Reference> fetch(Module module, Class<?> type) {
        Class<?> baseClass = ArrayUtils.getBaseClass(type);
        Prototype baseReference = module.getModuleLoader().requirePrototype(baseClass);
        return fetch(module, baseReference.getSimpleName() + type.getSimpleName().replace(baseClass.getSimpleName(), StringUtils.EMPTY));
    }

    public static Optional<Reference> fetch(Module module, String type) {
        Referencable cached = ARRAY_PROTOTYPES.get(type);

        if (cached != null) {
            return Optional.of(cached.toReference());
        }

        Optional<Reference> baseReferenceValue = module.forName(StringUtils.replace(type, PandaArray.IDENTIFIER, StringUtils.EMPTY));

        if (!baseReferenceValue.isPresent()) {
            return Optional.empty();
        }

        Reference baseReference = baseReferenceValue.get();
        int dimensions = StringUtils.countOccurrences(type, PandaArray.IDENTIFIER);

        return Optional.of(getArrayOf(module, baseReference, dimensions).toReference());
    }

    public static Prototype getArrayOf(Module module, Referencable baseReferencable, int dimensions) {
        Reference baseReference = baseReferencable.toReference();
        Class<?> componentType = ArrayUtils.getDimensionalArrayType(baseReference.getAssociatedClass().getImplementation(), dimensions);
        Class<?> arrayType = ArrayUtils.getArrayClass(componentType);
        Reference componentReference;

        if (componentType.isArray()) {
            componentReference = fetch(module, componentType).orElseThrow((Supplier<PandaFrameworkException>) () -> {
                throw new PandaFrameworkException("Cannot fetch array class for array type " + componentType);
            });
        }
        else {
            componentReference = module.forClass(componentType).orElseThrow((Supplier<PandaFrameworkException>) () -> {
                throw new PandaFrameworkException("Cannot fetch array class for " + componentType);
            });
        }

        ArrayPrototype arrayPrototype = new ArrayPrototype(module, arrayType, componentReference.fetch());
        ARRAY_PROTOTYPES.put(baseReference.getName() + dimensions, arrayPrototype);
        ModuleLoader loader = module.getModuleLoader();

        arrayPrototype.getMethods().declare("size", () -> ArrayClassPrototypeConstants.SIZE.apply(loader));
        arrayPrototype.getMethods().declare("toString", () -> ArrayClassPrototypeConstants.TO_STRING.apply(loader));

        module.add(arrayPrototype);
        return arrayPrototype;
    }

}
