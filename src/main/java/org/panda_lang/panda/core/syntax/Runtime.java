package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.memory.Memory;

public class Runtime implements NamedExecutable
{

    private Factor instance;
    private Executable executable;
    private Factor[] factors;
    private Equality equality;
    private Method method;
    private Math math;

    public Runtime()
    {
    }

    public Runtime(Equality equality)
    {
        this.equality = equality;
    }

    public Runtime(Method method)
    {
        this.method = method;
    }

    public Runtime(Math math)
    {
        this.math = math;
    }

    public Runtime(Factor instance, Executable executable, Factor[] factors)
    {
        this.instance = instance;
        this.executable = executable;
        this.factors = factors;
    }

    @Override
    public Essence run(Particle particle)
    {
        if (particle == null)
        {
            particle = new Particle(new Memory());
        }
        particle.setInstance(instance);
        particle.setFactors(factors);
        if (method != null) return method.run(particle);
        else if (math != null) return math.run(particle);
        else if (equality != null) return equality.run(particle);
        else if (executable != null) return executable.run(particle);
        return null;
    }

    public void setInstance(Factor instance)
    {
        this.instance = instance;
    }

    public void setExecutable(Executable executable)
    {
        this.executable = executable;
    }

    public void setFactors(Factor[] factors)
    {
        this.factors = factors;
    }

    public void setEquality(Equality equality)
    {
        this.equality = equality;
    }

    public void setMethod(Method method)
    {
        this.method = method;
    }

    public void setMath(Math math)
    {
        this.math = math;
    }

    @Override
    public String getName()
    {
        return "Runtime";
    }

}
