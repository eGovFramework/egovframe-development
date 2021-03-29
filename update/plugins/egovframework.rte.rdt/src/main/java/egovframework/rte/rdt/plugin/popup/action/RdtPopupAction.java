/*
 * Copyright 2011 MOSPA(Ministry of Security and Public Administration).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package egovframework.rte.rdt.plugin.popup.action;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import egovframework.rte.rdt.plugin.editor.RdtEditor;
import egovframework.rte.rdt.plugin.util.ProjectUtil;

/**
 * 팝업메뉴를 선택했을때 실행될 내용을 정의한 클래스
 * @author 이영진
 */
public class RdtPopupAction implements IObjectActionDelegate {

	/** 현재 shell 을 저장 */
	private Shell shell;
	/** 현재 선택된 프로젝트를 저장 */
	private IProject activeProject;

	/**
	 * RdtPopupAction의 기본생성자
	 */
	public RdtPopupAction() {
		super();
	}

	/**
	 * 활성화된 파트에서 동작하기 위해 setActivePart 를 구현한다.
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	/**
	 * 팝업액션이 호출됬을때 실행될 메소드
	 * RdtEditor를 생성한다
	 * @param action
	 */
	public void run(IAction action) {
		IFile filebinder = activeProject.getFile("Distribution.tool");

		IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

		try {
			FileEditorInput fei = new FileEditorInput(filebinder);

			IEditorPart editor = activePage.openEditor(fei, "egovframework.rte.rdt.plugin.rdtEditor");

			RdtEditor rdtEditor = (RdtEditor) editor;
			rdtEditor.setEditor(editor);
			rdtEditor.setCurrentProject(activeProject);
		} catch (Exception e) {
			MessageDialog.openInformation(shell, "Error In New Action", "error");
		}
	}

	/**
	 * 선택된 프로젝트가 바뀌면 바뀐프로젝트로 업데이트한다.
	 * @param action
	 * @param selection 선택한 리소스
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		IResource resource = ProjectUtil.getSelectedResource(selection);

		if (resource == null)
			return;
		activeProject = resource.getProject();
	}

}
