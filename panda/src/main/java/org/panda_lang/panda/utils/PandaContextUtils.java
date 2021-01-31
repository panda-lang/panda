/*
 * Copyright (c) 2021 dzikoysk
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

package org.panda_lang.panda.utils;

import org.panda_lang.framework.FrameworkController;
import org.panda_lang.framework.architecture.PandaScript;
import org.panda_lang.framework.architecture.module.Imports;
import org.panda_lang.framework.architecture.module.ModulePath;
import org.panda_lang.framework.architecture.module.PandaModule;
import org.panda_lang.framework.architecture.module.PandaModulePath;
import org.panda_lang.framework.architecture.module.PandaTypeLoader;
import org.panda_lang.framework.architecture.module.TypeLoader;
import org.panda_lang.framework.architecture.statement.StandardizedFramedScope;
import org.panda_lang.framework.architecture.statement.StaticScope;
import org.panda_lang.framework.architecture.statement.VariableData;
import org.panda_lang.framework.architecture.type.generator.TypeGenerator;
import org.panda_lang.framework.interpreter.parser.Context;
import org.panda_lang.framework.interpreter.parser.ContextCreator;
import org.panda_lang.framework.interpreter.parser.PandaContextCreator;
import org.panda_lang.framework.interpreter.parser.expression.PandaExpressionParser;
import org.panda_lang.framework.interpreter.parser.pool.PandaPoolService;
import org.panda_lang.framework.interpreter.parser.stage.PandaStageManager;
import org.panda_lang.framework.interpreter.parser.stage.Phases;
import org.panda_lang.framework.interpreter.parser.stage.StageService;
import org.panda_lang.framework.interpreter.source.PandaSourceService;
import org.panda_lang.framework.interpreter.token.PandaLocation;
import org.panda_lang.framework.architecture.PandaApplication;
import org.panda_lang.panda.language.PandaEnvironment;
import org.panda_lang.panda.language.std.StdLoader;
import org.panda_lang.panda.language.syntax.expressions.PandaExpressions;
import org.panda_lang.utilities.commons.function.Option;

import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

public final class PandaContextUtils {

    private PandaContextUtils() { }

    public static ContextCreator<?> createStubContext(FrameworkController controller) {
        return createStubContext(controller, context -> Collections.emptyMap());
    }

    public static ContextCreator<?> createStubContext(FrameworkController controller, Function<Context<?>, Map<VariableData, Object>> variablesSupplier) {
        PandaEnvironment environment = new PandaEnvironment(controller, new File("./"));
        environment.initialize();

        ModulePath path = new PandaModulePath(new PandaSourceService());
        TypeLoader loader = new PandaTypeLoader(path);

        StdLoader stdLoader = new StdLoader();
        stdLoader.load(path, new TypeGenerator(controller), loader);

        Imports imports = new Imports(loader);
        imports.importModule("java");
        imports.importModule("panda");

        PandaScript script = new PandaScript("stub-script");
        script.getModule().complete(new PandaModule("stub-module"));

        Context<?> context = new PandaContextCreator<>(
                Option.none(),
                environment,
                new StageService(new PandaStageManager().initialize(Phases.getValues())),
                new PandaPoolService(),
                new PandaExpressionParser(PandaExpressions.createExpressionSubparsers()),
                loader,
                new PandaApplication(environment)
        )
        .withImports(imports)
        .withScript(script)
        .toContext();

        StandardizedFramedScope scope = new StaticScope(PandaLocation.unknownLocation("stub-context"), variablesSupplier.apply(context));
        return context.forkCreator().withScope(scope);
    }

}
