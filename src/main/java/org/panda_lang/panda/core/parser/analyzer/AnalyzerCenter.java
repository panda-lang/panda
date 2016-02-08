package org.panda_lang.panda.core.parser.analyzer;

import java.util.ArrayList;
import java.util.Collection;

public class AnalyzerCenter {

    private static final Collection<Analyzer> analyzers = new ArrayList<>();

    public static void registerAnalyzer(Analyzer analyzer) {
        analyzers.add(analyzer);
    }

    public static Collection<Analyzer> getAnalyzers() {
        return analyzers;
    }

}
