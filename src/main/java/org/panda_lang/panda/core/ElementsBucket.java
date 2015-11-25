package org.panda_lang.panda.core;

import org.panda_lang.panda.core.parser.improved.Pattern;
import org.panda_lang.panda.core.scheme.BlockScheme;
import org.panda_lang.panda.core.scheme.ObjectScheme;
import org.panda_lang.panda.core.scheme.ParserScheme;

import java.util.ArrayList;
import java.util.Collection;

public class ElementsBucket {

    private final static Collection<ParserScheme> parsers = new ArrayList<>();
    private final static Collection<BlockScheme> blocks = new ArrayList<>();
    private final static Collection<ObjectScheme> objects = new ArrayList<>();

    public static void registerParser(ParserScheme scheme) {
        parsers.add(scheme);
    }

    public static void registerBlock(BlockScheme scheme) {
        blocks.add(scheme);
    }

    public static void registerObject(ObjectScheme scheme) {
        objects.add(scheme);
    }

    public static Collection<ParserScheme> getParsers() {
        return parsers;
    }

    public static Collection<ObjectScheme> getObjects() {
        return objects;
    }

    public static Collection<BlockScheme> getBlocks() {
        return blocks;
    }

    public static void loadClasses(String basePackage, String... classes) {
        for (String clazz : classes) {
            try {
                Class.forName(basePackage + "." + clazz);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static ParserScheme getParserScheme(String pattern) {
        for (ParserScheme parserScheme : parsers) {
            Pattern parserSchemePattern = parserScheme.getPattern();
            if (parserSchemePattern.match(pattern)) {
                return parserScheme;
            }
        }
        return null;
    }

}
