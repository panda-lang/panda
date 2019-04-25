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

package org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.extractor;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.pattern.lexical.elements.LexicalPatternWildcard;
import org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.wildcard.WildcardCompiler;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.language.interpreter.token.PandaSnippet;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.TokenDistributor;

class WildcardExtractor extends AbstractElementExtractor<LexicalPatternWildcard> {

    private final WildcardCompiler wildcardCompiler;

    protected WildcardExtractor(ExtractorWorker worker) {
        super(worker);
        this.wildcardCompiler = new WildcardCompiler(worker.pattern);
    }

    @Override
    public ExtractorResult extract(LexicalPatternWildcard wildcard, TokenDistributor distributor) {
        if (!distributor.hasNext()) {
            return new ExtractorResult() /*.addWildcard(wildcard.getName(), new PandaSnippet(Collections.emptyList()))*/;
        }

        int index = distributor.getIndex();
        Object wildcardContent;

        if (!wildcard.hasCondition() && !wildcard.getName().startsWith("*")) {
            wildcardContent = new PandaSnippet(distributor.next());
        }
        else if (wildcard.getData() != null) {
            wildcardContent = matchWildcardWithCondition(wildcard, distributor);

            if (wildcardContent == null || (wildcardContent instanceof Snippet && ((Snippet) wildcardContent).isEmpty()) /*SnippetUtils.isEmpty(wildcardContent)*/) {
                return new ExtractorResult("Empty wildcard with condition: " + wildcard.getData());
            }
        }
        else {
            return new ExtractorResult("Cannot match wildcard");
        }

        //int wildcardSize = wildcardContent != null ? wildcardContent.size() : 0;
        //distributor.setIndex(index + wildcardSize);

        return new ExtractorResult().addWildcard(wildcard.getName(), wildcardContent);
    }

    private @Nullable Object matchWildcardWithCondition(LexicalPatternWildcard wildcard, TokenDistributor distributor) {
        Snippet source = null;
        boolean full = false;

        if (wildcard.getName().startsWith("*")) {
            source = new PandaSnippet(distributor.next(distributor.size() - distributor.getIndex()));
            full = true;
        }

        if (!wildcard.hasCondition()) {
            return source;
        }

        if (source != null) {
            distributor = new TokenDistributor(source);
        }

        Object matched = wildcardCompiler.compile(getWorker().data, wildcard.getCondition(), distributor);

        if (full && matched != null /*&& matched.size() != source.size()*/) {
            return null;
        }

        return matched;
    }

}
