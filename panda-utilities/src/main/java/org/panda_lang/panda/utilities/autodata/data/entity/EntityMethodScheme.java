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

package org.panda_lang.panda.utilities.autodata.data.entity;

import java.lang.reflect.Method;

public final class EntityMethodScheme {

    private final Method method;
    private final EntitySchemeProperty property;
    private final EntityMethodType type;

    EntityMethodScheme(Method method, EntitySchemeProperty property, EntityMethodType type) {
        this.method = method;
        this.property = property;
        this.type = type;
    }

    public EntityMethodType getType() {
        return type;
    }

    public EntitySchemeProperty getProperty() {
        return property;
    }

    public Method getMethod() {
        return method;
    }

}