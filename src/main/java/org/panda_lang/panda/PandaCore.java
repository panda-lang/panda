package org.panda_lang.panda;

import org.panda_lang.panda.core.Basis;
import org.panda_lang.panda.core.parser.ParserCenter;
import org.panda_lang.panda.core.parser.ParserLayout;
import org.panda_lang.panda.core.parser.analyzer.Analyzer;
import org.panda_lang.panda.core.parser.analyzer.AnalyzerCenter;
import org.panda_lang.panda.core.parser.essential.BlockCenter;
import org.panda_lang.panda.core.parser.essential.util.BlockLayout;

public class PandaCore {

    private final Basis basis;

    protected PandaCore() {
        this.basis = new Basis(this);

        this.basis.loadParsers();
        this.basis.loadBlocks();
        this.basis.loadObjects();
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

}
