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

package org.panda_lang.panda.util.configuration;

import org.panda_lang.panda.framework.util.FileUtils;

import java.io.File;
import java.util.*;

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

    public ConfigurationFile save() {
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

    public void set(String path, Object object) {
        this.map.put(path, object);
    }

    public boolean containsKey(String path) {
        return this.map.containsKey(path);
    }

    public Object get(String path) {
        return this.map.get(path);
    }

    public String getString(String path) {
        Object co = map.get(path);
        if (co != null) {
            if (co instanceof String) {
                return (String) co;
            }
        }
        return null;
    }

    public boolean getBoolean(String path) {
        Object co = map.get(path);
        if (co != null) {
            if (co instanceof String) {
                return Boolean.valueOf((String) co);
            }
        }
        return false;
    }

    public int getInt(String path) {
        Object co = map.get(path);
        if (co != null) {
            if (co instanceof String) {
                return Integer.valueOf((String) co);
            }
        }
        return 0;
    }

    public long getLong(String path) {
        Object co = map.get(path);
        if (co != null) {
            if (co instanceof String) {
                return Long.valueOf((String) co);
            }
        }
        return 0;
    }

    public double getDouble(String path) {
        Object co = map.get(path);
        if (co != null) {
            if (co instanceof String) {
                return Double.valueOf((String) co);
            }
        }
        return 0;
    }

    @SuppressWarnings("unchecked")
    public List<String> getStringList(String path) {
        Object co = map.get(path);
        if (co != null) {
            if (co instanceof List) {
                return (List<String>) co;
            }
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

    public void clear() {
        this.configuration = null;
        this.map = null;
    }

    public Collection<String> getKeys() {
        Collection<String> list = new ArrayList<>();
        list.addAll(map.keySet());
        return list;
    }

    public Map<String, Object> getMap() {
        return this.map;
    }

    public File getConfigurationFile() {
        return this.configuration;
    }

}
