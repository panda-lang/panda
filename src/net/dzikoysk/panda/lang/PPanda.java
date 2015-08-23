package net.dzikoysk.panda.lang;

import net.dzikoysk.panda.Panda;
import net.dzikoysk.panda.PandaLoader;
import net.dzikoysk.panda.PandaScript;
import net.dzikoysk.panda.core.scheme.MethodScheme;
import net.dzikoysk.panda.core.scheme.ObjectScheme;
import net.dzikoysk.panda.core.syntax.IExecutable;
import net.dzikoysk.panda.core.syntax.Parameter;

import java.io.File;

public class PPanda extends PObject {

	static {
		// Register object
		ObjectScheme os = new ObjectScheme(PPanda.class, "Panda");
		// Static method: loadSimpleScript
		os.registerMethod(new MethodScheme("getVersion", new IExecutable(){
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
