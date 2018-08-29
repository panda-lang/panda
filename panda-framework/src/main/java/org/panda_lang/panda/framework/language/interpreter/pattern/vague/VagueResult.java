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

package org.panda_lang.panda.framework.language.interpreter.pattern.vague;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VagueResult {

    private final List<VagueElement> elements;
    private boolean succeeded;

    public VagueResult() {
        this.elements = new ArrayList<>(3);
    }

    public void succeed() {
        this.succeeded = true;
    }

    public int size() {
        return elements.size();
    }

    protected void addElement(VagueElement element) {
        this.elements.add(element);
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    public @Nullable String get(int index) {
        if (index < 0 || index > size() - 1) {
            return null;
        }

        return elements.get(index).toString();
    }

    public List<VagueElement> getElements() {
        return elements;
    }

}
