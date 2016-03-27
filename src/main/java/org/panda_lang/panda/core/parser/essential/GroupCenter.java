package org.panda_lang.panda.core.parser.essential;

import org.panda_lang.panda.core.statement.Group;

import java.util.HashMap;
import java.util.Map;

public class GroupCenter {

    private static final Map<String, Group> groups = new HashMap<>();

    public static Group getGroup(String groupName) {
        Group group = groups.get(groupName);

        if (group == null) {
            group = new Group(groupName);
            groups.put(groupName, group);
        }

        return group;
    }

}
