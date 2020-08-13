/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.panda.manager;

import org.panda_lang.language.PandaFrameworkException;

final class DependencyFactory {

    public Dependency createDependency(String qualifier) {
        String type;

        if (qualifier.startsWith("github:")) {
            type = "github";
        }
        else if (qualifier.startsWith("maven:")) {
            type = "maven";
        }
        else {
            throw new PandaFrameworkException("Unsupported dependency format: " + qualifier);
        }

        String uri = qualifier.substring(type.length() + 1);

        String[] byOwner = uri.split("/");
        String owner = byOwner[0];

        String[] byVersion = byOwner[1].split("@");
        String version = byVersion[1];
        String name = byVersion[0];

        return new Dependency(type, owner, name, version);
    }

}
