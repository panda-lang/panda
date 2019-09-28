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

import org.panda_lang.framework.design.interpreter.pattern.custom.AbstractCustomPatternElement;
import org.panda_lang.framework.design.interpreter.pattern.custom.CustomPatternElement;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.language.architecture.prototype.TypeDeclarationUtils;
import org.panda_lang.framework.language.interpreter.token.PandaSnippet;

import java.util.Optional;

public final class TypeElement extends AbstractCustomPatternElement {

    private TypeElement(TypeElementBuilder builder) {
        super(builder);
    }

    public static TypeElementBuilder create(String id) {
        return new TypeElementBuilder(id);
    }

    public static final class TypeElementBuilder extends AbstractCustomPatternElementBuilder<Snippet, TypeElementBuilder> {

        private TypeElementBuilder(String id) {
            super(id);

            super.custom((source, current) -> {
                Snippet typeSource = new PandaSnippet(current);
                typeSource.addTokens(source.getAvailableSource());

                Optional<Snippet> type = TypeDeclarationUtils.readType(typeSource);

                if (!type.isPresent()) {
                    return null;
                }

                source.next(type.get().size() - 1);
                return type.get();
            });
        }

        @Override
        public CustomPatternElement build() {
            return new TypeElement(this);
        }

    }

}
