package org.panda_lang.panda.composition.classes;

import org.panda_lang.framework.runtime.ExecutableBridge;
import org.panda_lang.framework.structure.Executable;
import org.panda_lang.framework.structure.Value;
import org.panda_lang.panda.implementation.element.method.Method;
import org.panda_lang.panda.implementation.element.method.MethodVisibility;
import org.panda_lang.panda.implementation.element.struct.ClassPrototype;

import java.util.Arrays;

public class SystemPrototype {

    static {
        ClassPrototype classPrototype = ClassPrototype.builder()
                .className("System")
                .method(new Method("print", new Executable() {
                    @Override
                    public void execute(ExecutableBridge executionInfo) {
                        Value[] values = executionInfo.getParameters();
                        System.out.println(Arrays.toString(values));
                    }
                }, true, MethodVisibility.PUBLIC))
                .build();
    }

}
