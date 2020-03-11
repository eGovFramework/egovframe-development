package net.java.amateras.uml.activitydiagram.model;

import net.java.amateras.uml.model.AbstractUMLEntityModel;
import net.java.amateras.uml.model.ICloneableModel;

import org.eclipse.draw2d.geometry.Rectangle;

public class JoinNodeModel extends AbstractUMLEntityModel implements ICloneableModel {
	private static final long serialVersionUID = 7045710569468176888L;

	public Object clone(){
		JoinNodeModel model = new JoinNodeModel();
		model.setParent(getParent());
		model.setConstraint(new Rectangle(getConstraint()));
		return model;
	}
}
