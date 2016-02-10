package org.panda_lang.panda;

import org.panda_lang.panda.core.parser.ParserCenter;
import org.panda_lang.panda.core.parser.ParserLayout;
import org.panda_lang.panda.core.parser.analyzer.Analyzer;
import org.panda_lang.panda.core.parser.analyzer.AnalyzerCenter;
import org.panda_lang.panda.core.parser.essential.BlockCenter;
import org.panda_lang.panda.core.parser.essential.util.BlockLayout;
import org.panda_lang.panda.core.parser.util.Injection;
import org.panda_lang.panda.core.parser.util.InjectionCenter;

public class PandaCore {

    private final Panda panda;
    private final ParserCenter parserCenter;
    private final InjectionCenter injectionCenter;
    private final BlockCenter blockCenter;
    private final AnalyzerCenter analyzerCenter;

    protected PandaCore(Panda panda) {
        this.panda = panda;
        this.parserCenter = new ParserCenter();
        this.injectionCenter = new InjectionCenter();
        this.blockCenter = new BlockCenter();
        this.analyzerCenter = new AnalyzerCenter();
    }

    public void registerParser(ParserLayout parser) {
        parserCenter.registerPatterns(parser.getPatterns());
    }

    public void registerInjection(Injection injection) {
        injectionCenter.registerInjection(injection);
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

    public InjectionCenter getInjectionCenter() {
        return injectionCenter;
    }

    public ParserCenter getParserCenter() {
        return parserCenter;
    }

    public Panda getPanda() {
        return panda;
    }

}
