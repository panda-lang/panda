/*
 * Copyright (c) 2015-2017 Dzikoysk
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

package org.panda_lang.panda.language.structure.expression.callbacks.util;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.framework.language.interpreter.lexer.Syntax;
import org.panda_lang.panda.framework.language.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.language.interpreter.token.extractor.Extractor;
import org.panda_lang.panda.framework.language.interpreter.token.reader.TokenReader;
import org.panda_lang.panda.framework.language.interpreter.parser.ParserInfo;
import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenPattern;
import org.panda_lang.panda.framework.implementation.token.reader.PandaTokenReader;
import org.panda_lang.panda.framework.implementation.parser.PandaParserException;
import org.panda_lang.panda.core.interpreter.parser.util.Components;
import org.panda_lang.panda.core.structure.PandaScript;
import org.panda_lang.panda.language.structure.argument.ArgumentParser;
import org.panda_lang.panda.language.structure.expression.Expression;
import org.panda_lang.panda.language.structure.expression.callbacks.InstanceExpressionCallback;
import org.panda_lang.panda.language.structure.imports.ImportRegistry;
import org.panda_lang.panda.language.structure.prototype.ClassPrototype;
import org.panda_lang.panda.language.structure.prototype.structure.constructor.Constructor;
import org.panda_lang.panda.language.structure.prototype.structure.constructor.ConstructorUtils;

import java.util.List;

public class InstanceExpressionParser {

    private ClassPrototype returnType;
    private Constructor constructor;
    private Expression[] arguments;

    public void parse(TokenizedSource source, ParserInfo info) {
        Panda panda = info.getComponent(Components.PANDA);
        PandaScript script = info.getComponent(Components.SCRIPT);

        TokenReader reader = new PandaTokenReader(source);
        Syntax syntax = panda.getPandaComposition().getSyntax();

        TokenPattern pattern = TokenPattern.builder().compile(syntax, "new +* ( +* )").build();
        Extractor extractor = pattern.extractor();
        List<TokenizedSource> gaps = extractor.extract(reader);

        if (gaps == null) {
            throw new PandaParserException("Cannot parse expression::instance");
        }

        String className = TokenizedSource.asString(gaps.get(0));
        ImportRegistry importRegistry = script.getImportRegistry();
        this.returnType = importRegistry.forClass(className);

        ArgumentParser argumentParser = new ArgumentParser();
        this.arguments = argumentParser.parse(info, gaps.get(1));
        this.constructor = ConstructorUtils.matchConstructor(returnType, arguments);

        if (constructor == null) {
            throw new PandaParserException("Cannot find constructor for the specified arguments");
        }
    }

    public Expression[] getArguments() {
        return arguments;
    }

    public Constructor getConstructor() {
        return constructor;
    }

    public ClassPrototype getReturnType() {
        return returnType;
    }

    public InstanceExpressionCallback toCallback() {
        return new InstanceExpressionCallback(returnType, constructor, arguments);
    }

}
