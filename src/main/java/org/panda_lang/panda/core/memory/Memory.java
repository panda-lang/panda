package org.panda_lang.panda.core.memory;

import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.Essence;

import java.util.HashMap;
import java.util.Map;

public class Memory
{

    private final Cache cache;
    private final Memory parent;
    private final Map<String, Essence> local;
    private Block block;

    private Memory(Memory parent, Map<String, Essence> local)
    {
        this.parent = parent;
        this.local = local;
        this.cache = new Cache();
    }

    public Memory(Memory parent)
    {
        this.parent = parent;
        this.local = new HashMap<>();
        this.cache = new Cache();
    }

    public Memory()
    {
        this.parent = null;
        this.local = new HashMap<>();
        this.cache = new Cache();
    }

    public void put(String key, Essence o)
    {
        if (this.parent != null && this.parent.parentContainsKey(key))
        {
            this.parent.put(key, o);
        }
        else
        {
            this.local.put(key, o);
        }
    }

    public Essence get(String name)
    {
        Essence o = local.get(name);
        if (o == null && parent != null)
        {
            o = parent.get(name);
        }
        return o;
    }

    public Memory copy()
    {
        return new Memory(parent, new HashMap<>(local));
    }

    public boolean containsKey(String name)
    {
        if (local.containsKey(name))
        {
            return true;
        }
        else if (parent != null && parent.containsKey(name))
        {
            return true;
        }
        return false;
    }

    public boolean parentContainsKey(String name)
    {
        return parent != null && parent.containsKey(name);
    }

    public void setBlock(Block block)
    {
        this.block = block;
    }

    public Block getBlock()
    {
        return block;
    }

    public Memory getParent()
    {
        return this.parent;
    }

    public Map<String, Essence> getLocal()
    {
        return this.local;
    }

    public Cache getCache()
    {
        return cache;
    }

}

