package org.panda_lang.panda.core.parser.essential.assistant;

public class FieldAssistant
{

    public static String[] splitAndClear(String source)
    {
        String[] ss = source.split("=", 2);
        switch (ss.length)
        {
            case 1:
                ss[0] = clear(ss[0], 1);
                break;
            case 2:
                ss[0] = clear(ss[0], 1);
                ss[1] = clear(ss[1], 0);
                break;
        }
        return ss;
    }

    public static String clear(String s, int space)
    {
        StringBuilder node = new StringBuilder();
        boolean string = false;
        int accruedSpaces = 0;
        for (char c : s.toCharArray())
        {
            if (!string && Character.isWhitespace(c))
            {
                if (node.toString().equals("new"))
                {
                    node.append(c);
                } else if (accruedSpaces < space)
                {
                    node.append(c);
                    accruedSpaces++;
                }
            } else if (string || c != ';')
            {
                if (c == '"')
                {
                    string = !string;
                }
                node.append(c);
            }
        }
        return node.toString();
    }

}
