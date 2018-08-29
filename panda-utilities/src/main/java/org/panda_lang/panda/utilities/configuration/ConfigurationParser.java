/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.utilities.configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

public class ConfigurationParser {

    private final String[] code;
    private final Map<String, ConfigurationObject> map;

    protected ConfigurationParser(String[] code) {
        this.code = code;
        this.map = new HashMap<>();
        this.run();
    }

    protected void run() {
        Stack<String> keys = new Stack<>();
        Stack<Character> operators = new Stack<>();
        StringBuilder chars = new StringBuilder();
        ConfigurationEntry<String, Collection<String>> entry = new ConfigurationEntry<>();

        int lastPosition = 0;
        entry.setValue(new ArrayList<>());

        for (String line : code) {
            if (line == null || line.isEmpty()) {
                continue;
            }

            String rx = line.replaceAll("\\s", "");

            if ("".equals(rx)) {
                continue;
            }

            if (rx.startsWith("#")) {
                continue;
            }

            int charPos = 0;
            chars.setLength(0);
            int whitespace = 0;
            boolean separator = false;
            boolean apostrophe = false;
            boolean aBeen = false;
            boolean skip = false;

            for (char c : line.toCharArray()) {
                charPos++;
                switch (c) {
                    case ' ':
                        if (!apostrophe && chars.length() == 0) {
                            whitespace++;
                            continue;
                        }
                        else {
                            chars.append(c);
                            break;
                        }
                    case '\'':
                        if (chars.length() == 0) {
                            if (apostrophe) {
                                aBeen = true;
                                skip = true;
                                break;
                            }
                            apostrophe = true;
                            continue;
                        }
                        if (line.length() == charPos) {
                            apostrophe = false;
                            aBeen = true;
                            continue;
                        }
                        chars.append(c);
                        break;
                    case ':':
                        if (!separator) {
                            if (!operators.isEmpty()) {
                                if (operators.peek() == '-') {
                                    chars.append(c);
                                    break;
                                }
                            }
                            int position = whitespace / 2;
                            patchPosition(position, lastPosition, keys);
                            keys.push(chars.toString());
                            operators.push(c);
                            chars.setLength(0);
                            separator = true;
                            break;
                        }
                        else {
                            chars.append(c);
                            break;
                        }
                    case '-':
                        if (chars.length() == 0) {
                            operators.push(c);
                            break;
                        }
                        else {
                            chars.append(c);
                            break;
                        }
                    case '[':
                        if (chars.length() == 0) {
                            operators.push(c);
                            break;
                        }
                        else {
                            chars.append(c);
                            break;
                        }
                    case ']':
                        if (chars.length() == 0) {
                            operators.push(c);
                            break;
                        }
                        else {
                            chars.append(c);
                            break;
                        }
                    default:
                        chars.append(c);
                }
                if (skip) {
                    break;
                }
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
                            if (entry.getValue() == null) {
                                entry.setValue(new ArrayList<>());
                            }
                            entry.getValue().add(string);
                            break;
                        default:
                            operators.push(c);
                            break;
                    }
                }
                continue;
            }

            if (!aBeen && string.isEmpty()) {
                char c = operators.pop();
                switch (c) {
                    case ']':
                        if (operators.peek() != '[') {
                            break;
                        }
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
                            addEntry(entry.getKey(), lo);

                            entry = new ConfigurationEntry<>();
                            entry.setValue(new ArrayList<>());
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
                    if (entry.getValue() == null) {
                        entry.setValue(new ArrayList<>());
                    }
                    entry.getValue().add(string);
                    break;
                case ':':
                    String key = keys.pop();
                    patchPosition(position, lastPosition, keys);

                    if (!entry.getValue().isEmpty()) {
                        ConfigurationObject lo = new ConfigurationObject(ConfigurationType.LIST);
                        lo.setObject(entry.getValue());
                        lo.setPosition(position);
                        addEntry(ConfigurationUtils.getPath(keys), lo);

                        if (keys.size() != 0) {
                            keys.pop();
                        }

                        entry = new ConfigurationEntry<>();
                        entry.setValue(new ArrayList<>());
                    }

                    keys.push(key);
                    ConfigurationObject co = new ConfigurationObject(ConfigurationType.STRING);
                    co.setObject(string);
                    co.setPosition(position);
                    addEntry(ConfigurationUtils.getPath(keys), co);
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

    private void patchPosition(int position, int lastPosition, Stack<String> keys) {
        if (lastPosition != position) {
            if (lastPosition > position) {
                int backspace = lastPosition - position;
                if (keys.size() >= backspace) {
                    for (int i = 0; i < backspace; i++) {
                        keys.pop();
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void print() {
        for (Entry<String, ConfigurationObject> entry : this.map.entrySet()) {
            if (entry.getValue().getType() == ConfigurationType.STRING) {
                String s = (String) entry.getValue().getObject();
                System.out.println(entry.getKey() + ": " + s);
            }
            else {
                System.out.println(entry.getKey() + ": ");
                for (String s : (List<String>) entry.getValue().getObject()) {
                    System.out.println("- " + s);
                }
            }
        }
    }

    private void addEntry(String s, ConfigurationObject co) {
        map.put(s, co);
    }

    protected Map<String, Object> getMap() {
        Map<String, Object> map = new HashMap<>();
        for (Entry<String, ConfigurationObject> entry : this.map.entrySet()) {
            if (entry.getKey() == null) {
                continue;
            }
            map.put(entry.getKey(), entry.getValue().getObject());
        }
        return map;
    }

}
