package org.panda_lang.panda.lang;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.PandaLoader;
import org.panda_lang.panda.PandaScript;
import org.panda_lang.panda.core.scheme.MethodScheme;
import org.panda_lang.panda.core.scheme.ObjectScheme;
import org.panda_lang.panda.core.syntax.IExecutable;
import org.panda_lang.panda.core.syntax.Parameter;

import java.io.File;

public class PPanda extends PObject {

    static {
        // Register object
        ObjectScheme os = new ObjectScheme(PPanda.class, "Panda");
        // Static method: reload
        os.registerMethod(new MethodScheme("reload", new IExecutable(){
            @Override
            public PObject run(Parameter instance, Parameter... parameters) {
                Panda.getInstance().reload();
                return null;
            }
        }));
        // Static method: loadSimpleScript
        os.registerMethod(new MethodScheme("loadSimpleScript", new IExecutable(){
            @Override
            public PObject run(Parameter instance, Parameter... parameters) {
                PObject o = parameters[0].getValue();
                if(o instanceof PString){
                    PandaScript si = PandaLoader.loadSimpleScript(o.toString());
                    Panda.getInstance().addScript(si);
                } else if(o instanceof PFile){
                    File file = ((PFile) o).getFile();
                    PandaScript si = PandaLoader.loadSimpleScript(file);
                    Panda.getInstance().addScript(si);
                }
                return null;
            }
        }));
        // Static method: getVersion
        os.registerMethod(new MethodScheme("getVersion", new IExecutable(){
            @Override
            public PObject run(Parameter instance, Parameter... parameters) {
                return new PString(Panda.PANDA_VERSION);
            }
        }));
    }

}
