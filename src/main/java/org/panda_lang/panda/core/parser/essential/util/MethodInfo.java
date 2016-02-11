package org.panda_lang.panda.core.parser.essential.util;

import org.panda_lang.panda.core.syntax.Factor;
import org.panda_lang.panda.core.syntax.Vial;

public class MethodInfo {

    private final String methodName;
    private final Factor[] factors;
    private Factor instance;
    private Vial vial;

    public MethodInfo(String methodName, Factor... factors) {
        this.methodName = methodName;
        this.factors = factors;
    }

    public MethodInfo(Factor instance, String methodName, Factor... factors) {
        this.instance = instance;
        this.methodName = methodName;
        this.factors = factors;
    }

    public MethodInfo(Vial vial, String methodName, Factor... factors) {
        this.vial = vial;
        this.methodName = methodName;
        this.factors = factors;
    }

    public Factor[] getFactors() {
        return factors;
    }

    public String getMethodName() {
        return methodName;
    }

    public Vial getVial() {
        return vial;
    }

    public Factor getInstance() {
        return instance;
    }

    public boolean isStatic() {
        return vial != null;
    }

    public boolean isExternal() {
        return instance != null || vial != null;
    }

}
