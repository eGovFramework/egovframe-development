package net.java.amateras.uml.usecasediagram.edit;

import net.java.amateras.uml.editpart.AbstractUMLConnectionEditPart;
import net.java.amateras.uml.usecasediagram.figure.UsecaseIncludeConnectionFigure;

import org.eclipse.draw2d.IFigure;

/**
 * 
 * @author Takahiro Shida.
 *
 */
public class UsecaseIncludeEditPart extends AbstractUMLConnectionEditPart {

	protected IFigure createFigure() {
		return new UsecaseIncludeConnectionFigure();
	}
}
