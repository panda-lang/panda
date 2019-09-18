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

package org.panda_lang.framework.language.interpreter.source;

import org.panda_lang.framework.PandaFrameworkException;
import org.panda_lang.framework.design.interpreter.source.SourceFragment;
import org.panda_lang.framework.design.interpreter.source.SourceLocation;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.Snippetable;

public final class PandaSourceFragment implements SourceFragment {

    private final String location;
    private final int line;
    private final int index;
    private final Snippet fragment;
    private final Snippet indicatedFragment;

    public PandaSourceFragment(SourceLocation location, Snippetable fragment, Snippetable indicatedFragment) {
        this(location.getSource().getTitle(), location.getLine(), location.getIndex(), fragment, indicatedFragment);
    }

    public PandaSourceFragment(String location, int line, int index, Snippetable fragment, Snippetable indicatedFragment) {
        this.fragment = fragment.toSnippet();

        if (this.fragment.isEmpty()) {
            throw new PandaFrameworkException("Source snippet cannot be empty");
        }

        this.indicatedFragment = indicatedFragment.toSnippet();

        if (this.indicatedFragment.isEmpty()) {
            throw new PandaFrameworkException("Indicated snippet cannot be empty");
        }

        this.location = location;
        this.line = line;
        this.index = index;
    }

    @Override
    public Snippet getIndicatedFragment() {
        return indicatedFragment;
    }

    @Override
    public Snippet getFragment() {
        return fragment;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public int getLine() {
        return line;
    }

    @Override
    public String getLocation() {
        return location;
    }

}
