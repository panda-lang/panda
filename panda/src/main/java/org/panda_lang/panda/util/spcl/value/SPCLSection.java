/*
 * Copyright (c) 2015-2017 Dzikoysk
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

package org.panda_lang.panda.util.spcl.value;

import org.panda_lang.panda.util.spcl.SPCLEntry;
import org.panda_lang.panda.util.spcl.SPCLSelector;

import java.util.HashMap;
import java.util.Map;

public class SPCLSection implements SPCLValue {

    private final Map<String, SPCLEntry> entries;
    private final SPCLSection parent;

    public SPCLSection() {
        this(null);
    }

    public SPCLSection(SPCLSection parent) {
        this.entries = new HashMap<>();
        this.parent = parent;
    }

    public void put(SPCLEntry entry) {
        entries.put(entry.getKey(), entry);
    }

    public Object get(String key) {
        SPCLEntry entry = SPCLSelector.select(this, key);
        return entry != null ? entry.getValue().getValue() : null;
    }

    @Override
    public boolean isSection() {
        return true;
    }

    @Override
    public boolean isList() {
        return false;
    }

    @Override
    public Object getValue() {
        return this;
    }

    public SPCLSection getParent() {
        return parent;
    }

    public Map<String, SPCLEntry> getEntries() {
        return entries;
    }

}
