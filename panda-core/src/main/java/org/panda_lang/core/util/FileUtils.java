package org.panda_lang.core.util;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class FileUtils {

    public static Collection<File> findFilesByExtension(File directory, String extension) {
        Collection<File> files = new ArrayList<>();

        if (directory.isFile()) {
            return files;
        }

        File[] filesList = directory.listFiles();

        if (filesList == null) {
            return files;
        }

        for (File file : filesList) {
            if (file.isDirectory()) {
                Collection<File> directoryFiles = findFilesByExtension(file, extension);
                files.addAll(directoryFiles);
                continue;
            }

            if (!file.getName().endsWith(extension)) {
                continue;
            }

            files.add(file);
        }

        return files;
    }

    public static void overrideFile(File file, String content) {
        PrintWriter writer = null;

        try {
            writer = new PrintWriter(file, "UTF-8");
            writer.print(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(writer);
        }
    }

    public static String getContentOfFile(File file) {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;

        try {
            if (file == null || !file.exists()) {
                return null;
            }

            br = new BufferedReader(new FileReader(file));
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(br);
        }

        return sb.toString();
    }

    public static String[] getContentAsLines(File file) {
        if (!file.exists()) {
            return new String[0];
        }

        try {
            List<String> list = Files.readAllLines(file.toPath(), Charset.forName("UTF-8"));
            String[] result = new String[list.size()];

            return list.toArray(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static int countLines(String filename) throws IOException {
        try (InputStream is = new BufferedInputStream(new FileInputStream(filename))) {
            byte[] c = new byte[1024];
            boolean empty = true;
            int count = 0;
            int readChars;

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

}
