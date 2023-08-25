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
package egovframework.dev.imp.confmngt.preferences.dialog;

import java.util.List;

import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.LayoutUtil;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import egovframework.dev.imp.confmngt.common.ConfMngtMessages;

/**
 * Nexus Info를 등록하는 다이어로그 클래스
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

@SuppressWarnings("restriction")
public class NexusDialog extends StatusDialog {
	/** Nexus ID */
	private String nexusId;
	/**
	 * Nexus URL
	 */
	private String nexusUrl;
	/**
	 * Text Field에 정의된 Nexus ID
	 */
	private StringDialogField nexusIdField;
	/**
	 * Text Field에 정의된 Nexus URL
	 */
	private StringDialogField nexusUrlField;

	private Button releaseButton;
	private Button snapshotsButton;
	private boolean isReleaseButtonPressed;
	private boolean isSnapshotsButtonPressed;
	private final boolean isAddButton;
	private final List<String> existingNexusIdList;

	/**
	 * NexusInfoDialog의 생성자
	 * 
	 * @param parentShell
	 * @param nexusId
	 * @param nexusUrl
	 */
	public NexusDialog(Shell parentShell, String nexusId, String nexusUrl, boolean isReleaseButtonPressed,
			boolean isSnapshotsButtonPressed, boolean isAddButton, List<String> nexusIdList) {
		super(parentShell);

		this.nexusId = nexusId;
		this.nexusUrl = nexusUrl;
		this.isReleaseButtonPressed = isReleaseButtonPressed;
		this.isSnapshotsButtonPressed = isSnapshotsButtonPressed;
		this.isAddButton = isAddButton;
		this.existingNexusIdList = nexusIdList;
	}

	private class NexusInputAdapter implements IDialogFieldListener {
		public void dialogFieldChanged(DialogField field) {
			doValidation();
		}
	}

	@Override
	protected Point getInitialSize() {
		Point point;
		if (isAddButton) {
			point = new Point(550, 350);
		} else {
			point = new Point(550, 300);
		}
		return point;
	}

	public boolean isReleaseButtonPressed() {
		return isReleaseButtonPressed;
	}

	public void setReleaseButtonPressed(boolean isReleaseButtonPressed) {
		this.isReleaseButtonPressed = isReleaseButtonPressed;
	}

	public boolean isSnapshotsButtonPressed() {
		return isSnapshotsButtonPressed;
	}

	public void setSnapshotsButtonPressed(boolean isSnapshotsButtonPressed) {
		this.isSnapshotsButtonPressed = isSnapshotsButtonPressed;
	}

	/**
	 * Nexus ID를 get 하는 메소드
	 */
	public String getNexusId() {
		return nexusId;
	}

	/**
	 * Nexus URL을 get 하는 메소드
	 */
	public String getNexusUrl() {
		return nexusUrl;
	}

	/**
	 * Dialog영역의 Layout을 구성하는 메소드
	 * 
	 * @param parent
	 * @return
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		if (isAddButton) {
			getShell().setText(ConfMngtMessages.nexusDialogNEWTITLE);
		} else {
			getShell().setText(ConfMngtMessages.nexusDialogEDITTITLE);
		}
		Composite composite = (Composite) super.createDialogArea(parent);
		Composite inner = new Composite(composite, SWT.NONE);
		GridLayout layout = new GridLayout();

		layout.numColumns = 2;
		inner.setLayout(layout);

		NexusInputAdapter adapter = new NexusInputAdapter();

		nexusIdField = new StringDialogField();
		nexusIdField.setLabelText(ConfMngtMessages.nexusDialogID);
		nexusIdField.setDialogFieldListener(adapter);
		nexusIdField.setText(nexusId != null ? nexusId : ""); //$NON-NLS-1$

		nexusUrlField = new StringDialogField();
		nexusUrlField.setLabelText(ConfMngtMessages.nexusDialogURL);
		nexusUrlField.setText(nexusUrl != null ? nexusUrl : ""); //$NON-NLS-1$

		releaseButton = new Button(composite, SWT.CHECK);
		releaseButton.setText(ConfMngtMessages.nexusDialogRELEASE);
		releaseButton.setSelection(isReleaseButtonPressed);

		snapshotsButton = new Button(composite, SWT.CHECK);
		snapshotsButton.setText(ConfMngtMessages.nexusDialogSNAPSHOTS);
		snapshotsButton.setSelection(isSnapshotsButtonPressed);

		if (isAddButton) {
			nexusIdField.doFillIntoGrid(inner, 2);
			nexusUrlField.doFillIntoGrid(inner, 2);

			LayoutUtil.setHorizontalGrabbing(nexusIdField.getTextControl(null));
			LayoutUtil.setWidthHint(nexusIdField.getTextControl(null), convertWidthInCharsToPixels(45));

			nexusIdField.postSetFocusOnDialogField(parent.getDisplay());

		} else {
			nexusUrlField.doFillIntoGrid(inner, 2);

			LayoutUtil.setHorizontalGrabbing(nexusUrlField.getTextControl(null));
			LayoutUtil.setWidthHint(nexusUrlField.getTextControl(null), convertWidthInCharsToPixels(45));
		}

		return composite;
	}

	/**
	 * Dialog에서 OK버튼 클릭시 수행되는 메소드
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed() {
		// parent에 입력값 저장
		nexusId = nexusIdField.getText();
		nexusUrl = nexusUrlField.getText();
		isReleaseButtonPressed = releaseButton.getSelection();
		isSnapshotsButtonPressed = snapshotsButton.getSelection();
		super.okPressed();
	}

	private void doValidation() {
		StatusInfo status = new StatusInfo();
		status = new StatusInfo();

		if (isAddButton) {
			String nexusId = nexusIdField.getText();
			if (nexusId.length() == 0) {
				status.setError(ConfMngtMessages.nexusDialogNODATA);
//				status.setError("Nexus Repository의 ID를 입력하세요.");
			} else {
				if (existingNexusIdList.contains(nexusId)) {
					status.setError(ConfMngtMessages.nexusDialogEXISTDATA);
//					status.setError("동일한 Nexus ID가 이미 존재합니다.");
				}
			}
		}
		updateStatus(status);
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

}
