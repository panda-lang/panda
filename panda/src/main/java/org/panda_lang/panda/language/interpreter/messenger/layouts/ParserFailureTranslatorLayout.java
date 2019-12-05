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

package org.panda_lang.panda.language.interpreter.messenger.layouts;

import org.panda_lang.framework.design.interpreter.messenger.MessengerFormatter;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.source.Source;
import org.panda_lang.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.framework.language.interpreter.source.PandaSource;
import org.panda_lang.framework.language.interpreter.source.PandaURLSource;
import org.panda_lang.panda.language.interpreter.messenger.PandaTranslatorLayout;
import org.slf4j.event.Level;

import java.util.Map;

public final class ParserFailureTranslatorLayout extends AbstractInterpreterFailureTranslatorLayout<PandaParserFailure> implements PandaTranslatorLayout<PandaParserFailure> {

    @Override
    public void onHandle(MessengerFormatter formatter, PandaParserFailure element, Map<String, Object> context) {
        super.onHandle(formatter, element, context);
        context.put("data", element.getContext());

        element.getContext()
                .getComponent(Components.GENERATION)
                .getCurrentCycle()
                .ifPresent(cycle -> context.put("cycle", cycle));
    }

    @Override
    public Source getTemplateSource() {
        return new PandaSource(PandaURLSource.fromResource("/default-failure-template.messenger"));
    }

    @Override
    public Class<PandaParserFailure> getType() {
        return PandaParserFailure.class;
    }

}
