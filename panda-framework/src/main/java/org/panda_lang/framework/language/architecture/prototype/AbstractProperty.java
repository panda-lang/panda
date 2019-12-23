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

package org.panda_lang.framework.language.architecture.prototype;

import org.panda_lang.framework.design.architecture.prototype.Property;
import org.panda_lang.framework.design.architecture.prototype.Visibility;
import org.panda_lang.framework.design.interpreter.source.SourceLocation;

public abstract class AbstractProperty implements Property {

    private final String name;
    private final SourceLocation location;
    private final Visibility visibility;
    private final boolean isNative;

    protected AbstractProperty(String name, SourceLocation location, Visibility visibility, boolean isNative) {
        if (name == null) {
            throw new IllegalArgumentException("Prototype name is not defined");
        }

        if (location == null) {
            throw new IllegalArgumentException("Missing location of prototype");
        }

        if (visibility == null) {
            throw new IllegalArgumentException("Missing visibility of prototype");
        }

        this.name = name;
        this.location = location;
        this.visibility = visibility;
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
    public SourceLocation getLocation() {
        return location;
    }

    @Override
    public String getSimpleName() {
        return name;
    }

}
