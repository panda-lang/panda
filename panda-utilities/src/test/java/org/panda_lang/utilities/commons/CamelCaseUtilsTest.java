/*
 * Copyright (c) 2015-2020 Dzikoysk
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

package org.panda_lang.utilities.commons;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class CamelCaseUtilsTest {

    @Test
    void split() {
        Assertions.assertEquals(Arrays.asList("camel", "Case"), CamelCaseUtils.split("camelCase"));
        Assertions.assertEquals(Arrays.asList("To", "Pascal", "Case"), CamelCaseUtils.split("toPascalCase", StringUtils::capitalize));
    }

}