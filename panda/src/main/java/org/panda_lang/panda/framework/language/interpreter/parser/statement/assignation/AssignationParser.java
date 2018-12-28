package org.panda_lang.panda.framework.language.interpreter.parser.statement.assignation;

import org.panda_lang.panda.framework.design.architecture.statement.Statement;
import org.panda_lang.panda.framework.design.architecture.statement.StatementCell;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaPriorities;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapParserBuilder;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.UnifiedParserBootstrap;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Component;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Src;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.handlers.TokenPatternHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.interceptor.TokenPatternInterceptor;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.layer.LocalData;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.PipelineRegistry;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;

@ParserRegistration(target = PandaPipelines.SCOPE_LABEL, priority = PandaPriorities.SCOPE_ASSIGNATION_PARSER)
public class AssignationParser extends UnifiedParserBootstrap {

    @Override
    public BootstrapParserBuilder initialize(BootstrapParserBuilder defaultBuilder) {
        return defaultBuilder
                .handler(new TokenPatternHandler())
                .interceptor(new TokenPatternInterceptor())
                .pattern("<*declaration> (=|+=|-=|`*=|/=) <assignation:reader expression> [;]");
                /*
                .pattern(PatternContentBuilder.create()

                        .variant(
                                "[mutable:[mutable] nullable:[nullable] <type>] <name:condition token {type:unknown}>",
                                "<name:reader expression include field>"
                        )
                        .optional("(=|+=|-=|`*=|/=) <assignation:reader expression>")
                        .optional(";")

                        .element("<*declaration> (=|+=|-=|`*=|/=) <assignation:reader expression> [;]")
                    .build()
                );*/
    }

    @Autowired
    public void parse(ParserData data, LocalData local, @Component PipelineRegistry registry, @Src("*declaration") Tokens declaration, @Src("assignation") Tokens assignation) throws Throwable {
        //System.out.println(name.getFirst().getLine() + "V: " + TokensUtils.asString(type) + " " + TokensUtils.asString(name) + " = " + TokensUtils.asString(assignation));
        ParserData delegatedData = data.fork();
        delegatedData.setComponent(AssignationComponents.SCOPE, delegatedData.getComponent(PandaComponents.SCOPE_LINKER).getCurrentScope());

        Expression assignationExpression = delegatedData.getComponent(PandaComponents.EXPRESSION).parse(delegatedData, assignation);
        AssignationSubparser subparser = registry.getPipeline(PandaPipelines.ASSIGNER).handle(data, new PandaSourceStream(declaration));

        StatementCell cell = delegatedData.getComponent(PandaComponents.CONTAINER).reserveCell();
        Statement statement = subparser.parseVariable(delegatedData, declaration, assignationExpression);

        if (statement == null) {
            throw new PandaParserFailure("Cannot parse assignment", delegatedData);
        }

        cell.setStatement(statement);
    }

    /*
    @Autowired(order = 1, delegation = Delegation.IMMEDIATELY)
    public void parse(ParserData data, LocalData local, ExtractorResult result, @Local Scope scope, @Src("type") Tokens type, @Src("name") Tokens name) {
        if (TokensUtils.isEmpty(type)) {
            return;
        }

        PandaScript script = data.getComponent(PandaComponents.PANDA_SCRIPT);
        boolean mutable = result.hasIdentifier("mutable");
        boolean nullable = result.hasIdentifier("nullable");

        VariableInitializer initializer = new VariableInitializer();
        local.allocateInstance(initializer.createVariable(script.getModuleLoader(), scope, mutable, nullable, type.asString(), name.asString()));
    }

    @Autowired(order = 2, delegation = Delegation.NEXT_AFTER)
    @AutowiredParameters(skip = 3, value = {
            @Type(with = Local.class),
            @Type(with = Local.class),
            @Type(with = Src.class, value = "type"),
            @Type(with = Src.class, value = "name"),
            @Type(with = Src.class, value = "assignation")
    })
    public void parse(ParserData data, LocalData local, ExtractorResult result, Scope scope, Container container, Tokens properties, Tokens name, Tokens assignation) {
        if (!TokensUtils.isEmpty(properties)) {
            return;
        }

        if (name.size() > 1) {
            Expression instanceExpression = data.getComponent(PandaComponents.EXPRESSION).parse(data, name.subSource(0, name.size() - 2));

            if (instanceExpression == null) {
                throw new PandaParserException("Cannot parse variable reference: " + name);
            }

            String fieldName = name.getLast().getTokenValue();
            PrototypeField field = instanceExpression.getReturnType().getFields().getField(fieldName);

            if (field == null) {
                throw new PandaParserException("Field " + fieldName + " does not exist");
            }

            local.allocateInstance(instanceExpression);
            local.allocateInstance(field);
            return;
        }

        Variable variable = scope.getVariable(name.asString());

        if (variable != null) {
            local.allocateInstance(variable);
            return;
        }

        ClassPrototype prototype = data.getComponent(ClassPrototypeComponents.CLASS_PROTOTYPE);

        if (prototype == null) {
            throw new PandaParserFailure("Cannot get field from non-prototype scope", data);
        }

        Expression instanceExpression = new PandaExpression(prototype, new ThisExpressionCallback());
        PrototypeField field = prototype.getFields().getField(name.asString());

        if (field != null) {
            local.allocateInstance(instanceExpression);
            local.allocateInstance(field);
            return;
        }

        throw new PandaParserFailure("Unknown variable: " + name.asString(), BootstrapUtils.updated(data));
    }

    @Autowired(order = 3, delegation = Delegation.NEXT_AFTER)
    @AutowiredParameters(skip = 2, value = {
            @Type(with = Local.class),
            @Type(with = Local.class),
            @Type(with = Local.class),
            @Type(with = Local.class),
            @Type(with = Src.class, value = "assignation")
    })
    public void parse(ParserData data, LocalData localData, StatementCell cell, Expression instanceExpression, Variable variable, Scope scope, @Nullable Tokens assignation) {
        if (TokensUtils.isEmpty(assignation)) {
            return;
        }

        Expression assignationExpression = data.getComponent(PandaComponents.EXPRESSION).parse(data, assignation);

        if (instanceExpression == null) {
            if (!assignationExpression.isNull() && !assignationExpression.getReturnType().isAssociatedWith(variable.getType())) {
                throw new PandaParserFailure(data,
                        "Return type is incompatible with the type of variable (",
                        "var: ", variable.getType().getClassName(),
                        "; expr: ", assignationExpression.getReturnType().getClassName(),
                        ")");
            }

            Statement accessor = new VariableAccessor(variable, scope.indexOf(variable), assignationExpression);
            cell.setStatement(accessor);
            return;
        }

        String fieldName = variable.getName();
        ClassPrototype type = instanceExpression.getReturnType();
        PrototypeField field = type.getFields().getField(fieldName);

        if (field == null) {
            throw new PandaParserException("Field '" + fieldName + "' does not belong to " + type.getClassName());
        }

        if (!field.getType().equals(assignationExpression.getReturnType())) {
            throw new PandaParserFailure("Return type is incompatible with the type of variable", data);
        }

        Statement accessor = new FieldAccessor(instanceExpression, field, assignationExpression);
        cell.setStatement(accessor);
    }
    */

}