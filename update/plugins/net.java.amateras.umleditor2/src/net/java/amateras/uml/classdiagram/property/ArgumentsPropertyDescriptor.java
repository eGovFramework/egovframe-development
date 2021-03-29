package net.java.amateras.uml.classdiagram.property;

import java.util.List;

import net.java.amateras.uml.classdiagram.model.Argument;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.views.properties.PropertyDescriptor;

/**
 *
 * @author Naoki Takezoe
 */
public class ArgumentsPropertyDescriptor extends PropertyDescriptor {

	public ArgumentsPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
	}

	public CellEditor createPropertyEditor(Composite parent) {
		FormPropertiesDialogCellEditor editor = new FormPropertiesDialogCellEditor(parent);
		if (getValidator() != null){
			editor.setValidator(getValidator());
		}
		return editor;
	}

	/**
	 */
	private class FormPropertiesDialogCellEditor extends DialogCellEditor {

		public FormPropertiesDialogCellEditor(Composite parent) {
			super(parent);
		}

		@SuppressWarnings("unchecked")
		protected Object openDialogBox(Control cellEditorWindow) {
			List<Argument> value = (List<Argument>) getValue();

			ArgumentsEditDialog dialog = new ArgumentsEditDialog(cellEditorWindow.getShell(), value);
			if(dialog.open()==Dialog.OK){
				value = dialog.getArguments();
			}

			return value;
		}

	}

}
