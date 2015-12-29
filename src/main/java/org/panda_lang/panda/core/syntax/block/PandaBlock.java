package org.panda_lang.panda.core.syntax.block;

import org.panda_lang.panda.core.GlobalVariables;
import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.syntax.*;

import java.util.ArrayList;
import java.util.Collection;

public class PandaBlock extends Block {

    private final Collection<Import> imports;
    private Group group;

    public PandaBlock() {
        super.setName("Panda Block");
        super.setVariableMap(GlobalVariables.VARIABLES);

        this.imports = new ArrayList<>();
    }

    public void initializeGlobalVariables() {
        Particle particle = new Particle();
        for (NamedExecutable executable : getExecutables()) {
            if (executable instanceof Variable) {
                executable.run(particle);
            }
        }
    }

    public Essence call(Class<? extends Block> blockType, String name, Parameter... parameters) {
        for (NamedExecutable executable : super.getExecutables()) {
            if (executable.getClass() == blockType && executable.getName().equals(name)) {
                Particle particle = new Particle();
                particle.setParameters(parameters);
                return executable.run(particle);
            }
        }
        return null;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

}
