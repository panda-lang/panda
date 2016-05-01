package org.panda_lang.panda.core.statement;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Inst;
import org.panda_lang.panda.core.memory.Memory;
import org.panda_lang.panda.core.statement.util.FactorType;

public class RuntimeValue implements Executable {

    private final FactorType type;
    private Inst definedInst;
    private String variableName;
    private Runtime runtime;
    private Inst value;
    private String dataType;

    public RuntimeValue(Inst definedInst) {
        this.type = FactorType.DEFINED;
        this.definedInst = definedInst;
        this.value = definedInst;
    }

    public RuntimeValue(Runtime runtime) {
        this.type = FactorType.RUNTIME;
        this.runtime = runtime;
    }

    public RuntimeValue(String variableName) {
        this.type = FactorType.VARIABLE;
        this.variableName = variableName;
    }

    @Override
    public Inst execute(Alice alice) {
        return getValue(alice);
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @SuppressWarnings("unchecked")
    public <T extends Inst> T getCurrentValue() {
        return (T) this.value;
    }

    @SuppressWarnings("unchecked")
    public <T extends Inst> T getValue(Alice alice) {
        switch (type) {
            case DEFINED:
                this.value = definedInst;
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

    public Inst getMemoryValue(Alice alice) {
        return alice.getMemory().get(variableName);
    }

    public Inst getRuntimeValue(Alice alice) {
        return runtime.execute(alice);
    }

    public Inst getDefinedInst() {
        return definedInst;
    }

    public String getVariableName() {
        return variableName;
    }

}