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
package egovframework.dev.imp.confmngt.preferences;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import egovframework.dev.imp.confmngt.EgovConfMngtPlugin;
import egovframework.dev.imp.confmngt.common.ConfMngtMessages;
import egovframework.dev.imp.confmngt.preferences.dialog.NexusDialog;
import egovframework.dev.imp.confmngt.preferences.model.NexusInfo;
import egovframework.dev.imp.confmngt.preferences.model.NexusTableLabelProvider;
import egovframework.dev.imp.core.utils.NullUtil;
import egovframework.dev.imp.core.utils.PrefrencePropertyUtil;

/**
 * Nexus 정보를 관리하는 클래스
 * 
 * @author 개발환경 개발팀 조윤정
 * @since 2011.06.13
 * @version 1.0
 * @see
 * 
 *      <pre>
 *      개정이력(Modification Information)
 *   
 *   수정일      수정자           수정내용
 *  ------- -------- ---------------------------
 *  2011.06.13  조윤정     최초 생성
 * 
 * 
 *      </pre>
 */
public class NexusPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	/** Nexus Info Preference Page의 TableViewer 정의 */
	private TableViewer tableViewer;
	private Button editButton;
	private Button removeButton;

	private NexusInfo nexusInfo;
	private int nexusCount;
	private ArrayList<NexusInfo> nexusInfoList;

	/**
	 * NexusPreferencePage 생성자
	 */
	public NexusPreferencePage() {
		setPreferenceStore(EgovConfMngtPlugin.getDefault().getPreferenceStore());
		setDescription(ConfMngtMessages.nexusPreferencePageDESC);
	}

	/**
	 * 
	 * workbench 초기화
	 * 
	 * @param workbench
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		nexusInfoList = new ArrayList<NexusInfo>();
		int nexusCnt = EgovConfMngtPlugin.getDefault().getPreferenceStore().getInt("nexusCount"); //$NON-NLS-1$

		if (nexusCnt > 0)
			for (int i = 0; i < nexusCnt; i++) {
				NexusInfo nexusInfo = new NexusInfo();
				nexusInfo.setId(String.valueOf(i));

				NexusInfo nexusInfo11 = (NexusInfo) PrefrencePropertyUtil
						.loadPreferences(EgovConfMngtPlugin.getDefault(), nexusInfo);
				nexusInfoList.add(nexusInfo11);
			}
	}

	private void handleRemoveButtonPressed() {
		IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();

		if (!NullUtil.isNull(selection) && selection.size() > 0)
			tableViewer.remove(selection.toArray());
	}

	/**
	 * OK버튼 클릭 시 Preference Store에 Nexus 정보를 반영함
	 * 
	 * @return
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#performOk()
	 */
	public boolean performOk() {
		nexusCount = tableViewer.getTable().getItemCount();
		EgovConfMngtPlugin.getDefault().getPreferenceStore().setValue("nexusCount", 0); //$NON-NLS-1$
		EgovConfMngtPlugin.getDefault().getPreferenceStore().setValue("nexusCount", nexusCount); //$NON-NLS-1$

		if (nexusCount > 0)
			for (int i = 0; i < nexusCount; i++) {
				NexusInfo nexusInfo = new NexusInfo();
				nexusInfo = (NexusInfo) tableViewer.getTable().getItem(i).getData();
				nexusInfo.setId(String.valueOf(i));

				PrefrencePropertyUtil.savePreferences(EgovConfMngtPlugin.getDefault(), nexusInfo);
			}
		else {
			PrefrencePropertyUtil.savePreferences(EgovConfMngtPlugin.getDefault(), new NexusInfo());
		}

		super.performOk();
		return true;

	}

	@Override
	protected Control createContents(Composite composite) {
		noDefaultAndApplyButton();
		GridData gData = new GridData(GridData.FILL_BOTH);
		gData.heightHint = 650;
		gData.widthHint = 460;
		GridLayout layout = new GridLayout();

		Composite innerContainer = new Composite(composite, SWT.NONE);
		GridLayout innerLayout = new GridLayout();
		innerLayout.numColumns = 2;
		innerContainer.setLayout(innerLayout);

		// Create table
		tableViewer = new TableViewer(innerContainer,
				SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION);
		Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		tableViewer.getControl().setLayoutData(gData);
		String[] columnNames = new String[] { ConfMngtMessages.nexusPreferencePageID,
				ConfMngtMessages.nexusPreferencePageURL, ConfMngtMessages.nexusPreferencePageRELEASE,
				ConfMngtMessages.nexusPreferencePageSNAPSHOTS };

		int[] columnWidth = new int[] { 70, 250, 73, 75 };
		int[] columnAlignments = new int[] { SWT.LEFT, SWT.LEFT, SWT.CENTER, SWT.CENTER };

		for (int i = 0; i < columnNames.length; i++) {
			TableColumn tableColumn = new TableColumn(table, columnAlignments[i]);
			tableColumn.setText(columnNames[i]);
			tableColumn.setWidth(columnWidth[i]);
		}

		tableViewer.setLabelProvider(new NexusTableLabelProvider());
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setInput(nexusInfoList);

		Composite buttons = new Composite(innerContainer, SWT.NONE);
		buttons.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
		layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		buttons.setLayout(layout);

		Button addButton = new Button(buttons, SWT.PUSH);
		addButton.setText(ConfMngtMessages.nexusPreferencePageNEW);
		addButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		addButton.addListener(SWT.Selection, new Listener() {

			// Nexus Dialog를 띄움
			public void handleEvent(Event event) {
				int itemCnt = tableViewer.getTable().getItemCount();

				final List<String> nexusIdList = new ArrayList<String>();

				if (itemCnt > 0)
					for (int i = 0; i < itemCnt; i++) {
						NexusInfo nexusInfo = (NexusInfo) tableViewer.getTable().getItem(i).getData();
						nexusIdList.add(nexusInfo.getNexusId());
					}

				NexusDialog nexusInfoDialog = new NexusDialog(getControl().getShell(), null, null, true, true, true,
						nexusIdList);

				if (nexusInfoDialog.open() == Window.OK) {
					nexusInfo = new NexusInfo(nexusInfoDialog.getNexusId(), nexusInfoDialog.getNexusUrl(),
							nexusInfoDialog.isReleaseButtonPressed(), nexusInfoDialog.isSnapshotsButtonPressed());
					tableViewer.add(nexusInfo);

				}
			}
		});

		editButton = new Button(buttons, SWT.PUSH);
		editButton.setText(ConfMngtMessages.nexusPreferencePageEDIT);
		editButton.setEnabled(false);
		editButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		editButton.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				int itemCnt = tableViewer.getTable().getItemCount();

				final List<String> nexusIdList = new ArrayList<String>();

				if (itemCnt > 0)
					for (int i = 0; i < itemCnt; i++) {
						NexusInfo nexusInfo = (NexusInfo) tableViewer.getTable().getItem(i).getData();
						nexusIdList.add(nexusInfo.getNexusId());
					}

				IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();

				if (!NullUtil.isNull(selection) && selection.size() > 0) {
					Object object = selection.getFirstElement();
					if (object instanceof NexusInfo) {
						NexusInfo nexusInfo = (NexusInfo) object;
						NexusDialog nexusInfoDialog = new NexusDialog(getControl().getShell(), nexusInfo.getNexusId(),
								nexusInfo.getNexusUrl(), nexusInfo.getIsRealeaseSelected(),
								nexusInfo.getIsSnapshotsSelected(), false, nexusIdList);

						if (nexusInfoDialog.open() == Window.OK) {
							nexusInfo.setNexusId(nexusInfoDialog.getNexusId());
							nexusInfo.setNexusUrl(nexusInfoDialog.getNexusUrl());
							nexusInfo.setIsRealeaseSelected(nexusInfoDialog.isReleaseButtonPressed());
							nexusInfo.setIsSnapshotsSelected(nexusInfoDialog.isSnapshotsButtonPressed());

							tableViewer.update(nexusInfo, null);
						}
					}
				}
			}
		});

		removeButton = new Button(buttons, SWT.PUSH);
		removeButton.setText(ConfMngtMessages.nexusPreferencePageREMOVE);
		removeButton.setEnabled(false);
		removeButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		removeButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				handleRemoveButtonPressed();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent e) {
				IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();

				if (!NullUtil.isNull(selection) && selection.size() > 0) {
					editButton.setEnabled(true);
					removeButton.setEnabled(true);

				} else {
					editButton.setEnabled(false);
					removeButton.setEnabled(false);
				}
			}
		});

		innerContainer.layout();
		return composite;
	}

}