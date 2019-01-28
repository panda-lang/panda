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

package org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks;

import org.panda_lang.panda.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.constructor.PrototypeConstructor;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.interpreter.token.stream.TokenReader;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.design.architecture.PandaScript;
import org.panda_lang.panda.framework.design.architecture.prototype.constructor.ConstructorUtils;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.resource.parsers.general.ArgumentParser;
import org.panda_lang.panda.framework.design.resource.parsers.expression.ExpressionCallbackParser;
import org.panda_lang.panda.framework.design.interpreter.pattern.gapped.GappedPattern;
import org.panda_lang.panda.framework.design.interpreter.pattern.gapped.extractor.GappedPatternExtractor;
import org.panda_lang.panda.framework.design.interpreter.pattern.gapped.GappedPatternBuilder;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaTokenReader;
import org.panda_lang.panda.framework.language.resource.PandaSyntax;

import java.util.Arrays;
import java.util.List;

public class ConstructorExpressionParser implements ExpressionCallbackParser<InstanceExpressionCallback> {

    private static final GappedPattern PATTERN = new GappedPatternBuilder()
            .compile(PandaSyntax.getInstance(), "new +** ( +* )")
            .build();

    private ClassPrototype returnType;
    private PrototypeConstructor constructor;
    private Expression[] arguments;

    @Override
    public void parse(Tokens source, ParserData data) {
        PandaScript script = data.getComponent(PandaComponents.PANDA_SCRIPT);
        TokenReader reader = new PandaTokenReader(source);

        GappedPatternExtractor extractor = PATTERN.extractor();
        List<Tokens> gaps = extractor.extract(reader);

        if (gaps == null) {
            throw new PandaParserException("Cannot parse expression::instance");
        }

        String className = gaps.get(0).asString();
        ModuleLoader moduleLoader = script.getModuleLoader();
        this.returnType = moduleLoader.forClass(className).fetch();

        if (returnType == null) {
            throw PandaParserFailure.builder()
                    .message("Unknown return type '" + className + "'")
                    .data(data)
                    .source(source)
                    .build();
        }

        ArgumentParser argumentParser = new ArgumentParser();
        this.arguments = argumentParser.parse(data, gaps.get(1));
        this.constructor = ConstructorUtils.matchConstructor(returnType, arguments);

        if (constructor == null) {
            throw new PandaParserFailure("Cannot find constructor of " + returnType.getClassName() + " for the specified arguments " + Arrays.toString(this.arguments), data);
        }
    }

    @Override
    public InstanceExpressionCallback toCallback() {
        return new InstanceExpressionCallback(returnType, constructor, arguments);
    }

    public Expression[] getArguments() {
        return arguments;
    }

    public PrototypeConstructor getConstructor() {
        return constructor;
    }

    public ClassPrototype getReturnType() {
        return returnType;
    }

}
