package egovframework.bdev.imp.ide.com.handlers.project;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.actions.NewWizardShortcutAction;
import org.eclipse.ui.wizards.IWizardDescriptor;

import egovframework.bdev.imp.ide.EgovBatchIdePlugin;
import egovframework.bdev.imp.ide.com.handlers.EgovBatchMenu;

/**
 * 배치 IDE용 잡런쳐 생성 메뉴 클래스
 * 
 * @author 배치개발환경 개발팀
 * @since 2012.07.02 
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 * 수정일      	수정자           수정내용
 *  -------			--------    ---------------------------
 * 2012.07.02	배치개발환경 개발팀    최초 생성
 * 
 * 
 * </pre>
 */
@SuppressWarnings("restriction")
public class NewBatchJobLauncher extends AbstractHandler implements EgovBatchMenu {
	/**
	 * 잡런쳐 생성 마법사
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWizardDescriptor wizardDesc = WorkbenchPlugin.getDefault().getNewWizardRegistry().findWizard("egovframework.bdev.imp.batch.wizards.NewBatchJobLauncherWizard");

		if (wizardDesc == null) {
			MessageDialog.openInformation(EgovBatchIdePlugin.getActiveWorkbenchWindow().getShell(), "Inform",
					"Selected function has not been installed. eGovFrame Batch IDE is required.");
		} else {

			IAction action = new NewWizardShortcutAction(EgovBatchIdePlugin.getActiveWorkbenchWindow(), wizardDesc);
			action.run();
		}

		return null;
	}
}
