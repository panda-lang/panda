package org.panda_lang.panda.core.statement;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Essence;
import org.panda_lang.panda.core.memory.Memory;
import org.panda_lang.panda.core.statement.util.FactorType;

public class Factor implements Executable {

    private final FactorType type;
    private Essence definedEssence;
    private String variableName;
    private Runtime runtime;
    private Essence value;
    private String dataType;

    public Factor(Essence definedEssence) {
        this.type = FactorType.DEFINED;
        this.definedEssence = definedEssence;
        this.value = definedEssence;
    }

    public Factor(Runtime runtime) {
        this.type = FactorType.RUNTIME;
        this.runtime = runtime;
    }

    public Factor(String variableName) {
        this.type = FactorType.VARIABLE;
        this.variableName = variableName;
    }

    @Override
    public Essence run(Alice alice) {
        return getValue(alice);
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @SuppressWarnings("unchecked")
    public <T extends Essence> T getCurrentValue() {
        return (T) this.value;
    }

    @SuppressWarnings("unchecked")
    public <T extends Essence> T getValue(Alice alice) {
        switch (type) {
            case DEFINED:
                this.value = definedEssence;
                break;
            case VARIABLE:
                this.value = getMemoryValue(alice);
                break;
            case RUNTIME:
                if (alice == null) {
                    alice = new Alice();
                    alice.setMemory(new Memory());
                }
                this.value = getRuntimeValue(alice);
                break;
        }
        return (T) this.value;
    }

    public Essence getMemoryValue(Alice alice) {
        return alice.getMemory().get(variableName);
    }

    public Essence getRuntimeValue(Alice alice) {
        return runtime.run(alice);
    }

    public Essence getDefinedEssence() {
        return definedEssence;
    }

    public String getVariableName() {
        return variableName;
    }

}