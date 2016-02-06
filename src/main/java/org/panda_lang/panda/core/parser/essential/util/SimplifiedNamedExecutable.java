package org.panda_lang.panda.core.parser.essential.util;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.Executable;
import org.panda_lang.panda.core.syntax.NamedExecutable;

public class SimplifiedNamedExecutable implements NamedExecutable
{

    private final Executable executable;
    private String name;

    public SimplifiedNamedExecutable(Executable executable)
    {
        this.executable = executable;
    }

    @Override
    public Essence run(Particle particle)
    {
        return executable.run(particle);
    }

    public Executable getExecutable()
    {
        return executable;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String getName()
    {
        return name;
    }

}
