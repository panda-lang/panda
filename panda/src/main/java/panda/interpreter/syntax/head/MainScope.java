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

package panda.interpreter.syntax.head;

import panda.interpreter.architecture.dynamic.AbstractFrame;
import panda.interpreter.architecture.dynamic.Frame;
import panda.interpreter.architecture.statement.AbstractStandardizedFramedScope;
import panda.interpreter.architecture.statement.Main;
import panda.interpreter.source.Location;
import panda.interpreter.runtime.ProcessStack;

public final class MainScope extends AbstractStandardizedFramedScope implements Main {

    public MainScope(Location location) {
        super(location);
    }

    @Override
    public Frame revive(ProcessStack stack, Object instance) {
        return new MainFrame(this);
    }

    @Override
    public String toString() {
        return "main scope";
    }

    public static final class MainFrame extends AbstractFrame<MainScope> {

        public MainFrame(MainScope main) {
            super(main);
        }

    }

}
