package org.panda_lang.panda.core.statement;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Essence;
import org.panda_lang.panda.core.statement.util.NamedExecutable;

import java.util.HashMap;
import java.util.Map;

public class Group implements NamedExecutable {

    private final String group;
    private final Map<String, Vial> vials;

    public Group(String group) {
        this.group = group;
        this.vials = new HashMap<>();
    }

    @Override
    public Essence execute(Alice alice) {
        return null;
    }

    public void registerVial(Vial vial) {
        vials.put(vial.getName(), vial);
    }

    public Vial getVial(String vialName) {
        return vials.get(vialName);
    }

    @Override
    public String getName() {
        return group;
    }

}
