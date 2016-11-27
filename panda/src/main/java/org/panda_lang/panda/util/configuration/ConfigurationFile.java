package org.panda_lang.panda.util.configuration;

import org.panda_lang.framework.util.FileUtils;

import java.io.File;
import java.util.Collection;
import java.util.Map;

public class ConfigurationFile {

    private final File file;
    private final PandaConfiguration panda;

    protected ConfigurationFile(File file, PandaConfiguration panda) {
        this.file = file;
        this.panda = panda;
    }

    protected void save() {
        StringBuilder configurationBuilder = new StringBuilder();
        Map<String, Object> map = panda.getMap();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            configurationBuilder.append(entry.getKey());
            configurationBuilder.append(":");

            Object value = entry.getValue();

            if (value instanceof Collection) {
                Collection collection = (Collection) value;

                if (collection.isEmpty()) {
                    configurationBuilder.append("[]");
                    configurationBuilder.append(System.lineSeparator());
                    continue;
                }

                for (Object element : collection) {
                    configurationBuilder.append(System.lineSeparator());
                    configurationBuilder.append("- ");
                    configurationBuilder.append(element);
                }

                configurationBuilder.append(System.lineSeparator());
                continue;
            }

            configurationBuilder.append(value);
            configurationBuilder.append(System.lineSeparator());
        }

        FileUtils.overrideFile(file, configurationBuilder.toString());
    }

}
