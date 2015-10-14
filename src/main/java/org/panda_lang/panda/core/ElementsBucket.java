package org.panda_lang.panda.core;

import org.panda_lang.panda.core.scheme.BlockScheme;
import org.panda_lang.panda.core.scheme.ObjectScheme;

import java.util.ArrayList;
import java.util.Collection;

public class ElementsBucket {

    private static Collection<BlockScheme> blocks = new ArrayList<>();
    private static Collection<ObjectScheme> objects = new ArrayList<>();

    public static void registerBlock(BlockScheme scheme){
        blocks.add(scheme);
    }

    public static void registerObject(ObjectScheme scheme){
        objects.add(scheme);
    }

    public static Collection<ObjectScheme> getObjects() {
        return objects;
    }

    public static Collection<BlockScheme> getBlocks() {
        return blocks;
    }

    public static void loadClasses(String basePackage, String... classes) {
        for(String clazz : classes) try {
            Class.forName(basePackage + "." + clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
