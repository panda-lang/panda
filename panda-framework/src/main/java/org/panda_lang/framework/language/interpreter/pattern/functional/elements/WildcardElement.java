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

package org.panda_lang.framework.language.interpreter.pattern.functional.elements;

import org.panda_lang.framework.design.interpreter.token.Snippetable;
import org.panda_lang.framework.language.interpreter.pattern.functional.FunctionalPatternElementBuilder;
import org.panda_lang.utilities.commons.ObjectUtils;

import java.util.function.Function;

public final class WildcardElement<T extends Snippetable> extends FunctionalPatternElementBuilder<T, WildcardElement<T>> {

    public WildcardElement(String id) {
        super(id);
    }

    public <V extends Snippetable> WildcardElement<V> mapWildcard(Function<T, V> mapper) {
        super.mappers.add(mapper);
        return ObjectUtils.cast(this);
    }

    public static <T extends Snippetable> WildcardElement<T> create(String id) {
        return new WildcardElement<T>(id).reader(((data, source) -> ObjectUtils.cast(source.next())));
    }

}
