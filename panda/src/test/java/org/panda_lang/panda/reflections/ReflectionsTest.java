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

package org.panda_lang.panda.reflections;

import org.junit.Test;
import org.junit.jupiter.api.*;
import org.reflections.*;
import org.reflections.scanners.*;
import org.reflections.util.*;

import java.io.*;
import java.lang.management.*;
import java.net.*;
import java.util.*;

public class ReflectionsTest {

    @Test
    public void testReflectionsLibrary() throws Exception {
        String bootClassPath = ManagementFactory.getRuntimeMXBean().getBootClassPath();
        String[] bootClassPathUrls = bootClassPath.split(Character.toString(File.pathSeparatorChar));
        Collection<URL> urls = new ArrayList<>(bootClassPathUrls.length);

        for (String bootClassPathUrl : bootClassPathUrls) {
            System.out.println(bootClassPathUrl);
            File file = new File(bootClassPathUrl);

            if (!file.exists()) {
                continue;
            }

            urls.add(file.toURI().toURL());
        }

        Configuration configuration = ConfigurationBuilder
                .build("java.lang", new SubTypesScanner(false))
                .addUrls(urls);

        Reflections reflections = new Reflections(configuration);
        Set<Class<?>> classes = reflections.getSubTypesOf(Object.class);

        Assertions.assertNotNull(classes);
        Assertions.assertTrue(classes.size() > 0);
    }

}
