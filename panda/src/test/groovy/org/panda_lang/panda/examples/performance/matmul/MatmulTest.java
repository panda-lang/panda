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

package org.panda_lang.panda.examples.performance.matmul;

import org.panda_lang.framework.architecture.Application;
import org.panda_lang.panda.utils.PandaUtils;
import org.panda_lang.utilities.commons.function.Option;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MatmulTest {

    // @Test TODO: Arrays not implemented
    void testMatmul() throws Exception {
        for (int iteration = 0; iteration < 1; iteration++) {
            Option<Object> result = PandaUtils.load("../examples/tests/performance", "../examples/tests/performance/matmul.panda")
                    .flatMap(Application::launch)
                    .get();

            assertEquals(-9.3358333, result.get());
        }
    }

}
