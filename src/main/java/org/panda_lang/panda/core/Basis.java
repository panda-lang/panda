package org.panda_lang.panda.core;

import org.panda_lang.panda.PandaCore;
import org.panda_lang.panda.util.ClassCaller;

public class Basis
{

    private final PandaCore pandaCore;

    public Basis(PandaCore pandaCore)
    {
        this.pandaCore = pandaCore;
    }

    public void loadParsers()
    {
        ClassCaller.loadClasses("org.panda_lang.panda.core.parser.essential",
                "BlockParser",
                "ConstructorParser",
                "ContinueParser",
                "EqualityParser",
                "FactorParser",
                "FieldParser",
                "GroupParser",
                "ImportParser",
                "MathParser",
                "MethodParser",
                "ReturnParser");
    }

    public void loadBlocks()
    {
        ClassCaller.loadClasses("org.panda_lang.panda.core.syntax.block",
                "ElseThenBlock",
                "ForBlock",
                "IfThenBlock",
                "MethodBlock",
                "RunnableBlock",
                "ThreadBlock",
                "VialBlock",
                "WhileBlock");
    }

    public void loadObjects()
    {
        // Default
        ClassCaller.loadClasses("org.panda_lang.panda.lang",
                "PArray",
                "PBoolean",
                "PByte",
                "PChar",
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
        // Network
        ClassCaller.loadClasses("org.panda_lang.panda.lang.net",
                "PPacket",
                "PServerSocket",
                "PServerSocketChannel",
                "PSocket",
                "PSocketChannel");
        // UI
        ClassCaller.loadClasses("org.panda_lang.panda.lang.ui",
                "PInterface");
    }

    public PandaCore getPandaCore()
    {
        return pandaCore;
    }

}
