package org.panda_lang.panda.core.statement.block;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.PandaScript;
import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Essence;
import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.essential.util.BlockInitializer;
import org.panda_lang.panda.core.parser.essential.util.BlockLayout;
import org.panda_lang.panda.core.statement.*;

import java.util.List;

public class VialBlock extends Block {

    private final PandaScript pandaScript;
    private final Vial vial;

    public VialBlock(PandaScript pandaScript, Group group, List<String> specifiers) {
        this.pandaScript = pandaScript;
        this.vial = new Vial(specifiers.get(0));
        this.vial.setVialBlock(this);

        if (group != null) {
            this.vial.group(group);
        }
        else {
            this.vial.group("default");
        }

        if (specifiers.size() > 2 && specifiers.get(1).equals("extends")) {
            vial.extension(specifiers.get(2));
        }

        super.setName(vial.getName());
    }

    public static void initialize(Panda panda) {
        BlockLayout blockLayout = new BlockLayout(VialBlock.class, false, "vial", "class").initializer(new BlockInitializer() {
            @Override
            public Block initialize(Atom atom) {
                Group group = atom.getPandaParser().getPandaBlock().getGroup();
                return new VialBlock(atom.getPandaScript(), group, atom.getBlockInfo().getSpecifiers());
            }
        });
        panda.getPandaCore().registerBlock(blockLayout);
    }

    public Alice initializeFields(Essence essence) {
        Alice alice = new Alice().pandaScript(pandaScript);
        alice.setMemory(essence.getMemory());
        for (Executable executable : getExecutables()) {
            if (executable instanceof Field) {
                executable.execute(alice);
            }
        }
        return alice;
    }

    @Override
    public Essence execute(Alice alice) {
        return vial.initializeInstance(alice);
    }

    @Override
    public void addExecutable(Executable executable) {
        if (executable instanceof Field) {
            Field field = (Field) executable;
            vial.getFields().put(field.getName(), field);
        }
        else if (executable instanceof MethodBlock) {
            MethodBlock methodBlock = (MethodBlock) executable;
            vial.method(new Method(methodBlock));
        }
        else {
            System.out.println("Cannot add " + executable + " (" + executable.getClass().getSimpleName() + ") to vial (class)");
        }
    }

    public Vial getVial() {
        return vial;
    }

}
