/*
 * Copyright (c) 2015-2016 Dzikoysk
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

    @Override
    public boolean isSection() {
        return true;
    }

    @Override
    public boolean isList() {
        return false;
    }

    public SPCLSection getParent() {
        return parent;
    }

    public Map<String, SPCLEntry> getEntries() {
        return entries;
    }

}
