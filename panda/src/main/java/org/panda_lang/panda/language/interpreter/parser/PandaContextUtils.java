/*
 * Copyright (c) 2015-2020 Dzikoysk
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

package org.panda_lang.panda.language.interpreter.parser;

import org.panda_lang.framework.FrameworkController;
import org.panda_lang.framework.design.architecture.module.Imports;
import org.panda_lang.framework.design.architecture.module.ModulePath;
import org.panda_lang.framework.design.architecture.module.TypeLoader;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.language.architecture.module.PandaImports;
import org.panda_lang.framework.language.architecture.module.PandaModule;
import org.panda_lang.framework.language.architecture.module.PandaModulePath;
import org.panda_lang.framework.language.architecture.module.PandaTypeLoader;
import org.panda_lang.framework.language.interpreter.parser.PandaContext;
import org.panda_lang.framework.language.interpreter.parser.expression.PandaExpressionParser;
import org.panda_lang.framework.language.interpreter.parser.generation.GenerationCycles;
import org.panda_lang.framework.language.interpreter.parser.generation.PandaGeneration;
import org.panda_lang.panda.language.architecture.PandaEnvironment;
import org.panda_lang.panda.language.architecture.PandaScript;
import org.panda_lang.panda.language.resource.ResourcesLoader;
import org.panda_lang.panda.language.resource.syntax.expressions.PandaExpressionUtils;

import java.io.File;

public final class PandaContextUtils {

    private PandaContextUtils() { }

    public static Context createStubContext(FrameworkController frameworkController) {
        PandaEnvironment environment = new PandaEnvironment(frameworkController, new File("."));
        environment.initialize();

        Context context = new PandaContext()
                .withComponent(Components.CONTROLLER, frameworkController)
                .withComponent(Components.ENVIRONMENT, environment)
                .withComponent(Components.EXPRESSION, new PandaExpressionParser(PandaExpressionUtils.collectSubparsers()));

        ModulePath path = new PandaModulePath();
        TypeLoader loader = new PandaTypeLoader();

        ResourcesLoader resourcesLoader = new ResourcesLoader();
        resourcesLoader.load(path, loader);

        Imports imports = new PandaImports(path, loader);
        imports.importModule("java");
        imports.importModule("panda");

        PandaScript script = new PandaScript("stub-script");
        script.setModule(new PandaModule("stub-module"));

        return context
                .withComponent(Components.SCRIPT, script)
                .withComponent(Components.TYPE_LOADER, loader)
                .withComponent(Components.IMPORTS, imports)
                .withComponent(Components.PIPELINE, frameworkController.getResources().getPipelinePath())
                .withComponent(Components.GENERATION, new PandaGeneration().initialize(GenerationCycles.getValues()));
    }

}
