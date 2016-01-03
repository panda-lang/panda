package org.panda_lang.panda.core;

import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.Factor;
import org.panda_lang.panda.lang.PNull;

import java.util.Arrays;

public class Particle {

    private Essence essence;
    private Factor instance;
    private Factor[] factors;

    public Particle() {
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

    public Essence getEssence() {
        return essence;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Particle{");
        sb.append("instance=").append(instance);
        sb.append(", factors=").append(Arrays.toString(factors));
        sb.append('}');
        return sb.toString();
    }

}
