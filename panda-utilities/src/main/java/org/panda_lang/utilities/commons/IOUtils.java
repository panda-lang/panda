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

package org.panda_lang.utilities.commons;

import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public final class IOUtils {

    private IOUtils() { }

    public static InputStream convertStringToStream(String str) {
        return new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
    }

    public static @Nullable String convertStreamToString(InputStream inputStream) {
        return toString(inputStream, StandardCharsets.UTF_8);
    }

    public static @Nullable String getURLContent(String s) {
        String body = null;
        InputStream stream = null;

        try {
            URLConnection connection = new URL(s).openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            stream = connection.getInputStream();

            String encoding = connection.getContentEncoding();
            Charset charset = encoding == null ? StandardCharsets.UTF_8 : Charset.forName(encoding);
            body = IOUtils.toString(stream, charset);

            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(stream);
        }

        return body;
    }

    public static @Nullable String toString(InputStream in, Charset encoding) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[8192];
            int length;

            while ((length = in.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }

            return new String(byteArrayOutputStream.toByteArray(), encoding);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public static void close(@Nullable Closeable closeable) {
        if (closeable == null) {
            return;
        }

        try {
            closeable.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
