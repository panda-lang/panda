package org.panda_lang.panda.framework.language.interpreter.parser.implementation.statement.variable;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeField;
import org.panda_lang.panda.framework.design.architecture.statement.Container;
import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.architecture.statement.Statement;
import org.panda_lang.panda.framework.design.architecture.statement.StatementCell;
import org.panda_lang.panda.framework.design.architecture.value.Variable;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.interpreter.token.TokensUtils;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.PandaScript;
import org.panda_lang.panda.framework.language.architecture.dynamic.accessor.FieldAccessor;
import org.panda_lang.panda.framework.language.architecture.dynamic.accessor.VariableAccessor;
import org.panda_lang.panda.framework.language.architecture.value.PandaVariable;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPriorities;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.BootstrapParser;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.AutowiredParameters;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Local;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Src;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Type;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.handlers.TokenPatternHandler;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.interceptor.TokenPatternInterceptor;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.layer.Delegation;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.layer.LocalData;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.old.OldExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.ExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.subparsers.DefaultSubparsers;
import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.PatternContentBuilder;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor.ExtractorResult;
import org.panda_lang.panda.utilities.commons.ObjectUtils;

@ParserRegistration(target = PandaPipelines.SCOPE, priority = PandaPriorities.SCOPE_VARIABLE_PARSER)
public class NewVariableParser extends BootstrapParser {

    {
        parserBuilder = builder()
                .handler(new TokenPatternHandler())
                .interceptor(new TokenPatternInterceptor())
                .pattern(PatternContentBuilder.create()
                        .variant(
                                "[mutable:[mutable] nullable:[nullable] <type>] <name:condition token {type:unknown}>",
                                "<name:reader expression include field>"
                        )
                        .optional("= <assignation:reader expression>")
                        .optional(";")
                    .build()
                );

        System.out.println(PatternContentBuilder.create()
                .variant(
                        "[mutable:[mutable] nullable:[nullable] <type>] <name:condition token {type:unknown}>",
                        "<name:reader expression include field>"
                )
                .optional("= <assignation:reader expression>")
                .optional(";")
                .build());
    }

    @Autowired
    public void parse(ParserData data, LocalData local, @Src("type") @Nullable Tokens type, @Src("name") Tokens name, @Src("assignation") @Nullable Tokens assignation) {
        System.out.println(name.getFirst().getLine() + "V: " + TokensUtils.asString(type) + " " + TokensUtils.asString(name) + " = " + TokensUtils.asString(assignation));

        if (ObjectUtils.areNull(type, assignation)) {
            throw new PandaParserFailure("Cannot parse variable statement", data).withUpdatedSource();
        }

        local.allocateInstance(data.getComponent(PandaComponents.CONTAINER));
        local.allocateInstance(data.getComponent(PandaComponents.CONTAINER).reserveCell());
        local.allocateInstance(data.getComponent(PandaComponents.SCOPE_LINKER).getCurrentScope());
    }

    @Autowired(order = 1, delegation = Delegation.NEXT_DEFAULT)
    @AutowiredParameters(skip = 3, value = {
            @Type(with = Local.class),
            @Type(with = Local.class),
            @Type(with = Src.class, value = "properties"),
            @Type(with = Src.class, value = "name"),
            @Type(with = Src.class, value = "assignation")
    })
    public void parse(ParserData data, LocalData local, ExtractorResult result, Scope scope, Container container, Tokens properties, Tokens name, Tokens assignation) {
        if (TokensUtils.isEmpty(properties)) {
            if (name.size() == 1) {
                Variable variable = VariableParserUtils.getVariable(scope, name.asString());

                if (variable == null) {
                    throw new PandaParserFailure("Unknown variable: " + name.asString(), data);
                }

                local.allocateInstance(variable);
                return;
            }

            OldExpressionParser expressionParser = new OldExpressionParser();
            Expression instanceExpression = expressionParser.parse(data, name.subSource(0, name.size() - 2), true);

            if (instanceExpression != null) {
                ClassPrototype prototype = instanceExpression.getReturnType();
                PrototypeField field = prototype.getFields().getField(name.getLast().getTokenValue());

                // local false
                if (field != null) {
                    local.allocateInstance(instanceExpression);
                    local.allocateInstance(field);
                    return;
                }
            }

            throw new PandaParserException("Cannot parse variable reference: " + name);
        }

        boolean mutable = result.hasIdentifier("mutable");
        boolean nullable = result.hasIdentifier("nullable");

        PandaScript script = data.getComponent(PandaComponents.PANDA_SCRIPT);
        ModuleLoader moduleLoader = script.getModuleLoader();
        ClassPrototype type = moduleLoader.forClass(properties.getLast().getTokenValue());

        Variable variable = local.allocateInstance(new PandaVariable(type, name.asString(), 0, mutable, nullable));
        scope.addVariable(variable);
    }

    @Autowired(order = 2, delegation = Delegation.NEXT_DEFAULT)
    @AutowiredParameters(skip = 2, value = {
            @Type(with = Local.class),
            @Type(with = Local.class),
            @Type(with = Local.class),
            @Type(with = Src.class, value = "assignation")
    })
    public void parse(ParserData data, LocalData localData, StatementCell cell, Variable variable, Scope scope, @Nullable Tokens assignation) {
        if (TokensUtils.isEmpty(assignation)) {
            return;
        }

        ExpressionParser parser = new ExpressionParser(DefaultSubparsers.Instances.getDefaultSubparsers());
        Expression expressionValue = parser.parse(data, assignation);
        Statement accessor;

        if (variable instanceof PrototypeField) {
            if (!expressionValue.isNull() && !expressionValue.getReturnType().isAssociatedWith(variable.getType())) {
                throw new PandaParserFailure("Return type is incompatible with the type of variable"
                        + " (var: " + variable.getType().getClassName() + "; expr: " + expressionValue.getReturnType().getClassName() + ")", data);
            }

            accessor = new VariableAccessor(variable, VariableParserUtils.indexOf(scope, variable), expressionValue);
        }
        else {
            Expression instanceExpression = localData.getValue(Expression.class);
            String fieldName = variable.getName();

            ClassPrototype type = instanceExpression.getReturnType();
            PrototypeField field = type.getFields().getField(fieldName);

            if (field == null) {
                throw new PandaParserException("Field '" + fieldName + "' does not belong to " + type.getClassName());
            }

            if (!field.getType().equals(expressionValue.getReturnType())) {
                throw new PandaParserFailure("Return type is incompatible with the type of variable", data);
            }

            accessor = new FieldAccessor(instanceExpression, field, expressionValue);
        }

        cell.setStatement(accessor);
    }

}