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

import org.panda_lang.framework.design.interpreter.parser.Parser;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.framework.language.interpreter.parser.expression.PandaExpressionSubparsers;
import org.panda_lang.panda.language.interpreter.parser.RegistrableParsersLoader;
import org.panda_lang.panda.language.resource.syntax.expressions.PandaExpressionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Define parsers used by framework
 */
public final class ParsersInitializer implements Initializer {

    private final PandaBootstrap bootstrap;
    private final RegistrableParsersLoader registrationLoader;
    private final Collection<ExpressionSubparser> expressionSubparsers = new ArrayList<>();

    ParsersInitializer(PandaBootstrap bootstrap) {
        this.bootstrap = bootstrap;
        this.registrationLoader = new RegistrableParsersLoader(bootstrap.logger);
    }

    /**
     * Load custom expression subparser classes
     *
     * @param subparserClasses the classes to load
     * @return the initializer instance
     */
    @SafeVarargs
    public final ParsersInitializer loadExpressionSubparsers(Class<? extends ExpressionSubparser>... subparserClasses) {
        this.expressionSubparsers.addAll(PandaExpressionUtils.collectSubparsers(subparserClasses).getSubparsers());
        return this;
    }

    /**
     * Load default expressions defined by Panda standard
     *
     * @return the initializer instance
     * @see org.panda_lang.panda.language.resource.syntax.expressions.PandaExpressions#SUBPARSERS
     */
    public ParsersInitializer loadDefaultExpressionSubparsers() {
        this.expressionSubparsers.addAll(PandaExpressionUtils.collectSubparsers().getSubparsers());
        return this;
    }

    /**
     * Load arrays of parsers classes
     *
     * @param classes classes to load
     * @return the initializer instance
     */
    @SafeVarargs
    public final ParsersInitializer loadParsersClasses(Class<? extends Parser>[]... classes) {
        for (Class<? extends Parser>[] parsers : classes) {
            loadParsersClasses(parsers);
        }

        return this;
    }

    /**
     * Load array of parsers classes
     *
     * @param classes classes to load
     * @return the initializer instance
     */
    @SafeVarargs
    public final ParsersInitializer loadParsersClasses(Class<? extends Parser>... classes) {
        return loadParsers(Arrays.asList(classes));
    }

    /**
     * Load collection of parsers classes
     *
     * @param classes classes to load
     * @return the initializer instance
     */
    public ParsersInitializer loadParsers(Collection<Class<? extends Parser>> classes) {
        if (bootstrap.resources.pipelinePath == null) {
            throw new BootstrapException("Cannot load parsers because pipeline was not initialized");
        }

        registrationLoader.loadParsers(bootstrap.resources.pipelinePath, classes);
        return this;
    }

    @Override
    public PandaBootstrap collect() {
        bootstrap.resources.expressionSubparsers = new PandaExpressionSubparsers(expressionSubparsers);
        return bootstrap;
    }

}
