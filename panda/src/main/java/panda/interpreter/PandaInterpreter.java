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

package panda.interpreter;

import panda.interpreter.architecture.Application;
import panda.interpreter.architecture.Environment;
import panda.interpreter.architecture.PandaApplication;
import panda.interpreter.architecture.module.Imports;
import panda.interpreter.architecture.packages.Package;
import panda.interpreter.architecture.packages.PandaScript;
import panda.interpreter.architecture.packages.Script;
import panda.interpreter.lexer.Lexer;
import panda.interpreter.lexer.PandaLexer;
import panda.interpreter.parser.Context;
import panda.interpreter.parser.ContextParser;
import panda.interpreter.parser.PandaContextCreator;
import panda.interpreter.parser.PandaParserException;
import panda.interpreter.parser.expression.PandaExpressionParser;
import panda.interpreter.parser.pool.ParserPool;
import panda.interpreter.parser.pool.PoolParser;
import panda.interpreter.parser.pool.Targets;
import panda.interpreter.parser.stage.PandaStageManager;
import panda.interpreter.parser.stage.Phases;
import panda.interpreter.parser.stage.StageService;
import panda.interpreter.source.Source;
import panda.interpreter.source.SourceService;
import panda.interpreter.token.PandaSourceStream;
import panda.interpreter.token.Snippet;
import panda.interpreter.token.SourceStream;
import panda.interpreter.resource.Resources;
import panda.std.Pair;
import panda.utilities.ObjectUtils;
import panda.utilities.TimeUtils;
import panda.std.reactive.Completable;
import panda.std.Option;
import panda.std.Result;

public final class PandaInterpreter implements Interpreter {

    private final Environment environment;

    public PandaInterpreter(Environment environment) {
        this.environment = environment;
    }

    @Override
    public Result<Application, Throwable> interpret(Package packageSource) {
        long uptime = System.nanoTime();

        Resources resources = environment.getController().getResources();
        PandaApplication application = new PandaApplication(environment);

        PandaStageManager stageManager = new PandaStageManager();
        stageManager.initialize(Phases.getValues());
        StageService stageService = new StageService(stageManager);

        Lexer lexer = PandaLexer.of(environment.getController().getLanguage().getSyntax())
                .enableSections()
                .build();

        Context<Object> context = new PandaContextCreator<>(
                Option.none(),
                environment,
                stageService,
                resources.getPipelinePath(),
                resources.getExpressionSubparsers().toParser(),
                environment.getTypeLoader(),
                application
        ).toContext();

        for (ContextParser<?, ?> parser : context.getPoolService().parsers()) {
            parser.initialize(ObjectUtils.cast(context));
        }

        SourceService sources = environment.getSources();
        ParserPool<Object> headPool = context.getPoolService().getPool(Targets.HEAD);

        packageSource.forModule(sources, Package.DEFAULT_MODULE).orThrow(() -> {
            throw new PandaParserException("Missing root module");
        });

        environment.getPackages().registerPackage(packageSource);

        try {
            stageManager.launch(() -> {
                while (sources.hasUnloadedSources()) {
                    Pair<? extends Source, Completable<Script>> unloadedSource = sources.retrieve();
                    parse(lexer, application, context, headPool, unloadedSource.getFirst(), unloadedSource.getSecond());
                }
            });
        }
        catch (Throwable throwable) {
            environment.getLogger().exception(throwable);
            throwable.printStackTrace();
            return Result.error(throwable);
        }

        String parseTime = TimeUtils.toMilliseconds(System.nanoTime() - uptime);
        environment.getLogger().debug("--- Interpretation of " + packageSource.getName() + " package details ");
        environment.getLogger().debug("• Parse time: " + parseTime);
        // environment.getLogger().debug("• Amount of types: " + environment.getModulePath().countTypes());
        // environment.getLogger().debug("• Amount of used types: " + environment.getModulePath().countUsedTypes());
        // environment.getLogger().debG, "• Amount of cached references: " + TypeGeneratorManager.getInstance().getCacheSize());
        environment.getLogger().debug("• Expression Parser Time: " + TimeUtils.toMilliseconds(PandaExpressionParser.time) + " (" + PandaExpressionParser.amount + ")");
        // environment.getLogger().debug("• Pipeline Handle Time: " + TimeUtils.toMilliseconds(environment.getController().getResources().getPipelinePath().getTotalHandleTime()));
        environment.getLogger().debug("");

        PandaExpressionParser.time = 0;
        PandaExpressionParser.amount = 0;

        return Result.ok(application);
    }

    private boolean parse(Lexer lexer, PandaApplication application, Context<Object> context, ParserPool<Object> headPool, Source source, Completable<Script> result) {
        PandaScript script = new PandaScript(source);
        application.addScript(script);

        Snippet tokenizedSource = lexer.convert(source);
        SourceStream stream = new PandaSourceStream(tokenizedSource);

        Imports imports = new Imports(context.getEnvironment().getPackages(), context.getTypeLoader());
        imports.importModule("java", Package.DEFAULT_MODULE);
        imports.importModule("panda", Package.DEFAULT_MODULE);
        imports.importModule(script.getModule());

        PoolParser<Object> headParser = headPool.toParser();

        Context<PoolParser<Object>> delegatedContext = context.forkCreator()
                .withSubject(headParser)
                .withScript(script)
                .withImports(imports)
                .withScriptSource(tokenizedSource)
                .withStream(stream)
                .withSource(tokenizedSource)
                .toContext();

        boolean status = headParser.parse(delegatedContext, stream);
        result.complete(script);

        return status;
    }

    @Override
    public Environment getEnvironment() {
        return environment;
    }

}
