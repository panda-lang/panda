package org.panda_lang.panda.core.syntax.block;

import org.panda_lang.panda.core.ElementsBucket;
import org.panda_lang.panda.core.parser.improved.Atom;
import org.panda_lang.panda.core.parser.improved.essential.ParameterParser;
import org.panda_lang.panda.core.parser.improved.essential.util.BlockInitializer;
import org.panda_lang.panda.core.scheme.BlockScheme;
import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.Parameter;
import org.panda_lang.panda.lang.PNumber;
import org.panda_lang.panda.lang.PObject;

public class ForBlock extends Block {

    static {
        ElementsBucket.registerBlock(new BlockScheme(ForBlock.class, "for", "loop").parser(new BlockInitializer() {
            @Override
            public Block initialize(Atom atom) {
                Block current = new ForBlock();
                current.setParameters(new ParameterParser().parse(atom));
                return current;
            }
        }));
    }

    public ForBlock() {
        super.setName("ForBlock");
    }

    @Override
    public PObject run(Parameter... vars) {
        PObject object = parameters[0].getValue();
        if (object instanceof PNumber) {
            Number times = ((PNumber) object).getNumber();
            for (int i = 0; i < times.intValue(); i++) {
                PObject o = super.run(vars);
                if (o != null) {
                    return o;
                }
            }
        }
        return null;
    }

}
