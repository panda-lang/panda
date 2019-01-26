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
