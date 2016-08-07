package org.panda_lang.core.util;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class IOUtils {

    public static String getURLContent(String s) {
        String body = null;
        InputStream in = null;

        try {
            URL url = new URL(s);
            URLConnection con = url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            in = con.getInputStream();
            String encoding = con.getContentEncoding();
            encoding = encoding == null ? "UTF-8" : encoding;
            body = IOUtils.toString(in, encoding);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(in);
        }
        return body;
    }

    public static String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static String toString(InputStream in, String encoding) {
        ByteArrayOutputStream byteArrayOutputStream = null;

        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buf = new byte[8192];
            int len;
            while ((len = in.read(buf)) != -1) {
                byteArrayOutputStream.write(buf, 0, len);
            }
            return new String(byteArrayOutputStream.toByteArray(), encoding);
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            close(byteArrayOutputStream);
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
