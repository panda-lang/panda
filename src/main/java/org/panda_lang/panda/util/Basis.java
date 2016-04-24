package org.panda_lang.panda.util;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.PandaCore;
import org.panda_lang.panda.core.parser.ParserInfo;
import org.panda_lang.panda.core.parser.essential.*;
import org.panda_lang.panda.core.parser.util.Injection;
import org.panda_lang.panda.core.statement.Block;
import org.panda_lang.panda.core.statement.Executable;
import org.panda_lang.panda.core.statement.block.*;

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
            public void call(ParserInfo parserInfo, Executable namedExecutable) {
                if (!(namedExecutable instanceof Block)) {
                    parserInfo.getPandaParser().getPandaBlock().addExecutable(namedExecutable);
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
