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

package panda.interpreter.source;

import panda.interpreter.PandaFrameworkException;
import panda.interpreter.architecture.module.Module;
import panda.utilities.IOUtils;
import panda.utilities.ValidationUtils;
import panda.std.function.ThrowingFunction;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public final class URLSource implements Source {

    private final Module module;
    private final URL location;

    private URLSource(Module module, URL location) {
        this.module = ValidationUtils.notNull(module, "Module cannot be null");
        this.location = ValidationUtils.notNull(location, "URL cannot be null");
    }

    public static URLSource fromFile(Module module, File file) {
        try {
            return fromUrl(module, file.toURI().toURL());
        } catch (MalformedURLException malformedURLException) {
            throw new PandaFrameworkException(malformedURLException);
        }
    }

    public static URLSource fromUrl(Module module, URL url) {
        return new URLSource(module, url);
    }

    @Override
    public String getContent() {
        try (InputStream inputStream = this.location.openStream()) {
            return IOUtils.convertStreamToString(inputStream).orElseThrow(ThrowingFunction.identity());
        } catch (IOException ioException) {
            throw new PandaFrameworkException(ioException);
        }
    }

    @Override
    public Module getModule() {
        return module;
    }

    @Override
    public boolean isVirtual() {
        return false;
    }

    public URL getLocation() {
        return this.location;
    }

    @Override
    public String getId() {
        return new File(location.getFile()).getAbsolutePath();
    }

}
