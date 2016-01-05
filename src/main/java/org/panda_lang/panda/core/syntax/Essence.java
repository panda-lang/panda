package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.memory.Memory;
import org.panda_lang.panda.core.syntax.block.VialBlock;

public class Essence {

    private final Vial vial;
    private final int instanceID;
    private VialBlock vialBlock;
    private Memory memory;

    public Essence(Vial vial) {
        this.vial = vial;
        this.instanceID = 0;
    }

    public Essence(Vial vial, int instanceID) {
        this.vial = vial;
        this.instanceID = instanceID;
    }

    public Essence(VialBlock vialBlock) {
        this.vial = vialBlock.getVial();
        this.instanceID = 0;
        this.vialBlock = vialBlock;
        this.memory = vialBlock.createBranch();

        vialBlock.initializeFields();
    }

    public Essence call(String methodName, Particle particle) {
        particle.setInstance(new Factor(this));
        return vial.call(methodName, particle);
    }

    public Essence call(String methodName, Factor... factors) {
        return vial.call(methodName, new Particle(new Factor(this), factors));
    }

    public <T> T cast(Class<T> clazz) {
        try {
            return (T) this;
        } catch (Exception e) {
            System.out.println("Cannot cast " + vial.getName() + " to " + clazz.getSimpleName());
            return null;
        }
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

}
