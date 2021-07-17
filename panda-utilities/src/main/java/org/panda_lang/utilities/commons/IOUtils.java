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

package org.panda_lang.utilities.commons;

import org.jetbrains.annotations.Nullable;
import panda.std.Result;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public final class IOUtils {

    public static final int DEFAULT_BUFFER_SIZE = 8192;

    private IOUtils() { }

    /**
     * Fetch content of the given website
     *
     * @param url the website to fetch
     * @return content of requested page or thrown exception
     */
    public static Result<String, IOException> fetchContent(String url) {
        InputStream stream = null;

        try {
            URLConnection connection = createConnection(url);
            stream = connection.getInputStream();

            String encoding = connection.getContentEncoding();
            Charset charset = encoding == null ? StandardCharsets.UTF_8 : Charset.forName(encoding);

            return IOUtils.convertStreamToString(stream, charset);
        } catch (IOException exception) {
            return Result.error(exception);
        } finally {
            close(stream);
        }
    }

    /**
     * Fetch content of the given website
     *
     * @param url the website to fetch
     * @return content of requested page or thrown exception
     */
    public static Result<InputStream, IOException> fetchContentAsStream(String url) {
        try {
            return Result.ok(createConnection(url).getInputStream());
        } catch (IOException exception) {
            return Result.error(exception);
        }
    }

    private static URLConnection createConnection(String url) throws IOException {
        URLConnection connection = new URL(url).openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        return connection;
    }

    /**
     * Convert {@link java.lang.String} to {@link java.io.InputStream} using {@link java.nio.charset.StandardCharsets#UTF_8}
     *
     * @param str the string to convert
     * @return the result input stream
     */
    public static InputStream convertStringToStream(String str) {
        return new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Convert {@link java.io.InputStream} to {@link java.lang.String} using {@link java.nio.charset.StandardCharsets#UTF_8}
     *
     * @param inputStream the input stream to convert
     * @return converted string or thrown exception
     */
    public static Result<String, IOException> convertStreamToString(InputStream inputStream) {
        return convertStreamToString(inputStream, StandardCharsets.UTF_8);
    }

    /**
     * Convert {@link java.io.InputStream} to {@link java.lang.String}
     *
     * @param inputStream the input stream to convert
     * @param encoding the encoding to use
     * @return converted string or thrown exception
     */
    public static Result<String, IOException> convertStreamToString(InputStream inputStream, Charset encoding) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int length;

            while ((length = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }

            return Result.ok(new String(byteArrayOutputStream.toByteArray(), encoding));
        } catch (IOException exception) {
            return Result.error(exception);
        }
    }

    /**
     * Close resource quietly using {@link org.panda_lang.utilities.commons.IOUtils#close(java.io.Closeable, java.util.function.Consumer)}
     *
     * @param closeable nullable resource to close
     */
    public static void close(@Nullable Closeable closeable) {
        close(closeable, null);
    }

    /**
     * Null-safe close operation
     *
     * @param closeable nullable resource to close
     * @param exceptionConsumer nullable exception consumer
     */
    public static void close(@Nullable Closeable closeable, @Nullable Consumer<IOException> exceptionConsumer) {
        if (closeable == null) {
            return;
        }

        try {
            closeable.close();
        } catch (IOException exception) {
            if (exceptionConsumer != null) {
                exceptionConsumer.accept(exception);
            }
        }
    }

}
