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

package org.panda_lang.framework.architecture.type.member;

import org.panda_lang.framework.architecture.type.Visibility;
import org.panda_lang.framework.interpreter.source.Location;
import panda.utilities.ValidationUtils;

public abstract class AbstractMetadata implements Metadata {

    private final String name;
    private final Location location;
    private final Visibility visibility;
    private final boolean isNative;

    protected AbstractMetadata(String name, Location location, Visibility visibility, boolean isNative) {
        this.name = ValidationUtils.notNull(name, "Name is not defined");
        this.location = ValidationUtils.notNull(location, "Missing location of type");
        this.visibility = ValidationUtils.notNull(visibility, "Missing visibility of type");
        this.isNative = isNative;
    }

    @Override
    public boolean isNative() {
        return isNative;
    }

    @Override
    public Visibility getVisibility() {
        return visibility;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public String getSimpleName() {
        return name;
    }

}
