/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.utilities.configuration;

import org.jetbrains.annotations.Nullable;

public class ConfigurationObject {

    private final ConfigurationType type;
    private Object object;
    private int position;

    protected ConfigurationObject(ConfigurationType type) {
        this.type = type;
    }

    protected void setPosition(int i) {
        this.position = i;
    }

    protected void setObject(Object o) {
        this.object = o;
    }

    protected @Nullable String getString() {
        if (type == ConfigurationType.STRING) {
            return object.toString();
        }

        return null;
    }

    protected int getPosition() {
        return this.position;
    }

    protected Object getObject() {
        return this.object;
    }

    protected ConfigurationType getType() {
        return this.type;
    }

}
