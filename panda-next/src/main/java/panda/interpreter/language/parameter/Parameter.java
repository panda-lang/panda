package panda.interpreter.language.parameter;

import org.jetbrains.annotations.Nullable;
import panda.interpreter.language.type.Signature;
import panda.interpreter.language.expression.ExpressionDeclaration;

public class Parameter {

    private final String name;
    private final Signature type;
    private final @Nullable ExpressionDeclaration defaultValue;

    public Parameter(String name, @Nullable Signature type, @Nullable ExpressionDeclaration defaultValue) {
        this.name = name;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    public ExpressionDeclaration getDefaultValue() {
        return defaultValue;
    }

    public Signature getType() {
        return type;
    }

    public String getName() {
        return name;
    }

}
