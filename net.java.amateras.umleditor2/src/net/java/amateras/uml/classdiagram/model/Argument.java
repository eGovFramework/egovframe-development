package net.java.amateras.uml.classdiagram.model;

import java.io.Serializable;

/**
 * 
 * @author Naoki Takezoe
 */
public class Argument implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 418834590075254845L;
	private String name;
	private String type;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString(){
		return name + ": " + type;
	}

	@Override
	public Object clone() {
		Argument arg = new Argument();
		arg.setName(getName());
		arg.setType(getType());
		return arg;
	}
}
