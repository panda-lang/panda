package org.panda_lang.panda.framework.language.interpreter.parser.prototype.field;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.field.FieldVisibility;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeField;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaPriorities;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.UnifiedParserBootstrap;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Local;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Src;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.handlers.TokenPatternHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.interceptor.TokenPatternInterceptor;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.layer.LocalData;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.PatternContentBuilder;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.extractor.ExtractorResult;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.interpreter.token.TokensUtils;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.PandaScript;
import org.panda_lang.panda.framework.language.architecture.prototype.field.PandaPrototypeField;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.parser.general.expression.old.OldExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.pipeline.PandaTypes;
import org.panda_lang.panda.framework.language.interpreter.parser.prototype.ClassPrototypeComponents;

@ParserRegistration(target = PandaPipelines.PROTOTYPE_LABEL, priority = PandaPriorities.PROTOTYPE_FIELD_PARSER)
public class NewFieldParser extends UnifiedParserBootstrap {

    {
        super.builder()
                .handler(new TokenPatternHandler())
                .interceptor(new TokenPatternInterceptor())
                .pattern(PatternContentBuilder.create()
                        .element("<visibility>")
                        .optional("static", "static")
                        .optional("mutable", "mutable")
                        .optional("nullable", "nullable")
                        .element("<type> <name:condition token {type:unknown}>")
                        .optional("= <assignation:reader expression>")
                        .optional(";")
                        .build()
                );
    }

    @Autowired(type = PandaTypes.TYPES_LABEL)
    public void parse(ParserData data, LocalData local, ExtractorResult result) {
        FieldVisibility visibility = FieldVisibility.valueOf(result.getWildcard("visibility").asString().toUpperCase());
        String type = result.getWildcard("type").asString();
        String name = result.getWildcard("name").asString();

        boolean isStatic = result.hasIdentifier("static");
        boolean mutable = result.hasIdentifier("mutable");
        boolean nullable = result.hasIdentifier("nullable");

        PandaScript script = data.getComponent(PandaComponents.PANDA_SCRIPT);
        ClassPrototype returnType = script.getModuleLoader().forClass(type);

        ClassPrototype prototype = data.getComponent(ClassPrototypeComponents.CLASS_PROTOTYPE);
        int fieldIndex = prototype.getFields().getAmountOfFields();

        PrototypeField field = PandaPrototypeField.builder()
                .fieldIndex(fieldIndex)
                .type(returnType)
                .name(name)
                .visibility(visibility)
                .isStatic(isStatic)
                .mutable(mutable)
                .nullable(nullable)
                .build();

        prototype.getFields().addField(field);
        local.allocateInstance(field);
    }

    @Autowired(order = 1)
    public void parseAssignation(ParserData data, @Local PrototypeField field, @Src("assignation") @Nullable Tokens assignation) {
        if (TokensUtils.isEmpty(assignation)) {
            return;
        }

        OldExpressionParser expressionParser = new OldExpressionParser();
        Expression expressionValue = expressionParser.parse(data, assignation);

        if (expressionValue == null) {
            throw new PandaParserException("Cannot parse expression '" + assignation + "'");
        }

        field.setDefaultValue(expressionValue);
    }

}
