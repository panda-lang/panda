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

package org.panda_lang.panda.bootstrap;

import org.panda_lang.language.interpreter.parser.ContextParser;
import org.panda_lang.language.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.language.interpreter.parser.expression.PandaExpressionSubparsers;
import org.panda_lang.panda.language.interpreter.parser.ParsersLoader;
import org.panda_lang.panda.language.resource.syntax.expressions.PandaExpressions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Define parsers used by framework
 */
public final class ParsersInitializer implements Initializer {

    private final PandaBootstrap bootstrap;
    private final ParsersLoader registrationLoader;
    private final Collection<ExpressionSubparser> expressionSubparsers = new ArrayList<>();

    ParsersInitializer(PandaBootstrap bootstrap) {
        this.bootstrap = bootstrap;
        this.registrationLoader = new ParsersLoader();
    }

    /**
     * Load custom expression subparser classes
     *
     * @param subparsers the subparsers to load
     * @return the initializer instance
     */
    public final ParsersInitializer loadExpressionSubparsers(ExpressionSubparser... subparsers) {
        this.expressionSubparsers.addAll(Arrays.asList(subparsers));
        return this;
    }

    /**
     * Load default expressions defined by Panda standard
     *
     * @return the initializer instance
     * @see org.panda_lang.panda.language.resource.syntax.expressions.PandaExpressions#SUBPARSERS
     */
    public ParsersInitializer loadDefaultExpressionSubparsers() {
        this.expressionSubparsers.addAll(PandaExpressions.getSubparsers());
        return this;
    }

    /**
     * Load array of parsers classes
     *
     * @param parsers classes to load
     * @return the initializer instance
     */
    public final ParsersInitializer loadParsers(ContextParser<?, ?>... parsers) {
        return loadParsers(Arrays.asList(parsers));
    }

    /**
     * Load collection of parsers classes
     *
     * @param parsers classes to load
     * @return the initializer instance
     */
    public ParsersInitializer loadParsers(Collection<ContextParser<?, ?>> parsers) {
        if (bootstrap.resources.poolService == null) {
            throw new BootstrapException("Cannot load parsers because pipeline was not initialized");
        }

        registrationLoader.loadParsers(bootstrap.resources.poolService, parsers);
        return this;
    }

    @Override
    public PandaBootstrap collect() {
        bootstrap.resources.expressionSubparsers = new PandaExpressionSubparsers(expressionSubparsers);
        return bootstrap;
    }

}
