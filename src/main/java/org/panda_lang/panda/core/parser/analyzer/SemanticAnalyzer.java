package org.panda_lang.panda.core.parser.analyzer;

import org.panda_lang.panda.PandaScript;

public class SemanticAnalyzer implements Analyzer {

    @Override
    public void analyze(PandaScript pandaScript) {
        for (Analyzer analyzer : pandaScript.getPanda().getPandaCore().getAnalyzerCenter().getAnalyzers()) {
            analyzer.analyze(pandaScript);
        }
    }

}
