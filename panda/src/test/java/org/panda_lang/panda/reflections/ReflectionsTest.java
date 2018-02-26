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
        String[] bootClassPathUrls = bootClassPath.split(";");
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
