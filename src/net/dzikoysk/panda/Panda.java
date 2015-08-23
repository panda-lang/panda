package net.dzikoysk.panda;

import net.dzikoysk.panda.core.Core;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class Panda {

	public static final String PANDA_VERSION = "0.0.8";
	private static Panda panda;

	private Collection<PandaScript> scripts;

	public Panda(){
		scripts = new ArrayList<>();
	}

	public void registerDefault(){
		Core.registerDefault();
	}

	public void loadDefault(){
		scripts.add(PandaLoader.loadSimpleScript(getResource("hello.pp")));
	}

	public void runDefault(){
		for(PandaScript script : scripts){
			script.callMethod("main");
		}
	}

	public void addScript(PandaScript script){
		scripts.add(script);
	}

	public Collection<PandaScript> getScripts(){
		return scripts;
	}

	public static File getResource(String file){
		return new File(Panda.class.getClassLoader().getResource(file).getFile());
	}

	public static String getDirectory() {
		return Panda.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	}

	public static Panda getInstance(){
		return panda;
	}



	public static void main(String[] args) throws Exception {
		panda = new Panda();

		panda.registerDefault();
		panda.loadDefault();
		panda.runDefault();
	}

}
