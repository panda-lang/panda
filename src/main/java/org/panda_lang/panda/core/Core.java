package org.panda_lang.panda.core;

public class Core {

    private static Core core;

    private Core() {
    }

    private void parsers() {
        ElementsBucket.loadClasses("org.panda_lang.panda.core.parser.essential",
                "ConstructorParser",
                "EqualityParser",
                "MathParser",
                "MethodParser",
                "ParameterParser",
                "VariableParser",
                "BlockParser");
    }

    private void blocks() {
        ElementsBucket.loadClasses("org.panda_lang.panda.core.syntax.block",
                "ElseThenBlock",
                "ForBlock",
                "IfThenBlock",
                "MethodBlock",
                "RunnableBlock",
                "VialBlock",
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
