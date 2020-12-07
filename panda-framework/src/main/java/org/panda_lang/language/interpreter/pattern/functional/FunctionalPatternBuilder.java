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

package org.panda_lang.language.interpreter.pattern.functional;

import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.interpreter.token.Snippetable;
import org.panda_lang.language.interpreter.token.Token;
import org.panda_lang.language.interpreter.token.TokenInfo;
import org.panda_lang.language.interpreter.token.TokenType;
import org.panda_lang.language.interpreter.pattern.functional.elements.ArgumentsElement;
import org.panda_lang.language.interpreter.pattern.functional.elements.ContentBeforeElement;
import org.panda_lang.language.interpreter.pattern.functional.elements.CustomElement;
import org.panda_lang.language.interpreter.pattern.functional.elements.ExpressionElement;
import org.panda_lang.language.interpreter.pattern.functional.elements.KeywordElement;
import org.panda_lang.language.interpreter.pattern.functional.elements.QualifierElement;
import org.panda_lang.language.interpreter.pattern.functional.elements.SectionElement;
import org.panda_lang.language.interpreter.pattern.functional.elements.SubPatternElement;
import org.panda_lang.language.interpreter.pattern.functional.elements.TypeElement;
import org.panda_lang.language.interpreter.pattern.functional.elements.UnitElement;
import org.panda_lang.language.interpreter.pattern.functional.elements.VariantElement;
import org.panda_lang.language.interpreter.pattern.functional.elements.WildcardElement;
import org.panda_lang.language.interpreter.pattern.functional.verifiers.NextSectionVerifier;
import org.panda_lang.language.interpreter.pattern.functional.verifiers.NextTokenTypeVerifier;
import org.panda_lang.language.interpreter.pattern.functional.verifiers.TokenTypeVerifier;
import org.panda_lang.language.resource.syntax.keyword.Keyword;
import org.panda_lang.language.resource.syntax.separator.Separator;
import org.panda_lang.utilities.commons.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public final class FunctionalPatternBuilder<B extends FunctionalPatternElementBuilder<V, B>, V> {

    private final List<Buildable<?>> elements = new ArrayList<>();
    private FunctionalPatternElementBuilder<?, ?> current;

    public FunctionalPatternBuilder<B, V> consume(Consumer<B> consumer) {
        consumer.accept(ObjectUtils.cast(current));
        return this;
    }

    public FunctionalPatternBuilder<ArgumentsElement, ExpressionTransaction[]> arguments(String id) {
        return element(ArgumentsElement.create(id));
    }

    public FunctionalPatternBuilder<ContentBeforeElement, Snippetable> contentBefore(String id, Separator separator) {
        return element(ContentBeforeElement.create(id, separator));
    }

    public <T> FunctionalPatternBuilder<CustomElement<T>, T> custom(String id) {
        return element(CustomElement.create(id));
    }

    public FunctionalPatternBuilder<ExpressionElement, ExpressionTransaction> expression(String id) {
        return element(ExpressionElement.create(id));
    }

    public FunctionalPatternBuilder<UnitElement, TokenInfo> keyword(Keyword keyword) {
        return element(KeywordElement.create(keyword));
    }

    public FunctionalPatternBuilder<UnitElement, TokenInfo> token(Token token) {
        return element(UnitElement.create("").content(token.getValue()));
    }

    public FunctionalPatternBuilder<QualifierElement, Snippetable> qualifier(String id) {
        return element(QualifierElement.create(id));
    }

    public FunctionalPatternBuilder<WildcardElement<Snippet>, Snippet> section(String id, Separator type) {
        return element(SectionElement.create(id, type));
    }

    public FunctionalPatternBuilder<SubPatternElement, FunctionalResult> subPattern(String id) {
        return element(SubPatternElement.create(id));
    }

    public FunctionalPatternBuilder<SubPatternElement, FunctionalResult> subPattern(String id, Function<FunctionalPatternBuilder<?, ?>, FunctionalPatternBuilder<?, ?>> function) {
        return element(SubPatternElement.create(id, function));
    }

    public FunctionalPatternBuilder<TypeElement, Snippetable> type(String id) {
        return element(TypeElement.create(id));
    }

    public FunctionalPatternBuilder<UnitElement, TokenInfo> unit(String id, String value) {
        return element(UnitElement.create(id).content(value));
    }

    public FunctionalPatternBuilder<VariantElement, Object> variant(String id) {
        return element(VariantElement.create(id));
    }

    public <T extends Snippetable> FunctionalPatternBuilder<WildcardElement<T>, T> wildcard(String id) {
        return element(WildcardElement.create(id));
    }

    public <T, R> FunctionalPatternBuilder<? extends FunctionalPatternElementBuilder<R, ? extends Buildable<R>>, R> map(Function<T, R> mapper) {
        this.current = this.current.map(ObjectUtils.cast(mapper));
        return ObjectUtils.cast(this);
    }

    public FunctionalPatternBuilder<B, V> verify(Verifier<V> verifier) {
        this.current.verify(ObjectUtils.cast(verifier));
        return this;
    }

    public FunctionalPatternBuilder<B, V> verifyType(TokenType... types) {
        current.verify(ObjectUtils.cast(new TokenTypeVerifier(types)));
        return this;
    }

    public FunctionalPatternBuilder<B, V> verifyNextType(TokenType... types) {
        current.verify(ObjectUtils.cast(new NextTokenTypeVerifier(types)));
        return this;
    }

    public FunctionalPatternBuilder<B, V> verifyNextSection(Separator separator) {
        current.verify(ObjectUtils.cast(new NextSectionVerifier(separator)));
        return this;
    }

    public FunctionalPatternBuilder<B, V> optional() {
        this.current.optional();
        return this;
    }

    public <R extends FunctionalPatternElementBuilder<C, R>, C> FunctionalPatternBuilder<R, C> element(R element) {
        this.current = element;
        this.elements.add(element);
        return ObjectUtils.cast(this);
    }

    public FunctionalPattern build() {
        return FunctionalPattern.of(elements);
    }

}
