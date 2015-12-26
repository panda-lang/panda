package org.panda_lang.panda.core;

public class ElementsPuller {

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
