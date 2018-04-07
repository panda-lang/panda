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

package org.panda_lang.panda.utilities.commons.ads;

import org.panda_lang.panda.utilities.commons.ads.collection.ADSCollection;

import java.util.Map;

public class AutomatedDataSpace {

    private final Map<Class<?>, ADSCollection> collections;

    public AutomatedDataSpace(AutomatedDataSpaceBuilder builder) {
        this.collections = builder.collections;
    }

    public AutomatedDataInterface createInterface() {
        return new AutomatedDataInterface(this);
    }

    public ADSCollection getCollection(Class<?> type) {
        return this.collections.get(type);
    }

    public static AutomatedDataSpaceBuilder builder() {
        return new AutomatedDataSpaceBuilder();
    }

}
