package org.panda_lang.panda.core.parser;

import org.panda_lang.panda.PandaScript;
import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.NamedExecutable;
import org.panda_lang.panda.core.syntax.block.PandaBlock;

public class PandaParser {

    private final Atom atom;
    private final PandaScript pandaScript;
    private final SourcesDivider divider;
    private final PatternExtractor extractor;
    private final PandaBlock pandaBlock;
    private boolean exception;

    public PandaParser(String source) {
        this.pandaScript = new PandaScript();
        this.divider = new SourcesDivider(source);
        this.extractor = new PatternExtractor();
        this.pandaBlock = new PandaBlock();
        this.atom = new Atom(pandaScript, this, divider, extractor, null, null, pandaBlock, pandaBlock, pandaBlock);
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
        String pattern = extractor.extract(line, PatternExtractor.DEFAULT);
        Parser parser = ParserCenter.getParser(pattern);

        // {initializer.not.found}
        if (parser == null) {
            return throwException(new PandaException("ParserNotFoundException", line, divider.getRealLine() + 1, divider.getCaretPosition()));
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
