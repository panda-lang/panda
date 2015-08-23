package net.dzikoysk.panda;

import net.dzikoysk.panda.core.syntax.Block;
import net.dzikoysk.panda.lang.PObject;
import net.dzikoysk.panda.core.syntax.block.MethodBlock;

import java.util.ArrayList;
import java.util.Collection;

public class PandaScript {

	private String name;
	private String author;
	private String version;
	private Collection<Block> blocks;

	public PandaScript(){
		this.blocks = new ArrayList<>();
	}

	public PandaScript name(String name){
		this.name = name;
		return this;
	}

	public PandaScript author(String author){
		this.author = author;
		return this;
	}

	public PandaScript version(String version){
		this.version = version;
		return this;
	}

	public PandaScript sections(Collection<Block> blocks){
		this.blocks = blocks;
		return this;
	}

	public void addSection(Block block){
		this.blocks.add(block);
	}

	public PObject callMethod(String name){
		for(Block block : blocks){
			if(block instanceof MethodBlock){
				if(block.getName().equals(name)){
					return block.run();
				}
			}
		} return null;
	}

}
