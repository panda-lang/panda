package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.memory.Memory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class Block implements NamedExecutable {

    protected static final AtomicInteger atomicInteger = new AtomicInteger();

    private final Collection<NamedExecutable> executables;
    private final Collection<Field> fields;
    protected Factor[] factors;
    private String name;
    private Block parent;

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
        Memory memory = particle.getMemory();

        if (particle.getFactors() != null) {
            for (int i = 0; i < particle.getFactors().length && i < factors.length; i++) {
                memory.put(factors[i].getVariable(), particle.getFactors()[i].getValue(particle));
            }
        }

        for (NamedExecutable executable : executables) {
            Essence result;

            if (executable instanceof Block) {
                Memory blockMemory = new Memory(memory);
                Particle blockParticle = new Particle(particle, blockMemory);
                blockMemory.getCache().proceed(true);
                result = executable.run(blockParticle);
            } else if (executable instanceof Return) {
                return executable.run(particle);
            } else {
                result = executable.run(particle);
            }

            if (result != null && !memory.getCache().isProceed()) {
                return result;
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

    public Collection<Field> getFields() {
        return fields;
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
