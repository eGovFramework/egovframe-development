package egovframework.bdev.imp.confmngt.preferences.com;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import egovframework.bdev.imp.confmngt.common.BConfMngtMessages;
import egovframework.dev.imp.core.utils.NullUtil;

/**
 *  Batch 정보를 관리하는 클래스
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
 * 
 * </pre>
 */
public class BatchPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	/** Add 버튼 */
	protected Button addButton;

	/** Edit 버튼 */
	protected Button editButton;

	/** Remove 버튼 */
	protected Button removeButton;

	public void init(IWorkbench workbench) {
	}

	/**
	 * 외부 Composite 설정
	 *  
	 * @param Composite
	 */
	public Control createContents(Composite composite) {
		noDefaultAndApplyButton();

		Composite innerContainer = new Composite(composite, SWT.None);
		innerContainer.setLayout(new GridLayout(2, false));
		innerContainer.setLayoutData(new GridData(GridData.FILL_BOTH));

		createInnerPart(innerContainer);

		return composite;
	}

	/**
	 * Add, Edit, Remove 버튼 생성
	 * 
	 * @param parent
	 */
	public void createControlButtons(Composite parent) {
		Composite buttons = new Composite(parent, SWT.NONE);
		buttons.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		buttons.setLayout(layout);

		addButton = new Button(buttons, SWT.PUSH);
		addButton.setText(BConfMngtMessages.BatchPreferencePage_ADD_BUTTON);
		addButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		editButton = new Button(buttons, SWT.PUSH);
		editButton.setText(BConfMngtMessages.BatchPreferencePage_EDIT_BUTTON);
		editButton.setEnabled(false);
		editButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		removeButton = new Button(buttons, SWT.PUSH);
		removeButton.setText(BConfMngtMessages.BatchPreferencePage_REMOVE_BUTTON);
		removeButton.setEnabled(false);
		removeButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}

	/**
	 * Add 버튼, Edit 버튼, Remove 버튼에 Listener 할당
	 * 
	 * @param focusTab
	 */
	public void setButtonListener(BatchContents focusTab) {
		editButton.setEnabled(false);
		removeButton.setEnabled(false);

		if (addButton.isListening(SWT.Selection)) {
			addButton.removeListener(SWT.Selection, addButton.getListeners(SWT.Selection)[0]);
		}

		if (editButton.isListening(SWT.Selection)) {
			editButton.removeListener(SWT.Selection, editButton.getListeners(SWT.Selection)[0]);
		}

		if (removeButton.isListening(SWT.Selection)) {
			removeButton.removeListener(SWT.Selection, removeButton.getListeners(SWT.Selection)[0]);
		}

		addButton.addListener(SWT.Selection, focusTab.addButtonListener(getShell()));
		editButton.addListener(SWT.Selection, focusTab.editButtonListener(getShell()));
		removeButton.addListener(SWT.Selection, focusTab.removeButtonListener(getShell()));
	}

	/**
	 * TableViewer의 data 클릭시 Edit, Remove버튼 활성화 Listener
	 * 
	 * @param tableViewer
	 */
	public void addEditAndRemoveButtonListener(final TableViewer tableViewer) {
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
	}

	/**
	 * TableViewer를 생성
	 * 
	 * @param parent
	 */
	public void createInnerPart(Composite parent) {
	}

	@Override
	public boolean performOk() {
		return super.performOk();
	}

}