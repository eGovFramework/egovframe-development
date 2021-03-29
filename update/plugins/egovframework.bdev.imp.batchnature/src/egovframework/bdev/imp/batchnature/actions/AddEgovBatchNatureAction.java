package egovframework.bdev.imp.batchnature.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.osgi.service.prefs.BackingStoreException;

import egovframework.bdev.imp.batchnature.EgovBatchNature;
import egovframework.bdev.imp.batchnature.EgovBatchNaturePlugin;
import egovframework.bdev.imp.batchnature.common.BatchNatureLog;
import egovframework.bdev.imp.batchnature.util.GetProjectInfoUtil;
import egovframework.bdev.imp.batchnature.util.HandlePomXMLFileUtil;

public class AddEgovBatchNatureAction implements IObjectActionDelegate {

	/**
	 * RdtPopupAction의 기본생성자
	 */
	public AddEgovBatchNatureAction() {
		super();
	}

	/**
	 * 활성화된 파트에서 동작하기 위해 setActivePart 를 구현한다.
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

	/**
	 * 팝업액션이 호출됬을때 실행될 메소드
	 * RdtEditor를 생성한다
	 * @param action
	 */
	public void run(IAction action) {

		IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

		ISelection selection = activePage.getSelection();

		IProject project = GetProjectInfoUtil.getSelectedProject(selection);
		//        WizardDialog dialog = new WizardDialog(GetProjectInfoUtil.getActiveShell(), new EgovBatchNatureWizard(project));
		//        dialog.setPageSize(400, 300);
		//        dialog.open();

		//Wizard 없앰 위저드 필요할 경우 위의 주석 풀고 아래 try문 주석화
		try {
			IProjectDescription description = project.getDescription();
			String natures[] = description.getNatureIds();
			String newNatures[] = new String[natures.length + 1];
			System.arraycopy(natures, 0, newNatures, 0, natures.length);
			newNatures[natures.length] = EgovBatchNature.Nature_ID;
			description.setNatureIds(newNatures);
			project.setDescription(description, null);
			new ProjectScope(project).getNode(EgovBatchNaturePlugin.PLUGIN_ID).flush();

			//eGovBatchNature를 추가하면서 pom.xml 파일 핸들링
			HandlePomXMLFileUtil.isPomFileExist(project);

		} catch (BackingStoreException e) {
			BatchNatureLog.logError(e);
			// fail store property for nature because of locking.. or something bad.
		} catch (Exception e) {
			BatchNatureLog.logError(e);
		}

		return;
	}

	/**
	 * 선택된 프로젝트가 바뀌면 바뀐프로젝트로 업데이트한다.
	 * @param action
	 * @param selection 선택한 리소스
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}
}
