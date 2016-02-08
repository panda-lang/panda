package org.panda_lang.panda;

import org.panda_lang.panda.core.parser.ParserCenter;
import org.panda_lang.panda.core.parser.ParserLayout;
import org.panda_lang.panda.core.parser.analyzer.Analyzer;
import org.panda_lang.panda.core.parser.analyzer.AnalyzerCenter;
import org.panda_lang.panda.core.parser.essential.BlockCenter;
import org.panda_lang.panda.core.parser.essential.util.BlockLayout;

public class PandaCore {

    private final Panda panda;
    private final ParserCenter parserCenter;
    private final BlockCenter blockCenter;
    private final AnalyzerCenter analyzerCenter;

    protected PandaCore(Panda panda) {
        this.panda = panda;
        this.parserCenter = new ParserCenter();
        this.blockCenter = new BlockCenter();
        this.analyzerCenter = new AnalyzerCenter();
    }

    public void registerParser(ParserLayout parser) {
        parserCenter.registerPatterns(parser.getPatterns());
    }

    public void registerAnalyzer(Analyzer analyzer) {
        analyzerCenter.registerAnalyzer(analyzer);
    }

    public void registerBlock(BlockLayout blockLayout) {
        blockCenter.registerBlock(blockLayout);
    }

    public BlockCenter getBlockCenter() {
        return blockCenter;
    }

    public AnalyzerCenter getAnalyzerCenter() {
        return analyzerCenter;
    }

    public ParserCenter getParserCenter() {
        return parserCenter;
    }

    public Panda getPanda() {
        return panda;
    }

}
