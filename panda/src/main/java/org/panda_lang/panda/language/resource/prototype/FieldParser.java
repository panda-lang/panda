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

package org.panda_lang.panda.language.resource.prototype;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.prototype.PrototypeReference;
import org.panda_lang.framework.design.architecture.prototype.PrototypeVisibility;
import org.panda_lang.framework.design.architecture.prototype.PrototypeField;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.ParserBootstrap;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Inter;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Local;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Src;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.data.LocalData;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Pipelines;
import org.panda_lang.framework.design.interpreter.pattern.descriptive.DescriptiveContentBuilder;
import org.panda_lang.framework.design.interpreter.pattern.descriptive.extractor.ExtractorResult;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.panda.language.interpreter.parser.loader.Registrable;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.language.architecture.module.ModuleLoaderUtils;
import org.panda_lang.framework.language.architecture.prototype.PandaPrototypeField;
import org.panda_lang.panda.language.interpreter.parser.PandaPriorities;
import org.panda_lang.framework.language.interpreter.parser.generation.GenerationCycles;
import org.panda_lang.framework.design.architecture.prototype.PrototypeComponents;
import org.panda_lang.framework.language.resource.syntax.keyword.Keywords;

@Registrable(pipeline = Pipelines.PROTOTYPE_LABEL, priority = PandaPriorities.PROTOTYPE_FIELD)
public class FieldParser extends ParserBootstrap {

    @Override
    protected BootstrapInitializer initialize(Context context, BootstrapInitializer initializer) {
        return initializer
                .pattern(DescriptiveContentBuilder.create()
                        .element("(p:public|l:local|h:hidden)")
                        .optional(Keywords.STATIC.getValue(), Keywords.STATIC.getValue())
                        .optional(Keywords.MUT.getValue(), Keywords.MUT.getValue())
                        .optional(Keywords.NIL.getValue(), Keywords.NIL.getValue())
                        .element("<type:reader type> <name:condition token {type:unknown}>")
                        .optional("= <assignation:reader expression>")
                        .optional(";")
                        .build()
                );
    }

    @Autowired(order = 1, cycle = GenerationCycles.TYPES_LABEL)
    void parse(Context context, LocalData local, @Inter ExtractorResult result, @Src("type") Snippet type, @Src("name") Snippet name) {
        PrototypeReference returnType = ModuleLoaderUtils.getReferenceOrThrow(context, type.asSource(), type);

        PrototypeVisibility visibility = PrototypeVisibility.LOCAL;
        visibility = result.hasIdentifier("p") ? PrototypeVisibility.PUBLIC : visibility;
        visibility = result.hasIdentifier("h") ? PrototypeVisibility.HIDDEN : visibility;

        boolean isStatic = result.hasIdentifier(Keywords.STATIC.getValue());
        boolean mutable = result.hasIdentifier(Keywords.MUT.getValue());
        boolean nillable = result.hasIdentifier(Keywords.NIL.getValue());

        Prototype prototype = context.getComponent(PrototypeComponents.CLASS_PROTOTYPE);
        int fieldIndex = prototype.getFields().getProperties().size();

        PrototypeField field = PandaPrototypeField.builder()
                .prototype(prototype.getReference())
                .returnType(returnType)
                .fieldIndex(fieldIndex)
                .name(name.asSource())
                .visibility(visibility)
                .isStatic(isStatic)
                .mutable(mutable)
                .nillable(nillable)
                .build();

        prototype.getFields().declare(field);
        local.allocated(field);
    }

    @Autowired(order = 2)
    void parseAssignation(@Local PrototypeField field, @Src("assignation") @Nullable Expression assignationValue) {
        if (assignationValue == null) {
            //throw new PandaParserFailure("Cannot parse expression '" + assignationValue + "'", context, name);
            return;
        }

        field.setDefaultValue(assignationValue);
    }

}
