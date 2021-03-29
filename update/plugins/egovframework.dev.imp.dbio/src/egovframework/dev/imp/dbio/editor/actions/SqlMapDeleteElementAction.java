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

import java.util.Iterator;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.w3c.dom.Element;

import egovframework.dev.imp.dbio.editor.model.SqlMapElement;
import egovframework.dev.imp.dbio.editor.pages.SqlMapPage;

/**
 * DeleteSqlMapElement Action
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
public class SqlMapDeleteElementAction extends SqlMapAction {

	private SqlMapPage page;
	/**
	 * 생성자
	 * 
	 * @param viewer
	 */
	public SqlMapDeleteElementAction(SqlMapPage page, StructuredViewer viewer) {
		super(viewer, "Delete");
		this.setPage(page);		
	}
	private void setPage(SqlMapPage page) {
		this.page = page;
	}
	/**
	 * 업데이트 여부를 판별
	 */
	@Override
	protected boolean updateSelection(IStructuredSelection selection) {
		Iterator<?> iterator = selection.iterator();
		while (iterator.hasNext()) {
			Object obj = iterator.next();
			if (!(obj instanceof SqlMapElement)) return false;
		}
		return true;
	}

	/**
	 * 항목 삭제
	 */
	@Override
	public void run() {
		Element root = (Element) getViewer().getInput();
		IStructuredSelection selection = getStructuredSelection();
		Iterator<?> iter = selection.iterator();
		while (iter.hasNext()) {
			SqlMapElement element = (SqlMapElement) iter.next();
			root.removeChild(element.getDOMElement());
		}
		resetGroups();
		//refreshViewer();
		page.getMdBlock().getMasterPart().refreshViewer();
		page.getMdBlock().getMasterPart().chkIdValidation();
		page.getEditor().refreshOutlinePage();		
	}

}
