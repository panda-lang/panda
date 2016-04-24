package org.panda_lang.panda.core.parser.essential;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.core.parser.ParserInfo;
import org.panda_lang.panda.core.parser.Parser;
import org.panda_lang.panda.core.parser.ParserLayout;
import org.panda_lang.panda.core.parser.SourcesDivider;
import org.panda_lang.panda.core.parser.essential.assistant.BlockAssistant;
import org.panda_lang.panda.core.parser.essential.util.BlockInfo;
import org.panda_lang.panda.core.parser.essential.util.BlockLayout;
import org.panda_lang.panda.core.parser.essential.util.EssentialPriority;
import org.panda_lang.panda.core.statement.Block;
import org.panda_lang.panda.core.statement.Executable;

public class BlockParser implements Parser {

    public static void initialize(Panda panda) {
        BlockParser blockParser = new BlockParser();
        ParserLayout parserLayout = new ParserLayout(blockParser, "*{", EssentialPriority.VIAL.getPriority());
        panda.getPandaCore().registerParser(parserLayout);
    }

    @Override
    public Block parse(ParserInfo parserInfo) {
        SourcesDivider sourcesDivider = parserInfo.getSourcesDivider();
        String vialLine = sourcesDivider.getLine();
        String vialIndication = BlockAssistant.extractIndication(vialLine);
        BlockInfo blockInfo = BlockAssistant.extractBlock(vialLine);
        Block current = parserInfo.getParent();

        indication:
        for (BlockLayout blockLayout : parserInfo.getBlockCenter().getBlocks()) {
            for (String indication : blockLayout.getIndications()) {
                if (vialIndication.equals(indication)) {
                    parserInfo.setBlockInfo(blockInfo);
                    current = blockLayout.getInitializer().initialize(parserInfo);
                    current.setParent(parserInfo.getParent());
                    if (blockLayout.isConventional()) {
                        parserInfo.getParent().addExecutable(current);
                    }
                    break indication;
                }
            }
        }

        while (sourcesDivider.hasNext() && parserInfo.getPandaParser().isHappy()) {
            String line = sourcesDivider.next();

            if (BlockAssistant.isPlug(line)) {
                break;
            }

            parserInfo.update(current, current);

            Executable executable = parserInfo.getPandaParser().parseLine(line, parserInfo);
            if (executable instanceof Block) {
                parserInfo.setPrevious((Block) executable);
                parserInfo.getPrevious().setParent(current);
                parserInfo.setCurrent(current);
            }
            else {
                current.addExecutable(executable);
            }
        }

        return current;
    }

}
