package org.panda_lang.panda.core;

import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.Parameter;
import org.panda_lang.panda.lang.PNull;

import java.util.Arrays;

public class Particle {

    private Essence essence;
    private Parameter instance;
    private Parameter[] parameters;

    public Particle() {
    }

    public Particle(Parameter... parameters) {
        this.parameters = parameters;
    }

    public Particle(Essence essence, Parameter... parameters) {
        this(parameters);
        this.essence = essence;
    }

    public Particle(Parameter instance, Parameter... parameters) {
        this(parameters);
        this.instance = instance;
    }

    public Parameter get(int i) {
        return i < parameters.length ? parameters[i] : null;
    }

    public <T> T get(int i, Class<T> clazz) {
        Essence essence = i < parameters.length ? parameters[i].getValue() : new PNull();
        return essence.cast(clazz);
    }

    public Essence getValue(int i) {
        Parameter parameter = get(i);
        return parameter != null ? parameter.getValue() : new PNull();
    }

    public <T> T getInstance(Class<T> clazz) {
        Essence essence = instance.getValue();
        return essence.cast(clazz);
    }

    public Parameter getInstance() {
        return instance;
    }

    public void setInstance(Parameter instance) {
        this.instance = instance;
    }

    public Parameter[] getParameters() {
        return parameters;
    }

    public void setParameters(Parameter[] parameters) {
        this.parameters = parameters;
    }

    public Essence getEssence() {
        return essence;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Particle{");
        sb.append("instance=").append(instance);
        sb.append(", parameters=").append(Arrays.toString(parameters));
        sb.append('}');
        return sb.toString();
    }

}
