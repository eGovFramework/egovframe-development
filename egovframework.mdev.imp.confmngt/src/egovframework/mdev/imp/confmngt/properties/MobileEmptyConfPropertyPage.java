package egovframework.mdev.imp.confmngt.properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;

public class MobileEmptyConfPropertyPage extends PropertyPage implements
		IWorkbenchPropertyPage {

	public MobileEmptyConfPropertyPage() {
	}

	@Override
	protected Control createContents(Composite parent) {
		noDefaultAndApplyButton();
		
		String description = "Configure eGovFrame properties for your project";  

		Text text = new Text(parent, SWT.READ_ONLY); 
		text.setBackground(parent.getBackground()); 
		text.setText(description); 
		
		return parent;
	}

}
