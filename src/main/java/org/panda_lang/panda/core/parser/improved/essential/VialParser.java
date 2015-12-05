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
        ParserScheme parserScheme = new ParserScheme(new VialParser(), "*{", EssentialPriority.VIAL.getPriority());
        ElementsBucket.registerParser(parserScheme);
    }

    @Override
    public Block parse(Atom atom) {
        SourcesDivider sourcesDivider = atom.getSourcesDivider();
        String vialLine = sourcesDivider.getLine();
        String vialIndication = VialAssistant.extractIndication(vialLine);
        BlockInfo blockInfo = VialAssistant.extractVial(vialLine);
        Block current = null;

        indication:
        for (BlockScheme blockScheme : ElementsBucket.getBlocks()) {
            for (String indication : blockScheme.getIndications()) {
                if (vialIndication.equals(indication)) {
                    atom.setBlockInfo(blockInfo);
                    current = blockScheme.getParser().initialize(atom);
                    current.setParent(atom.getParent());
                    atom.setCurrent(current);
                    if (blockScheme.isConventional()) {
                        atom.getParent().addExecutable(current);
                    }
                    break indication;
                }
            }
        }

        while (sourcesDivider.hasNext() && atom.getPandaParser().isHappy()) {
            String line = sourcesDivider.next();

            //System.out.println("vial_" + current.getName() + ": " + line);

            if (VialAssistant.isPlug(line)) {
                break;
            }

            atom.update(current, current);

            Executable executable = atom.getPandaParser().parseLine(line, atom);
            if (executable instanceof Block) {
                atom.setPrevious((Block) executable);
                atom.getPrevious().setParent(current);
            } else {
                atom.getCurrent().addExecutable(executable);
            }
        }

        return atom.getCurrent();
    }

}
