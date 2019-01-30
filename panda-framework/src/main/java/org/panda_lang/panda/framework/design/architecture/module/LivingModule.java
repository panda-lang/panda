package org.panda_lang.panda.framework.design.architecture.module;

public interface LivingModule extends Module {

    ModuleLoader getModuleLoader();

    Module getModule();

}
