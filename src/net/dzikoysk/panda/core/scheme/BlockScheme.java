package net.dzikoysk.panda.core.scheme;

import net.dzikoysk.panda.core.syntax.Block;
import net.dzikoysk.panda.core.ElementsBucket;

public class BlockScheme {

	private final Class<? extends Block> clazz;
	private final String[] indications;
	private final boolean conventional;

	public BlockScheme(Class<? extends Block> clazz, String... indications){
		this(clazz, true, indications);
	}
	
	public BlockScheme(Class<? extends Block> clazz, boolean conventional, String... indications){
		this.clazz = clazz;
		this.indications = indications;
		this.conventional = conventional;
		ElementsBucket.registerBlock(this);
	}

	public boolean isConventional() {
		return conventional;
	}
	
	public String[] getIndications() {
		return indications;
	}
	
	public Class<? extends Block> getClazz(){
		return clazz;
	}

}
