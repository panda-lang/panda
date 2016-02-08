package org.panda_lang.panda.core.parser.essential;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.Parser;
import org.panda_lang.panda.core.parser.ParserLayout;
import org.panda_lang.panda.core.parser.PatternExtractor;
import org.panda_lang.panda.core.parser.essential.util.EssentialPriority;
import org.panda_lang.panda.core.syntax.Group;
import org.panda_lang.panda.core.syntax.Import;
import org.panda_lang.panda.core.syntax.NamedExecutable;

public class GroupParser implements Parser {

    @Override
    public NamedExecutable parse(Atom atom) {
        final String source = atom.getSourcesDivider().getLine();
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
        atom.getPandaParser().getPandaBlock().setGroup(group);
        atom.getPandaParser().getDependencies().importElement(anImport);
        return group;
    }

    public static void initialize(Panda panda) {
        GroupParser groupParser = new GroupParser();
        ParserLayout parserLayout = new ParserLayout(groupParser);
        parserLayout.pattern("group *;", EssentialPriority.GROUP.getPriority(), EssentialPriority.GROUP.getPriority() * 10, PatternExtractor.FULL);
        panda.registerParser(parserLayout);
    }

}
