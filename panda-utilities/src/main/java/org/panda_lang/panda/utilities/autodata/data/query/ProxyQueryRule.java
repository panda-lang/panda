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

package org.panda_lang.panda.utilities.autodata.data.query;

import org.panda_lang.panda.utilities.autodata.data.entity.EntitySchemeProperty;
import org.panda_lang.panda.utilities.commons.collection.Pair;

import java.util.List;

final class ProxyQueryRule implements DataQueryRule {

    private final List<Pair<DataRuleProperty, Object>> properties;

    public ProxyQueryRule(List<Pair<DataRuleProperty, Object>> properties) {
        this.properties = properties;
    }

    @Override
    public List<Pair<DataRuleProperty, Object>> getProperties() {
        return properties;
    }

}
