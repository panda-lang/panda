package org.panda_lang.panda.core.parser.essential;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.Parser;
import org.panda_lang.panda.core.parser.ParserLayout;
import org.panda_lang.panda.core.parser.SourcesDivider;
import org.panda_lang.panda.core.parser.essential.assistant.BlockAssistant;
import org.panda_lang.panda.core.parser.essential.util.BlockInfo;
import org.panda_lang.panda.core.parser.essential.util.BlockLayout;
import org.panda_lang.panda.core.parser.essential.util.EssentialPriority;
import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.NamedExecutable;

public class BlockParser implements Parser {

    @Override
    public Block parse(Atom atom) {
        SourcesDivider sourcesDivider = atom.getSourcesDivider();
        String vialLine = sourcesDivider.getLine();
        String vialIndication = BlockAssistant.extractIndication(vialLine);
        BlockInfo blockInfo = BlockAssistant.extractBlock(vialLine);
        Block current = atom.getParent();

        indication:
        for (BlockLayout blockLayout : atom.getBlockCenter().getBlocks()) {
            for (String indication : blockLayout.getIndications()) {
                if (vialIndication.equals(indication)) {
                    atom.setBlockInfo(blockInfo);
                    current = blockLayout.getInitializer().initialize(atom);
                    current.setParent(atom.getParent());
                    if (blockLayout.isConventional()) {
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
                atom.setCurrent(current);
            }
            else {
                current.addExecutable(executable);
            }
        }

        return current;
    }

    public static void initialize(Panda panda) {
        BlockParser blockParser = new BlockParser();
        ParserLayout parserLayout = new ParserLayout(blockParser, "*{", EssentialPriority.VIAL.getPriority());
        panda.getPandaCore().registerParser(parserLayout);
    }

}
