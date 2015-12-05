package org.pandalang.panda.lang;

import org.pandalang.panda.Panda;
import org.pandalang.panda.PandaLoader;
import org.pandalang.panda.PandaScript;
import org.pandalang.panda.core.scheme.MethodScheme;
import org.pandalang.panda.core.scheme.ObjectScheme;
import org.pandalang.panda.core.syntax.IExecutable;
import org.pandalang.panda.core.syntax.Parameter;

import java.io.File;

public class PPanda extends PObject {

    static {
        // Register object
        ObjectScheme os = new ObjectScheme(PPanda.class, "Panda");
        // Static method: reload
        os.registerMethod(new MethodScheme("reload", new IExecutable() {
            @Override
            public PObject run(Parameter instance, Parameter... parameters) {
                Panda.getInstance().reload();
                return null;
            }
        }));
        // Static method: loadSimpleScript
        os.registerMethod(new MethodScheme("loadSimpleScript", new IExecutable() {
            @Override
            public PObject run(Parameter instance, Parameter... parameters) {
                PObject o = parameters[0].getValue();
                if (o instanceof PString) {
                    PandaScript si = PandaLoader.loadSimpleScript(o.toString());
                    Panda.getInstance().addScript(si);
                } else if (o instanceof PFile) {
                    File file = ((PFile) o).getFile();
                    PandaScript si = PandaLoader.loadSimpleScript(file);
                    Panda.getInstance().addScript(si);
                }
                return null;
            }
        }));
        // Static method: getVersion
        os.registerMethod(new MethodScheme("getVersion", new IExecutable() {
            @Override
            public PObject run(Parameter instance, Parameter... parameters) {
                return new PString(Panda.PANDA_VERSION);
            }
        }));
    }

}
