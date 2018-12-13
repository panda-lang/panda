package org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeField;
import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.architecture.value.Variable;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.linker.ScopeLinker;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.old.callbacks.instance.ThisExpressionCallback;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.old.callbacks.memory.FieldExpressionCallback;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.old.callbacks.memory.VariableExpressionCallback;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.ExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.ExpressionSubparser;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.number.NumberParser;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.prototype.ClassPrototypeComponents;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.statement.variable.VariableParserUtils;
import org.panda_lang.panda.framework.language.runtime.expression.PandaExpression;

class SingleExpressionParser implements ExpressionSubparser {

    @Override
    public @Nullable Tokens read(ExpressionParser main, Tokens source) {
        return SubparserUtils.readFirstOfType(source, TokenType.UNKNOWN);
    }

    @Override
    public Expression parse(ExpressionParser main, ParserData data, Tokens source) {
        TokenRepresentation token = source.get(0);

        NumberParser numberParser = new NumberParser();
        Value numericValue = numberParser.parse(data, source);

        if (numericValue != null) {
            return new PandaExpression(numericValue);
        }

        ScopeLinker scopeLinker = data.getComponent(PandaComponents.SCOPE_LINKER);
        Scope scope = scopeLinker.getCurrentScope();
        Variable variable = VariableParserUtils.getVariable(scope, token.getTokenValue());

        if (variable != null) {
            int memoryIndex = VariableParserUtils.indexOf(scope, variable);
            return new PandaExpression(variable.getType(), new VariableExpressionCallback(memoryIndex));
        }

        ClassPrototype prototype = data.getComponent(ClassPrototypeComponents.CLASS_PROTOTYPE);

        if (prototype != null) {
            PrototypeField field = prototype.getFields().getField(token.getTokenValue());

            if (field != null) {
                int memoryIndex = prototype.getFields().getIndexOfField(field);
                return new PandaExpression(field.getType(), new FieldExpressionCallback(ThisExpressionCallback.asExpression(prototype), field, memoryIndex));
            }
        }

        return null;
    }

    @Override
    public double getPriority() {
        return DefaultSubparsers.Priorities.SINGLE;
    }

    @Override
    public String getName() {
        return DefaultSubparsers.Names.SINGLE;
    }

}
