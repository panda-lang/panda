package net.dzikoysk.panda.util.configuration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

public class ConfigurationFile {
	
	private final File file;
	private final PandaConfiguration panda;
	
	public ConfigurationFile(File file, PandaConfiguration panda){
		this.file = file;
		this.panda = panda;
		
	}

	@SuppressWarnings("unchecked")
	public void save(){
		try {
			LinkedList<String> lines = new LinkedList<>();
			Stack<String> keys = new Stack<>();
			StringBuilder chars = new StringBuilder();
			Map<String, Object> map = panda.getMap();
			
			for(String line : panda.getCode()){
				if(line == null) continue;
				String rx = line.replaceAll("\\s", "");
				if(rx.equals("") || rx.startsWith("#")){
					lines.add(line);
					break;
				}
				
				chars.setLength(0);
				
				boolean separator = false;
				boolean skip = false;
				int whitespace = 0;
				
				for(char c : line.toCharArray()){			
					switch(c){
					case ' ':
						if(chars.length() == 0){
							whitespace++;
							continue;
						} else {
							chars.append(c);
							break;
						}
					case ':':
						if(!separator){
							keys.push(chars.toString());
							chars.setLength(0);
							if(map.containsKey(keys.peek())){
								String key = keys.pop();
								Object o = map.get(key);
								map.remove(key);
								if(o == null){
									skip = true;
									break;
								} else if(o instanceof List){
									List<String> value = (List<String>) o;
									if(value.isEmpty()) lines.add(getSpace(whitespace) + key + ": []");
									else {
										lines.add(getSpace(whitespace) + key + ":");
										for(String s : value) lines.add(getSpace(whitespace) + "- " + s);
									}
									break;
								}
								lines.add(getSpace(whitespace) + key + ": " + o.toString());
								break;
							}
							separator = true;
							break;
						}
					case '-':
						if(chars.length() == 0){
							skip = true;
							break;
						}
					default:
						chars.append(c);
					}
					if(skip){
						skip = false;
						break;
					}
				}
			}
			for(Entry<String, Object> entry : map.entrySet()){
				Object o = entry.getValue();
				if(o == null) continue;
				else if(o instanceof List){
					List<String> value = (List<String>) o;
					if(value.isEmpty()) lines.add(entry.getKey() + ": []");
					else{
						lines.add(entry.getKey() + ":");
						for(String s : value) lines.add("- " + s);
					}
				} else lines.add(entry.getKey() + ": " + o.toString());
			}
			file.delete();
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			for(String line : lines){
				out.write(line);
				out.newLine();
			}
			out.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private String getSpace(int s){
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < s; i++) sb.append(" ");
		return sb.toString();
	}
}
