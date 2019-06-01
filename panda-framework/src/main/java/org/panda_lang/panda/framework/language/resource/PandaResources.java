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

package org.panda_lang.panda.framework.language.resource;

import org.panda_lang.panda.framework.design.interpreter.messenger.MessengerInitializer;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.PipelinePath;
import org.panda_lang.panda.framework.design.resource.Resources;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionSubparsers;
import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.PandaPipelinePath;
import org.panda_lang.panda.utilities.annotations.AnnotationsScanner;
import org.panda_lang.panda.utilities.annotations.AnnotationsScannerProcess;

public class PandaResources implements Resources {

    private final PandaResourcesBuilder builder;

    private PandaResources(PandaResourcesBuilder builder) {
        this.builder = builder;
    }

    @Override
    public ExpressionSubparsers getExpressionSubparsers() {
        return builder.expressionSubparsers;
    }

    @Override
    public PipelinePath getPipelinePath() {
        return builder.pipelinePath;
    }

    @Override
    public MessengerInitializer getMessengerInitializer() {
        return builder.messengerInitializer;
    }

    @Override
    public AnnotationsScannerProcess getScannerProcess() {
        return builder.scannerProcess;
    }

    public static PandaResourcesBuilder builder() {
        return new PandaResourcesBuilder();
    }

    public static final class PandaResourcesBuilder {

        public AnnotationsScannerProcess scannerProcess;
        public MessengerInitializer messengerInitializer;
        public PipelinePath pipelinePath;
        public ExpressionSubparsers expressionSubparsers;

        private PandaResourcesBuilder() { }

        public PandaResourcesBuilder withScannerProcess(AnnotationsScannerProcess scannerProcess) {
            this.scannerProcess = scannerProcess;
            return this;
        }

        public PandaResourcesBuilder withMessengerInitializer(MessengerInitializer messengerInitializer) {
            this.messengerInitializer = messengerInitializer;
            return this;
        }

        public PandaResourcesBuilder withPipelinePath(PipelinePath pipelinePath) {
            this.pipelinePath = pipelinePath;
            return this;
        }

        public PandaResourcesBuilder withExpressionSubparsers(ExpressionSubparsers expressionSubparsers) {
            this.expressionSubparsers = expressionSubparsers;
            return this;
        }

        public PandaResources build() {
            if (pipelinePath == null) {
                this.pipelinePath = new PandaPipelinePath();
            }

            if (scannerProcess == null) {
                this.scannerProcess = AnnotationsScanner.configuration()
                        .muted()
                        .build()
                        .createProcess()
                        .fetch();

                this.scannerProcess.getAnnotationsScanner().getLogger().unmute();
            }

            return new PandaResources(this);
        }

    }

}
