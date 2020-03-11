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


import org.eclipse.jface.viewers.TreeViewer;
import org.w3c.dom.Element;

import egovframework.dev.imp.dbio.editor.model.SqlMapElement;
import egovframework.dev.imp.dbio.editor.model.SqlMapParameterMapElement;
import egovframework.dev.imp.dbio.editor.pages.SqlMapPage;
import egovframework.dev.imp.dbio.editor.parts.SqlMapMasterPart;

/**
 * AddParameterMap Action
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
public class SqlMapAddParameterMapAction extends SqlMapAddElementAction{
//public class AddParameterMapAction extends AddSqlMapElementAction implements AddParameterMap{//이벤트 미처리로 AddParameterMap 인터페이스를 뺀다. 
	private SqlMapPage page;
	/**
	 * 생성자
	 * @param viewer
	 */
	public SqlMapAddParameterMapAction(SqlMapPage page, TreeViewer viewer) {
		super(viewer, "Add parameterMap");
		this.setPage(page);		
	}
	private void setPage(SqlMapPage page) {
		this.page = page;
	}
		
	/**
	 * 새 모델 인스턴스 생성
	 */
	@Override
	protected SqlMapElement createNewElement(Element root) {
		SqlMapParameterMapElement newElement = 
			new SqlMapParameterMapElement(root.getOwnerDocument().createElement("parameterMap"), //$NON-NLS-1$
					getGroups()[SqlMapMasterPart.PARAMETER_GROUP]);
		newElement.setId("parameterMap");
		root.appendChild(newElement.getDOMElement());
		return newElement;
	}
	
	/**
	 * 패러미터맵 추가 실행
	 */
	public void run() {
		super.run();
		page.getMdBlock().getMasterPart().chkIdValidation();
		page.getEditor().refreshOutlinePage();		
	}	
}
