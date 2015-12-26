package org.panda_lang.panda.core.syntax.block;

import org.panda_lang.panda.core.ElementsPuller;
import org.panda_lang.panda.core.VialCenter;
import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.essential.util.BlockInitializer;
import org.panda_lang.panda.core.parser.essential.util.BlockLayout;
import org.panda_lang.panda.core.syntax.*;

import java.util.List;

public class VialBlock extends Block {

    static {
        ElementsPuller.registerBlock(new BlockLayout(VialBlock.class, false, "vial", "class").parser(new BlockInitializer() {
            @Override
            public Block initialize(Atom atom) {
                return new VialBlock(atom.getBlockInfo().getSpecifiers());
            }
        }));
    }

    private final Vial vial;

    public VialBlock(List<String> specifiers) {
        this.vial = VialCenter.initializeVial(specifiers.get(0));

        if (specifiers.size() > 2 && specifiers.get(1).equals("extends")) {
            vial.extension(specifiers.get(2));
        }

        super.setName(vial.getName());
    }

    @Override
    public void addExecutable(NamedExecutable executable) {
        if (executable instanceof Variable) {

        } else if (executable instanceof MethodBlock) {
            vial.method(new Method(executable));
        } else {
            System.out.println("Cannot add " + executable.getName() + " (" + executable.getClass().getSimpleName() + ") to vial (class)");
        }
    }

}
