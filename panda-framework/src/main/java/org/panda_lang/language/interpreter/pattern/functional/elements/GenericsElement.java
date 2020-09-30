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

package org.panda_lang.language.interpreter.pattern.functional.elements;

import org.panda_lang.language.interpreter.pattern.functional.FunctionalPatternElementBuilder;
import org.panda_lang.language.interpreter.token.PandaSnippet;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.interpreter.token.Snippetable;
import org.panda_lang.language.interpreter.token.TokenInfo;
import org.panda_lang.language.resource.syntax.operator.Operators;

public final class GenericsElement extends FunctionalPatternElementBuilder<Snippetable, GenericsElement> {

    private GenericsElement(String id) {
        super(id);
    }

    public static GenericsElement create(String id) {
        return new GenericsElement(id).reader((data, source) -> {
            boolean found = source.getCurrent()
                    .map(tokenInfo -> Operators.LESS_THAN.equals(tokenInfo.getToken()))
                    .orElseGet(false);

            if (!found) {
                source.next(-1);
                return PandaSnippet.empty();
            }

            Snippet declaration = PandaSnippet.createMutable();
            declaration.append(source.getCurrent().get());

            for (TokenInfo representation : source) {
                declaration.append(representation);

                if (Operators.GREATER_THAN.equals(representation.toToken())) {
                    break;
                }
            }

            return declaration;
        });
    }

}
