package org.panda_lang.panda;

import org.panda_lang.panda.core.Basis;
import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.Factor;
import org.panda_lang.panda.util.Exec;

import java.util.ArrayList;
import java.util.Collection;

public class Panda {

    public static final String PANDA_VERSION = "1.0.0-SNAPSHOT";

    private final PandaCore pandaCore;
    private final PandaLoader pandaLoader;
    private final Collection<PandaExtension> extensions;
    private final Collection<PandaScript> scripts;
    private Runnable reload;

    public Panda() {
        this.pandaCore = new PandaCore(this);
        this.pandaLoader = new PandaLoader(this);
        this.extensions = new ArrayList<>();
        this.scripts = new ArrayList<>();
    }

    public void registerExtension(PandaExtension pandaExtension) {
        pandaExtension.onLoad();
        extensions.add(pandaExtension);
    }

    public void initializeDefaultElements() {
        Basis basis = getBasis();
        basis.loadParsers();
        basis.loadInjections();
        basis.loadBlocks();
        basis.loadObjects();
    }

    public void callAll(Class<? extends Block> blockType, String name, Factor... factors) {
        for (PandaScript script : getScripts()) {
            script.call(blockType, name, factors);
        }
    }

    public void exec(String... commands) {
        for (String command : commands) {
            Exec.activate(this, command);
        }
    }

    public void reload() {
        if (reload != null) {
            reload.run();
        }
    }

    public void clear() {
        this.scripts.clear();
    }

    public void enableReload(Runnable reload) {
        this.reload = reload;
    }

    public void addScript(PandaScript script) {
        this.scripts.add(script);
    }

    public Basis getBasis() {
        return new Basis(pandaCore);
    }

    public Collection<PandaScript> getScripts() {
        return scripts;
    }

    public Collection<PandaExtension> getExtensions() {
        return extensions;
    }

    public PandaLoader getPandaLoader() {
        return pandaLoader;
    }

    public PandaCore getPandaCore() {
        return pandaCore;
    }


    public static void main(String[] args) throws Exception {
        Panda panda = new Panda();
        panda.initializeDefaultElements();
        panda.exec(args);
    }

}
