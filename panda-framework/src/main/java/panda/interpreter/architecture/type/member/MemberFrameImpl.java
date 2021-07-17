/*
 * Copyright (c) 2021 dzikoysk
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

package panda.interpreter.architecture.type.member;

import panda.interpreter.architecture.dynamic.AbstractFrame;
import panda.interpreter.architecture.dynamic.Frame;
import panda.interpreter.architecture.statement.StandardizedFramedScope;

public class MemberFrameImpl<T extends StandardizedFramedScope> extends AbstractFrame<T> implements MemberFrame {

    private final Frame instance;

    public MemberFrameImpl(T frame, Frame instance) {
        super(frame);
        this.instance = instance;
    }

    @Override
    public Frame getInstance() {
        return instance;
    }

}
