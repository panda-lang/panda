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

import org.junit.jupiter.api.Test;
import org.panda_lang.panda.utilities.annotations.monads.filters.JavaFilter;
import org.panda_lang.panda.utilities.annotations.monads.filters.PackageFileFilter;

import java.util.Set;

@AnnotationTest
public class AnnotationsScannerTest extends WrappedTestType {

    @Test
    void testScanner() {
        AnnotationsScanner scanner = AnnotationsScanner.createScanner()
                .includeClassLoaders(false, this.getClass().getClassLoader())
                .build();

        AnnotationsScannerProcess process = scanner.createWorker()
                .addURLFilter(new JavaFilter())
                .addFileFilters(new PackageFileFilter(false, "org.panda_lang"))
                .fetch();

        Set<Class<?>> classes = process.createSelector()
                .selectTypesAnnotatedWith(AnnotationTest.class);

        System.out.println(classes);
    }

}

@interface AnnotationTest { }

class WrappedTestType extends TestType { }

class TestType { }