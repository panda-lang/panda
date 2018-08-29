/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.framework.language.interpreter.parser.implementation.prototype;

import org.panda_lang.panda.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.language.architecture.PandaScript;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.redactor.AbyssRedactor;

public class ClassPrototypeParserUtils {

    public static void readDeclaration(ParserData delegatedInfo) {
        ClassPrototype classPrototype = delegatedInfo.getComponent(ClassPrototypeComponents.CLASS_PROTOTYPE);
        AbyssRedactor redactor = delegatedInfo.getComponent(PandaComponents.REDACTOR);
        TokenizedSource classDeclaration = redactor.get("class-declaration");
        Token next = classDeclaration.getToken(1);

        if (next.getType() != TokenType.KEYWORD) {
            throw new PandaParserException("Unknown element " + next);
        }

        switch (next.getTokenValue()) {
            case "extends":
                for (int i = 2; i < classDeclaration.size(); i++) {
                    Token classNameToken = classDeclaration.getToken(i);

                    if (classNameToken.getType() == TokenType.UNKNOWN) {
                        PandaScript script = delegatedInfo.getComponent(PandaComponents.PANDA_SCRIPT);
                        ModuleLoader registry = script.getModuleLoader();
                        ClassPrototype extendedPrototype = registry.forClass(classNameToken.getTokenValue());

                        if (extendedPrototype == null) {
                            throw new PandaParserException("Class " + classNameToken.getTokenValue() + " not found");
                        }

                        classPrototype.getExtended().add(extendedPrototype);
                        continue;
                    }
                    else if (classNameToken.getType() == TokenType.SEPARATOR) {
                        continue;
                    }

                    break;
                }
                break;
            default:
                throw new PandaParserException("Illegal keyword " + next);
        }
    }

}
