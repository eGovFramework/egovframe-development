package egovframework.bdev.imp.confmngt.preferences.listeners;

import static egovframework.bdev.imp.confmngt.preferences.listeners.ListenerPreferencePage.CHUNK;
import static egovframework.bdev.imp.confmngt.preferences.listeners.ListenerPreferencePage.JOB;
import static egovframework.bdev.imp.confmngt.preferences.listeners.ListenerPreferencePage.STEP;

import java.util.List;

import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.core.BinaryType;
import org.eclipse.jdt.internal.core.SourceType;
import org.eclipse.jdt.internal.ui.dialogs.FilteredTypesSelectionDialog;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import egovframework.bdev.imp.confmngt.common.BConfMngtMessages;
import egovframework.bdev.imp.confmngt.preferences.listeners.model.ListenerInfo;
import egovframework.dev.imp.core.utils.NullUtil;
import egovframework.dev.imp.core.utils.StringUtil;

/**
 * Batch Listener Info를 등록하는 Dialog
 * 
 * @since 2012.07.24
 * @author 배치개발환경 개발팀 조용현
 * @version 1.0
 * @see
 * 
 *      <pre>
 *  &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *    
 * 수정일	  	수정자	  수정내용
 * -----------	------	----------------
 * 2012.07.24	조용현	최초생성
 * 
 * 
 *      </pre>
 */
@SuppressWarnings("restriction")
public class ListenerDialog extends StatusDialog {

	/** Add Listener Dialog인지 여부 */
	final private boolean isAddButton;

	/** 기존에 존재하는 Listener ID List */
	final List<String> existingIdList;

	/** 기존에 선택된 Listener */
	private ListenerInfo listener = null;

	/** Dialog를 open한 Listener를 구분 */
	private String description;

	/** Name을 입력받는 Text */
	private Text nameText = null;

	/** Class를 입력받는 Text */
	private Text classText = null;

	/** Type을 선택하는 Combo */
	private Combo typeCombo = null;

	/**
	 * ListenerDialog의 생성자
	 * 
	 * @param shell
	 * @param isAddButton
	 * @param existingIdList
	 * @param listenerType
	 * @param name
	 * @param classValue
	 * @param description
	 */
	public ListenerDialog(Shell shell, boolean isAddButton, List<String> existingIdList, ListenerInfo listener,
			String description) {
		super(shell);

		this.isAddButton = isAddButton;
		this.existingIdList = existingIdList;

		this.listener = listener;

		this.description = description;

		if (JOB.equals(description)) {
			if (isAddButton) {
				setTitle(BConfMngtMessages.ListenerDialog_NEW_JOB_TITLE);
			} else {
				setTitle(BConfMngtMessages.ListenerDialog_EDIT_JOB_TITLE);
			}
		} else if (STEP.equals(description)) {
			if (isAddButton) {
				setTitle(BConfMngtMessages.ListenerDialog_NEW_STEP_TITLE);
			} else {
				setTitle(BConfMngtMessages.ListenerDialog_EDIT_STEP_TITLE);
			}
		} else if (CHUNK.equals(description)) {
			if (isAddButton) {
				setTitle(BConfMngtMessages.ListenerDialog_NEW_CHUNK_TITLE);
			} else {
				setTitle(BConfMngtMessages.ListenerDialog_EDIT_CHUNK_TITLE);
			}
		}
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite inner = new Composite(parent, SWT.NONE);
		inner.setLayout(new GridLayout(3, false));
		inner.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		createNameField(inner);

		createClassField(inner);

		if (isAddButton) {
			createTypeField(inner);
		}

		createNote(inner);

		validation.handleEvent(null);

		return inner;
	}

	/**
	 * Name Field 생성
	 * 
	 * @param control
	 */
	private void createNameField(Composite control) {
		GridData horizontalSpanTwo = new GridData(GridData.FILL_HORIZONTAL);
		horizontalSpanTwo.horizontalSpan = 2;

		Label nameLabel = new Label(control, SWT.None);
		nameLabel.setText(BConfMngtMessages.ListenerDialog_NAME_LABEL);

		nameText = new Text(control, SWT.BORDER);
		nameText.setText(StringUtil.returnEmptyStringIfNull(listener.getName()));
		nameText.addListener(SWT.Modify, validation);
		nameText.setLayoutData(horizontalSpanTwo);

		if (!isAddButton) {
			nameText.setEnabled(false);
		}
	}

	/**
	 * Class Field 생성
	 * 
	 * @param control
	 */
	private void createClassField(Composite control) {
		GridData horizontalSpanTwo = new GridData(GridData.FILL_HORIZONTAL);
		horizontalSpanTwo.horizontalSpan = 2;

		Label classLabel = new Label(control, SWT.None);
		classLabel.setText(BConfMngtMessages.ListenerDialog_CLASS_LABEL);

		classText = new Text(control, SWT.BORDER);
		classText.setText(StringUtil.returnEmptyStringIfNull(listener.getClassValue()));
		classText.addListener(SWT.Modify, validation);
		classText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Button browseSelectButton = new Button(control, SWT.PUSH);
		browseSelectButton.setText(BConfMngtMessages.ListenerDialog_BROWSE_BUTTON);
		browseSelectButton.addListener(SWT.Selection, browseListener);
	}

