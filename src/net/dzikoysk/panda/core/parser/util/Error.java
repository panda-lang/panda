package net.dzikoysk.panda.core.parser.util;

import net.dzikoysk.panda.Panda;

public class Error {

	private final String message;

	public Error(String message){
		this.message = message;
	}

	public void print(){
		System.out.println("Caused by: " + message);
		System.out.println("Panda Version: " + Panda.PANDA_VERSION);
	}

}
