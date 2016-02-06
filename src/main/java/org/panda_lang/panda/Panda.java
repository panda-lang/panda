package org.panda_lang.panda;

import org.panda_lang.panda.core.parser.ParserLayout;
import org.panda_lang.panda.core.parser.analyzer.Analyzer;
import org.panda_lang.panda.core.parser.essential.util.BlockLayout;
import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.Factor;
import org.panda_lang.panda.util.Exec;

import java.util.ArrayList;
import java.util.Collection;

public class Panda
{

    public static final String PANDA_VERSION = "1.0.0-SNAPSHOT";
    private static Panda panda;

    private final PandaCore pandaCore;
    private final PandaLoader pandaLoader;
    private final Collection<PandaExtension> extensions;
    private final Collection<PandaScript> scripts;
    private Runnable reload;

    public Panda()
    {
        panda = this;
        pandaCore = new PandaCore();
        pandaLoader = new PandaLoader();
        extensions = new ArrayList<>();
        scripts = new ArrayList<>();
    }

    public void callAll(Class<? extends Block> blockType, String name, Factor... factors)
    {
        for (PandaScript script : getScripts())
        {
            script.call(blockType, name, factors);
        }
    }

    public void registerParser(ParserLayout parserLayout)
    {
        pandaCore.registerParser(parserLayout);
    }

    public void registerAnalyzer(Analyzer analyzer)
    {
        pandaCore.registerAnalyzer(analyzer);
    }

    public void registerBlock(BlockLayout blockLayout)
    {
        pandaCore.registerBlock(blockLayout);
    }

    public void registerExtension(PandaExtension pandaExtension)
    {
        pandaExtension.onLoad();
        this.extensions.add(pandaExtension);
    }

    public void exec(String... commands)
    {
        for (String command : commands)
        {
            Exec.activate(command);
        }
    }

    public void reload()
    {
        if (reload != null) this.reload.run();
    }

    public void clear()
    {
        this.scripts.clear();
    }

    public void enableReload(Runnable reload)
    {
        this.reload = reload;
    }

    public void addScript(PandaScript script)
    {
        this.scripts.add(script);
    }

    public Collection<PandaScript> getScripts()
    {
        return scripts;
    }

    public Collection<PandaExtension> getExtensions()
    {
        return extensions;
    }

    public PandaLoader getPandaLoader()
    {
        return pandaLoader;
    }

    public PandaCore getPandaCore()
    {
        return pandaCore;
    }


    public static void main(String[] args) throws Exception
    {
        panda = new Panda();
        panda.exec(args);
    }

    public static String getDirectory()
    {
        return Panda.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    }

    public static Panda getInstance()
    {
        return panda != null ? panda : new Panda();
    }

}
