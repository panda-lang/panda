package org.panda_lang.panda.framework.language.architecture.module;

import org.panda_lang.panda.framework.design.architecture.module.LivingModule;
import org.panda_lang.panda.framework.design.architecture.module.Module;
import org.panda_lang.panda.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.utilities.commons.iterable.ResourcesIterable;

public class PandaLivingModule extends PandaModule implements LivingModule {

    protected final Module module;
    protected final ModuleLoader loader;

    public PandaLivingModule(ModuleLoader loader, Module module) {
        super(module.getName());

        this.module = module;
        this.loader = loader;
    }

    @Override
    public int getAmountOfReferences() {
        return super.getAmountOfReferences() + module.getAmountOfReferences();
    }

    @Override
    public Iterable<ClassPrototypeReference> getReferences() {
        return new ResourcesIterable<>(super.getReferences(), module.getReferences());
    }

    @Override
    public ModuleLoader getModuleLoader() {
        return loader;
    }

    @Override
    public Module getModule() {
        return module;
    }

}
