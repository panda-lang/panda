package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.core.Particle;

public class Essence {

    private final Vial vial;
    private Particle particle;

    public Essence(Vial vial) {
        this.vial = vial;
        this.particle = new Particle();
    }

    public <T> T cast(Class<T> clazz) {
        try {
            return (T) this;
        } catch (Exception e) {
            System.out.println("Cannot cast " + vial.getName() + " to " + clazz.getSimpleName());
            return null;
        }
    }

    public void setParticle(Particle particle) {
        this.particle = particle;
    }

    public Particle getParticle() {
        return particle;
    }

    public String getType() {
        return vial.getName();
    }

    public Vial getVial() {
        return vial;
    }

}
