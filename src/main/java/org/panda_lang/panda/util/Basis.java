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
                "ArrayInst",
                "BooleanInst",
                "ByteInst",
                "CharInst",
                "FileInst",
                "ListInst",
                "MapInst",
                "NumberInst",
                "ObjectInst",
                "PandaInst",
                "RunnableInst",
                "StackInst",
                "StringInst",
                "SystemInst",
                "ThreadInst");

        ClassCaller.loadClasses("org.panda_lang.panda.lang.net",
                "PacketInst",
                "ServerSocketInst",
                "ServerSocketChannelInst",
                "SocketInst",
                "SocketChannelInst");

        ClassCaller.loadClasses("org.panda_lang.panda.lang.ui",
                "InterfaceInst");
    }

    public PandaCore getPandaCore() {
        return pandaCore;
    }

}
