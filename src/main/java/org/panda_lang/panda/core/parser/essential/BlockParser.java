package org.panda_lang.panda.core.parser.essential;

import org.panda_lang.panda.core.ElementsBucket;
import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.Parser;
import org.panda_lang.panda.core.parser.SourcesDivider;
import org.panda_lang.panda.core.parser.essential.assistant.BlockAssistant;
import org.panda_lang.panda.core.parser.essential.util.BlockInfo;
import org.panda_lang.panda.core.scheme.BlockScheme;
import org.panda_lang.panda.core.scheme.ParserScheme;
import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.NamedExecutable;

public class BlockParser implements Parser {

    static {
        ParserScheme parserScheme = new ParserScheme(new BlockParser(), "*{", EssentialPriority.VIAL.getPriority());
        ElementsBucket.registerParser(parserScheme);
    }

    @Override
    public Block parse(Atom atom) {
        SourcesDivider sourcesDivider = atom.getSourcesDivider();
        String vialLine = sourcesDivider.getLine();
        String vialIndication = BlockAssistant.extractIndication(vialLine);
        BlockInfo blockInfo = BlockAssistant.extractVial(vialLine);
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

            if (BlockAssistant.isPlug(line)) {
                break;
            }

            atom.update(current, current);

            NamedExecutable executable = atom.getPandaParser().parseLine(line, atom);
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