	/**
	 * Type Field 생성
	 * 
	 * @param control
	 */
	private void createTypeField(Composite control) {
		GridData horizontalSpanTwo = new GridData(GridData.FILL_HORIZONTAL);
		horizontalSpanTwo.horizontalSpan = 2;

		Label typeLabel = new Label(control, SWT.None);
		typeLabel.setText(BConfMngtMessages.ListenerDialog_LISTENER_TYPE_LABEL);

		String[] list = new String[] { "Job", "Step", "Chunk" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		typeCombo = new Combo(control, SWT.DROP_DOWN | SWT.READ_ONLY);
		typeCombo.setItems(list);
		typeCombo.setEnabled(false);
		typeCombo.setText(description);
		typeCombo.setLayoutData(horizontalSpanTwo);
	}

	/**
	 * Note 생성
	 * 
	 * @param control
	 */
	private void createNote(Composite control) {
		GridData horizontalSpanTwo = new GridData(GridData.FILL_HORIZONTAL);
		horizontalSpanTwo.horizontalSpan = 3;

		Composite noteControl = new Composite(control, SWT.None);
		noteControl.setLayout(new GridLayout(2, false));
		noteControl.setLayoutData(horizontalSpanTwo);

		Label noteLabel = new Label(noteControl, SWT.None);
		noteLabel.setText(BConfMngtMessages.ListenerDialog_NOTE);
		StringUtil.setLabelStringBold(noteLabel);

		Label contentLabel = new Label(noteControl, SWT.None);
		if (isAddButton) {
			contentLabel.setText(BConfMngtMessages.ListenerDialog_ADD_NOTE_CONTENTS);
		} else {
			contentLabel.setText(BConfMngtMessages.ListenerDialog_EDIT_NOTE_CONTENTS);
		}
	}

	/**
	 * 클래스 정보 search기능
	 * 
	 */
	Listener browseListener = new Listener() {

		public void handleEvent(Event event) {

			FilteredTypesSelectionDialog dialog = new FilteredTypesSelectionDialog(getShell(), false,
					PlatformUI.getWorkbench().getProgressService(), SearchEngine.createWorkspaceScope(),
					IJavaSearchConstants.CLASS);
			dialog.setTitle(BConfMngtMessages.ListenerDialog_BROWSE_BUTTON_DIALOG_TITLE);
			dialog.setMessage(BConfMngtMessages.ListenerDialog_BROWSE_BUTTON_DIALOG_DESCRIPTION);
			dialog.setInitialPattern(""); //$NON-NLS-1$

			if (dialog.open() == Window.OK) {
				Object type = dialog.getFirstResult();

				if (!NullUtil.isNull(type)) {
					if (type instanceof BinaryType) {
						classText.setText(((BinaryType) type).getFullyQualifiedName());
					} else if (type instanceof SourceType) {
						classText.setText(((SourceType) type).getFullyQualifiedName());
					}
				}
				return;
			}

		}
	};

	@Override
	protected void okPressed() {
		listener.setName(nameText.getText());
		listener.setClassValue(classText.getText());
		if (isAddButton) {
			listener.setListenerType(typeCombo.getText());
		}
		super.okPressed();
	}

	/** 유효성 검사 */
	Listener validation = new Listener() {

		public void handleEvent(Event event) {
			StatusInfo status = new StatusInfo();
			status = new StatusInfo();

			if (isAddButton) {
				String id = nameText.getText();
				if (id.length() == 0) {
					status.setError(BConfMngtMessages.ListenerDialog_EMPTY_NAME);
					updateStatus(status);
					return;
				} else {
					if (!StringUtil.isBatchJobBeanIDAvailable(id)) {
						status.setError(BConfMngtMessages.ListenerDialog_INVALID_NAME);
						updateStatus(status);
						return;
					}
					if (existingIdList.contains(id)) {
						status.setError(BConfMngtMessages.ListenerDialog_DUPLICATE_NAME);
						updateStatus(status);
						return;
					}
				}
			}

			if (classText != null) {
				String classValueString = classText.getText();
				if (classValueString.length() < 1) {
					status.setError(BConfMngtMessages.ListenerDialog_EMPTY_CLASS_VALUE);
					updateStatus(status);
					return;
				} else if (!isClassNameOfBatchPreferenceAvailable(classValueString)) {
					status.setError(BConfMngtMessages.ListenerDialog_INVALID_CLASS_VALUE);
					updateStatus(status);
					return;
				}
			}

			status.setOK();
			updateStatus(status);
		}
	};

	/**
	 * Class 값의 유효성 검사
	 * 
	 * @param className
	 * @return
	 */
	protected boolean isClassNameOfBatchPreferenceAvailable(String className) {
		if (StringUtil.hasKorean(className) || StringUtil.hasInvalidClassFileSignal(className)
				|| StringUtil.hasEmptySpace(className)) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	protected Point getInitialSize() {
		Point point;
		if (isAddButton) {
			point = new Point(380, 245);
		} else {
			point = new Point(380, 225);
		}
		return point;
	}

	/**
	 * listener의 값을 가져온다
	 * 
	 * @return the listener
	 */
	public ListenerInfo getListener() {
		return listener;
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

}
