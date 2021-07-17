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

package panda.interpreter.utils;

import panda.interpreter.FrameworkController;
import panda.interpreter.architecture.PandaApplication;
import panda.interpreter.architecture.module.Imports;
import panda.interpreter.architecture.module.TypeLoader;
import panda.interpreter.architecture.packages.Package;
import panda.interpreter.architecture.packages.Packages;
import panda.interpreter.architecture.packages.PandaScript;
import panda.interpreter.architecture.statement.StandardizedFramedScope;
import panda.interpreter.architecture.statement.StaticScope;
import panda.interpreter.architecture.statement.VariableData;
import panda.interpreter.parser.Context;
import panda.interpreter.parser.ContextCreator;
import panda.interpreter.parser.PandaContextCreator;
import panda.interpreter.parser.expression.PandaExpressionParser;
import panda.interpreter.parser.pool.PandaPoolService;
import panda.interpreter.parser.stage.PandaStageManager;
import panda.interpreter.parser.stage.Phases;
import panda.interpreter.parser.stage.StageService;
import panda.interpreter.source.ClassSource;
import panda.interpreter.token.PandaLocation;
import panda.interpreter.PandaEnvironment;
import panda.interpreter.syntax.expressions.PandaExpressions;
import panda.std.Option;

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

        TypeLoader loader = environment.getTypeLoader();
        Packages packages = environment.getPackages();

        Imports imports = new Imports(packages, loader);
        imports.importModule("java", Package.DEFAULT_MODULE);
        imports.importModule("panda", Package.DEFAULT_MODULE);

        Package stubPackage = new Package("stub-package", "stub-implementation", "0.0.0", new File("stub"));
        packages.registerPackage(stubPackage);
        PandaScript script = new PandaScript(new ClassSource(stubPackage.createModule("stub-module").getModule(), PandaContextUtils.class));

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

        StandardizedFramedScope scope = new StaticScope(PandaLocation.unknownLocation(script.getModule(), "stub-context"), variablesSupplier.apply(context));
        return context.forkCreator().withScope(scope);
    }

}
