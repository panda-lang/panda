package org.panda_lang.panda.core;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.PandaScript;
import org.panda_lang.panda.core.memory.Memory;
import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.Factor;

public class Particle {

    private PandaScript pandaScript;
    private Memory memory;
    private Essence essence;
    private Factor instance;
    private Factor[] factors;

    public Particle() {
    }

    public Particle(PandaScript pandaScript, Memory memory, Essence essence, Factor instance, Factor... factors) {
        this.pandaScript = pandaScript;
        this.memory = memory;
        this.essence = essence;
        this.instance = instance;
        this.factors = factors;
    }

    public Particle pandaScript(PandaScript pandaScript) {
        this.pandaScript = pandaScript;
        return this;
    }

    public Particle memory(Memory memory) {
        this.memory = memory;
        return this;
    }

    public Particle essence(Essence essence) {
        this.essence = essence;
        return this;
    }

    public Particle instance(Factor instance) {
        this.instance = instance;
        return this;
    }

    public Particle factors(Factor... factors) {
        this.factors = factors;
        return this;
    }

    public Particle fork() {
        return new Particle(pandaScript, memory, essence, instance, factors);
    }

    public boolean hasFactors() {
        return factors != null && factors.length > 0;
    }

    public void setEssence(Essence essence) {
        this.essence = essence;
    }

    public void setInstance(Factor instance) {
        this.instance = instance;
    }

    public void setFactors(Factor... factors) {
        this.factors = factors;
    }

    public void setMemory(Memory memory) {
        this.memory = memory;
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

    public Factor getInstance() {
        return instance;
    }

    public Factor[] getFactors() {
        return factors;
    }

    public Memory getMemory() {
        return memory;
    }

    public PandaScript getPandaScript() {
        return pandaScript;
    }

}
