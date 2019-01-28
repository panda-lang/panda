/*
 * Copyright (c) 2015-2019 Dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.panda_lang.panda.framework.language.resource.parsers.prototype.field;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.PandaScript;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.framework.design.architecture.prototype.field.FieldVisibility;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PandaPrototypeField;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeField;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaPriorities;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapParserBuilder;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.UnifiedParserBootstrap;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Local;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Src;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.layer.LocalData;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.PatternContentBuilder;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.extractor.ExtractorResult;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.interpreter.token.TokensUtils;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.GenerationTypes;
import org.panda_lang.panda.framework.language.resource.parsers.prototype.ClassPrototypeComponents;

import java.util.Optional;

@ParserRegistration(target = PandaPipelines.PROTOTYPE_LABEL, priority = PandaPriorities.PROTOTYPE_FIELD_PARSER)
public class FieldParser extends UnifiedParserBootstrap {

    @Override
    protected BootstrapParserBuilder initialize(ParserData data, BootstrapParserBuilder defaultBuilder) {
        return defaultBuilder
                .pattern(PatternContentBuilder.create()
                        .element("(p:public|l:local|h:hidden)")
                        .optional("static", "static")
                        .optional("mutable", "mutable")
                        .optional("nullable", "nullable")
                        .element("<type:reader type> <name:condition token {type:unknown}>")
                        .optional("= <assignation:reader expression>")
                        .optional(";")
                        .build()
                );
    }

    @Autowired(order = 1, type = GenerationTypes.TYPES_LABEL)
    public void parse(ParserData data, LocalData local, ExtractorResult result,  @Src("type") Tokens type, @Src("name") Tokens name) {
        FieldVisibility visibility = FieldVisibility.LOCAL;
        visibility = result.hasIdentifier("p") ? FieldVisibility.PUBLIC : visibility;
        visibility = result.hasIdentifier("h") ? FieldVisibility.HIDDEN : visibility;

        boolean isStatic = result.hasIdentifier("static");
        boolean mutable = result.hasIdentifier("mutable");
        boolean nullable = result.hasIdentifier("nullable");

        PandaScript script = data.getComponent(PandaComponents.PANDA_SCRIPT);
        Optional<ClassPrototypeReference> returnType = script.getModuleLoader().forClass(type.asString());

        if (!returnType.isPresent()) {
            throw new PandaParserFailure("Unknown type", data, type);
        }

        ClassPrototype prototype = data.getComponent(ClassPrototypeComponents.CLASS_PROTOTYPE);
        int fieldIndex = prototype.getFields().getAmountOfFields();

        PrototypeField field = PandaPrototypeField.builder()
                .fieldIndex(fieldIndex)
                .type(returnType.get())
                .name(name.asString())
                .visibility(visibility)
                .isStatic(isStatic)
                .mutable(mutable)
                .nullable(nullable)
                .build();

        prototype.getFields().addField(field);
        local.allocateInstance(field);
    }

    @Autowired(order = 2)
    public void parseAssignation(ParserData data, @Local PrototypeField field, @Src("assignation") @Nullable Tokens assignation) {
        if (TokensUtils.isEmpty(assignation)) {
            return;
        }

        Expression expressionValue = data.getComponent(PandaComponents.EXPRESSION).parse(data, assignation);

        if (expressionValue == null) {
            throw new PandaParserFailure("Cannot parse expression '" + assignation + "'", data, assignation);
        }

        field.setDefaultValue(expressionValue);
    }

}
