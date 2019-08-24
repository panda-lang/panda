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

package org.panda_lang.panda.framework.language.interpreter.messenger.formatters;

import org.panda_lang.panda.framework.design.interpreter.messenger.MessengerTypeFormatter;
import org.panda_lang.panda.framework.language.interpreter.messenger.MessengerDataFormatter;
import org.panda_lang.panda.framework.design.interpreter.source.SourceFragment;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.utilities.commons.StringUtils;
import org.panda_lang.panda.utilities.commons.console.Colored;
import org.panda_lang.panda.utilities.commons.console.Effect;

public final class SourceFragmentFormatter implements MessengerDataFormatter<SourceFragment> {

    @Override
    public void onInitialize(MessengerTypeFormatter<SourceFragment> typeFormatter) {
        typeFormatter
                .register("{{location}}", (formatter, fragment) -> fragment.getLocation())
                .register("{{line}}", (formatter, fragment) -> fragment.getLine() < 0 ? "?" : fragment.getLine() + 1)
                .register("{{index}}", (formatter, fragment) -> fragment.getIndicatedFragment().getLocation().getIndex())
                .register("{{source}}", (formatter, fragment) -> {
                    String source = getCurrentLine(fragment).toString();
                    String element = fragment.getIndicatedFragment().toString();

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
                    String element = fragment.getIndicatedFragment().toString();

                    return StringUtils.buildSpace(source.indexOf(element)) + "^";
                });
    }

    private Snippet getCurrentLine(SourceFragment fragment) {
        return fragment.getFragment().getLine(fragment.getIndicatedFragment().getLocation().getLine());
    }

    @Override
    public Class<SourceFragment> getType() {
        return SourceFragment.class;
    }

}
