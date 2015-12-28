package org.panda_lang.panda.core.parser.essential;

import org.panda_lang.panda.core.parser.*;
import org.panda_lang.panda.core.parser.essential.util.EssentialPriority;
import org.panda_lang.panda.core.syntax.Group;
import org.panda_lang.panda.core.syntax.NamedExecutable;

public class GroupParser implements Parser {

    static {
        GroupParser groupParser = new GroupParser();
        ParserLayout parserLayout = new ParserLayout(groupParser);
        parserLayout.pattern("group *;", 1.0D, EssentialPriority.GROUP.getPriority() * 10, PatternExtractor.FULL);
        ParserCenter.registerParser(parserLayout);
    }

    @Override
    public NamedExecutable parse(Atom atom) {
        final String source = atom.getSourceCode();
        final StringBuilder ns = new StringBuilder();
        boolean nsFlag = false;

        for (char c : source.toCharArray()) {
            if (c == '\'') {
                if (nsFlag) {
                    break;
                } else {
                    nsFlag = true;
                }
            } else if (nsFlag) {
                ns.append(c);
            }
        }

        Group group = new Group(ns.toString());
        return group;
    }

}
