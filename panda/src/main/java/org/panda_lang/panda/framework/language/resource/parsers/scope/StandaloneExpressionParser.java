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

package org.panda_lang.panda.framework.language.resource.parsers.scope;

import org.panda_lang.panda.framework.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaPriorities;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapParserBuilder;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.UnifiedParserBootstrap;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.resource.parsers.expression.ExpressionParser;
import org.panda_lang.panda.framework.design.resource.parsers.expression.ExpressionSubparsers;
import org.panda_lang.panda.framework.design.resource.parsers.expression.ExpressionType;

@ParserRegistration(target = PandaPipelines.SCOPE_LABEL, priority = PandaPriorities.SCOPE_EXPRESSION)
public class StandaloneExpressionParser extends UnifiedParserBootstrap {

    private ExpressionParser expressionParser;

    @Override
    protected BootstrapParserBuilder initialize(ParserData data, BootstrapParserBuilder defaultBuilder) {
        ExpressionParser parent = data.getComponent(PandaComponents.EXPRESSION);

        ExpressionSubparsers subparsers = parent.getSubparsers().fork();
        subparsers.getSubparsers().removeIf(expressionParser -> expressionParser.getType() == ExpressionType.STANDALONE);
        this.expressionParser = new ExpressionParser(parent, subparsers);

        return defaultBuilder;
    }

    @Override
    public boolean customHandle(ParserHandler handler, ParserData data, SourceStream source) {
        return expressionParser.read(source) != null;
    }

    @Autowired
    public void parseExpression(ParserData data) {
        System.out.println(":O");
    }

}
