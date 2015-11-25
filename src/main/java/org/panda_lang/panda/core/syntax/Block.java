package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.lang.PObject;
import org.panda_lang.panda.util.VariableMap;

import java.util.Collection;
import java.util.LinkedList;

public class Block implements Executable {

    private String name;
    private Block parent;
    private Collection<Executable> executables;
    private VariableMap<String, PObject> variables;
    protected Parameter[] parameters;

    public Block() {
        this.executables = new LinkedList<>();
    }

    public Block(Block parent) {
        this.executables = new LinkedList<>();
        this.variables = new VariableMap<>(parent.getVariables());
        this.parent = parent;
    }

    @Override
    public PObject run(Parameter... vars) {
        // {vars.init}
        if (vars != null && parameters != null && vars.length <= parameters.length) {
            for (int i = 0; i < vars.length; i++) {
                parameters[i].setValue(vars[i].getValue());
            }
        }
        for (Executable e : executables) {
            e.run(null); // {vars} -> {null}
        }
        return null;
    }

    public void addExecutable(Executable e) {
        this.executables.add(e);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParent(Block block) {
        this.parent = block;
        this.variables = new VariableMap<>(parent.getVariables());
    }

    public void setVariable(String var, PObject value) {
        this.variables.put(var, value);
    }

    public void setVariableMap(VariableMap<String, PObject> variables) {
        this.variables = variables;
    }

    public void setParameters(Parameter... parameters) {
        this.parameters = parameters;
    }

    public boolean hasParent() {
        return parent != null;
    }

    public PObject getVariable(String var) {
        return variables.get(var);
    }

    public VariableMap<String, PObject> getVariables() {
        return variables;
    }

    public Collection<Executable> getExecutables() {
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

}
