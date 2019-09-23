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

package org.panda_lang.panda;

import org.panda_lang.framework.design.interpreter.parser.pipeline.Pipelines;
import org.panda_lang.framework.language.resource.syntax.PandaSyntax;
import org.panda_lang.panda.bootstrap.PandaBootstrap;
import org.panda_lang.panda.language.interpreter.messenger.formatters.EnvironmentFormatter;
import org.panda_lang.panda.language.interpreter.messenger.formatters.ParserFailureFormatter;
import org.panda_lang.panda.language.interpreter.messenger.formatters.ProcessFailureFormatter;
import org.panda_lang.panda.language.interpreter.messenger.formatters.IndicatedSourceFormatter;
import org.panda_lang.panda.language.interpreter.messenger.formatters.StacktraceElementsFormatter;
import org.panda_lang.panda.language.interpreter.messenger.formatters.ThrowableFormatter;
import org.panda_lang.panda.language.interpreter.messenger.layouts.ExceptionTranslatorLayout;
import org.panda_lang.panda.language.interpreter.messenger.layouts.InterpreterFailureTranslatorLayout;
import org.panda_lang.panda.language.interpreter.messenger.layouts.PandaLexerFailureTranslatorLayout;
import org.panda_lang.panda.language.interpreter.messenger.layouts.ParserFailureTranslatorLayout;
import org.panda_lang.panda.language.interpreter.messenger.layouts.ProcessFailureTranslatorLayout;
import org.panda_lang.panda.language.interpreter.messenger.mappers.StacktraceMapper;
import org.panda_lang.panda.language.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.language.resource.PandaParsers;
import org.panda_lang.panda.language.resource.expression.subparsers.assignation.AssignationParsers;

public final class PandaFactory {

    public Panda createPanda() {
        return PandaBootstrap.initializeBootstrap()
                // load syntax
                .withSyntax(new PandaSyntax())

                // initialize messenger
                .initializeMessenger()
                    .withLayout(ExceptionTranslatorLayout.class)
                    .withLayouts(PandaLexerFailureTranslatorLayout.class, InterpreterFailureTranslatorLayout.class, ParserFailureTranslatorLayout.class)
                    .withLayout(ProcessFailureTranslatorLayout.class)
                    .withDataFormatters(EnvironmentFormatter.class, ThrowableFormatter.class, StacktraceElementsFormatter.class)
                    .withDataFormatters(IndicatedSourceFormatter.class, ParserFailureFormatter.class)
                    .withDataFormatter(ProcessFailureFormatter.class)
                    .withDataMapper(new StacktraceMapper())
                    .collect()

                // load pipelines
                .initializePipelines()
                    .usePipelines(Pipelines.class, PandaPipelines.class)
                    .collect()

                // load parsers and expressions subparsers
                .initializeParsers()
                    .loadParsersClasses(PandaParsers.PARSERS, AssignationParsers.SUBPARSERS)
                    .loadDefaultExpressionSubparsers()
                    .collect()

                // load models
                .get();
    }

}
