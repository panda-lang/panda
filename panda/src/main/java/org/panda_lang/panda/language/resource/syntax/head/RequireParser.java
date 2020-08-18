/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.panda.language.resource.syntax.head;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.architecture.Environment;
import org.panda_lang.language.architecture.module.Imports;
import org.panda_lang.language.architecture.module.Module;
import org.panda_lang.language.interpreter.parser.Components;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.language.interpreter.parser.Parser;
import org.panda_lang.language.interpreter.parser.pipeline.PipelineComponent;
import org.panda_lang.language.interpreter.parser.pipeline.Pipelines;
import org.panda_lang.language.interpreter.parser.stage.Stages;
import org.panda_lang.language.interpreter.pattern.functional.elements.QualifierElement;
import org.panda_lang.language.interpreter.pattern.functional.elements.WildcardElement;
import org.panda_lang.language.interpreter.pattern.functional.verifiers.TokenTypeVerifier;
import org.panda_lang.language.interpreter.source.PandaURLSource;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.interpreter.token.Snippetable;
import org.panda_lang.language.interpreter.token.TokenInfo;
import org.panda_lang.language.interpreter.token.TokenUtils;
import org.panda_lang.language.resource.syntax.TokenTypes;
import org.panda_lang.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.interpreter.parser.autowired.AutowiredInitializer;
import org.panda_lang.panda.language.interpreter.parser.autowired.AutowiredParser;
import org.panda_lang.panda.language.interpreter.parser.autowired.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.autowired.annotations.Ctx;
import org.panda_lang.panda.language.interpreter.parser.autowired.annotations.Src;
import org.panda_lang.panda.language.interpreter.parser.autowired.handlers.TokenHandler;
import org.panda_lang.panda.manager.PackageManagerUtils;
import org.panda_lang.utilities.commons.ArrayUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class RequireParser extends AutowiredParser<Void> {

    @Override
    public PipelineComponent<? extends Parser>[] pipeline() {
        return ArrayUtils.of(Pipelines.HEAD);
    }

    @Override
    protected AutowiredInitializer<Void> initialize(Context context, AutowiredInitializer<Void> initializer) {
        return initializer
                .handler(new TokenHandler(Keywords.REQUIRE))
                .functional(pattern -> pattern
                        .keyword(Keywords.REQUIRE)
                        .variant("variant").consume(variant -> variant.content(
                                QualifierElement.create("required").pandaModule(),
                                WildcardElement.create("requiredFile").verify(new TokenTypeVerifier(TokenTypes.SEQUENCE))
                        )));
    }

    @Autowired(order = 1, stage = Stages.TYPES_LABEL)
    public void parse(Context context, @Ctx Imports imports, @Src("required") @Nullable Snippetable require, @Src("requiredFile") @Nullable TokenInfo requiredFile) {
        if (require != null) {
            parseModule(context, imports, require.toSnippet());
        }
        else {
            parseFile(context, imports, Objects.requireNonNull(requiredFile));
        }
    }

    private Module parseModule(Context context, Imports imports, Snippet require) {
        String moduleName = require.asSource();

        return context.getComponent(Components.ENVIRONMENT).getModulePath().get(moduleName)
                .peek(imports::importModule)
                .orThrow(() -> new PandaParserFailure(context, require,
                            "Unknown module " + moduleName,
                            "Make sure that the name does not have a typo and module is added to the module path"
                ));
    }

    private void parseFile(Context context, Imports imports, TokenInfo requiredFile) {
        if (!TokenUtils.hasName(requiredFile, "String")) {
            throw new PandaParserFailure(context, requiredFile, "Invalid token", "You should use string sequence to import file");
        }

        Environment environment = context.getComponent(Components.ENVIRONMENT);

        File environmentDirectory = environment.getDirectory();
        File file = new File(environmentDirectory, requiredFile.getValue() + ".panda");

        if (file.exists()) {
            context.getComponent(Components.SOURCES).addSource(PandaURLSource.fromFile(file));
            return;
        }

        file = new File(environmentDirectory, requiredFile.getValue());

        if (!file.exists()) {
            throw new PandaParserFailure(context, requiredFile, "File " + file + " does not exist", "Make sure that the path does not have a typo");
        }

        try {
            PackageManagerUtils.loadToEnvironment(environment, file);
        } catch (IOException e) {
            throw new PandaParserFailure(context, requiredFile, e.getMessage());
        }

        environment.getModulePath().get(requiredFile.getValue())
                .peek(imports::importModule)
                .onEmpty(() -> environment.getLogger().warn("Imported local package " + requiredFile.getValue() + " does not have module with the same name"));
    }

}
