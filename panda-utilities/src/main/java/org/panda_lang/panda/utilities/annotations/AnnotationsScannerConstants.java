package org.panda_lang.panda.utilities.annotations;

import java.util.Arrays;
import java.util.List;

public class AnnotationsScannerConstants {

    static final String[] PANDA_PACKAGES = {
            "META-INF",
            "java", "com.sun", "sun", "jdk", "javax", "oracle", "com.oracle", "netscape",       // Java
            "org.apache", "com.google", "org.slf4j",                                            // Popular
            "org.junit", "junit", "org.opentest4j",                                            // Tests
            "org.jetbrains", "org.intellij", "com.intellij",                                    // IDE
            "javassist", "org.fusesource", "org.apiguardian"                                    // Internal
    };

    static List<String> primitiveNames = Arrays.asList("boolean", "char", "byte", "short", "int", "long", "float", "double", "void");

    static List<String> primitiveDescriptors = Arrays.asList("Z", "C", "B", "S", "I", "J", "F", "D", "V");

    static List<Class> primitiveTypes = Arrays.asList(boolean.class, char.class, byte.class, short.class, int.class, long.class, float.class, double.class, void.class);

}
