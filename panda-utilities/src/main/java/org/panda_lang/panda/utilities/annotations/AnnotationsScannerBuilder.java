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

import javassist.bytecode.ClassFile;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.MethodInfo;
import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.utilities.annotations.adapter.JavassistAdapter;
import org.panda_lang.panda.utilities.annotations.adapter.MetadataAdapter;
import org.panda_lang.panda.utilities.annotations.resource.AnnotationsScannerResourceFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Set;

public class AnnotationsScannerBuilder {

    private final Set<AnnotationsScannerResource<?>> resources;
    private final AnnotationsScannerResourceFactory resourceFactory;
    private MetadataAdapter<ClassFile, FieldInfo, MethodInfo> metadataAdapter;

    AnnotationsScannerBuilder() {
        this.resources = new HashSet<>(2);
        this.resourceFactory = new AnnotationsScannerResourceFactory();
        this.metadataAdapter = new JavassistAdapter();
    }

    public AnnotationsScanner build() {
        return new AnnotationsScanner(resources, metadataAdapter);
    }

    public AnnotationsScannerBuilder metadataAdapter(MetadataAdapter<ClassFile, FieldInfo, MethodInfo> adapter) {
        this.metadataAdapter = adapter;
        return this;
    }

    public AnnotationsScannerBuilder includeClassLoaders(ClassLoader... classLoaders) {
        for (ClassLoader classLoader : classLoaders) {
            includeClassLoader(classLoader);
        }

        return this;
    }

    public AnnotationsScannerBuilder includeDefaultClassLoaders() {
        return includeClassLoaders(this.getClass().getClassLoader(), Thread.currentThread().getContextClassLoader());
    }

    public AnnotationsScannerBuilder includeJavaClassPath() {
        String javaClassPath = System.getProperty("java.class.path");

        if (javaClassPath != null) {
            String[] paths = javaClassPath.split(File.pathSeparator);

            for (String path : paths) {
                includePath(path);
            }
        }

        return this;
    }

    public AnnotationsScannerBuilder includePath(String path) {
        try {
            URL url = new File(path).toURI().toURL();
            includeResources(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return this;
    }

    private void includeClassLoader(@Nullable ClassLoader classLoader) {
        if (classLoader == null) {
            return;
        }

        for (ClassLoader currentClassLoader = classLoader; currentClassLoader != null; currentClassLoader = currentClassLoader.getParent()) {
            if (!(currentClassLoader instanceof URLClassLoader)) {
                continue;
            }

            URL[] urls = ((URLClassLoader) currentClassLoader).getURLs();
            includeResources(urls);
        }
    }

    private void includeResources(@Nullable URL... urls) {
        if (urls == null) {
            return;
        }

        Set<AnnotationsScannerResource<?>> currentResources = new HashSet<>(urls.length);

        for (URL url : urls) {
            AnnotationsScannerResource<?> resource = resourceFactory.createTypedResource(url);

            if (resource == null) {
                // log unknown resource
                continue;
            }

            currentResources.add(resource);
        }

        resources.addAll(currentResources);
    }

}
