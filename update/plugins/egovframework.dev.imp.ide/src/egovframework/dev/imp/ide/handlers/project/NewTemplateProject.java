package egovframework.dev.imp.ide.handlers.project;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.actions.NewWizardShortcutAction;
import org.eclipse.ui.wizards.IWizardDescriptor;

import egovframework.dev.imp.ide.EgovIdePlugin;
import egovframework.dev.imp.ide.handlers.EgovMenu;

@SuppressWarnings("restriction")
public class NewTemplateProject extends AbstractHandler implements EgovMenu {
	/**
	 * 템플릿 프로젝트 생성 마법사
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWizardDescriptor wizardDesc = WorkbenchPlugin.getDefault().getNewWizardRegistry().findWizard("egovframework.dev.imp.ide.wizards.egovtemplateprojectwizard");

		if (wizardDesc == null) {
			MessageDialog.openInformation(EgovIdePlugin.getActiveWorkbenchWindow().getShell(), "Inform", "Selected function has not been installed. eGovFrmae IDE is required.");
		} else {
			IAction action = new NewWizardShortcutAction(EgovIdePlugin.getActiveWorkbenchWindow(), wizardDesc);
			action.run();
		}
		return null;
	}
}
