package org.panda_lang.panda.core.parser;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.PandaScript;
import org.panda_lang.panda.core.statement.Executable;
import org.panda_lang.panda.core.statement.block.PandaBlock;
import org.panda_lang.panda.core.parser.analyzer.SemanticAnalyzer;
import org.panda_lang.panda.core.parser.util.Dependencies;
import org.panda_lang.panda.core.parser.util.Injection;
import org.panda_lang.panda.core.parser.util.match.parser.PatternExtractor;

import java.util.ArrayList;
import java.util.Collection;

public class PandaParser {

    private final Atom atom;
    private final Panda panda;
    private final PandaScript pandaScript;
    private final Dependencies dependencies;

    private final SourcesDivider divider;
    private final PatternExtractor extractor;
    private final SemanticAnalyzer semanticAnalyzer;
    private final Collection<Injection> injections;
    private final Collection<Runnable> postProcesses;

    private PandaBlock pandaBlock;
    private boolean exception;

    public PandaParser(Panda panda, PandaScript pandaScript, String source) {
        this.panda = panda;
        this.pandaScript = pandaScript;
        this.dependencies = new Dependencies(pandaScript);
        this.pandaBlock = new PandaBlock(pandaScript);
        this.divider = new SourcesDivider(source);
        this.extractor = new PatternExtractor();
        this.semanticAnalyzer = new SemanticAnalyzer();
        this.injections = panda.getPandaCore().getInjectionCenter().getInjections();
        this.postProcesses = new ArrayList<>();
        this.atom = new Atom(panda, pandaScript, this, dependencies, divider, extractor, null, null, pandaBlock, pandaBlock, pandaBlock);
    }

    public PandaParser(Panda panda, String source) {
        this.panda = panda;
        this.pandaScript = new PandaScript(panda);
        this.dependencies = new Dependencies(pandaScript);
        this.pandaBlock = new PandaBlock(pandaScript);
        this.divider = new SourcesDivider(source);
        this.extractor = new PatternExtractor();
        this.semanticAnalyzer = new SemanticAnalyzer();
        this.injections = panda.getPandaCore().getInjectionCenter().getInjections();
        this.postProcesses = new ArrayList<>();
        this.atom = new Atom(panda, pandaScript, this, dependencies, divider, extractor, null, null, pandaBlock, pandaBlock, pandaBlock);
    }

    public PandaScript parse() {
        while (divider.hasNext() && isHappy()) {
            String line = divider.next();
            if (line == null || line.isEmpty()) {
                break;
            }

            atom.update(pandaBlock, pandaBlock);
            Executable executable = parseLine(line, atom);

            for (Injection injection : injections) {
                injection.call(atom, executable);
            }
        }

        for (Runnable process : postProcesses) {
            process.run();
        }

        pandaBlock.initializeGlobalVariables();
        pandaScript.addPandaBlock(pandaBlock);

        semanticAnalyzer.analyze(pandaScript);
        return pandaScript;
    }

    public Executable parseLine(String line, Atom atom) {
        Parser parser = atom.getParserCenter().getParser(atom, line);

        // {initializer.not.found}
        if (parser == null) {
            return throwException(new PandaException("ParserNotFoundException", divider));
        }

        return parser.parse(atom);
    }

    public <T> T throwException(PandaException pandaException) {
        pandaException.print();
        exception = true;
        return null;
    }

    public void addInjection(Injection injection) {
        injections.add(injection);
    }

    public void addPostProcess(Runnable process) {
        postProcesses.add(process);
    }

    public boolean isHappy() {
        return !exception;
    }

    public Collection<Injection> getInjections() {
        return injections;
    }

    public Collection<Runnable> getPostProcesses() {
        return postProcesses;
    }

    public SemanticAnalyzer getSemanticAnalyzer() {
        return semanticAnalyzer;
    }

    public SourcesDivider getSourceDivider() {
        return divider;
    }

    public PatternExtractor getPatternExtractor() {
        return extractor;
    }

    public Dependencies getDependencies() {
        return dependencies;
    }

    public PandaBlock getPandaBlock() {
        return pandaBlock;
    }

    public void setPandaBlock(PandaBlock pandaBlock) {
        this.pandaBlock = pandaBlock;
    }

    public PandaScript getPandaScript() {
        return pandaScript;
    }

    public Panda getPanda() {
        return panda;
    }

    public Atom getAtom() {
        return atom;
    }

}
