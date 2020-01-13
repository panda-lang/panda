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

package org.panda_lang.panda.language.interpreter.messenger.formatters;

import org.panda_lang.framework.design.interpreter.messenger.MessengerTypeFormatter;
import org.panda_lang.framework.design.interpreter.source.IndicatedSource;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.Snippetable;
import org.panda_lang.panda.language.interpreter.messenger.MessengerDataFormatter;
import org.panda_lang.utilities.commons.StringUtils;
import org.panda_lang.utilities.commons.console.Colored;
import org.panda_lang.utilities.commons.console.Effect;

public final class IndicatedSourceFormatter implements MessengerDataFormatter<IndicatedSource> {

    @Override
    public void onInitialize(MessengerTypeFormatter<IndicatedSource> typeFormatter) {
        typeFormatter
                .register("{{location}}", (formatter, indicatedSource) -> {
                    return indicatedSource.getIndicated().getLocation();
                })
                .register("{{line}}", (formatter, indicatedSource) -> {
                    return indicatedSource.getIndicated().getLocation().getLine() < 0 ? "?" : indicatedSource.getIndicated().getLocation().getDisplayLine();
                })
                .register("{{index}}", (formatter, indicatedSource) -> {
                    return indicatedSource.getIndicated().getLocation().getIndex();
                })
                .register("{{source}}", (formatter, indicatedSource) -> {
                    String source = getCurrentLine(indicatedSource).toString();
                    String element = getCurrentLine(indicatedSource, indicatedSource.getIndicated()).toString();

                    int index = source.indexOf(element);
                    int endIndex = index + element.length();

                    String content = index < 0 ? source : source.substring(0, index)
                            + Colored.on(source.substring(index, endIndex)).effect(Effect.RED)
                            + source.substring(endIndex);

                    if (content.isEmpty()) {
                        content = Colored.on("<omitted>").effect(Effect.BOLD).toString();
                    }

                    return content;
                })
                .register("{{marker}}", (formatter, fragment) -> {
                    String source = getCurrentLine(fragment).toString();
                    String element = fragment.getIndicated().toString();

                    return StringUtils.buildSpace(source.indexOf(element)) + Colored.on("^").effect(Effect.BOLD);
                });
    }

    private Snippet getCurrentLine(IndicatedSource indicatedSource) {
        return getCurrentLine(indicatedSource, indicatedSource.getSource());
    }

    private Snippet getCurrentLine(IndicatedSource indicatedSource, Snippetable source) {
        return source.toSnippet().getLine(indicatedSource.getIndicated().getFirst().getLocation().getLine());
    }

    @Override
    public Class<IndicatedSource> getType() {
        return IndicatedSource.class;
    }

}
