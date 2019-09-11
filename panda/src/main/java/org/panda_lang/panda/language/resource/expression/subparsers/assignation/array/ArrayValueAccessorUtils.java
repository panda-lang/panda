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

package org.panda_lang.panda.language.resource.expression.subparsers.assignation.array;

import org.panda_lang.panda.language.architecture.dynamic.accessor.Accessor;
import org.panda_lang.panda.language.architecture.dynamic.assigner.Assigner;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.token.Snippetable;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.language.architecture.dynamic.accessor.ArrayAccessor;
import org.panda_lang.panda.language.architecture.prototype.array.ArrayClassPrototype;
import org.panda_lang.panda.language.interpreter.parser.PandaParserFailure;

public class ArrayValueAccessorUtils {

    public static Assigner of(Context context, Snippetable source, Expression instance, Expression index, Expression value) {
        return of(context, source, instance, index).toAssigner(value);
    }

    public static Accessor<?> of(Context context, Snippetable source, Expression instance, Expression index) {
        if (!instance.getReturnType().isArray()) {
            throw PandaParserFailure.builder("Cannot use index on non-array type (" + instance.getReturnType() + ")", context)
                    .withStreamOrigin(source)
                    .build();
        }

        ArrayClassPrototype arrayPrototype = (ArrayClassPrototype) instance.getReturnType();

        if (arrayPrototype == null) {
            throw PandaParserFailure.builder("Cannot locate array class", context)
                    .withStreamOrigin(source)
                    .build();
        }

        return new ArrayAccessor(instance, index);
    }

}
