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

package org.panda_lang.panda.bootstrap;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.PandaBuilder;
import org.panda_lang.panda.PandaResources;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.PipelinePath;
import org.panda_lang.panda.framework.design.resource.Syntax;
import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.PandaPipelinePath;
import org.panda_lang.panda.framework.language.resource.PandaLanguage;
import org.panda_lang.panda.framework.language.resource.PandaSyntax;
import org.panda_lang.panda.utilities.annotations.AnnotationsScanner;
import org.panda_lang.panda.utilities.annotations.AnnotationsScannerProcess;

public class PandaBootstrap {

    private final PandaBuilder pandaBuilder = PandaBuilder.builder();

    protected Syntax syntax;
    protected PipelinePath pipelinePath;
    protected AnnotationsScannerProcess scannerProcess;

    private PandaBootstrap() { }

    public PandaBootstrap withSyntax(Syntax syntax) {
        this.syntax = syntax;
        return this;
    }

    protected PandaBootstrap withPipelinePath(PipelinePath pipelinePath) {
        this.pipelinePath = pipelinePath;
        return this;
    }

    protected PandaBootstrap withScannerProcess(AnnotationsScannerProcess scannerProcess) {
        this.scannerProcess = scannerProcess;
        return this;
    }

    public PipelinePandaBootstrap initializePipelines() {
        return new PipelinePandaBootstrap(this);
    }

    public ScannerPandaBootstrap initializeScanner() {
        return new ScannerPandaBootstrap(this);
    }

    public ParsersPandaBootstrap initializeParsers() {
        return new ParsersPandaBootstrap(this);
    }

    public Panda get() {
        if (syntax == null) {
            this.syntax = new PandaSyntax();
        }

        if (pipelinePath == null) {
            this.pipelinePath = new PandaPipelinePath();
        }

        if (scannerProcess == null) {
            this.scannerProcess = AnnotationsScanner.configuration()
                    .build()
                    .createProcess()
                    .fetch();
        }

        return pandaBuilder
                .withLanguage(new PandaLanguage(syntax))
                .withResources(new PandaResources(scannerProcess, pipelinePath))
                .build();
    }

    public PandaBuilder getPandaBuilder() {
        return pandaBuilder;
    }

    public static PandaBootstrap initializeBootstrap() {
        return new PandaBootstrap();
    }

}