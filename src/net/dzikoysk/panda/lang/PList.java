package net.dzikoysk.panda.lang;

import net.dzikoysk.panda.core.scheme.ConstructorScheme;
import net.dzikoysk.panda.core.scheme.MethodScheme;
import net.dzikoysk.panda.core.scheme.ObjectScheme;
import net.dzikoysk.panda.core.syntax.Constructor;
import net.dzikoysk.panda.core.syntax.IExecutable;
import net.dzikoysk.panda.core.syntax.Parameter;

import java.util.ArrayList;
import java.util.List;

public class PList extends PObject {

	static {
		// Register object
		ObjectScheme os = new ObjectScheme(PList.class, "List");
		// Constructor
		os.registerConstructor(new ConstructorScheme(new Constructor<PList>() {
			@Override
			public PList run(Parameter... parameters) {
				return new PList();
			}
		}));
		// Method: add
		os.registerMethod(new MethodScheme("add", new IExecutable() {
			@Override
			public PObject run(Parameter instance, Parameter... parameters) {
				PList b = instance.getValue(PList.class);
				b.getList().add(parameters[0].getValue());
				return null;
			}
		}));
	}

	private final List<PObject> list;

	public PList(){
		this.list = new ArrayList<>();
	}

	public List<PObject> getList(){
		return list;
	}

	@Override
	public String getType(){
		return "List";
	}

	@Override
	public String toString(){
		StringBuilder node = new StringBuilder();
		for(PObject o : list){
			if(node.length() != 0) node.append(", ");
			node.append(o);
		}
		return node.toString();
	}

}
