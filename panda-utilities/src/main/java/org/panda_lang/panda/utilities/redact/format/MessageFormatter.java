package org.panda_lang.panda.utilities.redact.format;

import org.panda_lang.panda.utilities.commons.objects.*;

import java.util.*;

public class MessageFormatter {

    private final Map<String, String> placeholders = new HashMap<>(3);

    public String format(String message) {
        for (Map.Entry<String, String> placeholderEntry : placeholders.entrySet()) {
            message = StringUtils.replace(message, placeholderEntry.getKey(), placeholderEntry.getValue());
        }

        return message;
    }

    public MessageFormatter register(String placeholder, Object value) {
        this.placeholders.put(placeholder, value.toString());
        return this;
    }

}
