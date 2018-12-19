/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.framework.design.interpreter.parser.bootstrap;

import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRepresentation;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.handlers.BootstrapHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.interceptor.BootstrapInterceptor;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.layer.LayerMethod;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.PandaParserRepresentation;

import java.util.List;

public class PandaParserBootstrap {

    private final BootstrapParserBuilder bootstrap;

    protected PandaParserBootstrap(BootstrapParserBuilder bootstrap) {
        this.bootstrap = bootstrap;
    }

    protected ParserRepresentation<UnifiedParser> generate() {
        UnifiedBootstrapParser bootstrapParser = new UnifiedBootstrapParser(this);

        if (hasInterceptor()) {
            getInterceptor().initialize(this);
        }

        if (hasBootstrapHandler()) {
            getBootstrapHandler().initialize(this);
        }

        return new PandaParserRepresentation<>(bootstrapParser, bootstrap.handler, bootstrap.priority);
    }

    public boolean hasBootstrapHandler() {
        return getHandler() != null && getHandler() instanceof BootstrapHandler;
    }

    public boolean hasInterceptor() {
        return getInterceptor() != null;
    }

    public Object getInstance() {
        return bootstrap.instance;
    }

    public BootstrapHandler getBootstrapHandler() {
        return hasBootstrapHandler() ? (BootstrapHandler) getHandler() : null;
    }

    public ParserHandler getHandler() {
        return bootstrap.handler;
    }

    public BootstrapInterceptor getInterceptor() {
        return bootstrap.interceptor;
    }

    public List<LayerMethod> getLayers() {
        return bootstrap.layers;
    }

    public String[] getWildcards() {
        return bootstrap.wildcardNames;
    }

    public String getPattern() {
        return bootstrap.pattern;
    }

    public String getName() {
        return bootstrap.name;
    }

    public static BootstrapParserBuilder builder() {
        return new BootstrapParserBuilder();
    }

}
