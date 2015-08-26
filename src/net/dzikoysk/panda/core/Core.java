package net.dzikoysk.panda.core;

public class Core {

	private static Core core;
	
	private Core(){ }
	
	private void blocks(){
		ElementsBucket.loadClasses("net.dzikoysk.panda.core.syntax.block", new String[]{
			"ElseThenBlock",
			"ForBlock",
			"IfThenBlock",
			"MethodBlock",
			"RunnableBlock",
			"ClassBlock",
			"ThreadBlock",
			"WhileBlock"
		});
	}
	
	private void objects(){
		ElementsBucket.loadClasses("net.dzikoysk.panda.lang", new String[]{
			"PArray",
			"PBoolean",
			"PCharacter",
			"PFile",
			"PList",
			"PMap",
			"PNumber",
			"PObject",
			"PPanda",
			"PRunnable",
			"PStack",
			"PString",
			"PSystem",
			"PThread"
		});
	}
	
	public static void registerDefault(){
		if(core != null) return;
		core = new Core();
		core.blocks();
		core.objects();
	}

}
