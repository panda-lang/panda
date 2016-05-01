package org.panda_lang.panda.core.memory;

import org.panda_lang.panda.core.Inst;
import org.panda_lang.panda.core.statement.Block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Memory {

    private final Memory parent;
    private final Map<String, Inst> local;
    private final Collection<MemoryFollower> memoryFollowers;
    private Cache cache;
    private Block block;

    private Memory(Memory parent, Map<String, Inst> local) {
        this.parent = parent;
        this.local = local;
        this.cache = new Cache();
        this.memoryFollowers = new ArrayList<>(0);
    }

    public Memory(Memory parent) {
        this.parent = parent;
        this.local = new HashMap<>();
        this.cache = new Cache();
        this.memoryFollowers = new ArrayList<>(0);
    }

    public Memory() {
        this.parent = Global.COMMON_MEMORY;
        this.local = new HashMap<>();
        this.cache = new Cache();
        this.memoryFollowers = new ArrayList<>(0);
    }

    public void put(String key, Inst o) {
        if (this.parent != null && this.parent.containsKey(key)) {
            this.parent.put(key, o);
        }
        else {
            this.local.put(key, o);
        }
        for (MemoryFollower memoryFollower : memoryFollowers) {
            memoryFollower.put(key, o);
        }
    }

    public Inst get(String name) {
        Inst o = local.get(name);
        if (o == null && parent != null) {
            o = parent.get(name);
        }
        return o;
    }

    public void delete(String name) {
        Inst inst = local.remove(name);
        if (inst == null && parent != null) {
            parent.delete(name);
        }
    }

    public void registerMemoryFollower(MemoryFollower memoryFollower) {
        memoryFollowers.add(memoryFollower);
    }

    public Memory copy() {
        return new Memory(parent, new HashMap<>(local));
    }

    public boolean containsKey(String name) {
        if (local.containsKey(name)) {
            return true;
        }
        else if (parent != null && parent.containsKey(name)) {
            return true;
        }
        return false;
    }

    public boolean parentContainsKey(String name) {
        return parent != null && parent.containsKey(name);
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public Memory getParent() {
        return this.parent;
    }

    public Map<String, Inst> getLocal() {
        return this.local;
    }

    public Cache getCache() {
        return cache;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }

}

