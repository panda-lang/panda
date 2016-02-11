package org.panda_lang.panda.core;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.PandaCore;
import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.essential.*;
import org.panda_lang.panda.core.parser.util.Injection;
import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.NamedExecutable;
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

    public void loadInjections() {
        panda.getPandaCore().registerInjection(new Injection() {
            @Override
            public void call(Atom atom, NamedExecutable namedExecutable) {
                if (!(namedExecutable instanceof Block)) {
                    atom.getPandaParser().getPandaBlock().addExecutable(namedExecutable);
                }
            }
        });
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
                "ArrayEssence",
                "BooleanEssence",
                "ByteEssence",
                "CharEssence",
                "FileEssence",
                "ListEssence",
                "MapEssence",
                "NumberEssence",
                "ObjectEssence",
                "PandaEssence",
                "RunnableEssence",
                "StackEssence",
                "StringEssence",
                "SystemEssence",
                "ThreadEssence");

        ClassCaller.loadClasses("org.panda_lang.panda.lang.net",
                "PacketEssence",
                "ServerSocketEssence",
                "ServerSocketChannelEssence",
                "SocketEssence",
                "SocketChannelEssence");

        ClassCaller.loadClasses("org.panda_lang.panda.lang.ui",
                "InterfaceEssence");
    }

    public PandaCore getPandaCore() {
        return pandaCore;
    }

}
