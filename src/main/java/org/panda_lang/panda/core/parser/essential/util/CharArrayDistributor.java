package org.panda_lang.panda.core.parser.essential.util;

public class CharArrayDistributor
{

    private final char[] array;
    private int i;

    public CharArrayDistributor(char[] array)
    {
        this.array = array;
    }

    public char previous()
    {
        return i - 1 > 0 ? array[i - 1] : array[0];
    }

    public char current()
    {
        return array[i];
    }

    public boolean hasNext()
    {
        return i < array.length - 1;
    }

    public char next()
    {
        if (i + 1 < array.length)
        {
            i++;
            return array[i];
        }
        else
        {
            return array[array.length - 1];
        }
    }

    public char further()
    {
        if (i + 1 < array.length)
        {
            return array[i + 1];
        }
        else
        {
            return array[array.length - 1];
        }
    }

}
