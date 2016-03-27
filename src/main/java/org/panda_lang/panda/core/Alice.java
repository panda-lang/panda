package org.panda_lang.panda.core;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.PandaScript;
import org.panda_lang.panda.core.statement.Factor;
import org.panda_lang.panda.core.memory.Memory;
import org.panda_lang.panda.core.statement.Block;

public class Alice {

    private PandaScript pandaScript;
    private Block block;
    private Memory memory;
    private Essence essence;
    private Factor instance;
    private Factor[] factors;
    private Object custom;

    public Alice() {
    }

    public Alice(PandaScript pandaScript, Memory memory, Essence essence, Factor instance, Factor... factors) {
        this.pandaScript = pandaScript;
        this.memory = memory;
        this.essence = essence;
        this.instance = instance;
        this.factors = factors;
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

    public Alice essence(Essence essence) {
        this.essence = essence;
        return this;
    }

    public Alice instance(Factor instance) {
        this.instance = instance;
        return this;
    }

    public Alice factors(Factor... factors) {
        this.factors = factors;
        return this;
    }

    public Alice custom(Object custom) {
        this.custom = custom;
        return this;
    }

    public Alice fork() {
        return new Alice(pandaScript, memory, essence, instance, factors);
    }

    public boolean hasFactors() {
        return factors != null && factors.length > 0;
    }

    @SuppressWarnings("unchecked")
    public <T> T getCustom() {
        return (T) custom;
    }

    public void setCustom(Object custom) {
        this.custom = custom;
    }

    public Factor getFactor(int i) {
        return i < factors.length ? factors[i] : null;
    }

    @SuppressWarnings("unchecked")
    public <T extends Essence> T getValueOfFactor(int i) {
        return (T) getFactor(i).getValue(this);
    }

    @SuppressWarnings("unchecked")
    public <T extends Essence> T getValueOfFactor(int i, Class<T> clazz) {
        return (T) getFactor(i).getValue(this);
    }

    @SuppressWarnings("unchecked")
    public <T extends Essence> T getValueOfInstance() {
        return (T) instance.getValue(this);
    }

    @SuppressWarnings("unchecked")
    public <T extends Essence> T getValueOfInstance(Class<T> clazz) {
        return (T) instance.getValue(this);
    }

    public Panda getPanda() {
        return pandaScript.getPanda();
    }

    public Essence getEssence() {
        return essence;
    }

    public void setEssence(Essence essence) {
        this.essence = essence;
    }

    public Factor getInstance() {
        return instance;
    }

    public void setInstance(Factor instance) {
        this.instance = instance;
    }

    public Factor[] getFactors() {
        return factors;
    }

    public void setFactors(Factor... factors) {
        this.factors = factors;
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
