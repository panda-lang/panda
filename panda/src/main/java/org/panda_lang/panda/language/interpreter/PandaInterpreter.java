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

package org.panda_lang.panda.language.interpreter;

import org.panda_lang.language.architecture.Application;
import org.panda_lang.language.architecture.Environment;
import org.panda_lang.language.architecture.Script;
import org.panda_lang.language.architecture.module.Imports;
import org.panda_lang.language.interpreter.Interpreter;
import org.panda_lang.language.interpreter.lexer.Lexer;
import org.panda_lang.language.interpreter.lexer.PandaLexer;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.ContextParser;
import org.panda_lang.language.interpreter.parser.PandaContextCreator;
import org.panda_lang.language.interpreter.parser.expression.PandaExpressionParser;
import org.panda_lang.language.interpreter.parser.pool.ParserPool;
import org.panda_lang.language.interpreter.parser.pool.PoolParser;
import org.panda_lang.language.interpreter.parser.pool.Targets;
import org.panda_lang.language.interpreter.parser.stage.PandaStageManager;
import org.panda_lang.language.interpreter.parser.stage.Phases;
import org.panda_lang.language.interpreter.parser.stage.StageService;
import org.panda_lang.language.interpreter.source.Source;
import org.panda_lang.language.interpreter.source.SourceService;
import org.panda_lang.language.interpreter.source.SourceService.Priority;
import org.panda_lang.language.interpreter.token.PandaSourceStream;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.interpreter.token.SourceStream;
import org.panda_lang.language.resource.Resources;
import org.panda_lang.panda.language.architecture.PandaApplication;
import org.panda_lang.panda.language.architecture.PandaScript;
import org.panda_lang.utilities.commons.ObjectUtils;
import org.panda_lang.utilities.commons.TimeUtils;
import org.panda_lang.utilities.commons.collection.Pair;
import org.panda_lang.utilities.commons.function.Completable;
import org.panda_lang.utilities.commons.function.Result;

import java.util.Stack;

public final class PandaInterpreter implements Interpreter {

    private final Environment environment;

    public PandaInterpreter(Environment environment) {
        this.environment = environment;
    }

    @Override
    public Result<Application, Throwable> interpret(Source source) {
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
        sources.addSource(Priority.STANDARD, source);

        ParserPool<Object> headPool = context.getPoolService().getPool(Targets.HEAD);
        Stack<Runnable> interrupted = new Stack<>();

        try {
            while (sources.hasRequired() || sources.hasStandard() || !interrupted.isEmpty()) {
                while (sources.hasRequired()) {
                    parse(lexer, application, context, headPool, interrupted, sources.retrieveRequired());
                }

                if (!interrupted.isEmpty()) {
                    interrupted.pop().run();
                    continue;
                }

                if (sources.hasStandard()) {
                    parse(lexer, application, context, headPool, interrupted, sources.retrieveStandard());
                }
            }

            stageManager.launch();
        }
        catch (Throwable throwable) {
            environment.getLogger().exception(throwable);
            throwable.printStackTrace();
            return Result.error(throwable);
        }

        String parseTime = TimeUtils.toMilliseconds(System.nanoTime() - uptime);
        environment.getLogger().debug("--- Interpretation of " + source.getId() + " details ");
        environment.getLogger().debug("• Parse time: " + parseTime);
        environment.getLogger().debug("• Amount of types: " + environment.getModulePath().countTypes());
        environment.getLogger().debug("• Amount of used types: " + environment.getModulePath().countUsedTypes());
        // environment.getLogger().debG, "• Amount of cached references: " + TypeGeneratorManager.getInstance().getCacheSize());
        environment.getLogger().debug("• Expression Parser Time: " + TimeUtils.toMilliseconds(PandaExpressionParser.time) + " (" + PandaExpressionParser.amount + ")");
        // environment.getLogger().debug("• Pipeline Handle Time: " + TimeUtils.toMilliseconds(environment.getController().getResources().getPipelinePath().getTotalHandleTime()));
        environment.getLogger().debug("");

        PandaExpressionParser.time = 0;
        PandaExpressionParser.amount = 0;

        return Result.ok(application);
    }

    private boolean parse(Lexer lexer, PandaApplication application, Context<Object> context, ParserPool<Object> headPool, Stack<Runnable> interrupted, Pair<Source, Completable<Script>> sourceEntry) {
        Source current = sourceEntry.getKey();

        PandaScript script = new PandaScript(current.getId());
        application.addScript(script);

        Snippet tokenizedSource = lexer.convert(current);
        SourceStream stream = new PandaSourceStream(tokenizedSource);

        Imports imports = new Imports(context.getTypeLoader());
        imports.importModule("java");
        imports.importModule("panda");

        Context<PoolParser<Object>> delegatedContext = context.forkCreator()
                .withSubject(headPool.toParser())
                .withScript(script)
                .withImports(imports)
                .withScriptSource(tokenizedSource)
                .withStream(stream)
                .withSource(tokenizedSource)
                .toContext();

        return parse(sourceEntry.getValue(), script, interrupted, delegatedContext, stream);
    }

    private boolean parse(Completable<Script> result, Script script, Stack<Runnable> interrupted, Context<PoolParser<Object>> context, SourceStream stream) {
        if (context.getSubject().parse(context, stream)) {
            result.complete(script);
            return true;
        }

        interrupted.push(() -> parse(result, script, interrupted, context, stream));
        return false;
    }

    @Override
    public Environment getEnvironment() {
        return environment;
    }

}
