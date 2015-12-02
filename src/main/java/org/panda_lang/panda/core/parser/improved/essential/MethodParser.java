package org.panda_lang.panda.core.parser.improved.essential;

import org.panda_lang.panda.core.ElementsBucket;
import org.panda_lang.panda.core.parser.improved.Atom;
import org.panda_lang.panda.core.parser.improved.Parser;
import org.panda_lang.panda.core.parser.improved.essential.assistant.MethodAssistant;
import org.panda_lang.panda.core.parser.improved.essential.util.MethodInfo;
import org.panda_lang.panda.core.scheme.MethodScheme;
import org.panda_lang.panda.core.scheme.ObjectScheme;
import org.panda_lang.panda.core.scheme.ParserScheme;
import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.Method;
import org.panda_lang.panda.core.syntax.Parameter;

public class MethodParser implements Parser {

    static {
        ParserScheme parserScheme = new ParserScheme(new MethodParser(), "*(*);", EssentialPriority.METHOD.getPriority());
        ElementsBucket.registerParser(parserScheme);
    }

    @Override
    public Method parse(Atom atom) {
        Block parent = atom.getParent();
        String source = atom.getSourcesDivider().getLine();
        MethodInfo mi = MethodAssistant.getMethodIndication(atom, source);

        if (mi == null) {
            System.out.println("[MethodParser] Indication failed");
            return null;
        }

        if (mi.isExternal()) {
            if (mi.isStatic()) {
                for (ObjectScheme os : ElementsBucket.getObjects()) {
                    if (!os.getName().equals(mi.getPseudoclass())) {
                        continue;
                    }
                    for (MethodScheme ms : os.getMethods()) {
                        if (!ms.getName().equals(mi.getMethod())) {
                            continue;
                        }
                        return new Method(null, parent, mi.getMethod(), ms.getExecutable(), mi.getParameters());
                    }
                }
            } else {
                Parameter instance = mi.getInstance();
                String type = instance.getDataType();
                if (type != null) {
                    for (ObjectScheme os : ElementsBucket.getObjects()) {
                        if (!type.equals(os.getName())) {
                            continue;
                        }
                        for (MethodScheme ms : os.getMethods()) {
                            if (!ms.getName().equals(mi.getMethod())) {
                                continue;
                            }
                            return new Method(mi.getInstance(), parent, mi.getMethod(), ms.getExecutable(), mi.getParameters());
                        }
                    }
                }
                return new Method(mi.getInstance(), parent, mi.getMethod(), null, mi.getParameters());
            }
        } else {
            return new Method(atom.getPandaScript(), parent, mi.getMethod(), mi.getParameters());
        }
        return null;
    }

}
