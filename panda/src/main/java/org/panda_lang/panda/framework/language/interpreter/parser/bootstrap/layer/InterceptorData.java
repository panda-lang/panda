/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.layer;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class InterceptorData {

    private final List<Object> data;

    public InterceptorData() {
        this.data = new ArrayList<>();
    }

    public void addElement(Object element) {
        data.add(element);
    }

    @SuppressWarnings("unchecked")
    public @Nullable <T> T getValue(Class<T> type) {
        for (Object datum : data) {
            if (datum == null) {
                continue;
            }

            if (datum.getClass().isAssignableFrom(type)) {
                return (T) datum;
            }
        }

        return null;
    }

}
