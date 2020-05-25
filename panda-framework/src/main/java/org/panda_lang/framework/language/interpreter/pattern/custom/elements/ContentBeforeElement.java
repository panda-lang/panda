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

package org.panda_lang.framework.language.interpreter.pattern.custom.elements;

import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.Snippetable;
import org.panda_lang.framework.design.interpreter.token.TokenInfo;
import org.panda_lang.framework.language.interpreter.pattern.custom.CustomPatternElementBuilder;
import org.panda_lang.framework.language.interpreter.token.PandaSnippet;
import org.panda_lang.framework.language.resource.syntax.TokenTypes;
import org.panda_lang.framework.language.resource.syntax.auxiliary.Section;
import org.panda_lang.framework.language.resource.syntax.separator.Separator;

public final class ContentBeforeElement extends CustomPatternElementBuilder<Snippetable, ContentBeforeElement> {

    private ContentBeforeElement(String id) {
        super(id);
    }

    public ContentBeforeElement before(Separator separator) {
        return super.custom((data, source) -> {
            Snippet declaration = new PandaSnippet();

            for (TokenInfo representation : source) {
                if (representation.getType() == TokenTypes.SECTION && representation.toToken(Section.class).getSeparator().equals(separator)) {
                    source.next(-1);
                    break;
                }

                declaration.addToken(representation);
            }

            return declaration;
        });
    }

    public static ContentBeforeElement create(String id) {
        return new ContentBeforeElement(id);
    }

}
