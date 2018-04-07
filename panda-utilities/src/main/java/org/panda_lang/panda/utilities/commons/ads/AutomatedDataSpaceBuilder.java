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
import org.panda_lang.panda.utilities.commons.ads.collection.ADSCollectionBuilder;

import java.util.HashMap;
import java.util.Map;

public class AutomatedDataSpaceBuilder {

    protected final Map<Class<?>, ADSCollection> collections;

    public AutomatedDataSpaceBuilder() {
        this.collections = new HashMap<>();
    }

    public ADSCollectionBuilder addCollection() {
        return new ADSCollectionBuilder(this);
    }

    public AutomatedDataSpaceBuilder addCollection(ADSCollection collection) {
        this.collections.put(collection.getType(), collection);
        return this;
    }

    public AutomatedDataSpace build() {
        return new AutomatedDataSpace(this);
    }

}
