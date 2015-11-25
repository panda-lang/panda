package org.panda_lang.panda.core.parser.improved.essential;

import org.panda_lang.panda.core.ElementsBucket;
import org.panda_lang.panda.core.parser.improved.Atom;
import org.panda_lang.panda.core.parser.improved.Parser;
import org.panda_lang.panda.core.parser.improved.SourcesDivider;
import org.panda_lang.panda.core.parser.improved.essential.assistant.VialAssistant;
import org.panda_lang.panda.core.parser.improved.essential.util.BlockInfo;
import org.panda_lang.panda.core.scheme.BlockScheme;
import org.panda_lang.panda.core.scheme.ParserScheme;
import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.Executable;

public class VialParser implements Parser {

    static {
        ParserScheme scheme = new ParserScheme(new VialParser(), "{");
        ElementsBucket.registerParser(scheme);
    }

    private Atom atom;

    @Override
    public Block parse(Atom atom) {
        this.atom = atom;

        SourcesDivider sourcesDivider = atom.getSourcesDivider();
        String vialLine = sourcesDivider.getLine();
        String vialIndication = VialAssistant.extractIndication(vialLine);
        BlockInfo blockInfo = VialAssistant.extractVial(vialLine);

        indication:
        for (BlockScheme blockScheme : ElementsBucket.getBlocks()) {
            for (String indication : blockScheme.getIndications()) {
                if (vialIndication.equals(indication)) {
                    atom.setBlockInfo(blockInfo);
                    atom.setCurrent(blockScheme.getParser().parse(atom));
                    break indication;
                }
            }
        }

        while (sourcesDivider.hasNext()) {
            String line = sourcesDivider.next();
            if (VialAssistant.isPlug(line)) {
                break;
            }
            Executable executable = atom.getPandaParser().parseLine(line, atom);
            if (executable instanceof Block) {
                atom.setPrevious((Block) executable);
            } else {
                atom.getCurrent().addExecutable(executable);
            }
        }

        return atom.getCurrent();
    }

    @Override
    public Block getParent() {
        return atom.getParent();
    }

}
