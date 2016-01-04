package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.util.VariableMap;

import java.util.Collection;
import java.util.LinkedList;

public class Block implements NamedExecutable {

    private String name;
    private Block parent;
    private Collection<NamedExecutable> executables;
    //private VariableMap variables;
    protected Factor[] factors;

    public Block(Block parent) {
        this.executables = new LinkedList<>();
        //this.variables = new VariableMap(parent.getVariables());
        this.parent = parent;
    }

    public Block() {
        this.executables = new LinkedList<>();
        //this.variables = new VariableMap();
    }

    @Override
    public Essence run(Particle particle) {
        if (particle.getFactors() != null) {
            for (int i = 0; i < particle.getFactors().length && i < factors.length; i++) {
                //variables.put(factors[i].getVariable(), particle.getFactors()[i].getValue());
            }
        }
        for (NamedExecutable e : executables) {
            e.run(particle);
        }
        return null;
    }

    public void addExecutable(NamedExecutable e) {
        this.executables.add(e);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParent(Block block) {
        this.parent = block;
        //this.variables = new VariableMap(parent.getVariables());
    }

    public void setVariable(String var, Essence value) {
        //this.variables.put(var, value);
    }

    public void setVariableMap(VariableMap variables) {
        //this.variables = variables;
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

    public Essence getVariable(String var) {
        //return variables.get(var);
        return null;
    }

    public VariableMap getVariables() {
        //return variables;
        return null;
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
