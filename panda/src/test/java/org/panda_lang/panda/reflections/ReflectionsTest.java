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

import org.junit.Assert;
import org.junit.Test;
import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ConfigurationBuilder;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

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

        Assert.assertNotNull(classes);
        Assert.assertEquals(true, classes.size() > 0);
    }

}
