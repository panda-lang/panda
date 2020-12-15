package org.panda_lang.language.architecture.type.member;

import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.language.architecture.type.Typed;
import org.panda_lang.language.architecture.type.member.parameter.PropertyParameter;
import org.panda_lang.language.architecture.type.signature.Signature;
import org.panda_lang.language.runtime.ProcessStack;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractParametrizedMember<E extends Member> extends AbstractMember<E> implements ParametrizedMember {

    private final List<? extends PropertyParameter> parameters;
    private final MemberInvoker<E, Object> callback;

    protected AbstractParametrizedMember(PandaParametrizedExecutableBuilder<E, ?> builder) {
        super(builder);

        this.parameters = builder.parameters;
        this.callback = builder.callback;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object invoke(ProcessStack stack, Object instance, Object... arguments) throws Exception {
        return callback.invoke((E) this, stack, instance, arguments);
    }

    @Override
    public boolean isInvokableBy(List<? extends Typed> arguments) {
        if (getParameters().size() != arguments.size()) {
            return false;
        }

        for (int index = 0; index < parameters.size(); index++) {
            PropertyParameter parameter = parameters.get(index);
            Type type = arguments.get(index).toType();

            if (!parameter.getKnownType().isAssignableFrom(type)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public List<Signature> getParameterSignatures() {
        return getParameters().stream()
                .map(PropertyParameter::getSignature)
                .collect(Collectors.toList());
    }

    @Override
    public List<? extends PropertyParameter> getParameters() {
        return parameters;
    }

}
