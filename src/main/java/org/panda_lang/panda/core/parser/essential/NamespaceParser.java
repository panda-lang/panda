package org.panda_lang.panda.core.parser.essential;

import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.Parser;
import org.panda_lang.panda.core.parser.ParserCenter;
import org.panda_lang.panda.core.parser.ParserLayout;
import org.panda_lang.panda.core.syntax.NamedExecutable;
import org.panda_lang.panda.core.syntax.Namespace;

public class NamespaceParser implements Parser {

    static {
        NamespaceParser namespaceParser = new NamespaceParser();
        ParserLayout parserLayout = new ParserLayout(namespaceParser);
        parserLayout.pattern(">*;", 1.0D, 40);
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

        Namespace namespace = new Namespace(ns.toString());
        return namespace;
    }

}
