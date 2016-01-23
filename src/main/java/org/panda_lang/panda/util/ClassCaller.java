package org.panda_lang.panda.util;

public class ClassCaller {

    public static void loadClasses(String basePackage, String... classes) {
        for (String clazz : classes) {
            try {
                Class.forName(basePackage + "." + clazz);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
