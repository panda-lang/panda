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

package org.panda_lang.panda.language.runtime;

import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.panda_lang.panda.framework.design.architecture.dynamic.Block;
import org.panda_lang.panda.framework.design.architecture.dynamic.Executable;
import org.panda_lang.panda.framework.design.runtime.ProcessStack;
import org.panda_lang.panda.language.architecture.dynamic.DefaultBlock;
import org.panda_lang.panda.language.architecture.statement.AbstractStatement;
import org.panda_lang.panda.language.resource.head.MainFrame;

class ProcessStackTest {

    private MainFrame main;

    @BeforeEach
    void prepare() {
        this.main = new MainFrame();

        Block block = new DefaultBlock(main);
        main.addStatement(block);

        // ~StackOverflowError
        for (int i = 0; i < 1024; i++) {
            Block subBlock = new DefaultBlock(block);

            for (int j = 0; j < 1024; j++) {
                subBlock.addStatement(new ExecutableStub());
            }

            block.addStatement(subBlock);
            block = subBlock;
        }

    }

    @Test
    void testProcess() {
        PandaProcess process = new PandaProcess(null, main);
        process.execute();
    }

    private static class ExecutableStub extends AbstractStatement implements Executable {

        @Override
        public @Nullable Object execute(ProcessStack stack, Object instance) {
            return null;
        }

    }

}
