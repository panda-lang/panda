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

package org.panda_lang.utilities.commons

import groovy.transform.CompileStatic;
import javassist.CtClass;
import javassist.NotFoundException;
import org.junit.jupiter.api.Test


import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertNotNull;

@CompileStatic
final class ClassPoolUtilsTest {

    @Test
    void toCtClasses() throws NotFoundException {
        Class<?>[] classes = [ Object.class, ClassPoolUtilsTest.class ]
        CtClass[] ctClasses = ClassPoolUtils.toCt(classes)

        assertNotNull ctClasses
        assertEquals classes.length, ctClasses.length
        assertEquals classes[0].getSimpleName(), ctClasses[0].getSimpleName()
        assertEquals classes[1].getSimpleName(), ctClasses[1].getSimpleName()
    }

    @Test
    void get() throws NotFoundException {
        CtClass ctClass = ClassPoolUtils.get(ClassPoolUtilsTest.class)
        assertNotNull(ctClass)
        assertEquals(ClassPoolUtilsTest.class.getSimpleName(), ctClass.getSimpleName())
    }

}