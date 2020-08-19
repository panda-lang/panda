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

package org.panda_lang.panda.examples.tests;

import org.junit.jupiter.api.Test;
import org.panda_lang.language.architecture.Application;
import org.panda_lang.panda.util.PandaUtils;

class CurrentTestExampleTest {

    @Test
    void helloWorld() {
        PandaUtils.load("../examples", "../examples/hello_world.panda")
                .flatMap(Application::launch)
                .get();
    }

    @Test
    void literalMethod() {
        PandaUtils.load("../examples/tests", "../examples/tests/literal_methods.panda")
                .flatMap(Application::launch)
                .get();
    }

    @Test
    void testCurrentTest() {
        for (int i = 0; i < 1; i++) {
            PandaUtils.load("../examples/tests", "../examples/tests/current_test.panda")
                    .flatMap(Application::launch)
                    .get();
        }
    }

    @Test
    void testClassTest() {
        PandaUtils.load("../examples/tests", "../examples/tests/class_test.panda")
                .flatMap(Application::launch)
                .get();
    }

}
