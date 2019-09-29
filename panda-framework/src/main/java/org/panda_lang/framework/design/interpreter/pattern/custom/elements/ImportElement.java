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

package org.panda_lang.framework.design.interpreter.pattern.custom.elements;

import org.panda_lang.framework.design.interpreter.pattern.custom.CustomPatternElementBuilder;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.Snippetable;
import org.panda_lang.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.framework.design.interpreter.token.TokenType;
import org.panda_lang.framework.language.interpreter.token.PandaSnippet;
import org.panda_lang.framework.language.resource.syntax.operator.Operators;
import org.panda_lang.framework.language.resource.syntax.separator.Separators;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public final class ImportElement extends CustomPatternElementBuilder<Snippetable, ImportElement> {

    private BiPredicate<Predicate<TokenRepresentation>, TokenRepresentation> condition;

    private ImportElement(String id) {
        super(id);

        super.custom((data, source) -> {
            Snippet importSource = new PandaSnippet();

            while (source.hasNext()) {
                TokenRepresentation next = source.getNext();

                if (!getCondition().test(null, next)) {
                    break;
                }

                importSource.addToken(source.next());
            }

            return importSource.isEmpty() ? null : importSource;
        });
    }

    public ImportElement condition(BiPredicate<Predicate<TokenRepresentation>, TokenRepresentation> condition) {
        this.condition = condition;
        return this;
    }

    public ImportElement javaClass() {
        return condition((previous, token) -> token.getType() == TokenType.UNKNOWN || token.contentEquals(Separators.PERIOD));
    }

    public ImportElement pandaModule() {
        return condition((previous, token) -> token.getType() == TokenType.UNKNOWN || token.contentEquals(Operators.SUBTRACTION));
    }

    public BiPredicate<Predicate<TokenRepresentation>, TokenRepresentation> getCondition() {
        return condition;
    }

    public static ImportElement create(String id) {
        return new ImportElement(id);
    }

}
