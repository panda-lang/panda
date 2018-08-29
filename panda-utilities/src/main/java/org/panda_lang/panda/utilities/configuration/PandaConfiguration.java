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

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.utilities.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated
public class PandaConfiguration {

    private File configuration;
    private Map<String, Object> map;

    public PandaConfiguration() {
        this.map = new HashMap<>();
    }

    public PandaConfiguration(File file) {
        String[] source = FileUtils.getContentAsLines(file);
        ConfigurationParser configurationParser = new ConfigurationParser(source);

        this.configuration = file;
        this.map = configurationParser.getMap();
    }

    public PandaConfiguration(String content) {
        String[] source = content.split("\n");
        ConfigurationParser configurationParser = new ConfigurationParser(source);

        this.map = configurationParser.getMap();
    }

    public @Nullable ConfigurationFile save() {
        if (configuration == null) {
            return null;
        }

        ConfigurationFile file = new ConfigurationFile(configuration, this);
        file.save();

        return file;
    }

    public ConfigurationFile save(File file) {
        this.configuration = file;

        ConfigurationFile configurationFile = new ConfigurationFile(file, this);
        configurationFile.save();

        return configurationFile;
    }

    public boolean containsKey(String path) {
        return this.map.containsKey(path);
    }

    public void clear() {
        this.configuration = null;
        this.map = null;
    }

    public void set(String path, Object object) {
        this.map.put(path, object);
    }

    public Object get(String path) {
        return this.map.get(path);
    }

    public @Nullable String getString(String path) {
        Object co = map.get(path);

        if (co instanceof String) {
            return co.toString();
        }

        return null;
    }

    public boolean getBoolean(String path) {
        Object co = map.get(path);

        if (co instanceof String) {
            return Boolean.parseBoolean((String) co);
        }

        return false;
    }

    public int getInt(String path) {
        Object co = map.get(path);

        if (co instanceof String) {
            return Integer.parseInt((String) co);
        }

        return 0;
    }

    public long getLong(String path) {
        Object co = map.get(path);

        if (co instanceof String) {
            return Long.parseLong((String) co);
        }

        return 0;
    }

    public double getDouble(String path) {
        Object co = map.get(path);

        if (co instanceof String) {
            return Double.parseDouble((String) co);
        }

        return 0;
    }

    @SuppressWarnings("unchecked")
    public @Nullable List<String> getStringList(String path) {
        if (map.containsKey(path)) {
            Object co = map.get(path);

            if (co instanceof List) {
                return (List<String>) co;
            }

            return new ArrayList<>();
        }

        return null;
    }

    public Collection<String> getSectionKeys(String path) {
        Collection<String> list = new ArrayList<>();
        path = path + ".";

        for (String key : map.keySet()) {
            if (key.startsWith(path)) {
                list.add(key.substring(path.length()));
            }
        }

        return list;
    }

    public Collection<String> getKeys() {
        return new ArrayList<>(map.keySet());
    }

    public Map<String, Object> getMap() {
        return this.map;
    }

    public File getConfigurationFile() {
        return this.configuration;
    }

}
