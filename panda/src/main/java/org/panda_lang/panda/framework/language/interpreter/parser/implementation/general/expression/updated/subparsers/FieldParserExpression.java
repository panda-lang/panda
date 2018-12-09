package org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeField;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.PandaScript;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.old.ExpressionPatterns;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.old.callbacks.memory.FieldExpressionCallback;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.ExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.ExpressionSubparser;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.number.NumberUtils;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaTokenReader;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;
import org.panda_lang.panda.framework.language.runtime.expression.PandaExpression;
import org.panda_lang.panda.utilities.commons.ArrayUtils;

import java.util.List;

class FieldParserExpression implements ExpressionSubparser {

    private static final Token[] FIELD_SEPARATORS = ArrayUtils.of(Separators.PERIOD);

    @Override
    public @Nullable Tokens read(ExpressionParser main, Tokens source) {
        Tokens selected = SubparserUtils.readDotted(main, source, FIELD_SEPARATORS, distributor -> distributor.next() != null);

        if (selected == null) {
            return null;
        }

        if (selected.getLast().getToken().getType() != TokenType.UNKNOWN) {
            return null;
        }

        return selected;
    }

    @Override
    public Expression parse(ExpressionParser main, ParserData data, Tokens source) {
        List<Tokens> fieldMatches = ExpressionPatterns.FIELD_PATTERN.match(new PandaTokenReader(source));

        if (fieldMatches != null && fieldMatches.size() == 2 && !NumberUtils.startsWithNumber(fieldMatches.get(1))) {
            PandaScript script = data.getComponent(PandaComponents.PANDA_SCRIPT);

            Tokens instanceSource = fieldMatches.get(0);
            ClassPrototype instanceType = null;
            Expression fieldLocationExpression = null;

            if (instanceSource.size() == 1) {
                ModuleLoader moduleLoader = data.getComponent(PandaComponents.PANDA_SCRIPT).getModuleLoader();
                instanceType = moduleLoader.forClass(fieldMatches.get(0).asString());
            }

            if (instanceType == null) {
                fieldLocationExpression = main.parse(data, fieldMatches.get(0));
                instanceType = fieldLocationExpression.getReturnType();
            }

            if (instanceType == null) {
                throw new PandaParserFailure("Unknown instance source", data);
            }

            String instanceFieldName = fieldMatches.get(1).asString();
            PrototypeField instanceField = instanceType.getFields().getField(instanceFieldName);

            if (instanceField == null) {
                throw new PandaParserFailure("Class " + instanceType.getClassName() + " does not contain field " + instanceFieldName, data);
            }

            int memoryIndex = instanceType.getFields().getIndexOfField(instanceField);
            return new PandaExpression(instanceField.getType(), new FieldExpressionCallback(fieldLocationExpression, instanceField, memoryIndex));
        }

        return null;
    }

    @Override
    public double getPriority() {
        return DefaultSubparserPriorities.DYNAMIC;
    }

}
