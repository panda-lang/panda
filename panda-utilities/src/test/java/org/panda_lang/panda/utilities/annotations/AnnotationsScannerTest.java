/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.utilities.annotations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;

@AnnotationTest
public class AnnotationsScannerTest implements WrappedTestType {

    @Test
    void testScanner() {
        AnnotationsScanner scanner = AnnotationsScanner.createScanner()
                .includeClass(AnnotationsScannerTest.class)
                .build();

        AnnotationsScannerProcess process = scanner.createWorker()
                .addDefaultProjectFilters("org.panda_lang")
                .fetch();

        Collection<Class<?>> classes = process.createSelector()
                .selectTypesAnnotatedWith(AnnotationTest.class);

        Assertions.assertEquals(1, classes.size());
        Assertions.assertEquals(AnnotationsScannerTest.class, classes.iterator().next());
    }

}

@interface AnnotationTest {

}

interface WrappedTestType extends TestType {

}

interface TestType {

}