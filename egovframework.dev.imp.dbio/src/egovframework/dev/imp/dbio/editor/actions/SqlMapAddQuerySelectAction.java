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

import org.eclipse.jface.viewers.StructuredViewer;
import org.w3c.dom.Element;

import egovframework.dev.imp.dbio.editor.model.SqlMapCRUDElement;
import egovframework.dev.imp.dbio.editor.model.SqlMapElement;
import egovframework.dev.imp.dbio.editor.pages.SqlMapPage;
import egovframework.dev.imp.dbio.editor.parts.SqlMapMasterPart;

/**
 * select 쿼리맵을 생성하는 액션 클래스 
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
public class SqlMapAddQuerySelectAction extends SqlMapAddElementAction {

	private SqlMapPage page;
	/**
	 * 생성자
	 * @param page
	 * @param viewer
	 */
	public SqlMapAddQuerySelectAction(SqlMapPage page, StructuredViewer viewer) {
		super(viewer, "Add Select Query");
		this.setPage(page);
	}

	private void setPage(SqlMapPage page) {
		this.page = page;
	}

	@Override
	protected SqlMapElement createNewElement(Element root) {
		String sql = "SELECT * \n  FROM";
		
		String tagName = "select"; //$NON-NLS-1$
		
		SqlMapCRUDElement newElement =
			new SqlMapCRUDElement(root.getOwnerDocument().createElement(tagName),
					getGroups()[SqlMapMasterPart.QUERY_GROUP]);
		newElement.setId(tagName);
		root.appendChild(newElement.getDOMElement());
		newElement.setSQLStatement(sql);
		return newElement;	}

	/**
	 * 쿼리추가 실행
	 */
	public void run() {
		super.run();
		page.getMdBlock().getMasterPart().chkIdValidation();
		page.getEditor().refreshOutlinePage();		
	}	
}
