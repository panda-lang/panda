package org.pandalang.panda.core;

public class Core {

    private static Core core;

    private Core() {
    }

    private void parsers() {
        ElementsBucket.loadClasses("org.panda_lang.panda.core.parser.improved.essential",
                "ConstructorParser",
                "EqualityParser",
                "MathParser",
                "MethodParser",
                "ParameterParser",
                "VariableParser",
                "VialParser");
    }

    private void blocks() {
        ElementsBucket.loadClasses("org.panda_lang.panda.core.syntax.block",
                "ClassBlock",
                "ElseThenBlock",
                "ForBlock",
                "IfThenBlock",
                "MethodBlock",
                "RunnableBlock",
                "ClassBlock",
                "ThreadBlock",
                "WhileBlock");
    }

    private void objects() {
        ElementsBucket.loadClasses("org.panda_lang.panda.lang",
                "PArray",
                "PBoolean",
                "PCharacter",
                "PFile",
                "PList",
                "PMap",
                "PNumber",
                "PObject",
                "PPanda",
                "PRunnable",
                "PStack",
                "PString",
                "PSystem",
                "PThread");
        ElementsBucket.loadClasses("org.panda_lang.panda.lang.ui",
                "PWindow");
    }

    public static Core registerDefault() {
        if (core != null) return core;
        core = new Core();
        core.parsers();
        core.blocks();
        core.objects();
        return core;
    }

}
