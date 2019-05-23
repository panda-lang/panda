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

package org.panda_lang.panda.framework.language.resource.parsers.expression.subparsers.assignation.subparsers.array;

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.module.ModuleLoaderUtils;
import org.panda_lang.panda.framework.language.architecture.prototype.array.ArrayClassPrototype;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;

public class ArrayValueAccessorUtils {

    public static ArrayValueAccessor of(ParserData data, Snippet source, Expression instance, Expression index, ArrayValueAccessor.ArrayValueAccessorAction action) {
        if (!instance.getReturnType().isArray()) {
            throw new PandaParserFailure("Cannot use index on non-array type (" + instance.getReturnType() + ")", data, source);
        }

        ArrayClassPrototype arrayPrototype = (ArrayClassPrototype) instance.getReturnType();

        if (arrayPrototype == null) {
            throw new PandaParserFailure("Cannot locate array class", data, source);
        }

        ClassPrototypeReference type = ModuleLoaderUtils.getReferenceOrThrow(data, arrayPrototype.getType(), "Cannot locate type of the array", source);
        return new ArrayValueAccessor(arrayPrototype, type.fetch(), instance, index, action);
    }

}
