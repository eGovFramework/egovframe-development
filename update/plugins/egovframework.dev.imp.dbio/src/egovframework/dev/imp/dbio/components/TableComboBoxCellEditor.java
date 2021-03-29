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

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;

/**
 * 테이블 콤보박스 셀 편집기
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
public class TableComboBoxCellEditor extends ComboBoxViewerCellEditor {

	private TableViewer viewer;
	private String property;
	private String originalValue;
	
	/**
	 * 생성자
	 * @param viewer
	 * @param column
	 */
	public TableComboBoxCellEditor(TableViewer viewer, int column) {
		this(viewer, column, SWT.NONE);
	}
	
	/**
	 * 생성자
	 * @param viewer
	 * @param column
	 * @param style
	 */
	@SuppressWarnings("deprecation")
	public TableComboBoxCellEditor(TableViewer viewer, int column, int style) {
		super(viewer.getTable(), style);
		setContenProvider(new ArrayContentProvider());
		setLabelProvider(new LabelProvider());
		
		this.viewer = viewer;
		this.property = (String) viewer.getColumnProperties()[column];
	}
	
	/**
	 * 생성자
	 * @param viewer
	 * @param column
	 * @param items
	 */
	public TableComboBoxCellEditor(TableViewer viewer, int column, String[] items) {
		this(viewer, column, SWT.NONE);
		setInput(items);
	}
	
	/**
	 * 생성자
	 * @param viewer
	 * @param column
	 * @param items
	 * @param style
	 */
	public TableComboBoxCellEditor(TableViewer viewer, int column, String[] items, int style) {
		this(viewer, column, style);
		setInput(items);
	}
	
	/**
	 * 콤보박스 활성화
	 */
	@Override
	public void activate() {
		super.activate();
	
		originalValue = getCombo().getText();
	}
	
	/**
	 * 수정 이벤트 처리
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
		String value = getCombo().getText();
		if (!value.equals(originalValue)) {
			fireModifyEvent(value);
		}
		super.focusLost();
	}
	
	/**
	 * 콤보 인스턴스 반환
	 * @return 콤보 인스턴스
	 */
	protected CCombo getCombo() {
		return (CCombo) getControl();
	}
	
	/**
	 * 콤보박스 셀에 아이템 세팅
	 * @param items
	 */
	public void setItems(String[] items) {
		setInput(items);
	}
}
