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

package org.panda_lang.panda.framework.language.resource.parsers.prototype;

import org.panda_lang.panda.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;

import java.util.Optional;

public class ClassPrototypeParserUtils {

    public static void readDeclaration(ParserData data, Snippet classDeclaration) {
        ClassPrototype classPrototype = data.getComponent(ClassPrototypeComponents.CLASS_PROTOTYPE);
        Token next = classDeclaration.getToken(1);

        if (next == null || next.getType() != TokenType.KEYWORD) {
            throw new PandaParserException("Unknown element " + next);
        }

        switch (next.getValue()) {
            case "implements": //temp
            case "extends":
                readExtends(data, classDeclaration, classPrototype);
                break;
            default:
                throw new PandaParserException("Illegal keyword " + next);
        }
    }

    private static void readExtends(ParserData data, Snippet classDeclaration, ClassPrototype prototype) {
        ModuleLoader loader = data.getComponent(UniversalComponents.MODULE_LOADER);

        for (int i = 2; i < classDeclaration.size(); i++) {
            Token classNameToken = classDeclaration.getToken(i);

            if (classNameToken == null) {
                throw new PandaParserFailure("Declaration token not found", data);
            }
            else if (classNameToken.getType() == TokenType.SEPARATOR) {
                continue;
            }
            else if (classNameToken.getType() == TokenType.UNKNOWN) {
                Optional<ClassPrototypeReference> extendedPrototype = loader.forClass(classNameToken.getValue());

                if (!extendedPrototype.isPresent()) {
                    data.setComponent(UniversalComponents.SOURCE_STREAM, new PandaSourceStream(classDeclaration));
                    throw new PandaParserFailure("Class " + classNameToken.getValue() + " not found", data);
                }

                prototype.addExtended(extendedPrototype.get());
                continue;
            }

            break;
        }
    }

}
