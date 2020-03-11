/*
 * Copyright 2008-2009 the original author or authors.
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
package egovframework.dev.imp.dbio.editor.actions;


import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.ui.actions.BaseSelectionListenerAction;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import egovframework.dev.imp.dbio.editor.model.SqlMapGroupElement;

/**
 * SqlMap Action
 * @author 개발환경 개발팀 김형조
 * @since 2009.02.20
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.02.20  김형조          최초 생성
 *
 * 
 * </pre>
 */
public class SqlMapAction extends BaseSelectionListenerAction {

	/** 맵파일 정보 */
	private StructuredViewer viewer;

	/**
	 * 생성자
	 * 
	 * @param viewer
	 * @param text
	 */
	public SqlMapAction(StructuredViewer viewer, String text) {
		super(text);
		//this.viewer = viewer;
		this.setViewer(viewer);
	}

	private void setViewer(StructuredViewer viewer) {
		this.viewer = viewer;
	}
	/**
	 *  맵파일 정보 반환
	 * @return 맵파일 정보
	 */
	protected StructuredViewer getViewer() {
		return viewer;
	}
	
	/**
	 * 맵파일 정보를 갱신
	 * @param group
	 * @param selection
	 */
	public void refreshViewer(SqlMapGroupElement group, ISelection selection) {
		viewer.refresh(group);
		//viewer.refresh();
		viewer.setSelection(selection, true);
	}
	
	/**
	 * 맵파일 정보 갱신(화면)
	 */
	public void refreshViewer() {
		viewer.refresh();
	}
	
	/**
	 * 그룹 초기화
	 */
	protected void resetGroups() {
		for (Object group : getGroups()) {
			((SqlMapGroupElement) group).reset();
		}
	}
	
	/**
	 * 그룹목록 반환
	 * @return
	 */
	protected Object[] getGroups() {
		if (getViewer().getInput() instanceof Element) {
			ITreeContentProvider contentProvider = (ITreeContentProvider) getViewer().getContentProvider();
			return contentProvider.getChildren(getViewer().getInput());
		} else {
			return new Object[0];
		}
	}
	
	/**
	 * NewLine추가
	 * @param element
	 */
	protected void addNewLineElement(Element element) {
		Text newLineNode = element.getOwnerDocument().createTextNode(System.getProperty("line.separator")); //$NON-NLS-1$
		element.appendChild(newLineNode);
	}

}
