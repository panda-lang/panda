package org.panda_lang.panda.core.statement;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Inst;

public class Runtime implements Executable {

    private RuntimeValue instance;
    private Executable executable;
    private RuntimeValue[] runtimeValues;
    private Equality equality;
    private Method method;
    private Math math;

    public Runtime() {
    }

    public Runtime(Equality equality) {
        this.equality = equality;
    }

    public Runtime(Method method) {
        this.method = method;
    }

    public Runtime(Math math) {
        this.math = math;
    }

    public Runtime(RuntimeValue instance, Executable executable, RuntimeValue[] runtimeValues) {
        this.instance = instance;
        this.executable = executable;
        this.runtimeValues = runtimeValues;
    }

    @Override
    public Inst execute(Alice alice) {
        alice.setInstance(instance);
        alice.setRuntimeValues(runtimeValues);
        if (method != null) {
            return method.execute(alice);
        }
        else if (math != null) {
            return math.execute(alice);
        }
        else if (equality != null) {
            return equality.execute(alice);
        }
        else if (executable != null) {
            return executable.execute(alice);
        }
        return null;
    }

    public void setInstance(RuntimeValue instance) {
        this.instance = instance;
    }

    public void setExecutable(Executable executable) {
        this.executable = executable;
    }

    public void setRuntimeValues(RuntimeValue[] runtimeValues) {
        this.runtimeValues = runtimeValues;
    }

    public void setEquality(Equality equality) {
        this.equality = equality;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public void setMath(Math math) {
        this.math = math;
    }

}
