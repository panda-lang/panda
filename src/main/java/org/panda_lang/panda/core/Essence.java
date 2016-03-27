package org.panda_lang.panda.core;

import org.panda_lang.panda.core.statement.Factor;
import org.panda_lang.panda.core.memory.Memory;
import org.panda_lang.panda.core.statement.Field;
import org.panda_lang.panda.core.statement.util.NamedExecutable;
import org.panda_lang.panda.core.statement.Vial;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Essence implements NamedExecutable {

    protected static final AtomicInteger instanceIDAssigner = new AtomicInteger();

    private final int instanceID;
    private Vial vial;
    private Memory memory;

    public Essence(Vial vial) {
        this();
        this.vial = vial;
    }

    public Essence() {
        this.instanceID = instanceIDAssigner.incrementAndGet();
        this.memory = new Memory();
    }

    public void initializeParticle(Alice alice) {
        this.memory = new Memory(alice.getMemory());
    }

    public Essence call(String methodName, Alice alice) {
        return vial.call(methodName, particle(alice));
    }

    public Alice particle(Alice alice) {
        return new Alice(alice.getPandaScript(), memory, this, new Factor(this), alice.getFactors());
    }

    @Override
    public Essence run(Alice alice) {
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T extends Essence> T cast(Class<? extends Essence> clazz) {
        try {
            return (T) this;
        } catch (Exception e) {
            System.out.println("Cannot cast " + vial.getName() + " to " + clazz.getSimpleName());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Essence> T getMe() {
        return (T) this;
    }

    public Memory getMemory() {
        return memory;
    }

    public Object getJavaValue() {
        return this;
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

    public void setVial(Vial vial) {
        this.vial = vial;
    }

    @Override
    public String getName() {
        return vial != null ? vial.getName() : "Unknown";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj != null && obj instanceof Essence) {
            Essence compared = (Essence) obj;

            Vial currentVial = getVial();
            Vial comparedVial = compared.getVial();

            if (currentVial.isVeritableVial() || comparedVial.isVeritableVial()) {
                if (!currentVial.equals(comparedVial)) {
                    return false;
                }

                for (Map.Entry<String, Field> entry : currentVial.getFields().entrySet()) {
                    Essence currentEssence = memory.get(entry.getKey());
                    Essence comparedEssence = compared.getMemory().get(entry.getKey());

                    if (currentEssence != null || comparedEssence != null) {
                        if (currentEssence != null && currentEssence.equals(comparedEssence)) {
                            continue;
                        }
                    }

                    break;
                }

                return false;
            }

            Object currentJavaValue = getJavaValue();
            Object comparedJavaValue = compared.getJavaValue();

            if (currentJavaValue != null || comparedJavaValue != null) {
                return currentJavaValue != null && currentJavaValue.equals(comparedJavaValue);
            }

        }

        return false;
    }

    @Override
    public String toString() {
        return "{" + getName() + "@" + getInstanceID() + "}";
    }

}
