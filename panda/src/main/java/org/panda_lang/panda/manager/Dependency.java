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

import org.panda_lang.utilities.commons.ArrayUtils;
import org.panda_lang.utilities.commons.StringUtils;

import java.util.Objects;

final class Dependency {

    private final String type;
    private final String owner;
    private final String name;
    private final String version;

    protected Dependency(String type, String owner, String name, String version) {
        this.type = type;
        this.owner = owner;
        this.name = name;
        this.version = version;
    }

    protected boolean hasHigherVersion(String anotherVersion) {
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
        return getOwner().equals(that.getOwner()) && getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOwner(), getName());
    }

    protected String getVersion() {
        return version;
    }

    protected String getName() {
        return name;
    }

    protected String getOwner() {
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
