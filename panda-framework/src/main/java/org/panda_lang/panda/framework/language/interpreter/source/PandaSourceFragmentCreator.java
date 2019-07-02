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

package org.panda_lang.panda.framework.language.interpreter.source;

import org.jetbrains.annotations.NotNull;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.source.SourceFragment;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippetable;

import java.util.function.Function;

public final class PandaSourceFragmentCreator<T> {

    private final Function<SourceFragment, T> mapper;
    private SourceFragment sourceFragment;

    public PandaSourceFragmentCreator(Function<SourceFragment, T> mapper) {
        this.mapper = mapper;
    }

    public PandaSourceFragmentCreator<T> ofStreamOrigin(Context context, Snippetable indicated) {
        return of(context.getComponent(UniversalComponents.STREAM).getOriginalSource(), indicated);
    }

    public PandaSourceFragmentCreator<T> ofOriginals(Context context) {
        return ofOriginalSource(context, context.getComponent(UniversalComponents.STREAM).getOriginalSource());
    }

    public PandaSourceFragmentCreator<T> ofOriginalSource(Context context, Snippetable indicated) {
        return of(context.getComponent(UniversalComponents.SOURCE), indicated);
    }

    public PandaSourceFragmentCreator<T> ofCurrentStream(Context context, Snippetable indicated) {
        return of(context.getComponent(UniversalComponents.STREAM), indicated);
    }

    public PandaSourceFragmentCreator<T> ofIndicated(@NotNull Snippetable indicatedFragment) {
        return of(indicatedFragment, indicatedFragment);
    }

    public PandaSourceFragmentCreator<T> of(Snippetable fragment, Snippetable indicatedFragment) {
        return of(fragment, indicatedFragment.toSnippet());
    }

    public PandaSourceFragmentCreator<T> of(Snippetable fragment, Snippet indicatedFragment) {
        this.sourceFragment = new PandaSourceFragment(indicatedFragment.getCurrentLocation(), fragment, indicatedFragment);
        return this;
    }

    public T create() {
        return mapper.apply(sourceFragment);
    }

}
