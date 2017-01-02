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

package org.panda_lang.panda.util.spcl;

import org.panda_lang.panda.util.spcl.value.SPCLSection;
import org.panda_lang.panda.util.spcl.value.SPCLValue;

import java.util.Map;

public class SPCLSelector {

    public static SPCLEntry select(SPCLSection section, String key) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        Map<String, SPCLEntry> entries = section.getEntries();
        String[] parts = key.split(".");

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            SPCLEntry entry = entries.get(part);

            if (entry == null) {
                return null;
            }

            if (i + 1 == part.length()) {
                return entry;
            }

            SPCLValue value = entry.getValue();

            if (value == null) {
                return null;
            }

            if (!value.isSection()) {
                return null;
            }

            SPCLSection nextSection = value.toSection();
            entries = nextSection.getEntries();
        }

        return null;
    }

}
