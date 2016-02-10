package org.panda_lang.panda.core.parser;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.PandaScript;
import org.panda_lang.panda.core.parser.analyzer.SemanticAnalyzer;
import org.panda_lang.panda.core.parser.util.Dependencies;
import org.panda_lang.panda.core.parser.util.Injection;
import org.panda_lang.panda.core.syntax.NamedExecutable;
import org.panda_lang.panda.core.syntax.block.PandaBlock;

import java.util.ArrayList;
import java.util.Collection;

public class PandaParser {

    private final Atom atom;
    private final Panda panda;
    private final PandaScript pandaScript;
    private final Dependencies dependencies;
    private final PandaBlock pandaBlock;

    private final SourcesDivider divider;
    private final PatternExtractor extractor;
    private final SemanticAnalyzer semanticAnalyzer;
    private final Collection<Injection> injections;
    private final Collection<Runnable> postProcesses;
    private boolean exception;

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
            NamedExecutable namedExecutable = parseLine(line, atom);

            for (Injection injection : injections) {
                injection.call(atom, namedExecutable);
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

    public NamedExecutable parseLine(String line, Atom atom) {
        Parser parser = atom.getParserCenter().getParser(atom, line);

        // {initializer.not.found}
        if (parser == null) {
            return throwException(new PandaException("ParserNotFoundException", divider));
        }

        return parser.parse(atom);
    }

    public NamedExecutable throwException(PandaException pandaException) {
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

    public SourcesDivider getDivider() {
        return divider;
    }

    public PatternExtractor getExtractor() {
        return extractor;
    }

    public Dependencies getDependencies() {
        return dependencies;
    }

    public PandaBlock getPandaBlock() {
        return pandaBlock;
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
