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

package org.panda_lang.language.resource;

import org.panda_lang.language.interpreter.parser.expression.ExpressionSubparsers;
import org.panda_lang.language.interpreter.parser.pool.PandaParserPoolService;
import org.panda_lang.language.interpreter.parser.pool.ParserPoolService;

public final class PandaResources implements Resources {

    private final PandaResourcesBuilder builder;

    private PandaResources(PandaResourcesBuilder builder) {
        this.builder = builder;
    }

    @Override
    public ExpressionSubparsers getExpressionSubparsers() {
        return builder.expressionSubparsers;
    }

    @Override
    public ParserPoolService getPipelinePath() {
        return builder.parserPoolService;
    }

    public static PandaResourcesBuilder builder() {
        return new PandaResourcesBuilder();
    }

    public static final class PandaResourcesBuilder {

        public ParserPoolService parserPoolService;
        public ExpressionSubparsers expressionSubparsers;

        private PandaResourcesBuilder() { }

        public PandaResourcesBuilder withPipelinePath(ParserPoolService parserPoolService) {
            this.parserPoolService = parserPoolService;
            return this;
        }

        public PandaResourcesBuilder withExpressionSubparsers(ExpressionSubparsers expressionSubparsers) {
            this.expressionSubparsers = expressionSubparsers;
            return this;
        }

        public PandaResources build() {
            if (parserPoolService == null) {
                this.parserPoolService = new PandaParserPoolService();
            }

            return new PandaResources(this);
        }

    }

}
