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

package panda.manager;

import panda.utilities.ArrayUtils;
import panda.utilities.StringUtils;

import java.util.Objects;

public final class Dependency {

    private static final String MASTER = "master";

    private final String type;
    private final String owner;
    private final String name;
    private final String version;
    private final String alias;

    public Dependency(String type, String owner, String name, String version, String alias) {
        this.type = type;
        this.owner = owner;
        this.name = name;
        this.version = version;
        this.alias = alias;
    }

    public static Dependency createDependency(String qualifier) {
        String[] dependencyTypeElements = StringUtils.splitFirst(qualifier, ":");
        String type = dependencyTypeElements[0];

        String[] byOwner = dependencyTypeElements[1].split("/");
        String owner = byOwner[0];

        String[] byVersion = byOwner[1].split("@");
        String name = byVersion[0];
        String alias = name;
        String version = byVersion[1];

        if (version.contains(" as ")) {
            String[] byAlias = version.split(" as ");
            version = byAlias[0].trim();
            alias = byAlias[1].trim();
        }

        return new Dependency(type, owner, name, version, alias);
    }

    public boolean same(Dependency to) {
        return getAuthor().equals(to.getAuthor()) && getName().equals(to.getName());
    }

    public boolean hasHigherVersion(String anotherVersion) {
        if (version.equalsIgnoreCase(MASTER) || anotherVersion.equalsIgnoreCase(MASTER)) {
            return true;
        }

        String[] thisElements = StringUtils.split(version, ".");
        String[] anotherElements = StringUtils.split(anotherVersion, ".");
        int length = Math.max(version.length(), anotherElements.length);

        for (int index = 0; index < length; index++) {
            int thisUnit = Integer.parseInt(ArrayUtils.get(thisElements, index, "0"));
            int anotherUnit = Integer.parseInt(ArrayUtils.get(anotherElements, index, "0"));

            if (thisUnit == anotherUnit) {
                continue;
            }

            return thisUnit > anotherUnit;
        }

        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Dependency that = (Dependency) o;

        return type.equals(that.type) &&
                owner.equals(that.owner) &&
                name.equals(that.name) &&
                version.equals(that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, owner, name, version);
    }

    public String getIdentifier() {
        return getAuthor() + "/" + getName();
    }

    public String getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return owner;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return owner + "/" + name + "@" + version;
    }

}
