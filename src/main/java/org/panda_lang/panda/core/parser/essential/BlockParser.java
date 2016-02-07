package org.panda_lang.panda.core.parser.essential;

import org.panda_lang.panda.core.parser.*;
import org.panda_lang.panda.core.parser.essential.assistant.BlockAssistant;
import org.panda_lang.panda.core.parser.essential.util.BlockInfo;
import org.panda_lang.panda.core.parser.essential.util.BlockLayout;
import org.panda_lang.panda.core.parser.essential.util.EssentialPriority;
import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.NamedExecutable;

public class BlockParser implements Parser
{

    static
    {
        ParserLayout parserLayout = new ParserLayout(new BlockParser(), "*{", EssentialPriority.VIAL.getPriority());
        ParserCenter.registerParser(parserLayout);
    }

    @Override
    public Block parse(Atom atom)
    {
        SourcesDivider sourcesDivider = atom.getSourcesDivider();
        String vialLine = sourcesDivider.getLine();
        String vialIndication = BlockAssistant.extractIndication(vialLine);
        BlockInfo blockInfo = BlockAssistant.extractBlock(vialLine);
        Block current = null;

        indication:
        for (BlockLayout blockLayout : BlockCenter.getBlocks())
        {
            for (String indication : blockLayout.getIndications())
            {
                if (vialIndication.equals(indication))
                {
                    atom.setBlockInfo(blockInfo);
                    current = blockLayout.getInitializer().initialize(atom);
                    current.setParent(atom.getParent());
                    atom.setCurrent(current);
                    if (blockLayout.isConventional())
                    {
                        atom.getParent().addExecutable(current);
                    }
                    break indication;
                }
            }
        }

        while (sourcesDivider.hasNext() && atom.getPandaParser().isHappy())
        {
            String line = sourcesDivider.next();

            if (BlockAssistant.isPlug(line))
            {
                break;
            }

            atom.update(current, current);

            NamedExecutable executable = atom.getPandaParser().parseLine(line, atom);
            if (executable instanceof Block)
            {
                atom.setPrevious((Block) executable);
                atom.getPrevious().setParent(current);
            }
            else
            {
                atom.getCurrent().addExecutable(executable);
            }
        }

        return atom.getCurrent();
    }

}
