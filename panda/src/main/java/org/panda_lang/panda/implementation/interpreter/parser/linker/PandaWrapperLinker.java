/*
 * Copyright (c) 2015-2016 Dzikoysk
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

package org.panda_lang.panda.implementation.interpreter.parser.linker;

import org.panda_lang.framework.interpreter.parser.linker.WrapperLinker;
import org.panda_lang.framework.structure.Wrapper;

import java.util.Stack;

public class PandaWrapperLinker implements WrapperLinker {

    private final Stack<Wrapper> wrapperStack;

    public PandaWrapperLinker() {
        this.wrapperStack = new Stack<>();
    }

    @Override
    public void pushWrapper(Wrapper wrapper) {
        wrapperStack.push(wrapper);
    }

    @Override
    public Wrapper popWrapper() {
        return wrapperStack.pop();
    }

    @Override
    public Wrapper getCurrentWrapper() {
        return wrapperStack.peek();
    }

    @Override
    public int getNextID() {
        return wrapperStack.size();
    }

}
