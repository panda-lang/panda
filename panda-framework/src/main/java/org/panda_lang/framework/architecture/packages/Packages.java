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
import org.panda_lang.framework.interpreter.source.SourceService;
import org.panda_lang.utilities.commons.StringUtils;
import panda.std.Option;

import java.util.HashMap;
import java.util.Map;

public final class Packages {

    private final SourceService sourceService;
    private final Map<String, Package> packages = new HashMap<>();

    public Packages(SourceService sourceService) {
        this.sourceService = sourceService;
    }

    public Package registerPackage(Package pkg) {
        packages.put(pkg.getSimpleName(), pkg);
        packages.put(pkg.getName(), pkg);
        return pkg;
    }

    public Option<Module> forModule(String qualifier) {
        String[] elements = StringUtils.split(qualifier, "@");

        if (elements.length != 2) {
            throw new IllegalArgumentException("Invalid qualifier: " + qualifier);
        }

        return getPackage(elements[0]).flatMap(pkg -> pkg.forModule(sourceService, elements[1]));
    }

    public Option<Package> getPackage(String packageName) {
        return Option.of(packages.get(packageName));
    }

    public SourceService getSourceService() {
        return sourceService;
    }

}
