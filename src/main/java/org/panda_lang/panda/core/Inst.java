package org.panda_lang.panda.core;

import org.panda_lang.panda.core.memory.Memory;
import org.panda_lang.panda.core.statement.RuntimeValue;
import org.panda_lang.panda.core.statement.Structure;

import java.util.concurrent.atomic.AtomicInteger;

public class Inst {

    protected static final AtomicInteger instanceIDAssigner = new AtomicInteger();

    private final int instanceID;
    private Structure structure;
    private Memory memory;

    public Inst(Structure structure) {
        this();
        this.structure = structure;
    }

    public Inst() {
        this.instanceID = instanceIDAssigner.incrementAndGet();
        this.memory = new Memory();
    }

    public void initializeParticle(Alice alice) {
        this.memory = new Memory(alice.getMemory());
    }

    public Inst call(String methodName, Alice alice) {
        return structure.call(methodName, particle(alice));
    }

    public Alice particle(Alice alice) {
        return new Alice(alice.getPandaScript(), memory, new RuntimeValue(this), alice.getRuntimeValues());
    }

    @SuppressWarnings("unchecked")
    public <T extends Inst> T cast(Class<? extends Inst> clazz) {
        try {
            return (T) this;
        } catch (Exception e) {
            System.out.println("Cannot cast " + structure.getName() + " to " + clazz.getSimpleName());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Inst> T getMe() {
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
        return structure.getName();
    }

    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Inst)) {
            return false;
        }

        Inst inst = (Inst) o;

        if (instanceID != inst.instanceID) {
            return false;
        }
        return structure.equals(inst.structure);

    }

    @Override
    public int hashCode() {
        int result = instanceID;
        result = 31 * result + structure.hashCode();
        return result;
    }

}
