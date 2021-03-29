package egovframework.bdev.imp.batchnature.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import egovframework.bdev.imp.batchnature.EgovBatchNature;
import egovframework.bdev.imp.batchnature.common.BatchNatureLog;
import egovframework.bdev.imp.batchnature.util.GetProjectInfoUtil;

public class RemoveEgovBatchNatureAction implements IObjectActionDelegate {

	/**
	 * RdtPopupAction의 기본생성자
	 */
	public RemoveEgovBatchNatureAction() {
		super();
	}

	/**
	 * 활성화된 파트에서 동작하기 위해 setActivePart 를 구현한다.
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

	/**
	 * 팝업액션이 호출됬을때 실행될 메소드 RdtEditor를 생성한다
	 * 
	 * @param action
	 */
	public void run(IAction action) {
		IWorkbenchPage activePage = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();

		ISelection selection = activePage.getSelection();
		IProject project = GetProjectInfoUtil.getSelectedProject(selection);
		
		if(project == null) {
			return;
		}

		try {
			IProjectDescription description = project.getDescription();
			String natures[] = description.getNatureIds();
			description.setNatureIds(removeNature(natures));
			project.setDescription(description, null);
			project.refreshLocal(2, new NullProgressMonitor());
		} catch (Exception e) {
			BatchNatureLog.logError(e);

		}
	}

	private static String[] removeNature(String natures[]) {
		List<String> newNatureList = new ArrayList<String>();
		for (int i = 0; i < natures.length; i++) {
			if (!natures[i].equalsIgnoreCase(EgovBatchNature.Nature_ID)) {
				newNatureList.add(natures[i]);
			}
		}
		return (String[]) newNatureList.toArray(new String[0]);
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}
}
