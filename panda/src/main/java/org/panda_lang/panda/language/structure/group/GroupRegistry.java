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

package org.panda_lang.panda.language.structure.group;

import org.panda_lang.panda.language.structure.prototype.ClassPrototype;

import java.util.HashMap;
import java.util.Map;

public class GroupRegistry {

    private static final GroupRegistry instance = new GroupRegistry();
    private final Map<String, Group> groups;

    public GroupRegistry() {
        this.groups = new HashMap<>();
    }

    public Group getOrCreate(String groupName) {
        Group group = groups.get(groupName);

        if (group == null) {
            group = new Group(groupName);
            groups.put(groupName, group);
        }

        return group;
    }

    public Group get(String groupName) {
        return groups.get(groupName);
    }

    public Map<String, Group> getGroups() {
        return groups;
    }

    public static GroupRegistry getDefault() {
        return instance;
    }

    public static ClassPrototype forName(String prototypePath) {
        GroupRegistry registry = getDefault();
        String[] parts = prototypePath.split(":");
        Group group = registry.get(parts[0]);

        if (group == null) {
            return null;
        }

        return group.get(parts[1]);
    }

}
