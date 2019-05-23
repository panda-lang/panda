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

import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.resource.parsers.ParserRegistrationLoader;

import java.util.Arrays;
import java.util.Collection;

public class ParsersPandaBootstrap implements PandaBootstrapElement {

    private final PandaBootstrap bootstrap;

    public ParsersPandaBootstrap(PandaBootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    @SafeVarargs
    public final ParsersPandaBootstrap loadParsers(Class<? extends Parser>... parsers) {
        return loadParsers(Arrays.asList(parsers));
    }

    public ParsersPandaBootstrap loadParsers(Collection<Class<? extends Parser>> parsers) {
        if (bootstrap.pipelinePath == null) {
            throw new PandaBootstrapException("Cannot load parsers because pipeline was not initialized");
        }

        ParserRegistrationLoader registrationLoader = new ParserRegistrationLoader();
        registrationLoader.loadParsers(bootstrap.pipelinePath, parsers);

        return null;
    }

    public ParsersPandaBootstrap loadParsers() {
        if (bootstrap.scannerProcess == null) {
            throw new PandaBootstrapException("Cannot load parsers using scanner because it's not initialized");
        }

        if (bootstrap.pipelinePath == null) {
            throw new PandaBootstrapException("Cannot load parsers because pipeline was not initialized");
        }

        ParserRegistrationLoader registrationLoader = new ParserRegistrationLoader();
        registrationLoader.load(bootstrap.pipelinePath, bootstrap.scannerProcess);

        return this;
    }

    @Override
    public PandaBootstrap collect() {
        return bootstrap;
    }

}
