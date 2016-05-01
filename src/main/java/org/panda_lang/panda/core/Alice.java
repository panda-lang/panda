package org.panda_lang.panda.core;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.PandaScript;
import org.panda_lang.panda.core.memory.Memory;
import org.panda_lang.panda.core.statement.Block;
import org.panda_lang.panda.core.statement.RuntimeValue;

public class Alice {

    private PandaScript pandaScript;
    private Block block;
    private Memory memory;
    private Inst value;
    private RuntimeValue instance;
    private RuntimeValue[] runtimeValues;
    private Object custom;

    public Alice() {
    }

    public Alice(PandaScript pandaScript, Memory memory, RuntimeValue instance, RuntimeValue... runtimeValues) {
        this.pandaScript = pandaScript;
        this.memory = memory;
        this.instance = instance;
        this.runtimeValues = runtimeValues;
    }

    public Alice pandaScript(PandaScript pandaScript) {
        this.pandaScript = pandaScript;
        return this;
    }

    public Alice block(Block block) {
        this.block = block;
        return this;
    }

    public Alice memory(Memory memory) {
        this.memory = memory;
        return this;
    }

    public Alice instance(RuntimeValue instance) {
        this.instance = instance;
        return this;
    }

    public Alice factors(RuntimeValue... runtimeValues) {
        this.runtimeValues = runtimeValues;
        return this;
    }

    public Alice custom(Object custom) {
        this.custom = custom;
        return this;
    }

    public Alice fork() {
        return new Alice(pandaScript, memory, instance, runtimeValues).custom(custom);
    }

    public boolean hasFactors() {
        return runtimeValues != null && runtimeValues.length > 0;
    }

    @SuppressWarnings("unchecked")
    public <T> T getCustom() {
        return (T) custom;
    }

    public void setCustom(Object custom) {
        this.custom = custom;
    }

    public RuntimeValue getFactor(int i) {
        return i < runtimeValues.length ? runtimeValues[i] : null;
    }

    @SuppressWarnings("unchecked")
    public <T extends Inst> T getValueOfFactor(int i) {
        return (T) getFactor(i).getValue(this);
    }

    @SuppressWarnings("unchecked")
    public <T extends Inst> T getValueOfFactor(int i, Class<T> clazz) {
        return (T) getFactor(i).getValue(this);
    }

    @SuppressWarnings("unchecked")
    public <T extends Inst> T getValueOfInstance() {
        return (T) instance.getValue(this);
    }

    @SuppressWarnings("unchecked")
    public <T extends Inst> T getValueOfInstance(Class<T> clazz) {
        return (T) instance.getValue(this);
    }

    public Panda getPanda() {
        return pandaScript.getPanda();
    }

    public RuntimeValue getInstance() {
        return instance;
    }

    public void setInstance(RuntimeValue instance) {
        this.instance = instance;
    }

    public RuntimeValue[] getRuntimeValues() {
        return runtimeValues;
    }

    public void setRuntimeValues(RuntimeValue... runtimeValues) {
        this.runtimeValues = runtimeValues;
    }

    public Memory getMemory() {
        return memory;
    }

    public void setMemory(Memory memory) {
        this.memory = memory;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public PandaScript getPandaScript() {
        return pandaScript;
    }

}
