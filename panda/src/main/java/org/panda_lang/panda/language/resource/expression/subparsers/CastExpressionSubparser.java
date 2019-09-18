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

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.panda.framework.design.interpreter.token.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.language.interpreter.parser.expression.AbstractExpressionSubparserWorker;
import org.panda_lang.panda.language.resource.expression.subparsers.assignation.variable.VariableDeclarationUtils;
import org.panda_lang.panda.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.runtime.expression.PandaDynamicExpression;

import java.util.Optional;

public class CastExpressionSubparser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createWorker() {
        return new CastWorker().withSubparser(this);
    }

    @Override
    public int getMinimalRequiredLengthOfSource() {
        return 2;
    }

    @Override
    public String getSubparserName() {
        return "cast";
    }

    private static final class CastWorker extends AbstractExpressionSubparserWorker {

        @Override
        public @Nullable ExpressionResult next(ExpressionContext context, TokenRepresentation token) {
            if (!token.contentEquals(Keywords.AS) || !context.hasResults()) {
                return null;
            }

            Optional<Snippet> typeSource = VariableDeclarationUtils.readType(context.getSynchronizedSource().getAvailableSource());

            if (!typeSource.isPresent()) {
                return ExpressionResult.error("Cannot read type", context.getSynchronizedSource().getAvailableSource());
            }

            ModuleLoader loader = context.getContext().getComponent(UniversalComponents.MODULE_LOADER);
            Optional<ClassPrototypeReference> typeReference = loader.forName(typeSource.get().asSource());

            if (!typeReference.isPresent()) {
                return ExpressionResult.error("Unknown type", context.getSynchronizedSource().getAvailableSource());
            }

            context.getSynchronizedSource().next(typeSource.get().size());
            return ExpressionResult.of(new PandaDynamicExpression(typeReference.get().fetch(), context.popExpression()).toExpression());
        }

    }

}
