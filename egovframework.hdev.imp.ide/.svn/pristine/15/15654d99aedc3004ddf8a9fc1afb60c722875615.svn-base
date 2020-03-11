/**
 * 
 */
package egovframework.hdev.imp.ide.project;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.actions.NewWizardShortcutAction;
import org.eclipse.ui.wizards.IWizardDescriptor;

import egovframework.hdev.imp.ide.EgovDeviceAPIIdePlugin;
import egovframework.hdev.imp.ide.common.DeviceAPIIdeUtils;
import egovframework.hdev.imp.ide.handlers.EgovDeviceAPIMenu;

/**  
 * @Class Name : NewDeviceAPIHybridProject
 * @Description : NewDeviceAPIHybridProject Class
 * @Modification Information  
 * @
 * @  수정일			수정자		수정내용
 * @ ---------		---------	-------------------------------
 * @ 2012. 8. 24.		이율경		최초생성
 * 
 * @author 디바이스 API 개발환경 팀
 * @since 2012. 8. 24.
 * @version 1.0
 * @see
 * 
 */
@SuppressWarnings("restriction")
public class NewDeviceAPIHybridProject extends AbstractHandler implements EgovDeviceAPIMenu {

	/**
	 * 기본 프로젝트 생성 마법사 실행
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {

		if (DeviceAPIIdeUtils.isAndroidDevelopmentTool() == false) {

			MessageDialog.openInformation(EgovDeviceAPIIdePlugin.getActiveWorkbenchWindow().getShell(), "Inform", "Selected function has not been installed. ADT is required.");
			return null;
		}

		if (DeviceAPIIdeUtils.isMaven3Version() == false) {

			MessageDialog.openInformation(EgovDeviceAPIIdePlugin.getActiveWorkbenchWindow().getShell(), "Inform",
					"Selected function has not been installed. Maven3(M2E) is required.");
			return null;
		}

		if (DeviceAPIIdeUtils.isM2EToAndroid() == false) {

			MessageDialog.openInformation(EgovDeviceAPIIdePlugin.getActiveWorkbenchWindow().getShell(), "Inform",
					"Selected function has not been installed. M2E Configure for Android is required.");
			return null;
		}

		IWizardDescriptor wizardDesc = WorkbenchPlugin.getDefault().getNewWizardRegistry().findWizard("egovframework.hdev.imp.ide.wizards.newdeviceapi");

		if (wizardDesc == null) {
			MessageDialog.openInformation(EgovDeviceAPIIdePlugin.getActiveWorkbenchWindow().getShell(), "Inform",
					"Selected function has not been installed. eGovFrmae IDE is required.");
		} else {
			IAction action = new NewWizardShortcutAction(EgovDeviceAPIIdePlugin.getActiveWorkbenchWindow(), wizardDesc);
			action.run();
		}

		return null;
	}
}
