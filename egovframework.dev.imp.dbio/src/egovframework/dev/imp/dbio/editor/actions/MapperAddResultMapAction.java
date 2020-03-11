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

import egovframework.dev.imp.dbio.editor.model.MapperElement;
import egovframework.dev.imp.dbio.editor.model.MapperResultMapElement;
import egovframework.dev.imp.dbio.editor.pages.MapperPage;
import egovframework.dev.imp.dbio.editor.parts.MapperMasterPart;

/**
 * MapperAddResultMap Action
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
public class MapperAddResultMapAction extends MapperAddElementAction{
//public class MapperAddResultMapAction extends MapperAddElementAction implements MapperAddResultMap{//이벤트 미처리로 인해 MapperAddResultMap 인터페이스를 제거
	private MapperPage page;
	/**
	 * 생성자
	 * 
	 * @param viewer
	 */
	public MapperAddResultMapAction(MapperPage page, StructuredViewer viewer) {
		super(viewer, "Add resultMap");
		this.setPage(page);			
	}

	private void setPage(MapperPage page) {
		this.page = page;
	}
	/**
	 * 결과맵 항목 추가
	 */
	@Override
	protected MapperElement createNewElement(Element root) {
		MapperResultMapElement newElement =
			new MapperResultMapElement(root.getOwnerDocument().createElement("resultMap"), //$NON-NLS-1$
					getGroups()[MapperMasterPart.RESULT_GROUP]);
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
