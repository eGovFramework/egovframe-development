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
package egovframework.dev.imp.dbio.components;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

/**
 * 테이블 텍스트 셀 편집
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
public class TableTextCellEditor extends TextCellEditor {

	private TableViewer viewer;
	private String property;
	private String originalValue;

	/**
	 * 생성자
	 * @param viewer
	 * @param column
	 */
	public TableTextCellEditor(TableViewer viewer, int column) {
		super(viewer.getTable());
		this.setViewer(viewer);
		//this.setColumn(column);

		this.setProperty(column);
	}

	private void setViewer(TableViewer viewer) {
		this.viewer = viewer;
	}
	
	private void setProperty(int column) {
		this.property = (String) viewer.getColumnProperties()[column];
	}
	
	/**
	 * 테이블 텍스트 셀 활성화
	 */
	@Override
	public void activate() {
		super.activate();

		originalValue = text.getText();
	}

	/**
	 * 변경 이벤트 발생시 처리
	 * @param newValue
	 */
	private void fireModifyEvent(String newValue) {
		viewer.getCellModifier().modify(
				((IStructuredSelection) viewer.getSelection()).getFirstElement(),
				property, newValue);
	}

	/**
	 * 포커스 이탈시 처리
	 */
	@Override
	protected void focusLost() {
		String value = text.getText();
		if (!value.equals(originalValue)) {
			fireModifyEvent(value);
		}
		super.focusLost();
	}

}
