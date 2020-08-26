package org.panda_lang.language.architecture.type.member.constructor;

import org.panda_lang.language.architecture.type.member.MemberFrameImpl;
import org.panda_lang.language.architecture.type.TypeInstance;
import org.panda_lang.language.architecture.type.member.parameter.ParameterUtils;
import org.panda_lang.language.runtime.ProcessStack;

public final class ConstructorFrame extends MemberFrameImpl<ConstructorScope> {

    public ConstructorFrame(ConstructorScope scope, TypeInstance instance) {
        super(scope, instance.__panda__get_frame());
    }

    public TypeInstance initialize(ProcessStack stack, TypeInstance typeInstance, Object[] parameters) throws Exception {
        ParameterUtils.assignValues(this, parameters);
        stack.callFrame(typeInstance, this);
        return typeInstance;
    }

}
