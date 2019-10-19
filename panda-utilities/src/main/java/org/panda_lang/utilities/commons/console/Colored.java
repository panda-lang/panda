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

package org.panda_lang.utilities.commons.console;

import org.panda_lang.utilities.commons.StringUtils;
import org.panda_lang.utilities.commons.text.ContentJoiner;

public class Colored {

    private final String text;
    private String effect = StringUtils.EMPTY;

    private Colored(String text) {
        this.text = text;
    }

    public Colored effect(Effect effect) {
        this.effect += effect.toString();
        return this;
    }

    @Override
    public String toString() {
        return ContentJoiner.on(StringUtils.EMPTY)
                .join(text.split(System.lineSeparator()), line -> effect + line + Effect.RESET)
                .toString();
    }

    public static Colored on(Object text) {
        return new Colored(text.toString());
    }

}
