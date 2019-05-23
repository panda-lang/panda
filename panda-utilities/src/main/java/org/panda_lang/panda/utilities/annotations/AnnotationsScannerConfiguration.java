/*
 * Copyright (c) 2015-2019 Dzikoysk
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
import org.panda_lang.panda.utilities.commons.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Set;

public class AnnotationsScannerConfiguration {

    protected final Set<ClassLoader> classLoaders;
    protected final Set<AnnotationsScannerResource<?>> resources;
    protected final AnnotationsScannerResourceFactory resourceFactory;

    protected AnnotationsScannerLogger logger = new AnnotationsScannerLogger(LoggerFactory.getLogger(AnnotationsScanner.class));
    protected MetadataAdapter<ClassFile, FieldInfo, MethodInfo> metadataAdapter = new JavassistAdapter();

    AnnotationsScannerConfiguration() {
        this.classLoaders = new HashSet<>(2);
        this.resources = new HashSet<>(2);
        this.resourceFactory = new AnnotationsScannerResourceFactory();
    }

    /**
     * Register logger used by AnnotationsScanner
     *
     * @param logger the logger to use, if null, scanner won't log anything
     * @return the configuration instance
     */
    public AnnotationsScannerConfiguration logger(@Nullable Logger logger) {
        this.logger = new AnnotationsScannerLogger(logger);
        return this;
    }

    /**
     * Mute logger by default
     *
     * @return the configuration instance
     */
    public AnnotationsScannerConfiguration muted() {
        this.logger.mute();
        return this;
    }

    /**
     * Include class loaders used by application by default
     *
     * @return the configuration instance
     */
    public AnnotationsScannerConfiguration includeDefaultClassLoaders() {
        return includeClassLoaders(true, this.getClass().getClassLoader(), Thread.currentThread().getContextClassLoader());
    }

    /**
     * Include Java Class Path
     *
     * @return the configuration instance
     */
    public AnnotationsScannerConfiguration includeJavaClassPath() {
        String javaClassPath = System.getProperty("java.class.path");

        if (javaClassPath != null) {
            String[] paths = StringUtils.split(javaClassPath, File.pathSeparator);

            for (String path : paths) {
                includePath(path);
            }
        }

        return this;
    }

    /**
     * Include classes
     *
     * @param classes classes to include
     * @return the configuration instance
     */
    public AnnotationsScannerConfiguration includeClasses(Class<?>... classes) {
        for (Class<?> clazz : classes) {
            includeResources(clazz.getProtectionDomain().getCodeSource().getLocation());
        }

        return this;
    }

    /**
     * Include class loaders
     *
     * @param classLoaders class loaders to include
     * @return the configuration instance
     */
    public AnnotationsScannerConfiguration includeClassLoaders(ClassLoader... classLoaders) {
        return includeClassLoaders(false, classLoaders);
    }

    /**
     * Include specified class loaders
     *
     * @param includeParents if true the configuration will include also parents of the specified class loaders
     * @param classLoaders   class loaders to include
     * @return the configuration instance
     *
     * @see ClassLoader#getParent()
     */
    public AnnotationsScannerConfiguration includeClassLoaders(boolean includeParents, ClassLoader... classLoaders) {
        for (ClassLoader classLoader : classLoaders) {
            if (classLoader == null) {
                continue;
            }

            for (ClassLoader currentClassLoader = classLoader; currentClassLoader != null; currentClassLoader = includeParents ? currentClassLoader.getParent() : null) {
                if (!(currentClassLoader instanceof URLClassLoader)) {
                    continue;
                }

                URL[] urls = ((URLClassLoader) currentClassLoader).getURLs();
                includeResources(urls);
            }
        }

        return this;
    }

    /**
     * Include paths
     *
     * @param paths paths to include
     * @return the configuration instance
     */
    public AnnotationsScannerConfiguration includePath(String... paths) {
        for (String path : paths) {
            try {
                URL url = new File(path).toURI().toURL();
                includeResources(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        return this;
    }

    /**
     * Include URLs
     *
     * @param urls urls to include
     * @return the configuration instance
     */
    public AnnotationsScannerConfiguration includeResources(@Nullable URL... urls) {
        if (urls == null) {
            return this;
        }

        Set<AnnotationsScannerResource<?>> currentResources = new HashSet<>(urls.length);

        for (URL url : urls) {
            AnnotationsScannerResource<?> resource = resourceFactory.createTypedResource(url);

            if (resource == null) {
                logger.warn("Unknown resource: " + url);
                continue;
            }

            currentResources.add(resource);
        }

        resources.addAll(currentResources);
        return this;
    }

    /**
     * Register custom metadata adapter used to gain data from classes without loading them by class loader
     *
     * @param adapter the adapter to use by scanner
     * @return the configuration instance
     */
    public AnnotationsScannerConfiguration metadataAdapter(MetadataAdapter<ClassFile, FieldInfo, MethodInfo> adapter) {
        this.metadataAdapter = adapter;
        return this;
    }

    /**
     * Build AnnotationScanner with current configuration
     *
     * @return created AnnotationScanner
     */
    public AnnotationsScanner build() {
        if (classLoaders.isEmpty()) {
            classLoaders.add(this.getClass().getClassLoader());
        }

        return new AnnotationsScanner(this);
    }

}
