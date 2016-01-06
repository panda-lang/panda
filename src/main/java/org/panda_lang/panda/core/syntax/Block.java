package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.memory.Memory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

public class Block implements NamedExecutable {

    private final Collection<NamedExecutable> executables;
    private final Collection<Field> fields;
    private String name;
    private Block parent;
    protected Factor[] factors;

    public Block(Block parent) {
        this();
        this.parent = parent;
    }

    public Block() {
        this.executables = new LinkedList<>();
        this.fields = new ArrayList<>();
    }

    @Override
    public Essence run(Particle particle) {
        if (particle.getFactors() != null) {
            for (int i = 0; i < particle.getFactors().length && i < factors.length; i++) {
                particle.getMemory().put(factors[i].getVariable(), particle.getFactors()[i].getValue(particle));
            }
        }
        for (NamedExecutable e : executables) {
            if (e instanceof Block) {
                Memory memory = new Memory(particle.getMemory());
                e.run(new Particle(particle, memory));
            } else {
                e.run(particle);
            }
        }
        return null;
    }

    public void addExecutable(NamedExecutable e) {
        this.executables.add(e);
        if (e instanceof Field) {
            fields.add((Field) e);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParent(Block block) {
        this.parent = block;
    }

    public void setFactors(Factor... factors) {
        this.factors = factors;
    }

    public boolean hasParent() {
        return parent != null;
    }

    public Factor[] getFactors() {
        return factors;
    }

    public Collection<NamedExecutable> getExecutables() {
        return executables;
    }

    public Block getParent() {
        return parent;
    }

    public Block getBlock() {
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

}
