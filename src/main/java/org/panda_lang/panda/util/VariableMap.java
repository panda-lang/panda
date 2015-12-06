package org.panda_lang.panda.util;

import org.panda_lang.panda.core.syntax.Essence;

import java.util.HashMap;
import java.util.Map;

public class VariableMap<K, V> {

    private final VariableMap<String, Essence> parent;
    private final Map<String, Essence> local;

    public VariableMap() {
        this.local = new HashMap<String, Essence>();
        this.parent = null;
    }

    public VariableMap(VariableMap<String, Essence> parent) {
        this.local = new HashMap<String, Essence>();
        this.parent = parent;
    }

    public void put(String key, Essence o) {
        if (this.parent != null && this.parent.parentContainsKey(key)) {
            this.parent.put(key, o);
        } else {
            this.local.put(key, o);
        }
    }

    public Essence get(String name) {
        Essence o = local.get(name);
        if (o == null && parent != null) {
            o = parent.get(name);
        }
        return o;
    }

    public boolean containsKey(String name) {
        if (local.containsKey(name)) {
            return true;
        } else if (parent != null && parent.containsKey(name)) {
            return true;
        }
        return false;
    }

    public boolean parentContainsKey(String name) {
        if (parent != null && parent.containsKey(name)) {
            return true;
        }
        return false;
    }

    public VariableMap<String, Essence> getParent() {
        return this.parent;
    }

    public Map<String, Essence> getLocal() {
        return this.local;
    }

}

