package net.java.amateras.uml.activitydiagram.model;

import net.java.amateras.uml.model.AbstractUMLEntityModel;
import net.java.amateras.uml.model.ICloneableModel;

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * 
 * @author Naoki Takezoe
 */
public class FinalStateModel extends AbstractUMLEntityModel implements ICloneableModel {
	private static final long serialVersionUID = -6728534619176051000L;

	public Object clone(){
		FinalStateModel model = new FinalStateModel();
		model.setParent(getParent());
		model.setConstraint(new Rectangle(getConstraint()));
		return model;
	}
}
