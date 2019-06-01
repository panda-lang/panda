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

import org.panda_lang.panda.framework.PandaFramework;
import org.panda_lang.panda.framework.language.resource.loader.AutoloadLoader;
import org.panda_lang.panda.utilities.annotations.AnnotationsScanner;
import org.panda_lang.panda.utilities.annotations.AnnotationsScannerConfiguration;
import org.panda_lang.panda.utilities.annotations.AnnotationsScannerProcess;
import org.panda_lang.panda.utilities.annotations.AnnotationsScannerProcessBuilder;

import java.util.function.Consumer;

public class ScannerPandaBootstrap implements PandaBootstrapElement {

    private final PandaBootstrap bootstrap;
    private AnnotationsScanner scanner;
    protected AnnotationsScannerProcess scannerProcess;

    public ScannerPandaBootstrap(PandaBootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    public ScannerPandaBootstrap configureScanner(Consumer<AnnotationsScannerConfiguration> scannerConsumer) {
        AnnotationsScannerConfiguration configuration = AnnotationsScanner.configuration();

        scannerConsumer.accept(configuration);
        this.scanner = configuration.build();

        return this;
    }

    public ScannerPandaBootstrap prepareProcess(Consumer<AnnotationsScannerProcessBuilder> processBuilderConsumer) {
        if (scanner == null) {
            throw new PandaBootstrapException("AnnotationScanner was not initialized by Scanner bootstrap");
        }

        AnnotationsScannerProcessBuilder processBuilder = scanner.createProcess();
        processBuilderConsumer.accept(processBuilder);

        PandaFramework.getLogger().debug("--- Scanning");
        this.scannerProcess = processBuilder.fetch();

        return this;
    }

    public ScannerPandaBootstrap autoloadAnnotatedClasses() {
        if (scannerProcess == null) {
            throw new PandaBootstrapException("Cannot load parsers using scanner because it's not initialized");
        }

        AutoloadLoader autoloadLoader = new AutoloadLoader();
        autoloadLoader.load(scannerProcess);
        return this;
    }

    @Override
    public PandaBootstrap collect() {
        if (scanner == null) {
            throw new PandaBootstrapException("AnnotationScanner was not initialized by Scanner bootstrap");
        }

        if (scannerProcess == null) {
            this.scannerProcess = scanner.createProcess()
                    .addDefaultFilters()
                    .fetch();
        }

        bootstrap.resources.withScannerProcess(scannerProcess);
        return bootstrap;
    }

}
