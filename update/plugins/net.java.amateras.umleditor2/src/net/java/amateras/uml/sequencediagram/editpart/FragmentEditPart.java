/**
 * 
 */
package net.java.amateras.uml.sequencediagram.editpart;

import java.beans.PropertyChangeEvent;

import net.java.amateras.uml.editpart.AbstractUMLEntityEditPart;
import net.java.amateras.uml.sequencediagram.model.ActivationModel;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

/**
 * @author Takahiro Shida.
 *
 */
public class FragmentEditPart extends AbstractUMLEntityEditPart {

	/* (뷄 Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		Figure figure = new Figure();
		figure.setBorder(new LineBorder(2));
		figure.setOpaque(false);
		return figure;
	}

	/* (뷄 Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ComponentEditPolicy() {
			protected Command getDeleteCommand(GroupRequest request) {
				return super.getDeleteCommand(request);
			}
		});
	}

	/* (뷄 Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(ActivationModel.P_CONSTRAINT)) {
			refreshVisuals();
		}
	}

}
