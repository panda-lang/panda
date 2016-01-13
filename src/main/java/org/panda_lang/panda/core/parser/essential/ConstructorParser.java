package org.panda_lang.panda.core.parser.essential;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.PandaException;
import org.panda_lang.panda.core.parser.Parser;
import org.panda_lang.panda.core.syntax.*;
import org.panda_lang.panda.core.syntax.Runtime;

import java.util.Stack;

public class ConstructorParser implements Parser {

    @Override
    public Runtime parse(Atom atom) {
        String source = atom.getSourceCode();
        source = source.substring(4);

        StringBuilder node = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        String clazz = null;
        boolean s = false,
                p = false;

        char[] chars = source.toCharArray();
        for (int i = 0; i < source.length(); i++) {
            char c = chars[i];

            if (c == '"') {
                s = !s;
            } else if (s) {
                node.append(c);
                continue;
            } else if (p) {
                if (c == '(') {
                    stack.push(c);
                } else if (c == ')') {
                    stack.pop();
                    if (stack.size() == 0) {
                        break;
                    }
                }
                node.append(c);
                continue;
            } else if (node.length() == 0 && Character.isWhitespace(c)) {
                continue;
            }

            switch (c) {
                case '(':
                    clazz = node.toString();
                    node.setLength(0);
                    stack.push(c);
                    p = true;
                    break;
                case ';':
                    break;
                default:
                    node.append(c);
                    break;
            }
        }

        String params = node.toString();
        node.setLength(0);

        atom.setSourceCode(params);
        FactorParser parser = new FactorParser();
        final Factor[] factors = parser.parseLocal(atom);

        final Vial vial = atom.getDependencies().getVial(clazz);
        if (vial == null) {
            PandaException exception = new PandaException("ConstructorParser: Vial '" + clazz + "' not found", atom.getSourcesDivider());
            atom.getPandaParser().throwException(exception);
            return null;
        }

        return new Runtime(null, new Executable() {
            @Override
            public Essence run(Particle particle) {
                return vial.initializeInstance(particle);
            }
        }, factors);
    }

}
