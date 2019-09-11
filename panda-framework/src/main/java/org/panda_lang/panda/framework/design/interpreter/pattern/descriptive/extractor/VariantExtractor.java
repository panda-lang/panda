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

import org.panda_lang.panda.framework.design.interpreter.pattern.lexical.elements.LexicalPatternElement;
import org.panda_lang.panda.framework.design.interpreter.pattern.lexical.elements.LexicalPatternNode;
import org.panda_lang.panda.language.interpreter.token.distributors.TokenDistributor;

class VariantExtractor extends AbstractElementExtractor<LexicalPatternNode> {

    protected VariantExtractor(ExtractorWorker worker) {
        super(worker);
    }

    @Override
    public ExtractorResult extract(LexicalPatternNode element, TokenDistributor distributor) {
        if (!element.isVariant()) {
            throw new RuntimeException("The specified node is not marked as a variant node");
        }

        int index = distributor.getIndex();

        for (LexicalPatternElement variantElement : element.getElements()) {
            ExtractorResult result = super.getWorker().extract(distributor, variantElement);

            if (result.isMatched()) {
                return result.identified(variantElement.getIdentifier());
            }

            distributor.setIndex(index);
        }

        return new ExtractorResult("Variant does not matched");
    }

}
