package org.panda_lang.panda.core.parser.improved.essential;

import org.panda_lang.panda.core.ElementsBucket;
import org.panda_lang.panda.core.parser.improved.Parser;
import org.panda_lang.panda.core.parser.improved.PatternExtractor;
import org.panda_lang.panda.core.parser.improved.SourcesDivider;
import org.panda_lang.panda.core.parser.improved.PandaParser;
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

    private Block parent;
    private Block current;
    private Block previous;

    @Override
    public Block parse(PandaParser pandaParser, SourcesDivider sourcesDivider, PatternExtractor extractor, Block parent, Block previous) {
        this.parent = parent;
        this.previous = previous;

        String vialLine = sourcesDivider.getLine();
        String vialIndication = VialAssistant.extractIndication(vialLine);
        BlockInfo blockInfo = VialAssistant.extractVial(vialLine);

        indication:
        for(BlockScheme blockScheme : ElementsBucket.getBlocks()) {
            for(String indication : blockScheme.getIndications()) {
                if(vialIndication.equals(indication)) {
                    current = blockScheme.getParser().parse(blockInfo, parent, current, previous);
                    break indication;
                }
            }
        }

        while(sourcesDivider.hasNext()) {
            String line = sourcesDivider.next();
            if(VialAssistant.isPlug(line)) {
                break;
            }
            Executable executable = pandaParser.parseLine(line, sourcesDivider, extractor, current, previous);
            if(executable instanceof Block) {
                previous = (Block) executable;
            } else {
                current.addExecutable(executable);
            }
        }

        return current;
    }

    @Override
    public Block getParent() {
        return parent;
    }

}
