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

package org.panda_lang.panda.language.structure.expression.callbacks;

import org.panda_lang.framework.composition.Syntax;
import org.panda_lang.framework.interpreter.lexer.token.TokenizedSource;
import org.panda_lang.framework.interpreter.lexer.token.extractor.Extractor;
import org.panda_lang.framework.interpreter.lexer.token.reader.TokenReader;
import org.panda_lang.framework.interpreter.parser.ParserInfo;
import org.panda_lang.panda.Panda;
import org.panda_lang.panda.implementation.interpreter.lexer.token.pattern.TokenPattern;
import org.panda_lang.panda.implementation.interpreter.lexer.token.reader.PandaTokenReader;
import org.panda_lang.panda.implementation.interpreter.parser.util.Components;
import org.panda_lang.panda.implementation.structure.PandaScript;
import org.panda_lang.panda.implementation.structure.value.Value;
import org.panda_lang.panda.language.runtime.ExecutableBridge;
import org.panda_lang.panda.language.structure.expression.Expression;
import org.panda_lang.panda.language.structure.expression.ExpressionCallback;
import org.panda_lang.panda.language.structure.imports.ImportRegistry;
import org.panda_lang.panda.language.structure.prototype.ClassPrototype;
import org.panda_lang.panda.language.structure.prototype.structure.method.argument.ArgumentParser;

import java.util.List;

public class CreateInstanceExpressionCallback implements ExpressionCallback {

    private final ClassPrototype returnType;

    public CreateInstanceExpressionCallback(TokenizedSource source, ParserInfo info) {
        TokenReader reader = new PandaTokenReader(source);
        Panda panda = info.getComponent(Components.PANDA);
        Syntax syntax = panda.getPandaComposition().getSyntax();

        TokenPattern pattern = TokenPattern.builder().compile(syntax, "new +* ( +* )").build();
        Extractor extractor = pattern.extractor();
        List<TokenizedSource> gaps = extractor.extract(reader);

        String className = TokenizedSource.asString(gaps.get(0));
        PandaScript script = info.getComponent(Components.SCRIPT);
        ImportRegistry importRegistry = script.getImportRegistry();

        this.returnType = importRegistry.forClass(className);

        ArgumentParser argumentParser = new ArgumentParser();
        Expression[] expressions = argumentParser.parse(info, gaps.get(1));
    }

    @Override
    public Value call(Expression expression, ExecutableBridge bridge) {
        return null;
    }

    public ClassPrototype getReturnType() {
        return returnType;
    }

}
