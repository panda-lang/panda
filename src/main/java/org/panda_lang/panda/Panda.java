package org.panda_lang.panda;

import org.panda_lang.panda.core.Core;
import org.panda_lang.panda.core.parser.essential.util.Exec;
import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.Parameter;

import java.util.ArrayList;
import java.util.Collection;

public class Panda {

    public static final String PANDA_VERSION = "1.0.0-SNAPSHOT";
    private static Panda panda;

    private Collection<PandaScript> scripts;
    private Runnable reload;

    public Panda() {
        panda = this;
        scripts = new ArrayList<>();
        Core.registerDefault();
    }

    public void exec(String... commands) {
        if (commands != null) {
            for (String command : commands) {
                Exec.activate(command);
            }
        }
    }

    public void call(Class<? extends Block> blockType, String name, Parameter... parameters) {
        for (PandaScript script : getScripts()) {
            script.call(blockType, name, parameters);
        }
    }

    public void clear() {
        this.scripts.clear();
    }

    public void reload() {
        if (reload != null) {
            this.reload.run();
        }
    }

    public void addScript(PandaScript script) {
        this.scripts.add(script);
    }

    public void enableReload(Runnable reload) {
        this.reload = reload;
    }

    public Collection<PandaScript> getScripts() {
        return scripts;
    }

    public static String getDirectory() {
        return Panda.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    }


    public static void main(String[] args) throws Exception {
        panda = new Panda();
        panda.exec(args);
    }

    public static Panda getInstance() {
        return panda;
    }

}
