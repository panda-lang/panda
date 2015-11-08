package org.panda_lang.panda.core;

public class Core {

    private static Core core;
    
    private Core(){ }
    
    private void blocks(){
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
    
    private void objects(){
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
    
    public static void registerDefault(){
        if(core != null) return;
        core = new Core();
        core.blocks();
        core.objects();
    }

}
