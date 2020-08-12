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

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.interpreter.token.Snippetable;
import org.panda_lang.language.interpreter.token.TokenInfo;
import org.panda_lang.language.interpreter.pattern.functional.FunctionalPatternElementBuilder;
import org.panda_lang.language.interpreter.token.PandaSnippet;
import org.panda_lang.language.resource.syntax.TokenTypes;
import org.panda_lang.language.resource.syntax.operator.Operators;
import org.panda_lang.language.resource.syntax.separator.Separators;

import java.util.function.BiPredicate;

public final class QualifierElement extends FunctionalPatternElementBuilder<Snippetable, QualifierElement> {

    private BiPredicate<@Nullable TokenInfo, TokenInfo> condition = (previous, current) -> false;

    private QualifierElement(String id) {
        super(id);

        super.reader((data, source) -> {
            Snippet importSource = PandaSnippet.createMutable();

            while (source.hasNext()) {
                TokenInfo next = source.getNext();

                if (!condition.test(source.getCurrent().getOrNull(), next)) {
                    break;
                }

                importSource.append(source.next());
            }

            return importSource.isEmpty() ? null : importSource;
        });
    }

    public QualifierElement javaClass() {
        this.condition = (previous, current) -> current.getType() == TokenTypes.UNKNOWN || current.contentEquals(Separators.PERIOD);
        return this;
    }

    public QualifierElement pandaModule() {
        this.condition = (previous, token) -> {
            if (previous == null || previous.contentEquals(Operators.SUBTRACTION)) {
                return token.getType() == TokenTypes.UNKNOWN || token.getType() == TokenTypes.KEYWORD;
            }

            return token.getType() == TokenTypes.UNKNOWN || token.contentEquals(Operators.SUBTRACTION) || token.equals(Operators.COLON);
        };

        return this;
    }

    public static QualifierElement create(String id) {
        return new QualifierElement(id);
    }

}
