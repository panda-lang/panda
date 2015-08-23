package net.dzikoysk.panda.lang;

import net.dzikoysk.panda.core.scheme.ConstructorScheme;
import net.dzikoysk.panda.core.scheme.MethodScheme;
import net.dzikoysk.panda.core.scheme.ObjectScheme;
import net.dzikoysk.panda.core.syntax.Constructor;
import net.dzikoysk.panda.core.syntax.IExecutable;
import net.dzikoysk.panda.core.syntax.Parameter;

public class PNumber extends PObject {

	static {
		// Register object
		ObjectScheme os = new ObjectScheme(PNumber.class, "Number");
		// Constructor
		os.registerConstructor(new ConstructorScheme(new Constructor<PNumber>() {
			@Override
			public PNumber run(Parameter... parameters) {
				if(parameters == null || parameters.length == 0) return new PNumber(0);
				else return parameters[0].getValue(PNumber.class);
			}
		}));
		// Static method: valueOf
		os.registerMethod(new MethodScheme("valueOf", new IExecutable() {
			@Override
			public PObject run(Parameter instance, Parameter... parameters) {
				return new PNumber(Long.valueOf(parameters[0].getValue(PString.class).toString()));
			}
		}));
		// Method: toString
		os.registerMethod(new MethodScheme("toString", new IExecutable() {
			@Override
			public PObject run(Parameter instance, Parameter... parameters) {
				PNumber number = instance.getValue(PNumber.class);
				return new PString(number.toString());
			}
		}));
	}

	private final Number number;

	public PNumber(Number number){
		this.number = number;
	}

	public Number getNumber(){
		return number;
	}

	@Override
	public String getType(){
		return "Number";
	}

	@Override
	public String toString(){
		return number.toString();
	}

}
