package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.memory.Cache;
import org.panda_lang.panda.core.memory.Memory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class Block implements NamedExecutable {

    protected static final AtomicInteger atomicInteger = new AtomicInteger();

    protected Collection<NamedExecutable> executables;
    protected Collection<Field> fields;
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
        Cache cache = memory.getCache();

        if (particle.getFactors() != null && factors != null) {
            for (int i = 0; i < particle.getFactors().length && i < factors.length; i++) {
                memory.put(factors[i].getVariableName(), particle.getFactors()[i].getValue(particle));
            }
        }

        for (NamedExecutable executable : executables) {
            Essence result;

            if (executable instanceof Block) {
                Memory blockMemory = new Memory(memory);
                Particle blockParticle = new Particle()
                        .fork()
                        .memory(blockMemory);
                blockMemory.setBlock((Block) executable);
                result = executable.run(blockParticle);
            }
            else if (executable instanceof Return) {
                result = executable.run(particle);
                cache.proceed(false);
            }
            else if (executable != null) {
                result = executable.run(particle);
            }
            else {
                result = null;
            }

            if (!cache.isProceed()) {
                if (!isReturned() && hasParent()) {
                    memory.getParent().getCache().proceed(false);
                }
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

    public boolean isReturned() {
        return false;
    }

    public boolean hasParent() {
        return parent != null;
    }

    public void setFactors(Factor... factors) {
        this.factors = factors;
    }

    public void setExecutables(Collection<NamedExecutable> executables) {
        this.executables = executables;
    }

    public void setFields(Collection<Field> fields) {
        this.fields = fields;
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

    public void setParent(Block block) {
        this.parent = block;
    }

    public Block getBlock() {
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }

}
