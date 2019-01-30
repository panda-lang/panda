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

package org.panda_lang.panda.framework.design.resource.prototypes.model.loader;

import org.panda_lang.panda.framework.PandaFramework;
import org.panda_lang.panda.framework.design.architecture.module.ModulePath;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeMetadata;
import org.panda_lang.panda.framework.design.resource.prototypes.model.ClassPrototypeModel;
import org.panda_lang.panda.utilities.annotations.AnnotationsScannerProcess;
import org.panda_lang.panda.utilities.commons.StreamUtils;

import java.util.Collection;

public class AnnotatedModelsLoader {

    public void load(ModulePath path, AnnotationsScannerProcess process) {
        PandaFramework.getLogger().debug("");
        PandaFramework.getLogger().debug("--- Loading models ");

        Collection<Class<? extends ClassPrototypeModel>> models = process
                .createSelector()
                .selectSubtypesOf(ClassPrototypeModel.class);

        ModelLoader loader = new ModelLoader(path);
        Collection<ClassPrototype> loaded = loader.load(models);

        PandaFramework.getLogger().debug("Models: (" + loaded.size() + ") " + StreamUtils.map(loaded, ClassPrototypeMetadata::getClassName));
    }

}
