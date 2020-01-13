/*
 * Copyright (c) 2015-2020 Dzikoysk
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

package org.panda_lang.framework.language.interpreter.pattern.descriptive.wildcard.reader.defaults;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.PandaFrameworkException;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.framework.language.interpreter.pattern.descriptive.utils.TokenDistributor;
import org.panda_lang.framework.language.interpreter.pattern.descriptive.wildcard.reader.WildcardReader;
import org.panda_lang.framework.language.interpreter.token.PandaSnippet;
import org.panda_lang.framework.language.resource.syntax.TokenTypes;
import org.panda_lang.framework.language.resource.syntax.auxiliary.Section;
import org.panda_lang.framework.language.resource.syntax.operator.Operators;
import org.panda_lang.framework.language.resource.syntax.separator.Separators;

final class TypeReader implements WildcardReader<Snippet> {

    @Override
    public boolean match(String context) {
        return context.startsWith("type");
    }

    @Override
    public @Nullable Snippet read(Context context, String content, TokenDistributor distributor) {
        if (!distributor.hasNext()) {
            return null;
        }

        TokenRepresentation type = distributor.getNext();

        //noinspection ConstantConditions
        if (type.getType() != TokenTypes.UNKNOWN && type.getType() != TokenTypes.SECTION) {
            return null;
        }

        distributor.next();
        PandaSnippet tokens = new PandaSnippet(type);

        while (distributor.hasNext()) {
            TokenRepresentation next = readSection(distributor);

            if (next == null) {
                distributor.setIndex(distributor.getIndex() - 1);
                break;
            }

            tokens.addToken(next);
        }

        return tokens;
    }

    private @Nullable TokenRepresentation readSection(TokenDistributor distributor) {
        TokenRepresentation next = distributor.next();

        if (next.getType() != TokenTypes.SECTION) {
            return null;
        }

        Section section = next.toToken();

        if (section.getSeparator().equals(Separators.SQUARE_BRACKET_LEFT)) {
            return next;
        }

        if (section.getSeparator().equals(Operators.GREATER_THAN)) {
            throw new PandaFrameworkException("Angle brackets not implemented yet");
        }

        return null;
    }

}
