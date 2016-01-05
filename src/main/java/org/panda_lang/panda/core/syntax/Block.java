package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.memory.Memory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

public class Block implements NamedExecutable {

    private String name;
    private Block parent;
    private Collection<NamedExecutable> executables;
    private Collection<Field> fields;
    private Memory variables;
    protected Factor[] factors;

    public Block(Block parent) {
        this();
        this.parent = parent;
    }

    public Block() {
        this.executables = new LinkedList<>();
        this.fields = new ArrayList<>();
        this.variables = new Memory();
    }

    @Override
    public Essence run(Particle particle) {
        if (particle.getFactors() != null) {
            for (int i = 0; i < particle.getFactors().length && i < factors.length; i++) {
                variables.put(factors[i].getVariable(), particle.getFactors()[i].getValue());
            }
        }
        for (NamedExecutable e : executables) {
            e.run(particle);
        }
        return null;
    }

    public Memory createBranch() {
        return new Memory(variables.getParent());
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
        this.variables = new Memory(parent.getVariables());
    }

    public void setVariable(String var, Essence value) {
        this.variables.put(var, value);
    }

    public void setVariableMap(Memory variables) {
        this.variables = variables;
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

    public Memory getVariables() {
        return variables;
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
