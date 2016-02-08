package org.panda_lang.panda.util.configuration;

import org.panda_lang.panda.util.IOUtils;

import java.io.File;
import java.util.*;

public class PandaConfiguration {

    private File configuration;
    private String[] code;
    private Map<String, Object> map;

    public PandaConfiguration() {
        this.map = new HashMap<>();
        this.code = new String[0];
    }

    public PandaConfiguration(File file) {
        this.configuration = file;
        this.code = IOUtils.getContentAsLines(file);
        this.map = new ConfigurationParser(code).getMap();
    }

    public void save() {
        if (configuration == null) {
            return;
        }
        new ConfigurationFile(configuration, this).save();
    }

    public void save(File file) {
        new ConfigurationFile(file, this).save();
        this.configuration = file;
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

    public Collection<String> getKeys() {
        Collection<String> list = new ArrayList<>();
        list.addAll(map.keySet());
        return list;
    }

    public void clear() {
        this.code = null;
        this.configuration = null;
        this.map = null;
    }

    public Map<String, Object> getMap() {
        return this.map;
    }

    public String[] getCode() {
        return this.code;
    }

    public File getConfigurationFile() {
        return this.configuration;
    }

}
