package org.panda_lang.panda.util;

public class SimpleEntry<K, V>
{

    private K key;
    private V value;

    public SimpleEntry()
    {
    }

    public SimpleEntry(K key, V value)
    {
        this.key = key;
        this.value = value;
    }

    public void set(K key, V value)
    {
        this.key = key;
        this.value = value;
    }

    public void setKey(K key)
    {
        this.key = key;
    }

    public void setValue(V value)
    {
        this.value = value;
    }

    public K getKey()
    {
        return this.key;
    }

    public V getValue()
    {
        return this.value;
    }

}
