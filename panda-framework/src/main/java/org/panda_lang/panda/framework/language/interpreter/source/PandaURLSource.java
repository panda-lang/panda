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

package org.panda_lang.panda.framework.language.interpreter.source;

import org.panda_lang.panda.framework.PandaFrameworkException;
import org.panda_lang.panda.utilities.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class PandaURLSource {

    private final URL location;

    private PandaURLSource(URL location) {
        this.location = location;
    }

    public String getContent() {
        try (InputStream inputStream = this.location.openStream()) {
            return IOUtils.convertStreamToString(inputStream);
        } catch (IOException e) {
            throw new PandaFrameworkException(e);
        }
    }

    public URL getLocation() {
        return this.location;
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
