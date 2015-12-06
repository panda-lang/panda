package org.panda_lang.panda.core;

import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.Parameter;
import org.panda_lang.panda.lang.PNull;

public class Particle {

    private Parameter instance;
    private Parameter[] parameters;

    public Particle() { }

    public Particle(Parameter... parameters) {
        this.parameters = parameters;
    }

    public Parameter get(int i) {
        return i < parameters.length ? parameters[i] : null;
    }

    public <T> T get(int i, Class<T> clazz) {
        Essence essence = i < parameters.length ? parameters[i].getValue() : new PNull();
        return essence.cast(clazz);
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

}
