package net.java.amateras.uml.usecasediagram.edit;

import net.java.amateras.uml.editpart.AbstractUMLConnectionEditPart;
import net.java.amateras.uml.usecasediagram.figure.UsecaseExtendConnectionFigure;

import org.eclipse.draw2d.IFigure;

/**
 * 
 * @author Takahiro Shida.
 *
 */
public class UsecaseExtendEditPart extends AbstractUMLConnectionEditPart {

	protected IFigure createFigure() {
		return new UsecaseExtendConnectionFigure();
	}
}
