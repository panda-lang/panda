/*
 * Copyright (c) 2021 dzikoysk
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

package org.panda_lang.panda.language.std;

import org.panda_lang.framework.PandaFrameworkException;
import org.panda_lang.framework.architecture.module.Module;
import org.panda_lang.framework.architecture.module.TypeLoader;
import org.panda_lang.framework.architecture.packages.Package;
import org.panda_lang.framework.architecture.packages.Packages;
import org.panda_lang.framework.architecture.type.generator.TypeGenerator;
import org.panda_lang.framework.resource.Mappings;
import org.panda_lang.framework.resource.Mappings.CustomInitializer;
import org.panda_lang.panda.PandaConstants;
import org.panda_lang.utilities.commons.ClassUtils;
import org.panda_lang.utilities.commons.StringUtils;

import java.io.File;

public final class StdLoader {

    public void load(Packages packages, TypeGenerator typeGenerator, TypeLoader typeLoader) {
        load(packages, typeGenerator, typeLoader, PandaModules.getMappings());
    }

    public void load(Packages packages, TypeGenerator typeGenerator, TypeLoader typeLoader, Object[] mappings) {
        for (Object object : mappings) {
            loadClass(packages, typeGenerator, typeLoader, object);
        }
    }

    private void loadClass(Packages packages, TypeGenerator typeGenerator, TypeLoader typeLoader, Object mappings) {
        Mappings mappingsInfo = mappings.getClass().getAnnotation(Mappings.class);

        Package packageInfo = packages.getPackage(mappingsInfo.pkg())
                .orElseGet(() -> packages.registerPackage(new Package(mappingsInfo.pkg(), mappingsInfo.author(), PandaConstants.VERSION, new File("mappings"))));

        Module module = packageInfo.getModuleSource(mappingsInfo.module())
                .orElseGet(() -> packageInfo.createModule(mappingsInfo.module()))
                .getModule();

        if (mappings instanceof CustomInitializer) {
            CustomInitializer initializer = (CustomInitializer) mappings;
            initializer.initialize(module, typeGenerator, typeLoader);
        }

        String packageName = mappingsInfo.commonPackage().isEmpty() ? StringUtils.EMPTY : mappingsInfo.commonPackage() + ".";

        for (String name : mappingsInfo.classes()) {
            ClassUtils.forName(packageName + name)
                    .map(type -> typeGenerator.generate(module, name, type))
                    .peek(module::add)
                    .peek(reference -> typeLoader.load(reference.fetchType()))
                    .orThrow(() -> {
                        throw new PandaFrameworkException("Cannot find class " + name + " in " + packageName);
                    });
        }
    }

}
