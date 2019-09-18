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
import org.panda_lang.framework.design.interpreter.parser.Parsers;
import org.panda_lang.panda.language.interpreter.parser.loader.RegistrableLoader;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.framework.language.interpreter.parser.expression.PandaExpressionSubparsers;
import org.panda_lang.panda.language.resource.expression.PandaExpressionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class ParsersPandaBootstrap implements PandaBootstrapElement {

    private final PandaBootstrap bootstrap;
    private final RegistrableLoader registrationLoader = new RegistrableLoader();
    private final Collection<ExpressionSubparser> expressionSubparsers = new ArrayList<>();

    public ParsersPandaBootstrap(PandaBootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    @SafeVarargs
    public final ParsersPandaBootstrap loadExpressionSubparsers(Class<? extends ExpressionSubparser>... subparserClasses) {
        this.expressionSubparsers.addAll(PandaExpressionUtils.collectSubparsers(subparserClasses).getSubparsers());
        return this;
    }

    public ParsersPandaBootstrap loadDefaultExpressionSubparsers() {
        this.expressionSubparsers.addAll(PandaExpressionUtils.collectSubparsers().getSubparsers());
        return this;
    }

    public ParsersPandaBootstrap loadParsers(Parsers... parsers) {
        for (Parsers group : parsers) {
            loadParsersClasses(group.getParsers());
        }

        return this;
    }

    @SafeVarargs
    public final ParsersPandaBootstrap loadParsers(Class<? extends Parsers>... parsers) {
        for (Class<? extends Parsers> group : parsers) {
            try {
                loadParsers(group.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                throw new PandaBootstrapException("Cannot create Parsers instance: " + e.getMessage(), e);
            }
        }

        return this;
    }

    @SafeVarargs
    public final ParsersPandaBootstrap loadParsersClasses(Class<? extends Parser>[]... classes) {
        for (Class<? extends Parser>[] parsers : classes) {
            loadParsersClasses(parsers);
        }

        return this;
    }

    @SafeVarargs
    public final ParsersPandaBootstrap loadParsersClasses(Class<? extends Parser>... classes) {
        return loadParsers(Arrays.asList(classes));
    }

    public ParsersPandaBootstrap loadParsers(Collection<Class<? extends Parser>> classes) {
        if (bootstrap.resources.pipelinePath == null) {
            throw new PandaBootstrapException("Cannot load parsers because pipeline was not initialized");
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
