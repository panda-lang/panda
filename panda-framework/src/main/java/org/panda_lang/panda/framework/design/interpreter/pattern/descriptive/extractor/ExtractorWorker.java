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

import org.panda_lang.panda.framework.PandaFramework;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.DescriptivePattern;
import org.panda_lang.panda.framework.design.interpreter.pattern.lexical.elements.LexicalPatternElement;
import org.panda_lang.panda.framework.design.interpreter.token.SourceStream;
import org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.utils.TokenDistributor;
import org.panda_lang.panda.utilities.commons.TimeUtils;

import java.util.HashMap;
import java.util.Map;

public class ExtractorWorker {

    public static long fullTime;
    public static Map<String, Long> timeMap = new HashMap<>();

    protected final DescriptivePattern pattern;
    protected final Context context;
    protected final SourceStream source;

    protected final UnitExtractor unitExtractor = new UnitExtractor(this);
    protected final WildcardExtractor wildcardExtractor = new WildcardExtractor(this);
    protected final SectionExtractor sectionExtractor = new SectionExtractor(this);
    protected final VariantExtractor variantExtractor = new VariantExtractor(this);
    protected final NodeExtractor nodeExtractor = new NodeExtractor(this);

    ExtractorWorker(DescriptivePattern pattern, Context context, SourceStream source) {
        this.pattern = pattern;
        this.context = context;
        this.source = source;

        this.wildcardExtractor.initialize();
    }

    protected ExtractorResult extract() {
        long time = System.nanoTime();

        TokenDistributor distributor = new TokenDistributor(source.toSnippet());
        ExtractorResult result = extract(distributor, pattern.getPatternContent());

        if (result.isMatched()) {
            result.withSource(source.read(distributor.getIndex()));
        }

        long period = System.nanoTime() - time;
        fullTime += period;

        String key = pattern.asString();
        Long value = timeMap.computeIfAbsent(key, (k) -> 0L);
        timeMap.put(key, value + period);

        return result;
    }

    protected ExtractorResult extract(TokenDistributor distributor, LexicalPatternElement element) {
        return extractInternal(distributor, element).identified(element.getIdentifier());
    }

    private ExtractorResult extractInternal(TokenDistributor distributor, LexicalPatternElement element) {
        if (element.isUnit()) {
            return unitExtractor.extract(element.toUnit(), distributor);
        }

        if (element.isWildcard()) {
            return wildcardExtractor.extract(element.toWildcard(), distributor);
        }

        if (element.isSection()) {
            return sectionExtractor.extract(element.toSection(), distributor);
        }

        if (element.isVariant()) {
            return variantExtractor.extract(element.toNode(), distributor);
        }

        if (element.isNode()) {
            return nodeExtractor.extract(element.toNode(), distributor);
        }

        return new ExtractorResult("Unknown element: " + element);
    }

    public static void printTimeMap() {
        for (Map.Entry<String, Long> entry : ExtractorWorker.timeMap.entrySet()) {
            PandaFramework.getLogger().debug("  " + entry.getKey() + ": " + TimeUtils.toMilliseconds(entry.getValue()));
        }

        PandaFramework.getLogger().debug("");
    }

}
