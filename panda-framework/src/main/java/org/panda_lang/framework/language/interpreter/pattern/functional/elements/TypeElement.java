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
import org.panda_lang.framework.language.architecture.type.utils.TypeDeclarationUtils;
import org.panda_lang.framework.language.interpreter.pattern.functional.FunctionalPatternElementBuilder;

public final class TypeElement extends FunctionalPatternElementBuilder<Snippetable, TypeElement> {

    private TypeElement(String id) {
        super(id);
    }

    public static TypeElement create(String id) {
        return new TypeElement(id)
                .reader((data, source) -> TypeDeclarationUtils.readType(source.getAvailableSource())
                    .peek(type -> source.next(type.size()))
                    .getOrNull()
                );
    }

}
