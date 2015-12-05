package org.pandalang.panda.core.parser.improved;

import org.pandalang.panda.PandaScript;
import org.pandalang.panda.core.syntax.Block;
import org.pandalang.panda.core.syntax.Executable;
import org.pandalang.panda.core.syntax.block.PandaBlock;

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
            Executable executable = parseLine(line, atom);
            if (!(executable instanceof Block)) {
                pandaBlock.addExecutable(executable);
            }

        }
        pandaBlock.initializeGlobalVariables();
        pandaScript.addPandaBlock(pandaBlock);
        return pandaScript;
    }

    public Executable parseLine(String line, Atom atom) {
        String pattern = extractor.extract(line, PatternExtractor.DEFAULT);
        Parser parser = ParserCenter.getParser(pattern);

        // {parser.not.found}
        if (parser == null) {
            throwException(new PandaException("ParserNotFoundException", line, divider.getRealLine() + 1, divider.getCaretPosition()));
            System.out.println(pattern);
            return null;
        }

        return parser.parse(atom);
    }

    public Executable throwException(PandaException pandaException) {
        pandaException.print();
        exception = true;
        return null;
    }

    public boolean isHappy() {
        return !exception;
    }

    public PandaBlock getPandaBlock() {
        return pandaBlock;
    }

    public PandaScript getPandaScript() {
        return pandaScript;
    }

}
