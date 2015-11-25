package org.panda_lang.panda.util.configuration;

import java.util.Iterator;
import java.util.Stack;

public class ConfigurationUtils {

    public static String getPath(Stack<String> stack) {
        StringBuilder sb = new StringBuilder();
        if (stack == null || stack.isEmpty()) return null;
        Iterator<String> it = stack.iterator();
        while (it.hasNext()) {
            String key = it.next();
            if (key == null || key.isEmpty()) continue;
            sb.append(".");
            sb.append(key);
        }
        String path = sb.toString();
        if (path.length() > 0) path = path.substring(1);
        return path;
    }

    public static int getTabs(String s) {
        if (s.isEmpty()) return 0;
        String t = "\t";
        int count = 0;
        int idx = 0;
        while ((idx = s.indexOf(t, idx)) != -1) {
            count++;
            idx += t.length();
        }
        return count;
    }

}
