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

import egovframework.dev.imp.dbio.editor.model.SqlMapElement;
import egovframework.dev.imp.dbio.editor.model.SqlMapResultMapElement;
import egovframework.dev.imp.dbio.editor.pages.SqlMapPage;
import egovframework.dev.imp.dbio.editor.parts.SqlMapMasterPart;

/**
 * AddResultMap Action
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
public class SqlMapAddResultMapAction extends SqlMapAddElementAction{
//public class AddResultMapAction extends AddSqlMapElementAction implements AddResultMap{//이벤트 미처리로 인해 AddResultMap 인터페이스를 제거
	private SqlMapPage page;
	/**
	 * 생성자
	 * 
	 * @param viewer
	 */
	public SqlMapAddResultMapAction(SqlMapPage page, StructuredViewer viewer) {
		super(viewer, "Add resultMap");
		this.setPage(page);			
	}

	private void setPage(SqlMapPage page) {
		this.page = page;
	}
	/**
	 * 결과맵 항목 추가
	 */
	@Override
	protected SqlMapElement createNewElement(Element root) {
		SqlMapResultMapElement newElement =
			new SqlMapResultMapElement(root.getOwnerDocument().createElement("resultMap"), //$NON-NLS-1$
					getGroups()[SqlMapMasterPart.RESULT_GROUP]);
		newElement.setId("resultMap");
		root.appendChild(newElement.getDOMElement());
		return newElement;
	}
	
	/**
	 * 결과맵 추가 실행
	 */
	public void run() {
		super.run();
		page.getMdBlock().getMasterPart().chkIdValidation();
		page.getEditor().refreshOutlinePage();		
	}	
}
