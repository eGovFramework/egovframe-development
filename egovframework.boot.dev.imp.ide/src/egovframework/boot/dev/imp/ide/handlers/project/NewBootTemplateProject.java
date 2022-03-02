package egovframework.boot.dev.imp.ide.handlers.project;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.actions.NewWizardShortcutAction;
import org.eclipse.ui.wizards.IWizardDescriptor;

import egovframework.boot.dev.imp.ide.EgovBootIdePlugin;
import egovframework.boot.dev.imp.ide.handlers.EgovBootMenu;
	
@SuppressWarnings("restriction")
public class NewBootTemplateProject extends AbstractHandler implements EgovBootMenu {
	/**
	 * 템플릿 프로젝트 생성 마법사
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWizardDescriptor wizardDesc = WorkbenchPlugin.getDefault().getNewWizardRegistry().findWizard("egovframework.boot.dev.imp.ide.wizards.egovboottemplateprojectwizard");

		if (wizardDesc == null) {
			MessageDialog.openInformation(EgovBootIdePlugin.getActiveWorkbenchWindow().getShell(), "Inform", "Selected function has not been installed. eGovFrmae Boot IDE is required.");
		} else {
			IAction action = new NewWizardShortcutAction(EgovBootIdePlugin.getActiveWorkbenchWindow(), wizardDesc);
			action.run();
		}
		return null;
	}
}
