package org.panda_lang.panda.core;

import org.panda_lang.panda.core.memory.Memory;
import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.Factor;
import org.panda_lang.panda.lang.PNull;

public class Particle {

    private Essence essence;
    private Factor instance;
    private Factor[] factors;
    private Memory memory;

    public Particle() {
    }

    public Particle(Memory memory) {
        this.memory = memory;
    }

    public Particle(Factor... factors) {
        this.factors = factors;
    }

    public Particle(Essence essence, Factor... factors) {
        this(factors);
        this.essence = essence;
    }

    public Particle(Factor instance, Factor... factors) {
        this(factors);
        this.instance = instance;
    }

    public boolean hasParameters() {
        return factors != null && factors.length > 0;
    }

    public Factor get(int i) {
        return i < factors.length ? factors[i] : null;
    }

    public <T> T get(int i, Class<T> clazz) {
        Essence essence = i < factors.length ? factors[i].getValue() : new PNull();
        return essence.cast(clazz);
    }

    public Essence getValue(int i) {
        Factor factor = get(i);
        return factor != null ? factor.getValue() : new PNull();
    }

    public <T> T getInstance(Class<T> clazz) {
        Essence essence = instance.getValue();
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
