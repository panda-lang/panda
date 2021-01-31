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

package org.panda_lang.framework.architecture.type;

import org.panda_lang.framework.architecture.module.Module;
import org.panda_lang.framework.interpreter.source.Location;
import org.panda_lang.utilities.commons.function.Completable;

public class Reference {

    private final Completable<Type> type;
    private final String name;
    private final Module module;
    private final Visibility visibility;
    private final String kind;
    private final Location location;

    public Reference(Completable<Type> type, Module module, String name, Visibility visibility, String kind, Location location) {
        this.type = type;
        this.name = name;
        this.module = module;
        this.visibility = visibility;
        this.kind = kind;
        this.location = location;
    }

    public Reference(Type type) {
        this(Completable.completed(type), type.getModule(), type.getSimpleName(), type.getVisibility(), type.getKind(), type.getLocation());
    }

    @Override
    public boolean equals(Object to) {
        if (this == to) {
            return true;
        }

        if (to == null || getClass() != to.getClass()) {
            return false;
        }

        Reference reference = (Reference) to;

        if (!name.equals(reference.name)) {
            return false;
        }

        return module.equals(reference.module);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + module.hashCode();
        return result;
    }

    public boolean isLoaded() {
        return type.isReady();
    }

    public Type fetchType() {
        return getType().get();
    }

    public Completable<Type> getType() {
        return type;
    }

    public String getName() {
        return module.getName() + "::" + getSimpleName();
    }

    public Module getModule() {
        return module;
    }

    public String getSimpleName() {
        return name;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public String getKind() {
        return kind;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return getName();
    }

}
