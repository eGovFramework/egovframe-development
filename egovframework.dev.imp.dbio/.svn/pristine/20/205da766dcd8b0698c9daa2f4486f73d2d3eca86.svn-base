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


import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.w3c.dom.Element;

import egovframework.dev.imp.dbio.editor.model.SqlMapElement;
import egovframework.dev.imp.dbio.editor.model.SqlMapGroupElement;

/**
 * AddSqlMapElement Action
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
public abstract class SqlMapAddElementAction extends SqlMapAction {

	/**
	 * 생성자
	 * 
	 * @param viewer
	 * @param text
	 */
	public SqlMapAddElementAction(StructuredViewer viewer, String text) {
		super(viewer, text);
	}

	/**
	 * 입력여부확인
	 */
	@Override
	protected boolean updateSelection(IStructuredSelection selection) {
		return getViewer().getInput() != null; 
	}
	
	@Override
	public void run() {
		Element element = (Element) getViewer().getInput();
		if (element != null) {
			SqlMapElement newElement = createNewElement(element);
			if (newElement != null) {
				addNewLineElement(element);
				SqlMapGroupElement group = (SqlMapGroupElement) newElement.getParent();
				if (group == null) {
					resetGroups();
				} else {
					group.reset();
				}
				refreshViewer(group, new StructuredSelection(newElement));
			}
		}
	}
	
	/**
	 * 새SqlMap 추가
	 * @param root
	 * @return
	 */
	protected abstract SqlMapElement createNewElement(Element root);
}
