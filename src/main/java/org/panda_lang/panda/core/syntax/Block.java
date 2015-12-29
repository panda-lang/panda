package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.util.VariableMap;

import java.util.Collection;
import java.util.LinkedList;

public class Block implements NamedExecutable {

    private String name;
    private Block parent;
    private Collection<NamedExecutable> executables;
    private VariableMap variables;
    protected Parameter[] parameters;

    public Block(Block parent) {
        this.executables = new LinkedList<>();
        this.variables = new VariableMap(parent.getVariables());
        this.parent = parent;
    }

    public Block() {
        this.executables = new LinkedList<>();
        this.variables = new VariableMap();
    }

    @Override
    public Essence run(Particle particle) {
        if (particle.getParameters() != null) {
            for (int i = 0; i < particle.getParameters().length && i < parameters.length; i++) {
                variables.put(parameters[i].getVariable(), particle.getParameters()[i].getValue());
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
        this.variables = new VariableMap(parent.getVariables());
    }

    public void setVariable(String var, Essence value) {
        this.variables.put(var, value);
    }

    public void setVariableMap(VariableMap variables) {
        this.variables = variables;
    }

    public void setParameters(Parameter... parameters) {
        this.parameters = parameters;
    }

    public boolean hasParent() {
        return parent != null;
    }

    public Parameter[] getParameters() {
        return parameters;
    }

    public Essence getVariable(String var) {
        return variables.get(var);
    }

    public VariableMap getVariables() {
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
