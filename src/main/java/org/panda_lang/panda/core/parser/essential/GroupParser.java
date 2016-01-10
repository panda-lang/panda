package org.panda_lang.panda.core.parser.essential;

import org.panda_lang.panda.core.parser.*;
import org.panda_lang.panda.core.parser.essential.util.EssentialPriority;
import org.panda_lang.panda.core.syntax.Group;
import org.panda_lang.panda.core.syntax.NamedExecutable;

public class GroupParser implements Parser {

    static {
        GroupParser groupParser = new GroupParser();
        ParserLayout parserLayout = new ParserLayout(groupParser);
        parserLayout.pattern("group *;", EssentialPriority.GROUP.getPriority(), EssentialPriority.GROUP.getPriority() * 10, PatternExtractor.FULL);
        ParserCenter.registerParser(parserLayout);
    }

    @Override
    public NamedExecutable parse(Atom atom) {
        final String source = atom.getSourcesDivider().getLine();
        final StringBuilder groupBuilder = new StringBuilder();
        boolean nsFlag = false;

        for (char c : source.toCharArray()) {
            if (Character.isWhitespace(c)) {
                nsFlag = true;
            } else if (c == ';') {
                break;
            } else if (nsFlag) {
                groupBuilder.append(c);
            }
        }

        String groupName = groupBuilder.toString();
        Group group = GroupCenter.getGroup(groupName);
        atom.getPandaParser().getPandaBlock().setGroup(group);
        return group;
    }

}
