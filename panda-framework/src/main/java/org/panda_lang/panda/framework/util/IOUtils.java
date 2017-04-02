/*
 * Copyright (c) 2015-2017 Dzikoysk
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

package org.panda_lang.panda.framework.util;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class IOUtils {

    public static InputStream convertStringToStream(String str) {
        return new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
    }

    public static String convertStreamToString(InputStream is) {
        try (Scanner s = new Scanner(is).useDelimiter("\\A")) {
            return s.hasNext() ? s.next() : "";
        }
    }

    public static String getURLContent(String s) {
        String body = null;
        InputStream in = null;

        try {
            URL url = new URL(s);
            URLConnection con = url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            in = con.getInputStream();
            String encoding = con.getContentEncoding();
            Charset charset = (encoding == null) ? StandardCharsets.UTF_8 : Charset.forName(encoding);
            body = IOUtils.toString(in, charset);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(in);
        }

        return body;
    }

    public static String toString(InputStream in, String encoding) {
        return toString(in, Charset.forName(encoding));
    }

    public static String toString(InputStream in, Charset encoding) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            byte[] buf = new byte[8192];
            int len;

            while ((len = in.read(buf)) != -1) {
                byteArrayOutputStream.write(buf, 0, len);
            }

            return new String(byteArrayOutputStream.toByteArray(), encoding);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public static void close(Closeable closeable) {
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
