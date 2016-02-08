package org.panda_lang.panda.core;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.PandaCore;
import org.panda_lang.panda.core.parser.essential.*;
import org.panda_lang.panda.core.syntax.block.*;
import org.panda_lang.panda.util.ClassCaller;

public class Basis {

    private final Panda panda;
    private final PandaCore pandaCore;

    public Basis(PandaCore pandaCore) {
        this.panda = pandaCore.getPanda();
        this.pandaCore = pandaCore;
    }

    public void loadParsers() {
        BlockParser.initialize(panda);
        ContinueParser.initialize(panda);
        FieldParser.initialize(panda);
        GroupParser.initialize(panda);
        ImportParser.initialize(panda);
        MethodParser.initialize(panda);
        ReturnParser.initialize(panda);
    }

    public void loadBlocks() {
        ElseThenBlock.initialize(panda);
        ForBlock.initialize(panda);
        IfThenBlock.initialize(panda);
        MethodBlock.initialize(panda);
        RunnableBlock.initialize(panda);
        ThreadBlock.initialize(panda);
        VialBlock.initialize(panda);
        WhileBlock.initialize(panda);
    }

    public void loadObjects() {
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

        ClassCaller.loadClasses("org.panda_lang.panda.lang.net",
                "PPacket",
                "PServerSocket",
                "PServerSocketChannel",
                "PSocket",
                "PSocketChannel");

        ClassCaller.loadClasses("org.panda_lang.panda.lang.ui",
                "PInterface");
    }

    public PandaCore getPandaCore() {
        return pandaCore;
    }

}
