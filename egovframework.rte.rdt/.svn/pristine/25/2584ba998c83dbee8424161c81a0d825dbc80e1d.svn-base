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
package egovframework.rte.rdt.plugin.editor;

import java.util.HashMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.MultiPageEditorPart;

import egovframework.rte.rdt.plugin.util.ProjectUtil;

/**
 * MultiPageEditorPart를 상속하여 구현한 RdtEditor 클래스
 * page를 추가하게 될 경우를 고려함 
 * @author 이영진
 */
public class RdtEditor extends MultiPageEditorPart {

	/** 자기객체를 저장함 */
	private RdtEditor instance;
	/** 표시될 텍스트에디터 */
	private TextEditor textEditor;

	/** 에디터영역을 저장 */
	private IEditorPart editor;
	/** 현재 쉘*/
	private Shell shell;
	/** 현재 프로젝트 저장 */
	private IProject currentProject;

	/** 페이지를 저장 */
	private HashMap<Integer, Page> mapPages = new HashMap<Integer, Page>();

	/** 페이지 인덱스 */
	private int indexTextEditor = -1;

	/** 페이지 이름을 저장한 스트링 */
	private String pageName = "RTE Tool";

	/**
	 * 생성자
	 */
	public RdtEditor() {

		super();
		instance = this;
	}

	/**
	 * 
	 * @return HashMap<Integer, Page>
	 */
	public HashMap<Integer, Page> getMapPages() {
		return mapPages;
	}

	public void setMapPages(HashMap<Integer, Page> mapPages) {
		this.mapPages = mapPages;
	}

	public RdtEditor getInstance() {
		return instance;
	}

	public IEditorPart getEditor() {
		return editor;
	}

	public void setEditor(IEditorPart editor) {
		this.editor = editor;
	}

	public IProject getCurrentProject() {
		return currentProject;
	}

	public void setCurrentProject(IProject currentProject) {
		this.currentProject = currentProject;
	}

	public Shell getShell() {
		return shell;
	}

	public void setShell(Shell shell) {
		this.shell = shell;
	}

	protected void createPages() {
		createPage(pageName);
	}

	void createPage(String pageNameKey) {
		Page page = null;
		page = new DistributionToolPage(this);

		int index = addPage(page.getPage(getContainer()));
		setPageText(index, pageName);
		mapPages.put(index, page);
	}

	public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
		shell = site.getShell();
		IResource selectionResource = null;
		ISelection selection = site.getPage().getSelection();
		if (selection != null)
			selectionResource = ProjectUtil.getSelectedResource(selection);

		if (selectionResource != null) {
			currentProject = selectionResource.getProject();
		}

		super.init(site, editorInput);

		refreshWorkspace();
	}

	public void refreshWorkspace() {
		try {
			this.currentProject.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	/**
	 * textEditor의 doSave메소드를 호출한다
	 * @param monitor
	 */
	public void doSave(IProgressMonitor monitor) {
		textEditor.doSave(monitor);
	}

	/**
	 * 현재상태와 페이지에 대한 내용을 저장한다
	 */
	public void doSaveAs() {
		textEditor.doSaveAs();

		String title = textEditor.getTitle();
		IEditorInput editorInput = textEditor.getEditorInput();

		setPageText(indexTextEditor, title);
		setInput(editorInput);
	}

	public void dispose() {
		super.dispose();
	}
}
