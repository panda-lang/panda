/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.design.architecture.module;

import org.panda_lang.panda.framework.design.architecture.module.*;
import org.panda_lang.panda.framework.design.architecture.prototype.*;
import org.panda_lang.panda.language.interpreter.parsers.prototype.mapper.*;
import org.panda_lang.panda.utilities.commons.io.*;
import org.panda_lang.panda.utilities.commons.objects.*;

import java.util.*;

public class PandaModuleRegistryAssistant {

    private static final ClassPrototypeMappingManager MAPPING_MANAGER = new ClassPrototypeMappingManager();

    public static int countPrototypes(ModuleRegistry registry) {
        int count = 0;

        for (Module module : registry.getModules()) {
            count += module.getPrototypeCount();
        }

        return count;
    }

    public static ClassPrototype forClass(ModuleRegistry registry, Class<?> clazz) {
        String name = PackageUtils.toString(clazz.getPackage(), "") + ":" + clazz.getSimpleName();
        ClassPrototype prototype = forName(registry, name);

        if (prototype == null) {
            MAPPING_MANAGER.loadClass(clazz);
            Collection<ClassPrototype> generated = MAPPING_MANAGER.generate(registry);

            if (generated == null) {
                return null;
            }
        }

        return forName(registry, name);
    }

    public static ClassPrototype forName(ModuleRegistry registry, String full) {
        String[] reference = full.split(":");

        if (reference.length == 0 || reference.length > 2) {
            return null;
        }

        Module module = (reference.length == 1) ? registry.get(null) : registry.get(reference[0]);

        if (module == null) {
            return null;
        }

        String className = (reference.length == 1) ? reference[0] : reference[1];

        if (StringUtils.isEmpty(className)) {
            return null;
        }

        return module.get(className);
    }

    public static long getTotalLoadTime() {
        return MAPPING_MANAGER.getTotalLoadTime();
    }

}
