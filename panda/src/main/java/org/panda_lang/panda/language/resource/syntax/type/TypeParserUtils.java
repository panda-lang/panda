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

package org.panda_lang.panda.language.resource.syntax.type;

import org.panda_lang.utilities.commons.function.Option;
import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.language.interpreter.parser.Components;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.interpreter.token.Snippetable;
import org.panda_lang.language.architecture.type.utils.StateComparator;
import org.panda_lang.language.architecture.type.utils.TypeDeclarationUtils;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.language.interpreter.token.SynchronizedSource;
import org.panda_lang.language.resource.syntax.separator.Separators;

import java.util.ArrayList;
import java.util.Collection;

final class TypeParserUtils {

    private TypeParserUtils() { }

    public static void appendExtended(Context context, Type type, Snippetable typeSource) {
        String name = typeSource.toString();
        Option<Type> extendedType = context.getComponent(Components.IMPORTS).forName(name);

        if (extendedType.isDefined()) {
            StateComparator.requireInheritance(context, extendedType.get(), typeSource);
            type.addBase(extendedType.get());
            return;
        }

        throw new PandaParserFailure(context, typeSource,
                "Type " + name + " not found",
                "Make sure that the name does not have a typo and module which should contain that class is imported"
        );
    }

    public static Collection<Snippetable> readTypes(SynchronizedSource source) {
        Collection<Snippetable> types = new ArrayList<>(1);

        while (source.hasNext()) {
            if (!types.isEmpty()) {
                if (!source.getNext().equals(Separators.COMMA)) {
                    break;
                }

                source.next();
            }

            Option<Snippet> type = TypeDeclarationUtils.readType(source).peek(types::add);

            if (!type.isDefined()) {
                break;
            }
        }

        return types;
    }

}
