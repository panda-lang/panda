package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.syntax.block.PandaBlock;

import java.util.ArrayList;
import java.util.Collection;

public class Group implements NamedExecutable {

    private final String group;
    private final Collection<PandaBlock> elements;

    public Group(String group) {
        this.group = group;
        this.elements = new ArrayList<>();
    }

    @Override
    public Essence run(Particle particle) {
        return null;
    }

    public Collection<PandaBlock> getElements() {
        return elements;
    }

    @Override
    public String getName() {
        return group;
    }

}
