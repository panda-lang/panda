package org.panda_lang.panda.framework.design.resource.prototypes.model.loader;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.runtime.ExecutableBranch;

class ModelLoaderUtils {

    protected static final ClassPool POOL = ClassPool.getDefault();

    protected static final CtClass OBJECT = getCtClass(Object.class);
    protected static CtClass VALUE = getCtClass(Value.class);
    protected static CtClass VALUE_ARRAY = getCtClass(Value[].class);

    protected static CtClass EXECUTABLE_BRANCH = getCtClass(ExecutableBranch.class);
    protected static final CtClass CPM_METHOD_CALLBACK = getCtClass(CPMMethodCallback.class);
    protected static CtClass[] IMPLEMENTATION = new CtClass[]{ EXECUTABLE_BRANCH, OBJECT, VALUE_ARRAY };

    private static CtClass getCtClass(Class<?> clazz) {
        try {
            return POOL.getCtClass(clazz.getName());
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
