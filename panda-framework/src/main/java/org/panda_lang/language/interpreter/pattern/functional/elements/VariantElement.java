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

import org.panda_lang.language.PandaFrameworkException;
import org.panda_lang.language.interpreter.token.Token;
import org.panda_lang.language.interpreter.pattern.functional.Buildable;
import org.panda_lang.language.interpreter.pattern.functional.FunctionalPattern;
import org.panda_lang.language.interpreter.pattern.functional.FunctionalPatternElementBuilder;
import org.panda_lang.language.interpreter.pattern.functional.Reader;
import org.panda_lang.language.interpreter.pattern.functional.FunctionalResult;
import org.panda_lang.language.interpreter.token.TokenUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class VariantElement extends FunctionalPatternElementBuilder<Object, VariantElement> {

    public VariantElement(String id) {
        super(id);
    }

    public VariantElement content(Token... tokens) {
        return content(Arrays.stream(tokens)
                .map(Token::getValue)
                .toArray(String[]::new));
    }

    public VariantElement content(String... variants) {
        super.reader((data, source) -> TokenUtils.valueEquals(source.next(), variants) ? source.getCurrent().getOrNull() : null);
        return this;
    }

    public final VariantElement content(Buildable<?>... elements) {
        List<Reader<FunctionalResult>> readers = Arrays.stream(elements)
                .map(FunctionalPattern::of)
                .map(SubPatternElement::createReader)
                .collect(Collectors.toList());

        super.reader((data, source) -> {
            PandaFrameworkException cachedException = null;

            for (Reader<FunctionalResult> reader : readers) {
                try {
                    FunctionalResult functionalResult = reader.read(data, source);

                    if (functionalResult != null) {
                        return functionalResult;
                    }
                } catch (PandaFrameworkException e) {
                    cachedException = e;
                }
            }

            if (cachedException != null) {
                throw cachedException;
            }

            return null;
        });

        return this;
    }

    public static VariantElement create(String id) {
        return new VariantElement(id);
    }

}
