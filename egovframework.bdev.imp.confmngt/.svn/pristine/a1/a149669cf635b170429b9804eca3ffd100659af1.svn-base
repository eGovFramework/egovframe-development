/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package egovframework.bdev.imp.confmngt.preferences.com;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;

import egovframework.bdev.imp.confmngt.EgovBatchConfMngtPlugin;
import egovframework.bdev.imp.confmngt.common.BConfMngtMessages;
import egovframework.bdev.imp.confmngt.preferences.com.model.Info;
import egovframework.dev.imp.core.utils.BatchTableColumn;
import egovframework.dev.imp.core.utils.NullUtil;
import egovframework.dev.imp.core.utils.PrefrencePropertyUtil;

/**
 * 배치 생성 컨택스트 추상 클래스
 * 
 * @since 2012.07.02
 * @author 배치개발환경 개발팀 조용현
 * @version 1.0
 * @see <pre>
 *      개정이력(Modification Information)
 *   
 *	  	수정일	수정자		수정내용
 *  ---------- ------- -------------------
 *  2012.07.02	조용현	최초 생성
 * 
 * </pre>
 */
abstract public class BatchContents {

	/** Nexus Info Preference Page의 TableViewer 정의 */
	protected TableViewer tableViewer;

	/** TableViewer를 그냥 TableViewer로 만들지 CheckBoxTableViewer로 만들지 설정 */
	private boolean isCheckBoxTableViewer;

	/** 생성자에서 받은 VO의 객체와 class명 */
	private Info voClassType;

	/** PreferencePage에서 해당 table contents의 이름 */
	private String preferenceStoreName;

	/** PreferencePage에서 해당 table contents의 개수 */
	private int voCount;

	/** table의 Column 이름 */
	private BatchTableColumn[] columns = null;

	/** TabItem에 String 설정 */
	private String description;

	/** TableViewer에 해당하는 LabelProvider */
	private ITableLabelProvider labelProvider;

	/**
	 * tableViewer의 값을 가져온다
	 *
	 * @return the tableViewer
	 */
	public TableViewer getTableViewer() {
		return tableViewer;
	}

	/**
	 * description의 값을 가져온다
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * BatchContents의 생성자
	 * 
	 * @param description
	 * @param voInfo
	 * @param preferenceStoreName
	 * @param labelProvider
	 * @param isCheckBoxTableViewer
	 */
	public BatchContents(String description, Info voInfo, String preferenceStoreName, ITableLabelProvider labelProvider, boolean isCheckBoxTableViewer) {
		this.description = description;
		voClassType = voInfo;
		this.preferenceStoreName = preferenceStoreName;
		this.labelProvider = labelProvider;
		this.isCheckBoxTableViewer = isCheckBoxTableViewer;
	}

	/** PreferenceStore에서 특정 ID에 해당하는 정보를 가져온다. */
	private ArrayList<Info> getStoredData() {

		ArrayList<Info> infoList = new ArrayList<Info>();
		int voCnt = EgovBatchConfMngtPlugin.getDefault().getPreferenceStore().getInt(preferenceStoreName); //$NON-NLS-1$

		for (int i = 0; i < voCnt; i++) {
			Info insertVoInfo = getInfoClass();
			insertVoInfo.setId(preferenceStoreName, i);

			insertVoInfo = (Info) PrefrencePropertyUtil.loadPreferences(EgovBatchConfMngtPlugin.getDefault(), insertVoInfo);
			infoList.add(insertVoInfo);
		}

		return infoList;
	}

	/** OK버튼 클릭 시 PreferenceStore에 특정 ID를 할당하여 정보를 저장한다. */
	public void saveData() {

		voCount = tableViewer.getTable().getItemCount();

		EgovBatchConfMngtPlugin.getDefault().getPreferenceStore().setValue(preferenceStoreName, voCount); //$NON-NLS-1$

		for (int i = 0; i < voCount; i++) {
			Info voInfo = (Info) tableViewer.getTable().getItem(i).getData();
			voInfo.setId(preferenceStoreName, i);
			PrefrencePropertyUtil.savePreferences(EgovBatchConfMngtPlugin.getDefault(), voInfo);
		}

	}

	/**
	 * TableColumn의 가로, 세로 길이, Column의 이름, 너비, 배열을 설정한다.
	 * 
	 * @param heightHint
	 * @param widthHint
	 * @param columnNames
	 * @param columnWidth
	 * @param columnAlignments
	 */
	public void setTableColumnProperty(BatchTableColumn[] columns) {
		this.columns = columns;
	}

	/**
	 * Tab으로 생성시 TabItem을 생성한 뒤, 그 내부에 TableViewer를 생성한다.
	 * 
	 * @param folder
	 * @return TableItem
	 */
	public TabItem createTabContents(TabFolder folder) {
		TabItem item = new TabItem(folder, SWT.NONE);
		item.setText(description);

		Composite control = new Composite(folder, SWT.None);
		control.setLayout(new GridLayout());
		control.setLayoutData(new GridData(GridData.FILL_BOTH));

		item.setControl(createTableViewerContents(control));

		return item;
	}

	/**
	 * 실질적으로 TableViewer를 생성한다.
	 * 
	 * @param parent
	 * @return Composite
	 */
	public Composite createTableViewerContents(Composite parent) {

		if (isCheckBoxTableViewer) {
			tableViewer = CheckboxTableViewer.newCheckList(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		} else {
			tableViewer = new TableViewer(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		}
		Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		tableViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));

		GridData gData = new GridData();
		gData.grabExcessHorizontalSpace = true;

		for (int i = 0; i < columns.length; i++) {
			columns[i].setColumnToTable(table);
		}

		tableViewer.setLabelProvider(labelProvider);
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setInput(getStoredData());

		return parent;
	}

	/**
	 * Add Button에 할당할 Listener 생성 
	 * 
	 * @param parent
	 * @return Listener
	 */
	abstract public Listener addButtonListener(final Shell parent);

	/** Edit Button에 할당할 Listener 생성
	 * 
	 * @param parent
	 * @return Listener
	 */
	abstract public Listener editButtonListener(final Shell parent);

	/**
	 * Remove Button에 할당할 Listener 생성
	 * 
	 * @param parent
	 * @return Listener
	 */
	public Listener removeButtonListener(final Shell parent) {
		return new Listener() {

			public void handleEvent(Event event) {
				IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();

				if (!NullUtil.isNull(selection) && selection.size() > 0) {
					if (MessageDialog.openConfirm(null, BConfMngtMessages.BatchContents_REMOVE_BUTTON_DIALOG_TITLE,
							BConfMngtMessages.BatchContents_REMOVE_BUTTON_DIALOG_DESCRIPTION)) {
						tableViewer.remove(selection.toArray());
					}
				}
			}
		};
	}

	/**
	 * 넘어온 변수의 Class Type으로 새로운 객체를 생성해 return 한다.
	 * 
	 * @return Info
	 */
	@SuppressWarnings("rawtypes")
	protected Info getInfoClass() {
		try {
			Class typeClass = Class.forName(voClassType.getClass().getName());
			return (Info) typeClass.newInstance();
		} catch (Exception e) {
			return null;
		}
	}

	/** TableViewer의 내용을 갱신한다. */
	public void refreshInputData() {
		tableViewer.setInput(getStoredData());
	}
}