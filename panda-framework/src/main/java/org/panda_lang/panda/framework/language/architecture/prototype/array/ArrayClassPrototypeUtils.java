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

package org.panda_lang.panda.framework.language.architecture.prototype.array;

import org.panda_lang.panda.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.method.PandaMethod;
import org.panda_lang.panda.framework.language.architecture.value.PandaValue;
import org.panda_lang.panda.framework.language.resource.PandaTypes;
import org.panda_lang.panda.utilities.commons.ArrayUtils;
import org.panda_lang.panda.utilities.commons.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ArrayClassPrototypeUtils {

    private static final Map<String, ClassPrototypeReference> ARRAY_PROTOTYPES = new HashMap<>();

    public static Optional<ClassPrototypeReference> obtain(ParserData data, String type) {
        return obtain(data.getComponent(UniversalComponents.MODULE_LOADER), type);
    }

    public static Optional<ClassPrototypeReference> obtain(ModuleLoader loader, String type) {
        ClassPrototypeReference cached = ARRAY_PROTOTYPES.get(type);

        if (cached != null) {
            return Optional.of(cached);
        }

        Optional<ClassPrototypeReference> baseReference = loader.forClass(StringUtils.replace(type, PandaArray.IDENTIFIER, StringUtils.EMPTY));

        if (!baseReference.isPresent()) {
            return Optional.empty();
        }

        int dimensions = StringUtils.countOccurrences(type, PandaArray.IDENTIFIER);
        ClassPrototypeReference array = getArrayOf(baseReference.get(), dimensions);

        return Optional.ofNullable(loader.getDefaultModule().add(array));
    }

    public static ClassPrototypeReference getArrayOf(ClassPrototypeReference prototype, int dimensions) {
        Class<?> arrayType = ArrayUtils.getDimensionalArrayType(prototype.getAssociatedClass(), dimensions);
        Class<?> arrayClass = ArrayUtils.getArrayClass(arrayType);

        ArrayClassPrototype arrayPrototype = new ArrayClassPrototype(prototype.getModule(), arrayClass, arrayType);
        ARRAY_PROTOTYPES.put(prototype.getClassName() + dimensions, arrayPrototype.getReference());

        arrayPrototype.getMethods().registerMethod(PandaMethod.builder()
                .methodName("toString")
                .returnType(PandaTypes.STRING.getReference())
                .methodBody((branch, instance, parameters) -> {
                    if (!instance.getClass().isArray()) {
                        throw new RuntimeException();
                    }

                    branch.setReturnValue(new PandaValue(PandaTypes.STRING, Arrays.toString((Object[]) instance)));
                })
                .build());

        return arrayPrototype.getReference();
    }

}
