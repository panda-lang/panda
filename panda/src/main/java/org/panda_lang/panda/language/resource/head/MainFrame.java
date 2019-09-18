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

package org.panda_lang.panda.language.resource.head;

import org.panda_lang.framework.design.architecture.dynamic.LivingFrame;
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.framework.language.architecture.dynamic.AbstractLivingFrame;
import org.panda_lang.framework.language.architecture.statement.AbstractFrame;

public final class MainFrame extends AbstractFrame {

    @Override
    public LivingFrame revive(ProcessStack stack, Object instance) {
        return new MainLivingFrame(this);
    }

    @Override
    public String toString() {
        return "main scope";
    }

    public static final class MainLivingFrame extends AbstractLivingFrame<MainFrame> {

        public MainLivingFrame(MainFrame main) {
            super(main);
        }

    }

}
