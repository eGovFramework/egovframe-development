package net.java.amateras.uml.classdiagram.model;

import java.io.Serializable;

public abstract class Visibility implements Serializable {
	
	private static final long serialVersionUID = 496932600857039406L;

	/**
	 * Don't create an instanse.
	 */
	private Visibility(){
	}
	
	public static final Visibility PUBLIC = new Visibility(){
		private static final long serialVersionUID = -7067406431990821925L;

		public String toString(){
			return "public";
		}
	};
	
	public static final Visibility PRIVATE = new Visibility(){
		private static final long serialVersionUID = -838985516837666057L;

		public String toString(){
			return "private";
		}
	};
	
	public static final Visibility PROTECTED = new Visibility(){
		private static final long serialVersionUID = -4284893486987627035L;

		public String toString(){
			return "protected";
		}
	};
	
	public static final Visibility PACKAGE = new Visibility(){
		private static final long serialVersionUID = -7008629216283901480L;

		public String toString(){
			return "package";
		}
	};
	
	public boolean equals(Object obj){
		if(obj instanceof Visibility && obj.toString().equals(toString())){
			return true;
		}
		return false;
	}
	
	public static Visibility[] getVisibilities(){
		return new Visibility[]{
				PUBLIC,PRIVATE,PROTECTED,PACKAGE
		};
	}
}
