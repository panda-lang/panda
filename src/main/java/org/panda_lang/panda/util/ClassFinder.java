package org.panda_lang.panda.util;

import org.panda_lang.panda.Panda;

import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassFinder {

    public void loadClasses(String basePackage, String[] subPackages) throws IOException {
        JarFile jar = new JarFile(Panda.getDirectory());
        for (int i = 0; i < subPackages.length; i++) {
            subPackages[i] = (subPackages[i].replace('.', '/') + "/");
        }
        basePackage = basePackage.replace('.', '/') + "/";
        try {
            Enumeration<JarEntry> en = jar.entries();
            while (en.hasMoreElements()) {
                JarEntry e = en.nextElement();
                if ((e.getName().startsWith(basePackage)) && (e.getName().endsWith(".class"))) {
                    boolean load = subPackages.length == 0;
                    for (String sub : subPackages) {
                        if (e.getName().startsWith(sub, basePackage.length())) {
                            load = true;
                            break;
                        }
                    }
                    if (load) {
                        String c = e.getName().replace('/', '.').substring(0, e.getName().length() - ".class".length());
                        try {
                            Class.forName(c, true, Panda.class.getClassLoader());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        } finally {
            try {
                jar.close();
            } catch (IOException localIOException) {
                localIOException.printStackTrace();
            }
        }
    }
}
