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

package org.panda_lang.framework.architecture.packages;

import org.panda_lang.framework.architecture.module.Module;
import org.panda_lang.framework.architecture.module.ModuleSource;
import org.panda_lang.framework.architecture.module.PandaModule;
import org.panda_lang.framework.interpreter.source.SourceService;
import org.panda_lang.utilities.commons.collection.Maps;
import org.panda_lang.utilities.commons.function.Option;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Package {

    public static final String DEFAULT_MODULE = "";

    private final String name;
    private final String author;
    private final String version;
    private final File directory;
    private final Map<String, ModuleSource> unloadedModules = new HashMap<>();
    private final Map<String, Module> loadedModules = new HashMap<>();

    public Package(String name, String author, String version, File directory) {
        this.name = name;
        this.author = author;
        this.version = version;
        this.directory = directory;
    }

    public ModuleSource addModule(ModuleSource moduleSource) {
        return Maps.put(unloadedModules, moduleSource.getSimpleName(), moduleSource);
    }

    public ModuleSource createModule(String moduleName) {
        return Maps.put(unloadedModules, moduleName, new ModuleSource(new PandaModule(this, moduleName), new ArrayList<>()));
    }

    public Option<Module> forModule(SourceService sourceService, String name) {
        Module loadedModule = loadedModules.get(name);

        if (loadedModule != null) {
            return Option.of(loadedModule);
        }

        ModuleSource unloadedModule = unloadedModules.get(name);

        if (unloadedModule == null) {
            return Option.none();
        }

        sourceService.pushSources(unloadedModule.getSources());
        return Option.of(unloadedModule.getModule());
    }

    public Module forMainModule(SourceService sourceService) {
        return forModule(sourceService, DEFAULT_MODULE).orThrow(() -> {
            throw new IllegalStateException("Broken package - missing main module");
        });
    }

    public Option<ModuleSource> getModuleSource(String moduleName) {
        return Option.of(unloadedModules.get(moduleName));
    }

    public File getDirectory() {
        return directory;
    }

    public String getVersion() {
        return version;
    }

    public String getAuthor() {
        return author;
    }

    public String getName() {
        return getAuthor() + "/" + getSimpleName();
    }

    public String getSimpleName() {
        return name;
    }

}
