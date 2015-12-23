package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.core.Particle;

public class Essence {

    private final Vial vial;

    public Essence(Vial vial) {
        this.vial = vial;
    }

    public Essence call(String methodName, Particle particle) {
        particle.setInstance(new Parameter(getType(), this));
        return vial.call(methodName, particle);
    }

    public Essence call(String methodName, Parameter... parameters) {
        return vial.call(methodName, new Particle(new Parameter(getType(), this), parameters));
    }

    public <T> T cast(Class<T> clazz) {
        try {
            return (T) this;
        } catch (Exception e) {
            System.out.println("Cannot cast " + vial.getName() + " to " + clazz.getSimpleName());
            return null;
        }
    }

    public String getType() {
        return vial.getName();
    }

    public Vial getVial() {
        return vial;
    }

}
