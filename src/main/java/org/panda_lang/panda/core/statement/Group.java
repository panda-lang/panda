package org.panda_lang.panda.core.statement;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Inst;
import org.panda_lang.panda.core.statement.util.NamedExecutable;

import java.util.HashMap;
import java.util.Map;

public class Group implements NamedExecutable {

    private final String group;
    private final Map<String, Structure> vials;

    public Group(String group) {
        this.group = group;
        this.vials = new HashMap<>();
    }

    @Override
    public Inst execute(Alice alice) {
        return null;
    }

    public void registerVial(Structure structure) {
        vials.put(structure.getName(), structure);
    }

    public Structure getVial(String vialName) {
        return vials.get(vialName);
    }

    @Override
    public String getName() {
        return group;
    }

}
