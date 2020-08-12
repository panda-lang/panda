/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.language.interpreter.source;

import org.panda_lang.language.PandaFramework;
import org.panda_lang.language.PandaFrameworkException;
import org.panda_lang.utilities.commons.IOUtils;
import org.panda_lang.utilities.commons.function.ThrowingFunction;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public final class PandaURLSource implements Source {

    private final URL location;

    private PandaURLSource(URL location) {
        if (location == null) {
            throw new IllegalArgumentException("URL cannot be null");
        }

        this.location = location;
    }

    @Override
    public boolean isVirtual() {
        return false;
    }

    @Override
    public String getContent() {
        try (InputStream inputStream = this.location.openStream()) {
            return IOUtils.convertStreamToString(inputStream).orElseThrow(ThrowingFunction.identity());
        } catch (IOException e) {
            throw new PandaFrameworkException(e);
        }
    }

    public URL getLocation() {
        return this.location;
    }

    @Override
    public String getId() {
        return new File(location.getFile()).getAbsolutePath();
    }

    public static PandaURLSource fromResource(String resourcePath) {
        URL resource = PandaFramework.class.getResource(resourcePath);

        if (resource == null) {
            throw new IllegalArgumentException("Resource '" + resourcePath + "' does not exist. Remember that the path should start with '/' separator.");
        }

        return fromUrl(resource);
    }

    public static PandaURLSource fromPath(String path) {
        return fromFile(new File(path));
    }

    public static PandaURLSource fromFile(File file) {
        try {
            return fromUrl(file.toURI().toURL());
        } catch (MalformedURLException e) {
            throw new PandaFrameworkException(e);
        }
    }

    public static PandaURLSource fromUrl(URL url) {
        return new PandaURLSource(url);
    }

}
