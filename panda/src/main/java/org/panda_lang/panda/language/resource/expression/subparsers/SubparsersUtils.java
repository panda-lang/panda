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

package org.panda_lang.panda.language.resource.expression.subparsers;

import org.panda_lang.framework.design.architecture.prototype.PrototypeReference;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.language.architecture.prototype.TypeDeclarationUtils;
import org.panda_lang.utilities.commons.function.Produce;

import java.util.Optional;

final class SubparsersUtils {

    private SubparsersUtils() { }

    protected static Produce<PrototypeReference, ExpressionResult> readType(ExpressionContext context) {
        Optional<Snippet> typeSource = TypeDeclarationUtils.readType(context.getSynchronizedSource().getAvailableSource());

        if (!typeSource.isPresent()) {
            return new Produce<>(() -> ExpressionResult.error("Cannot read type", context.getSynchronizedSource().getAvailableSource()));
        }

        Optional<PrototypeReference> typeReference = context.getContext().getComponent(Components.IMPORTS).forName(typeSource.get().asSource());

        if (!typeReference.isPresent()) {
            return new Produce<>(() -> ExpressionResult.error("Unknown type", context.getSynchronizedSource().getAvailableSource()));
        }

        context.getSynchronizedSource().next(typeSource.get().size());
        return new Produce<>(typeReference.get());
    }

}
