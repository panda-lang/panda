package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.memory.Memory;
import org.panda_lang.panda.core.syntax.block.VialBlock;

public class Essence {

    private int instanceID;
    private Vial vial;
    private VialBlock vialBlock;
    private Memory memory;

    public Essence(Vial vial) {
        this(0);
        this.vial = vial;
    }

    public Essence(Vial vial, int instanceID) {
        this(instanceID);
        this.vial = vial;
    }

    public Essence(int instanceID) {
        this.instanceID = instanceID;
        this.memory = new Memory();
    }

    public Essence call(String methodName, Particle particle) {
        particle = new Particle(memory, this, new Factor(this), particle.getFactors());
        return vial.call(methodName, particle);
    }

    public Essence call(String methodName, Factor... factors) {
        return vial.call(methodName, new Particle(memory, this, new Factor(this), factors));
    }

    public <T> T cast(Class<T> clazz) {
        try {
            return (T) this;
        } catch (Exception e) {
            System.out.println("Cannot cast " + vial.getName() + " to " + clazz.getSimpleName());
            return null;
        }
    }

    public void initializeParticle(Particle particle) {
        this.memory = new Memory(particle.getMemory());
    }

    public Memory getMemory() {
        return memory;
    }

    public Object getJavaValue() {
        return null;
    }

    public int getInstanceID() {
        return instanceID;
    }

    public String getType() {
        return vial.getName();
    }

    public Vial getVial() {
        return vial;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Essence) {
            Essence compared = (Essence) obj;

            Vial currentVial = getVial();
            Vial comparedVial = compared.getVial();

            if (currentVial.isVeritableVial() || comparedVial.isVeritableVial()) {

            }

            Object currentJavaValue = getJavaValue();
            Object comparedJavaValue = compared.getJavaValue();

            if (currentJavaValue != null || comparedJavaValue != null) {
                return currentJavaValue != null ? currentJavaValue.equals(comparedJavaValue) : comparedJavaValue.equals(currentJavaValue);
            }

        }
        return false;
    }

}
