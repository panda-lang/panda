package org.pandalang.panda.util.configuration;

import org.pandalang.panda.util.SimpleEntry;

import java.util.*;
import java.util.Map.Entry;

public class ConfigurationParser {

    private final String[] code;
    private Map<String, ConfigurationObject> map = new HashMap<>();

    public ConfigurationParser(String[] code) {
        this.code = code;
        this.run();
    }

    public void run() {
        Stack<String> keys = new Stack<>();
        Stack<Character> operators = new Stack<>();
        StringBuilder chars = new StringBuilder();
        SimpleEntry<String, Collection<String>> entry = new SimpleEntry<>();

        int lastPosition = 0;
        entry.setValue(new ArrayList<String>());

        for (String line : code) {
            if (line == null || line.isEmpty()) continue;
            String rx = line.replaceAll("\\s", "");
            if (rx.equals("")) continue;
            if (rx.startsWith("#")) continue;

            int charpos = 0;
            chars.setLength(0);
            int whitespace = 0;
            boolean separator = false;
            boolean apostrophe = false;
            boolean abeen = false;
            boolean skip = false;

            for (char c : line.toCharArray()) {
                charpos++;
                switch (c) {
                    case ' ':
                        if (!apostrophe && chars.length() == 0) {
                            whitespace++;
                            continue;
                        } else {
                            chars.append(c);
                            break;
                        }
                    case '\'':
                        if (chars.length() == 0) {
                            if (apostrophe) {
                                abeen = true;
                                skip = true;
                                break;
                            }
                            apostrophe = true;
                            continue;
                        }
                        if (line.length() == charpos) {
                            apostrophe = false;
                            abeen = true;
                            continue;
                        }
                        chars.append(c);
                        break;
                    case ':':
                        if (!separator) {
                            if (!operators.isEmpty())
                                if (operators.peek() == '-') {
                                    chars.append(c);
                                    break;
                                }
                            int position = whitespace / 2;
                            patchPosition(position, lastPosition, keys);
                            keys.push(chars.toString());
                            operators.push(c);
                            chars.setLength(0);
                            separator = true;
                            break;
                        } else {
                            chars.append(c);
                            break;
                        }
                    case '-':
                        if (chars.length() == 0) {
                            operators.push(c);
                            break;
                        } else {
                            chars.append(c);
                            break;
                        }
                    case '[':
                        if (chars.length() == 0) {
                            operators.push(c);
                            break;
                        } else {
                            chars.append(c);
                            break;
                        }
                    case ']':
                        if (chars.length() == 0) {
                            operators.push(c);
                            break;
                        } else {
                            chars.append(c);
                            break;
                        }
                    default:
                        chars.append(c);
                }
                if (skip) break;
            }

            int position = whitespace / 2;
            String string = chars.toString();
            chars.setLength(0);

            if (operators.isEmpty()) {
                lastPosition = position;
                continue;
            }

            if (keys.isEmpty()) {
                if (!string.isEmpty()) {
                    char c = operators.pop();
                    switch (c) {
                        case '-':
                            if (entry.getKey() == null) {
                                entry.setKey(ConfigurationUtils.getPath(keys));
                            }
                            if (entry.getValue() == null) entry.setValue(new ArrayList<String>());
                            entry.getValue().add(string);
                            break;
                        default:
                            operators.push(c);
                            break;
                    }
                }
                continue;
            }

            if (!abeen && string.isEmpty()) {
                char c = operators.pop();
                switch (c) {
                    case ']':
                        if (operators.peek() != '[') break;
                        operators.pop();
                        ConfigurationObject co = new ConfigurationObject(ConfigurationType.LIST);
                        co.setObject(new ArrayList<String>());
                        addEntry(ConfigurationUtils.getPath(keys), co);
                        keys.pop();
                        break;
                    case ':':
                        if (!entry.getValue().isEmpty()) {
                            ConfigurationObject lo = new ConfigurationObject(ConfigurationType.LIST);
                            lo.setObject(entry.getValue());
                            lo.setPosition(position);
                            this.addEntry(entry.getKey(), lo);
                            entry = new SimpleEntry<>();
                            entry.setValue(new ArrayList<String>());
                        }
                        break;
                    default:
                        operators.push(c);
                        break;
                }
                lastPosition = position;
                continue;
            }
            switch (operators.pop()) {
                case '-':
                    if (entry.getKey() == null) {
                        entry.setKey(ConfigurationUtils.getPath(keys));
                        keys.pop();
                    }
                    if (entry.getValue() == null) entry.setValue(new ArrayList<String>());
                    entry.getValue().add(string);
                    break;
                case ':':
                    String key = keys.pop();
                    patchPosition(position, lastPosition, keys);
                    if (!entry.getValue().isEmpty()) {
                        ConfigurationObject lo = new ConfigurationObject(ConfigurationType.LIST);
                        lo.setObject(entry.getValue());
                        lo.setPosition(position);
                        this.addEntry(entry.getKey(), lo);
                        if (keys.size() != 0) keys.pop();
                        entry = new SimpleEntry<>();
                        entry.setValue(new ArrayList<String>());
                    }
                    keys.push(key);
                    ConfigurationObject co = new ConfigurationObject(ConfigurationType.STRING);
                    co.setObject(string);
                    co.setPosition(position);
                    this.addEntry(ConfigurationUtils.getPath(keys), co);
                    keys.pop();
                    break;
            }
            lastPosition = position;
        }
        if (!entry.getValue().isEmpty()) {
            ConfigurationObject lo = new ConfigurationObject(ConfigurationType.LIST);
            lo.setObject(entry.getValue());
            this.addEntry(entry.getKey(), lo);
        }
    }

    private void addEntry(String s, ConfigurationObject co) {
        map.put(s, co);
    }

    private void patchPosition(int position, int lastPosition, Stack<String> keys) {
        if (lastPosition != position) {
            if (lastPosition > position) {
                int backspace = lastPosition - position;
                if (keys.size() >= backspace) {
                    for (int i = 0; i < backspace; i++) keys.pop();
                }
            }
        }
    }

    public void print() {
        for (Entry<String, ConfigurationObject> entry : this.map.entrySet()) {
            if (entry.getValue().getType() == ConfigurationType.STRING) {
                String s = (String) entry.getValue().getObject();
                System.out.println(entry.getKey() + ": " + s);
            } else {
                System.out.println(entry.getKey() + ": ");
                for (String s : (List<String>) entry.getValue().getObject()) {
                    System.out.println("- " + s);
                }
            }
        }
    }

    public Map<String, Object> getMap() {
        Map<String, Object> map = new HashMap<>();
        for (Entry<String, ConfigurationObject> entry : this.map.entrySet()) {
            if (entry.getKey() == null) continue;
            map.put(entry.getKey(), entry.getValue().getObject());
        }
        return map;
    }

}
