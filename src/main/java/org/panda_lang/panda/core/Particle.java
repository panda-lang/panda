package org.panda_lang.panda.core;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.core.memory.Memory;
import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.Factor;

public class Particle {

    private Panda panda;
    private Memory memory;
    private Essence essence;
    private Factor instance;
    private Factor[] factors;

    public Particle(Panda panda) {
        this.panda = panda;
    }

    public Particle(Panda panda, Memory memory) {
        this(panda);
        this.memory = memory;
    }

    public Particle(Panda panda, Factor... factors) {
        this.panda = panda;
        this.factors = factors;
    }

    public Particle(Panda panda, Essence essence, Factor... factors) {
        this(panda, null, essence, null, factors);
    }

    public Particle(Panda panda, Factor instance, Factor... factors) {
        this(panda, null, null, instance, factors);
    }

    public Particle(Panda panda, Particle particle, Factor... factors) {
        this(panda, particle.getMemory(), particle.getEssence(), particle.getInstance(), factors);
    }

    public Particle(Panda panda, Particle particle, Memory memory) {
        this(panda, memory, particle.getEssence(), particle.getInstance(), particle.getFactors());
    }

    public Particle(Panda panda, Memory memory, Essence essence, Factor instance, Factor... factors) {
        this.memory = memory;
        this.essence = essence;
        this.instance = instance;
        this.factors = factors;
    }

    public Particle memory(Memory memory) {
        this.memory = memory;
        return this;
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
        return (T) getFactor(i).getValue(memory);
    }

    @SuppressWarnings("unchecked")
    public <T extends Essence> T getValueOfFactor(int i, Class<T> clazz) {
        return (T) getFactor(i).getValue(memory);
    }

    @SuppressWarnings("unchecked")
    public <T extends Essence> T getValueOfInstance() {
        return (T) instance.getValue(memory);
    }

    @SuppressWarnings("unchecked")
    public <T extends Essence> T getValueOfInstance(Class<T> clazz) {
        return (T) instance.getValue(memory);
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

    public Panda getPanda() {
        return panda;
    }

}
