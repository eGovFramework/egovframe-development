/**
 * 
 */
package net.java.amateras.uml.editpart;

import net.java.amateras.uml.model.AnchorModel;
import net.java.amateras.uml.model.NoteModel;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

/**
 * Note궴Anchor궻귒뫮돒궥귡EditPartFactory.
 * @author Takahiro Shida.
 *
 */
public abstract class BaseUMLEditPartFactory implements EditPartFactory {

	/* (뷄 Javadoc)
	 * @see org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart, java.lang.Object)
	 */
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart part = createUMLEditPart(context, model);		
		if(model instanceof NoteModel) {
			part = new NoteEditPart();
		} else if (model instanceof AnchorModel) {
			part = new AnchorEditPart();
		}
		if (part.getModel() == null) {
			part.setModel(model);
		}
		return part;
	}

	protected abstract EditPart createUMLEditPart(EditPart context, Object model);
}
