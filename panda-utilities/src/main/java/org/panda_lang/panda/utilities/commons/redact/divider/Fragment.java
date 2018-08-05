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

package org.panda_lang.panda.utilities.commons.redact.divider;

public class Fragment {

    private String fragment;
    private String indention;
    private DividerSeparator separator;
    private int inline;
    private int line;

    public Fragment() {
    }

    public Fragment(String fragment) {
        this.fragment = fragment;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public void setInline(int inline) {
        this.inline = inline;
    }

    public void setSeparator(DividerSeparator separator) {
        this.separator = separator;
    }

    public void setIndention(String indention) {
        this.indention = indention;
    }

    public void setFragment(String fragment) {
        this.fragment = fragment;
    }

    public boolean isEmpty() {
        return fragment.isEmpty();
    }

    public String getFull() {
        return getIndention() + getFragment();
    }

    public int getLine() {
        return line;
    }

    public int getInline() {
        return inline;
    }

    public DividerSeparator getSeparator() {
        return separator;
    }

    public String getIndention() {
        return indention;
    }

    public String getFragment() {
        return fragment;
    }

}
