/**
 * 
 */
package net.java.amateras.uml.sequencediagram.model;

import net.java.amateras.uml.model.AbstractUMLEntityModel;

/**
 * @author Takahiro Shida.
 *
 */
public class FragmentModel extends AbstractUMLEntityModel {

	private static final long serialVersionUID = 6884609252444446136L;

	private static final String P_CONDITION = "_condition";
	
	private String condition;

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		String old = this.condition;
		this.condition = condition;
		firePropertyChange(P_CONDITION, old, condition);
	}

}
