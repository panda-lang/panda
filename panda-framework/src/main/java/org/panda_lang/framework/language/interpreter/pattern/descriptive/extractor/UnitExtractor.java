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

package org.panda_lang.framework.language.interpreter.pattern.descriptive.extractor;

import org.panda_lang.framework.language.interpreter.pattern.descriptive.utils.TokenDistributor;
import org.panda_lang.framework.language.interpreter.pattern.lexical.elements.LexicalPatternUnit;

final class UnitExtractor extends AbstractElementExtractor<LexicalPatternUnit> {

    protected UnitExtractor(ExtractorWorker worker) {
        super(worker);
    }

    @Override
    public ExtractorResult extract(LexicalPatternUnit element, TokenDistributor distributor) {
        String unitValue = element.getValue();
        String tokenValue = distributor.next().getValue();

        if (!unitValue.equals(tokenValue)) {
            return new ExtractorResult("Unit does not match");
        }

        return new ExtractorResult().identified(element.getIdentifier());
    }

}
