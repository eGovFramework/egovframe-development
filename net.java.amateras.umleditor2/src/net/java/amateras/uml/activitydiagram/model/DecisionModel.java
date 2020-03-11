package net.java.amateras.uml.activitydiagram.model;

import net.java.amateras.uml.model.AbstractUMLEntityModel;
import net.java.amateras.uml.model.ICloneableModel;

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * 
 * @author Naoki Takezoe
 */
public class DecisionModel extends AbstractUMLEntityModel implements ICloneableModel {

	private static final long serialVersionUID = -6797822519249229453L;

	public Object clone() {
		DecisionModel model = new DecisionModel();
		model.setParent(getParent());
		model.setConstraint(new Rectangle(getConstraint()));
		return model;
	}
	
}
