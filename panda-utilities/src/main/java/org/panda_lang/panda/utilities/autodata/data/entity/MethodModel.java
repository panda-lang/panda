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

public final class MethodModel {

    private final Method method;
    private final MethodType type;
    private final Property property;

    MethodModel(Method method, MethodType type, Property property) {
        this.method = method;
        this.property = property;
        this.type = type;
    }

    public Property getProperty() {
        return property;
    }

    public MethodType getType() {
        return type;
    }

    public Method getMethod() {
        return method;
    }

}