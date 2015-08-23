package net.dzikoysk.panda.util.configuration;

public class ConfigurationObject {
	
	private final ConfigurationType type;
	private Object object;
	private int position;
	
	public ConfigurationObject(ConfigurationType type){
		this.type = type;
	}
	
	public void setObject(Object o){
		this.object = o;
	}
	
	public void setPosition(int i){
		this.position = i;
	}
	
	public String getString(){
		if(type == ConfigurationType.STRING) return (String) object;
		return null;
	}
	
	public int getPosition(){
		return this.position;
	}

	public Object getObject(){
		return this.object;
	}
	
	public ConfigurationType getType(){
		return this.type;
	}
}
