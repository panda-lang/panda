package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.core.Particle;

import java.util.Arrays;

public class Runtime implements NamedExecutable {

    private Parameter instance;
    private Executable executable;
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

    public Runtime(Parameter instance, Executable executable, Parameter[] parameters) {
        this.instance = instance;
        this.executable = executable;
        this.parameters = parameters;
    }

    @Override
    public Essence run(Particle particle) {
        if (particle == null) {
            particle = new Particle();
        }
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Runtime{");
        sb.append("instance=").append(instance);
        sb.append(", executable=").append(executable);
        sb.append(", parameters=").append(Arrays.toString(parameters));
        sb.append(", equality=").append(equality);
        sb.append(", method=").append(method);
        sb.append(", math=").append(math);
        sb.append('}');
        return sb.toString();
    }

}
