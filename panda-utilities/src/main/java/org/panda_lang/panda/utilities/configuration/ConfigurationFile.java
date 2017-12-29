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

import org.panda_lang.panda.utilities.commons.io.FileUtils;

import java.io.File;
import java.util.Collection;
import java.util.Map;

@Deprecated
public class ConfigurationFile {

    private final File file;
    private final PandaConfiguration panda;

    protected ConfigurationFile(File file, PandaConfiguration panda) {
        this.file = file;
        this.panda = panda;
    }

    protected void save() {
        StringBuilder configurationBuilder = new StringBuilder();
        Map<String, Object> map = panda.getMap();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            configurationBuilder.append(entry.getKey());
            configurationBuilder.append(":");

            Object value = entry.getValue();

            if (value instanceof Collection) {
                Collection collection = (Collection) value;

                if (collection.isEmpty()) {
                    configurationBuilder.append("[]");
                    configurationBuilder.append(System.lineSeparator());
                    continue;
                }

                for (Object element : collection) {
                    configurationBuilder.append(System.lineSeparator());
                    configurationBuilder.append("- ");
                    configurationBuilder.append(element);
                }

                configurationBuilder.append(System.lineSeparator());
                continue;
            }

            configurationBuilder.append(value);
            configurationBuilder.append(System.lineSeparator());
        }

        FileUtils.overrideFile(file, configurationBuilder.toString());
    }

}
