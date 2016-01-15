package org.panda_lang.panda.core.parser;

import org.panda_lang.panda.PandaScript;
import org.panda_lang.panda.core.parser.analyzer.SemanticAnalyzer;
import org.panda_lang.panda.core.parser.essential.util.Dependencies;
import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.NamedExecutable;
import org.panda_lang.panda.core.syntax.block.PandaBlock;

public class PandaParser {

    private final Atom atom;
    private final PandaScript pandaScript;
    private final SourcesDivider divider;
    private final PatternExtractor extractor;
    private final Dependencies dependencies;
    private final PandaBlock pandaBlock;
    private final SemanticAnalyzer semanticAnalyzer;
    private boolean exception;

    public PandaParser(String source) {
        this.pandaScript = new PandaScript();
        this.divider = new SourcesDivider(source);
        this.extractor = new PatternExtractor();
        this.semanticAnalyzer = new SemanticAnalyzer();
        this.dependencies = new Dependencies();
        this.pandaBlock = new PandaBlock();
        this.atom = new Atom(pandaScript, this, dependencies, divider, extractor, null, null, pandaBlock, pandaBlock, pandaBlock);
    }

    public PandaScript parse() {
        while (divider.hasNext() && isHappy()) {
            String line = divider.next();
            if (line == null || line.isEmpty()) {
                break;
            }

            atom.update(pandaBlock, pandaBlock);
            NamedExecutable executable = parseLine(line, atom);
            if (!(executable instanceof Block)) {
                pandaBlock.addExecutable(executable);
            }

        }
        pandaBlock.initializeGlobalVariables();
        pandaScript.addPandaBlock(pandaBlock);
        return pandaScript;
    }

    public NamedExecutable parseLine(String line, Atom atom) {
        Parser parser = ParserCenter.getParser(atom, line);

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

    public Atom getAtom() {
        return atom;
    }

}
