/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.panda.examples.tests

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Test
import org.panda_lang.panda.util.PandaUtils

@CompileStatic
final class CurrentTestExampleTest {

    @Test
    void helloWorld() {
        PandaUtils.load("../examples", "../examples/hello_world.panda")
                .flatMap(application -> application.launch())
                .get()
    }

    @Test
    void literalMethod() {
        PandaUtils.load("../examples/tests", "../examples/tests/literal_methods.panda")
                .flatMap(application -> application.launch())
                .get()
    }

    @Test
    void testCurrentTest() {
        for (int iteration = 0; iteration < 1; iteration++) {
            PandaUtils.load("../examples/tests", "../examples/tests/current_test.panda")
                    .flatMap(application -> application.launch())
                    .get()
        }
    }

    @Test
    void testClassTest() {
        PandaUtils.load("../examples/tests", "../examples/tests/class_test.panda")
                .flatMap(application -> application.launch())
                .get()
    }

}
