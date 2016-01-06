package org.panda_lang.panda.core;

import org.panda_lang.panda.core.memory.Memory;
import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.Factor;
import org.panda_lang.panda.lang.PNull;

public class Particle {

    private Memory memory;
    private Essence essence;
    private Factor instance;
    private Factor[] factors;

    public Particle() {
    }

    public Particle(Memory memory) {
        this.memory = memory;
    }

    public Particle(Factor... factors) {
        this.factors = factors;
    }

    public Particle(Essence essence, Factor... factors) {
        this(null, essence, null, factors);
    }

    public Particle(Factor instance, Factor... factors) {
        this(null, null, instance, factors);
    }

    public Particle(Particle particle, Memory memory) {
        this(memory, particle.getEssence(), particle.getInstance(), particle.getFactors());
    }

    public Particle(Memory memory, Essence essence, Factor instance, Factor... factors) {
        this.memory = memory;
        this.essence = essence;
        this.instance = instance;
        this.factors = factors;
    }

    public boolean hasParameters() {
        return factors != null && factors.length > 0;
    }

    public Factor get(int i) {
        return i < factors.length ? factors[i] : null;
    }

    public <T> T get(int i, Class<T> clazz) {
        Essence essence = i < factors.length ? factors[i].getValue(memory) : new PNull();
        return essence.cast(clazz);
    }

    public Essence getValue(int i) {
        Factor factor = get(i);
        return factor != null ? factor.getValue(memory) : new PNull();
    }

    public <T> T getInstance(Class<T> clazz) {
        Essence essence = instance.getValue(memory);
        return essence.cast(clazz);
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

}
