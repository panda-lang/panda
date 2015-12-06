package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.core.Particle;

public class Runtime implements Executable {

    private Parameter instance;
    private IExecutable executable;
    private Parameter[] parameters;
    private Equality equality;
    private Method method;
    private Math math;

    public Runtime(Equality equality) {
        this.equality = equality;
    }

    public Runtime(Method method) {
        this.method = method;
    }

    public Runtime(Math math) {
        this.math = math;
    }

    public Runtime(Parameter instance, IExecutable executable, Parameter[] parameters) {
        this.instance = instance;
        this.executable = executable;
        this.parameters = parameters;
    }

    @Override
    public Essence run(Particle particle) {
        particle.setInstance(instance);
        particle.setParameters(parameters);
        if (method != null) return method.run(particle);
        else if (math != null) return math.run(particle);
        else if (equality != null) return equality.run(particle);
        return executable.run(particle);
    }

    @Override
    public String getName() {
        return "Runtime";
    }

}
