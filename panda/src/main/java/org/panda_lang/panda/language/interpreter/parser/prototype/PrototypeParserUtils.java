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

package org.panda_lang.panda.language.interpreter.parser.prototype;

import org.panda_lang.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.language.architecture.prototype.PrototypeComponents;
import org.panda_lang.framework.design.architecture.prototype.Reference;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.token.Token;
import org.panda_lang.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.framework.design.interpreter.token.TokenType;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.language.architecture.prototype.StateComparator;
import org.panda_lang.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.framework.language.interpreter.parser.PandaParserFailure;

import java.util.Optional;

final class PrototypeParserUtils {

    private PrototypeParserUtils() { }

    public static void readDeclaration(Context context, Snippet classDeclaration) {
        Prototype classPrototype = context.getComponent(PrototypeComponents.PROTOTYPE);
        Token next = classDeclaration.get(1);

        if (next == null || next.getType() != TokenType.KEYWORD) {
            throw new PandaParserException("Unknown element " + next);
        }

        switch (next.getValue()) {
            case "implements": //temp
            case "extends":
                readExtends(context, classDeclaration, classPrototype);
                break;
            default:
                throw new PandaParserException("Illegal keyword " + next);
        }
    }

    private static void readExtends(Context context, Snippet classDeclaration, Prototype prototype) {
        ModuleLoader loader = context.getComponent(Components.MODULE_LOADER);

        for (int i = 2; i < classDeclaration.size(); i++) {
            TokenRepresentation classNameToken = classDeclaration.get(i);

            if (classNameToken == null) {
                throw new PandaParserFailure(context, classDeclaration, "Declaration token not found");
            }
            else if (classNameToken.getType() == TokenType.SEPARATOR) {
                continue;
            }
            else if (classNameToken.getType() == TokenType.UNKNOWN) {
                Optional<Reference> extendedPrototype = loader.forName(classNameToken.getValue());

                if (extendedPrototype.isPresent()) {
                    StateComparator.requireInheritance(context, extendedPrototype.get(), classNameToken);
                    prototype.addSuper(extendedPrototype.get());
                    continue;
                }

                throw new PandaParserFailure(context, classDeclaration,
                        "Class " + classNameToken.getValue() + " not found",
                        "Make sure that the name does not have a typo and module which should contain that class is imported"
                );
            }

            break;
        }
    }

}
