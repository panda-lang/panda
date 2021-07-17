/*
 * Copyright (c) 2021 dzikoysk
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

package panda.interpreter.runtime

import groovy.transform.CompileStatic
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import panda.interpreter.architecture.dynamic.Executable
import panda.interpreter.architecture.statement.AbstractScope
import panda.interpreter.architecture.statement.AbstractStatement
import panda.interpreter.architecture.statement.Scope
import panda.interpreter.source.Location
import panda.interpreter.syntax.head.MainScope
import panda.interpreter.token.PandaLocation

@CompileStatic
final class ProcessStackTest {

    private MainScope main

    @BeforeEach
    void prepare() {
        this.main = new MainScope(PandaLocation.unknownLocation(null, "process-stack-test"))

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
