package net.dzikoysk.panda;

import net.dzikoysk.panda.core.Core;
import net.dzikoysk.panda.core.syntax.Block;
import net.dzikoysk.panda.core.syntax.Parameter;
import net.dzikoysk.panda.core.syntax.block.MethodBlock;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class Panda {

	public static final String PANDA_VERSION = "0.0.9";
	private static Panda panda;

	private Collection<PandaScript> scripts;
	private Runnable reload;

	public Panda(){
		panda = this;
		scripts = new ArrayList<>();
		Core.registerDefault();
	}

	public void clear(){
		this.scripts.clear();;
	}

	public void reload(){
		if(reload != null) this.reload.run();
	}

	@Deprecated
	public void callMethod(String method, Parameter... parameters){
		for(PandaScript script : getScripts()){
			script.call(MethodBlock.class, method, parameters);
		}
	}

	public void call(Class<? extends Block> blockType, String name, Parameter... parameters){
		for(PandaScript script : getScripts()){
			script.call(blockType, name, parameters);
		}
	}

	public void addScript(PandaScript script){
		this.scripts.add(script);
	}

	public void enableReload(Runnable reload){
		this.reload = reload;
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




	public static void main(String[] args) throws Exception {
		panda = new Panda();

		panda.addScript(PandaLoader.loadSimpleScript(getResource("hello.pp")));
		panda.call(MethodBlock.class, "main");
	}

	public static Panda getInstance(){
		return panda;
	}

}
