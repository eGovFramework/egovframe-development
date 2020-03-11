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

import egovframework.dev.imp.dbio.editor.model.MapperAliasElement;
import egovframework.dev.imp.dbio.editor.model.MapperElement;
import egovframework.dev.imp.dbio.editor.pages.MapperPage;
import egovframework.dev.imp.dbio.editor.parts.MapperMasterPart;

/**
 * 앨리어스를 추가하는 액션 클래스
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
public class MapperAddAliasAction extends MapperAddElementAction {
	private MapperPage page;
	
	/**
	 * 생성자
	 * @param viewer
	 */
	public MapperAddAliasAction(MapperPage page, TreeViewer viewer) {
		super(viewer, "Add alias");
		this.setPage(page);
	}

	private void setPage(MapperPage page) {
		this.page = page;
	}

	/**
	 * 노드 추가
	 */
	@Override
	protected MapperElement createNewElement(Element root) {
		MapperAliasElement newElement = 
			new MapperAliasElement(root.getOwnerDocument().createElement("typeAlias"), //$NON-NLS-1$
					getGroups()[MapperMasterPart.ALIAS_GROUP]);
		newElement.setAlias("newAlias");
		root.appendChild(newElement.getDOMElement());
		return newElement;
	}
	
	/**
	 * 결과맵 추가 실행
	 */
	public void run() {
		super.run();
		Element element = (Element) getViewer().getInput();
		page.getMdBlock().getMasterPart().refreshViewer(element);
		page.getMdBlock().getMasterPart().chkIdValidation();
		page.getEditor().refreshOutlinePage();		
	}	
}
