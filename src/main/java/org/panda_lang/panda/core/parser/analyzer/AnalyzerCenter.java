package org.panda_lang.panda.core.parser.analyzer;

import java.util.ArrayList;
import java.util.Collection;

public class AnalyzerCenter {

    private final Collection<Analyzer> analyzers = new ArrayList<>();

    public void registerAnalyzer(Analyzer analyzer) {
        analyzers.add(analyzer);
    }

    public Collection<Analyzer> getAnalyzers() {
        return analyzers;
    }

}
