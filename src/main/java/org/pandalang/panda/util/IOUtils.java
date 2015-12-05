package org.pandalang.panda.util;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

public class IOUtils {

    public static String getContent(File file) {
        StringBuilder sb = new StringBuilder();
        try {
            if (file == null) return "";
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static void overrideFile(File file, String content) {
        try {
            PrintWriter writer = new PrintWriter(file, "UTF-8");
            writer.print(content);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String[] getLines(File file) {
        if (!file.exists()) return new String[0];
        else try {
            List<String> list = Files.readAllLines(file.toPath(), Charset.forName("UTF-8"));
            String[] result = new String[list.size()];
            return list.toArray(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int countLines(String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        } finally {
            is.close();
        }
    }

    public static String patch(String s) {
        return s.replace("\t", "");
    }

    public static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}
