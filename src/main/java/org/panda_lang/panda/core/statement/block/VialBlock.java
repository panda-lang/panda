package org.panda_lang.panda.core.statement.block;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.PandaScript;
import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Inst;
import org.panda_lang.panda.core.parser.ParserInfo;
import org.panda_lang.panda.core.parser.essential.util.BlockInitializer;
import org.panda_lang.panda.core.parser.essential.util.BlockLayout;
import org.panda_lang.panda.core.statement.*;

import java.util.List;

public class VialBlock extends Block {

    private final PandaScript pandaScript;
    private final Structure structure;

    public VialBlock(PandaScript pandaScript, Group group, List<String> specifiers) {
        this.pandaScript = pandaScript;
        this.structure = new Structure(specifiers.get(0));
        this.structure.setVialBlock(this);

        if (group != null) {
            this.structure.group(group);
        }
        else {
            this.structure.group("default");
        }

        if (specifiers.size() > 2 && specifiers.get(1).equals("extends")) {
            structure.extension(specifiers.get(2));
        }

        super.setName(structure.getName());
    }

    public static void initialize(Panda panda) {
        BlockLayout blockLayout = new BlockLayout(VialBlock.class, false, "structure", "class").initializer(new BlockInitializer() {
            @Override
            public Block initialize(ParserInfo atom) {
                Group group = atom.getPandaParser().getPandaBlock().getGroup();
                return new VialBlock(atom.getPandaScript(), group, atom.getBlockInfo().getSpecifiers());
            }
        });
        panda.getPandaCore().registerBlock(blockLayout);
    }

    public Alice initializeFields(Inst inst) {
        Alice alice = new Alice().pandaScript(pandaScript);
        alice.setMemory(inst.getMemory());
        for (Executable executable : getExecutables()) {
            if (executable instanceof Field) {
                executable.execute(alice);
            }
        }
        return alice;
    }

    @Override
    public Inst execute(Alice alice) {
        return structure.initializeInstance(alice);
    }

    @Override
    public void addExecutable(Executable executable) {
        if (executable instanceof Field) {
            Field field = (Field) executable;
            structure.getFields().put(field.getName(), field);
        }
        else if (executable instanceof MethodBlock) {
            MethodBlock methodBlock = (MethodBlock) executable;
            structure.method(new Method(methodBlock));
        }
        else {
            System.out.println("Cannot add " + executable + " (" + executable.getClass().getSimpleName() + ") to structure (class)");
        }
    }

    public Structure getStructure() {
        return structure;
    }

}
