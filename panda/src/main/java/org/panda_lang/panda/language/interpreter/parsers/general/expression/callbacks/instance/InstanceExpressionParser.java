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

package org.panda_lang.panda.language.interpreter.parsers.general.expression.callbacks.instance;

import org.panda_lang.panda.design.architecture.PandaScript;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.design.architecture.prototype.constructor.ConstructorUtils;
import org.panda_lang.panda.framework.design.architecture.prototype.constructor.PrototypeConstructor;
import org.panda_lang.panda.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.design.interpreter.token.AbyssPatternBuilder;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.interpreter.token.extractor.Extractor;
import org.panda_lang.panda.framework.design.interpreter.token.reader.TokenReader;
import org.panda_lang.panda.framework.language.interpreter.parser.*;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.framework.language.interpreter.token.reader.PandaTokenReader;
import org.panda_lang.panda.language.interpreter.parsers.general.argument.ArgumentParser;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.language.interpreter.parsers.general.expression.ExpressionCallbackParser;
import org.panda_lang.panda.framework.design.architecture.module.ImportRegistry;
import org.panda_lang.panda.language.interpreter.PandaSyntax;

import java.util.Arrays;
import java.util.List;

public class InstanceExpressionParser implements ExpressionCallbackParser<InstanceExpressionCallback> {

    private static final AbyssPattern PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "new +** ( +* )")
            .build();

    private ClassPrototype returnType;
    private PrototypeConstructor constructor;
    private Expression[] arguments;

    @Override
    public void parse(TokenizedSource source, ParserData info) {
        PandaScript script = info.getComponent(PandaComponents.PANDA_SCRIPT);
        TokenReader reader = new PandaTokenReader(source);

        Extractor extractor = PATTERN.extractor();
        List<TokenizedSource> gaps = extractor.extract(reader);

        if (gaps == null) {
            throw new PandaParserException("Cannot parse expression::instance");
        }

        String className = gaps.get(0).asString();
        ImportRegistry importRegistry = script.getImportRegistry();
        this.returnType = importRegistry.forClass(className);

        if (returnType == null) {
            throw PandaParserFailure.builder()
                    .message("Unknown return type '" + className + "'")
                    .data(info)
                    .source(source)
                    .build();
        }

        ArgumentParser argumentParser = new ArgumentParser();
        this.arguments = argumentParser.parse(info, gaps.get(1));
        this.constructor = ConstructorUtils.matchConstructor(returnType, arguments);

        if (constructor == null) {
            throw new PandaParserException("Cannot find " + className +
                    " constructor for the specified arguments " + Arrays.toString(this.arguments));
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
