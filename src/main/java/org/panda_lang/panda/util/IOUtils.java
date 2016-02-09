package org.panda_lang.panda.util;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

public class IOUtils {

    public static String getContentOfFile(File file) {
        StringBuilder sb = new StringBuilder();
        try {
            if (file == null || !file.exists()) {
                return null;
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

    public static String[] getContentAsLines(File file) {
        if (!file.exists()) {
            return new String[0];
        }
        else {
            try {
                List<String> list = Files.readAllLines(file.toPath(), Charset.forName("UTF-8"));
                String[] result = new String[list.size()];
                return list.toArray(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static int countLines(String filename) throws IOException {
        try (InputStream is = new BufferedInputStream(new FileInputStream(filename))) {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars;
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
        }
    }

    public static String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}
