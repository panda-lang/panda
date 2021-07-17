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

package panda.interpreter.architecture.module;

import panda.interpreter.architecture.packages.Package;
import panda.interpreter.source.Source;

import java.util.Collection;

public class ModuleSource {

    private final Module module;
    private final Collection<? extends Source> sources;

    public ModuleSource(Module module, Collection<? extends Source> sources) {
        this.module = module;
        this.sources = sources;
    }

    public Collection<? extends Source> getSources() {
        return sources;
    }

    public Package getPackage() {
        return module.getPackage();
    }

    public Module getModule() {
        return module;
    }

    public String getSimpleName() {
        return module.getSimpleName();
    }

    public String getName() {
        return getPackage() + "@" + getSimpleName();
    }

}
