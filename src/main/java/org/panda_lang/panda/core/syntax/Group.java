package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.core.Particle;

import java.util.HashMap;
import java.util.Map;

public class Group implements NamedExecutable
{

    private final String group;
    private final Map<String, Vial> vials;

    public Group(String group)
    {
        this.group = group;
        this.vials = new HashMap<>();
    }

    @Override
    public Essence run(Particle particle)
    {
        return null;
    }

    public void registerVial(Vial vial)
    {
        vials.put(vial.getName(), vial);
    }

    public Vial getVial(String vialName)
    {
        return vials.get(vialName);
    }

    @Override
    public String getName()
    {
        return group;
    }

}
