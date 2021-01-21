/*
 * Copyright (c) 2020 Dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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

package org.panda_lang.panda.language.runtime

import groovy.transform.CompileStatic
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.panda_lang.language.architecture.dynamic.Executable
import org.panda_lang.language.architecture.statement.AbstractScope
import org.panda_lang.language.architecture.statement.AbstractStatement
import org.panda_lang.language.architecture.statement.Scope
import org.panda_lang.language.interpreter.source.Location
import org.panda_lang.language.interpreter.token.PandaLocation
import org.panda_lang.language.runtime.PandaProcess
import org.panda_lang.language.runtime.ProcessStack
import org.panda_lang.panda.language.resource.syntax.head.MainScope

@CompileStatic
final class ProcessStackTest {

    private MainScope main

    @BeforeEach
    void prepare() {
        this.main = new MainScope(PandaLocation.unknownLocation("process-stack-test"))

        Scope block = new ScopeStub(main)
        main.addStatement(block)

        // ~StackOverflowError
        for (int i = 0; i < 32; i++) {
            Scope subBlock = new ScopeStub(block)

            for (int j = 0; j < 32; j++) {
                subBlock.addStatement(new ExecutableStub(subBlock.getSourceLocation()))
            }

            block.addStatement(subBlock)
            block = subBlock
        }

    }

    @Test
    void testProcess() {
        PandaProcess process = new PandaProcess(null, main)
        process.execute()
    }

    private static final class ExecutableStub extends AbstractStatement implements Executable {

        private ExecutableStub(Location location) {
            super(location)
        }

        @Override
        Object execute(ProcessStack stack, Object instance) {
            return null;
        }

    }

    private static final class ScopeStub extends AbstractScope implements Scope {

        protected ScopeStub(Scope parent) {
            super(parent, parent.getSourceLocation())
        }

    }

}
