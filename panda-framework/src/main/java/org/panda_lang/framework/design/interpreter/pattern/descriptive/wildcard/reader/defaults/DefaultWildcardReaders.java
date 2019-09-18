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

package org.panda_lang.framework.design.interpreter.pattern.descriptive.wildcard.reader.defaults;

import org.panda_lang.framework.design.interpreter.pattern.descriptive.wildcard.reader.WildcardReader;

import java.util.ArrayList;
import java.util.Collection;

public class DefaultWildcardReaders {

    private static final Collection<WildcardReader> READERS = new ArrayList<>();

    static {
        READERS.add(new TypeReader());
    }

    public static Collection<WildcardReader> getDefaultReaders() {
        return new ArrayList<>(READERS);
    }

}
