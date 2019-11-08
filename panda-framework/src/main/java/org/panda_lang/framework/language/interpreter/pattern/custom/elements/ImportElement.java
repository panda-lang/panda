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

package org.panda_lang.framework.language.interpreter.pattern.custom.elements;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.Snippetable;
import org.panda_lang.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.framework.language.resource.syntax.TokenTypes;
import org.panda_lang.framework.language.interpreter.pattern.custom.CustomPatternElementBuilder;
import org.panda_lang.framework.language.interpreter.token.PandaSnippet;
import org.panda_lang.framework.language.resource.syntax.operator.Operators;
import org.panda_lang.framework.language.resource.syntax.separator.Separators;

import java.util.function.BiPredicate;

public final class ImportElement extends CustomPatternElementBuilder<Snippetable, ImportElement> {

    private BiPredicate<@Nullable TokenRepresentation, TokenRepresentation> condition = (previous, current) -> false;

    private ImportElement(String id) {
        super(id);

        super.custom((data, source) -> {
            Snippet importSource = new PandaSnippet();

            while (source.hasNext()) {
                TokenRepresentation next = source.getNext();

                if (!condition.test(source.getCurrent().orElse(null), next)) {
                    break;
                }

                importSource.addToken(source.next());
            }

            return importSource.isEmpty() ? null : importSource;
        });
    }

    public ImportElement javaClass() {
        this.condition = (previous, current) -> current.getType() == TokenTypes.UNKNOWN || current.contentEquals(Separators.PERIOD);
        return this;
    }

    public ImportElement pandaModule() {
        this.condition = (previous, token) -> {
            if (previous == null || previous.contentEquals(Operators.SUBTRACTION)) {
                return token.getType() == TokenTypes.UNKNOWN || token.getType() == TokenTypes.KEYWORD;
            }

            return token.getType() == TokenTypes.UNKNOWN || token.contentEquals(Operators.SUBTRACTION) || token.equals(Operators.COLON);
        };

        return this;
    }

    public static ImportElement create(String id) {
        return new ImportElement(id);
    }

}
