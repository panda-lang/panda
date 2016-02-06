package org.panda_lang.panda.core;

import org.panda_lang.panda.core.memory.Memory;
import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.Factor;

public class Particle
{

    private Memory memory;
    private Essence essence;
    private Factor instance;
    private Factor[] factors;

    public Particle()
    {
    }

    public Particle(Memory memory)
    {
        this.memory = memory;
    }

    public Particle(Factor... factors)
    {
        this.factors = factors;
    }

    public Particle(Essence essence, Factor... factors)
    {
        this(null, essence, null, factors);
    }

    public Particle(Factor instance, Factor... factors)
    {
        this(null, null, instance, factors);
    }

    public Particle(Particle particle, Factor... factors)
    {
        this(particle.getMemory(), particle.getEssence(), particle.getInstance(), factors);
    }

    public Particle(Particle particle, Memory memory)
    {
        this(memory, particle.getEssence(), particle.getInstance(), particle.getFactors());
    }

    public Particle(Memory memory, Essence essence, Factor instance, Factor... factors)
    {
        this.memory = memory;
        this.essence = essence;
        this.instance = instance;
        this.factors = factors;
    }

    public Particle memory(Memory memory)
    {
        this.memory = memory;
        return this;
    }

    public boolean hasFactors()
    {
        return factors != null && factors.length > 0;
    }

    public void setEssence(Essence essence)
    {
        this.essence = essence;
    }

    public void setInstance(Factor instance)
    {
        this.instance = instance;
    }

    public void setFactors(Factor... factors)
    {
        this.factors = factors;
    }

    public void setMemory(Memory memory)
    {
        this.memory = memory;
    }

    public Factor getFactor(int i)
    {
        return i < factors.length ? factors[i] : null;
    }

    @SuppressWarnings("unchecked")
    public <T extends Essence> T getValueOfFactor(int i)
    {
        return (T) getFactor(i).getValue(memory);
    }

    @SuppressWarnings("unchecked")
    public <T extends Essence> T getValueOfFactor(int i, Class<T> clazz)
    {
        return (T) getFactor(i).getValue(memory);
    }

    @SuppressWarnings("unchecked")
    public <T extends Essence> T getValueOfInstance()
    {
        return (T) instance.getValue(memory);
    }

    @SuppressWarnings("unchecked")
    public <T extends Essence> T getValueOfInstance(Class<T> clazz)
    {
        return (T) instance.getValue(memory);
    }

    public Essence getEssence()
    {
        return essence;
    }

    public Factor getInstance()
    {
        return instance;
    }

    public Factor[] getFactors()
    {
        return factors;
    }

    public Memory getMemory()
    {
        return memory;
    }

}
