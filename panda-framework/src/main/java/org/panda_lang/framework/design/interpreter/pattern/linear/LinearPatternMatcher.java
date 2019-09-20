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

package org.panda_lang.framework.design.interpreter.pattern.linear;

import org.panda_lang.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.framework.design.interpreter.token.TokenType;
import org.panda_lang.framework.design.interpreter.token.SourceStream;
import org.panda_lang.framework.language.interpreter.parser.expression.PandaExpressionParserFailure;
import org.panda_lang.framework.language.interpreter.token.SynchronizedSource;
import org.panda_lang.framework.language.resource.syntax.auxiliary.Section;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

class LinearPatternMatcher {

    private final LinearPattern pattern;
    private final SourceStream source;

    LinearPatternMatcher(LinearPattern pattern, SourceStream source) {
        this.pattern = pattern;
        this.source = source;
    }

    LinearPatternResult match(Function<SynchronizedSource, Object> expressionMatcher) {
        SynchronizedSource content = new SynchronizedSource(source.toSnippet());
        List<String> identifiers = new ArrayList<>(pattern.getElements().size());
        Map<String, Object> wildcards = new HashMap<>(pattern.getElements().size());

        for (LinearPatternElement element : pattern.getElements()) {
            boolean matched = false;

            try {
                matched = match(expressionMatcher, content, identifiers, wildcards, element);
            } catch (PandaExpressionParserFailure e) {
                continue;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (!matched) {
                if (!element.isOptional()) {
                    return LinearPatternResult.NOT_MATCHED;
                }

                content.setIndex(content.getCachedIndex());
            }

            content.cacheIndex();
        }

        return new LinearPatternResult(source.read(content.getIndex()), identifiers, wildcards);
    }

    private boolean match(Function<SynchronizedSource, Object> matcher, SynchronizedSource content, List<String> identifiers, Map<String, Object> wildcards, LinearPatternElement element) {
        if (!content.hasNext()) {
            return false;
        }

        Optional<String> identifier = element.getIdentifier();

        if (element.isUnit()) {
            identifier.ifPresent(identifiers::add);
            return content.next().getValue().equals(element.getValue());
        }

        if (element.isSection()) {
            SectionLinearPatternElement sectionElement = (SectionLinearPatternElement) element;
            TokenRepresentation next = content.next();

            if (next.getType() != TokenType.SECTION) {
                return false;
            }

            Section section = next.toToken();

            if (!section.getSeparator().equals(sectionElement.getSeparator())) {
                return false;
            }

            if (identifier.isPresent()) {
                identifiers.add(identifier.get());
                wildcards.put(identifier.get(), section.getContent());
            }

            return true;
        }

        if (element.isWildcard()) {
            WildcardLinearPatternElement wildcard = (WildcardLinearPatternElement) element;

            Object value = null;

            if (wildcard.getType() == WildcardLinearPatternElement.Type.DEFAULT) {
                value = content.next();
            }

            if (wildcard.getType() == WildcardLinearPatternElement.Type.EXPRESSION) {
                value = matcher.apply(content);
            }

            if (value != null && identifier.isPresent()) {
                wildcards.put(identifier.get(), value);
                identifiers.add(identifier.get());
            }

            return value != null;
        }

        return false;
    }

}