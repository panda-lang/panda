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

package org.panda_lang.panda.pmm;

import org.panda_lang.framework.PandaFrameworkException;

final class Dependency {

    private final String scope;
    private final String name;
    private final String version;
    private final String address;

    private Dependency(String scope, String name, String version, String address) {
        this.scope = scope;
        this.name = name;
        this.version = version;
        this.address = address;
    }

    protected String getAddress() {
        return address;
    }

    protected String getDirectoryName() {
        return getName() + "-" + getVersion();
    }

    protected String getVersion() {
        return version;
    }

    protected String getName() {
        return name;
    }

    protected String getScope() {
        return scope;
    }

    @Override
    public String toString() {
        return scope + "/" + name + " (" + address + ")";
    }

    protected static Dependency parseDependency(String qualifier) {
        if (qualifier.startsWith("github:")) {
            String uri = qualifier.substring("github:".length());

            String[] byOwner = uri.split("/");
            String owner = byOwner[0];

            String[] byVersion = byOwner[1].split("@");
            String name = byVersion[0];
            String version = byVersion[1];

            return new Dependency(owner, name, version, "https://github.com/" + owner + "/" + name +  "/archive/" + version + ".zip");
        }

        throw new PandaFrameworkException("Unsupported dependency format: " + qualifier);
    }

}
