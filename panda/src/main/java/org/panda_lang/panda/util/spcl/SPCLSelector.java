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

package org.panda_lang.panda.util.spcl;

import org.panda_lang.panda.util.spcl.value.SPCLSection;

import java.util.Map;

public class SPCLSelector {

    public static SPCLEntry select(SPCLSection section, String key) {
        Map<String, SPCLEntry> entries = section.getEntries();

        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        String[] parts = key.split(".");
        SPCLEntry entry = entries.get(parts[0]);

        if (entry == null) {
            return null;
        }

        // TODO

        return entry;
    }

}
