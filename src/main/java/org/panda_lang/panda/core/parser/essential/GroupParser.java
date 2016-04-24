package org.panda_lang.panda.core.parser.essential;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.core.parser.ParserInfo;
import org.panda_lang.panda.core.parser.Parser;
import org.panda_lang.panda.core.parser.ParserLayout;
import org.panda_lang.panda.core.parser.essential.util.EssentialPriority;
import org.panda_lang.panda.core.parser.util.match.parser.PatternExtractor;
import org.panda_lang.panda.core.statement.Group;
import org.panda_lang.panda.core.statement.Import;

public class GroupParser implements Parser {

    public static void initialize(Panda panda) {
        GroupParser groupParser = new GroupParser();
        ParserLayout parserLayout = new ParserLayout(groupParser);
        parserLayout.pattern("group *;", EssentialPriority.GROUP.getPriority(), EssentialPriority.GROUP.getPriority() * 10, PatternExtractor.FULL);
        panda.getPandaCore().registerParser(parserLayout);
    }

    @Override
    public Group parse(ParserInfo parserInfo) {
        final String source = parserInfo.getSourcesDivider().getLine();
        final StringBuilder groupBuilder = new StringBuilder();
        boolean nsFlag = false;

        for (char c : source.toCharArray()) {
            if (Character.isWhitespace(c)) {
                nsFlag = true;
            }
            else if (c == ';') {
                break;
            }
            else if (nsFlag) {
                groupBuilder.append(c);
            }
        }

        String groupName = groupBuilder.toString();
        Group group = GroupCenter.getGroup(groupName);
        Import anImport = new Import(group);
        parserInfo.getPandaParser().getPandaBlock().setGroup(group);
        parserInfo.getPandaParser().getDependencies().importElement(anImport);
        return group;
    }

}
