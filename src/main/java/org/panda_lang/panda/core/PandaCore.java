package org.panda_lang.panda.core;

import org.panda_lang.panda.core.parser.ParserCenter;
import org.panda_lang.panda.core.parser.ParserLayout;
import org.panda_lang.panda.core.parser.analyzer.Analyzer;
import org.panda_lang.panda.core.parser.analyzer.AnalyzerCenter;
import org.panda_lang.panda.core.parser.essential.BlockCenter;
import org.panda_lang.panda.core.parser.essential.util.BlockLayout;
import org.panda_lang.panda.util.ClassCaller;

public class PandaCore {

    public PandaCore() {
        parsers();
        blocks();
        objects();
    }

    public void registerParser(ParserLayout parser) {
        ParserCenter.registerPatterns(parser.getPatterns());
    }

    public void registerAnalyzer(Analyzer analyzer) {
        AnalyzerCenter.registerAnalyzer(analyzer);
    }

    public void registerBlock(BlockLayout blockLayout) {
        BlockCenter.registerBlock(blockLayout);
    }

    protected void parsers() {
        ClassCaller.loadClasses("org.panda_lang.panda.core.parser.essential",
                "BlockParser",
                "ConstructorParser",
                "EqualityParser",
                "ImportParser",
                "MathParser",
                "MethodParser",
                "GroupParser",
                "FactorParser",
                "FieldParser");
    }

    protected void blocks() {
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

    protected void objects() {
        // Default
        ClassCaller.loadClasses("org.panda_lang.panda.lang",
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
        // Network
        ClassCaller.loadClasses("org.panda_lang.panda.lang.net",
                "PPacket",
                "PServerSocket",
                "PServerSocketChannel",
                "PSocket");
    }

}
