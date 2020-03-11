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

import egovframework.dev.imp.dbio.editor.model.MapperCRUDElement;
import egovframework.dev.imp.dbio.editor.model.MapperElement;
import egovframework.dev.imp.dbio.editor.parts.MapperMasterPart;

/**
 * AddQuery Action
 * @author 개발환경 개발팀 김효수
 * @since 2019.02.25
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2019.02.25  김효수          MyBatis DBIO  
 *
 * 
 * </pre>
 */
public class MapperAddQueryAction extends MapperAddElementAction implements MapperAddQuery{

	/**
	 * 생성자
	 * 
	 * @param page
	 * @param viewer
	 */
	public MapperAddQueryAction(StructuredViewer viewer) {
		super(viewer, "Add query");
		//this.setPage(page);
	}

	/**
	 * 쿼리항목추가
	 */
	@Override
	protected MapperElement createNewElement(Element root) {
		String sql = "SELECT * \n  FROM";
		
		String tagName = "select"; //$NON-NLS-1$
		
		MapperCRUDElement newElement =
			new MapperCRUDElement(root.getOwnerDocument().createElement(tagName),
					getGroups()[MapperMasterPart.QUERY_GROUP]);
		newElement.setId(tagName);
		root.appendChild(newElement.getDOMElement());
		newElement.setSQLStatement(sql);
		return newElement;
	}

	/**
	 * 쿼리추가 실행
	 */
	public void run() {
		// DbioLog.logInfo("sham");
	}
}
