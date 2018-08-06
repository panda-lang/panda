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
import org.panda_lang.panda.utilities.annotations.filters.PackageFileFilter;
import org.panda_lang.panda.utilities.annotations.filters.PublicPseudoClassFilter;
import org.panda_lang.panda.utilities.annotations.filters.SubTypeSelector;
import org.panda_lang.panda.utilities.annotations.filters.TypeClassFilter;

import java.util.Set;

public class AnnotationsScannerTest extends WrappedTestType {

    @Test
    void testScanner() {
        AnnotationsScanner scanner = AnnotationsScanner.builder()
                .includeClassLoaders(this.getClass().getClassLoader())
                .build();

        AnnotationsScannerProcess process = scanner.createWorker()
                .addFileFilters(new PackageFileFilter("org.panda_lang"))
                .addPseudoClassFilters(new PublicPseudoClassFilter())
                .addClassFilters(new TypeClassFilter("AnnotationsScannerTest"))
                .fetch();

        Set<Class<?>> classes = process.createSelector()
                .addPseudoClassSelectors(new SubTypeSelector(TestType.class))
                .select();

        System.out.println(classes);
    }

}

class WrappedTestType extends TestType { }

class TestType { }