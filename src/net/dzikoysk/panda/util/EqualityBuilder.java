package net.dzikoysk.panda.util;

import net.dzikoysk.panda.core.syntax.Operator;
import net.dzikoysk.panda.core.syntax.Parameter;

public class EqualityBuilder {

	private Parameter one, other;
	private Operator operator;

	public void setOne(Parameter one) {
		this.one = one;
	}

	public void setOther(Parameter other) {
		this.other = other;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public void setOperator(String s) {
		this.operator = Operator.getOperator(s);
	}

	public Operator getOperator() {
		return operator;
	}

	public Parameter getOther() {
		return other;
	}

	public Parameter getOne() {
		return one;
	}

}
