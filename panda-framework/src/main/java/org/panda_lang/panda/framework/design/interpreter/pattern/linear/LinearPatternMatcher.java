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

package org.panda_lang.panda.framework.design.interpreter.pattern.linear;

import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionParserException;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.DiffusedSource;
import org.panda_lang.panda.framework.language.resource.syntax.auxiliary.Section;

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

    LinearPatternResult match(Function<DiffusedSource, Object> expressionMatcher) {
        DiffusedSource content = new DiffusedSource(source.toSnippet());
        List<String> identifiers = new ArrayList<>(pattern.getElements().size());
        Map<String, Object> wildcards = new HashMap<>(pattern.getElements().size());

        for (LinearPatternElement element : pattern.getElements()) {
            boolean matched = false;

            try {
                matched = match(expressionMatcher, content, identifiers, wildcards, element);
            } catch (ExpressionParserException e) {
                continue;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (!matched) {
                if (!element.isOptional()) {
                    return LinearPatternResult.NOT_MATCHED;
                }

                content.restore();
            }

            content.backup();
        }

        source.read(content.getIndex());
        return new LinearPatternResult(identifiers, wildcards);
    }

    private boolean match(Function<DiffusedSource, Object> matcher, DiffusedSource content, List<String> identifiers, Map<String, Object> wildcards, LinearPatternElement element) {
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
