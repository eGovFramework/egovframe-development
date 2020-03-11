package egovframework.bdev.tst.wizards.pages;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;

import egovframework.bdev.imp.confmngt.preferences.parameter.JobParameterContents;
import egovframework.bdev.imp.confmngt.preferences.parameter.JobParameterPreferencePage;
import egovframework.bdev.imp.confmngt.preferences.parameter.model.JobParameterInfo;
import egovframework.bdev.tst.common.BatchTestMessages;
import egovframework.dev.imp.core.utils.BatchTableColumn;
import egovframework.dev.imp.core.utils.StringUtil;

/**
 * 배치 테스트 사용자 정의시 파라미터 정보 마법사 페이지 클래스
 * 
 * @author 조용현
 * @since 2012.07.24
 * @version 1.0
 * @see <pre>
 *  &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *    
 * 수정일	  	수정자	  수정내용
 * -----------	------	----------------
 * 2012.07.24	조용현	최초생성
 * 
 * 
 * </pre>
 */
public class JobParameterInfoToWizardDialog extends StatusDialog {

	/** Dialog에서 선택한 Info 목록 */
	private ArrayList<JobParameterInfo> info;

	/** Job Parameter의 TableViewer를 생성하는 객체 */
	private JobParameterContents contents;

	/** 기존에 선택한 Job Parameter 목록 */
	private TableItem[] items;

	/**
	 * info의 값을 가져온다
	 * 
	 * @return the info
	 */
	public ArrayList<JobParameterInfo> getInfo() {
		return info;
	}

	/**
	 * JobParameterInfoToWizardDialog의 생성자
	 * 
	 * @param parent
	 * @param items
	 */
	public JobParameterInfoToWizardDialog(Shell parent, TableItem[] items) {
		super(parent);
		this.items = items;

		setTitle(BatchTestMessages.JobParameterInfoToWizardDialog_TITLE);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		GridData tableGData = new GridData(GridData.FILL_HORIZONTAL);
		tableGData.heightHint = 400;

		Composite control = new Composite(parent, SWT.NONE);
		control.setLayout(new GridLayout());
		control.setLayoutData(tableGData);

		Label description = new Label(control, SWT.None);
		description
				.setText(BatchTestMessages.JobParameterInfoToWizardDialog_DESCRIPTION);
		StringUtil.setLabelStringBold(description);

		createLink(control);

		createJobParameterTableViewer(control);

		createButton(control);

		checkExistItems();

		return control;
	}

	/** Link 생성 */
	private void createLink(Composite control) {
		Link link = new Link(control, SWT.RIGHT);
		link.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		link.setText(BatchTestMessages.JobParameterInfoToWizardDialog_LINK);
		link.addListener(SWT.MouseUp, new Listener() {

			public void handleEvent(Event event) {
				PreferencesUtil
						.createPreferenceDialogOn(
								PlatformUI.getWorkbench()
										.getActiveWorkbenchWindow().getShell(),
								"egovframework.bdev.imp.confmngt.preferences.jobparampreferencepage", //$NON-NLS-1$
								new String[] { "egovframework.bdev.imp.confmngt.preferences.jobparampreferencepage" }, //$NON-NLS-1$
								null).open();
				contents.refreshInputData();
			}
		});
	}

	/** Select All, Deselect All Button 생성 */
	private void createButton(Composite control) {
		Composite buttonControl = new Composite(control, SWT.None);
		buttonControl.setLayout(new GridLayout(2, false));
		buttonControl.setLayoutData(new GridData());

		Button selectAll = new Button(buttonControl, SWT.PUSH);
		selectAll
				.setText(BatchTestMessages.JobParameterInfoToWizardDialog_SELECT_ALL_BUTTON);
		selectAll.addListener(SWT.MouseUp, new Listener() {

			public void handleEvent(Event event) {
				((CheckboxTableViewer) contents.getTableViewer())
						.setAllChecked(true);
			}
		});

		Button deselectAll = new Button(buttonControl, SWT.PUSH);
		deselectAll
				.setText(BatchTestMessages.JobParameterInfoToWizardDialog_DESELECT_ALL_BUTTON);
		deselectAll.addListener(SWT.MouseUp, new Listener() {

			public void handleEvent(Event event) {
				((CheckboxTableViewer) contents.getTableViewer())
						.setAllChecked(false);
			}
		});
	}

	/** 기존 선택된 항목은 table에서 체크된 상태로 있도록 설정 */
	private void checkExistItems() {
		if (items != null && items.length > 0) {
			String[] wizardItems;
			wizardItems = new String[items.length];
			for (int i = 0; i < items.length; i++) {
				wizardItems[i] = ((JobParameterInfo) items[i].getData())
						.getParameterName();
			}

			CheckboxTableViewer tableViewer = (CheckboxTableViewer) contents
					.getTableViewer();
			Item[] preferenceItems = tableViewer.getTable().getItems();

			if (preferenceItems != null && preferenceItems.length > 0) {
				for (int i = 0; i < wizardItems.length; i++) {
					for (int j = 0; j < preferenceItems.length; j++) {
						JobParameterInfo info = (JobParameterInfo) preferenceItems[j]
								.getData();
						if (wizardItems[i].equals(info.getParameterName())) {
							tableViewer.setChecked(info, true);
							break;
						}
					}
				}
			}
		}
	}

	/** Job Parameter의 TableViewer 생성 */
	private void createJobParameterTableViewer(Composite control) {
		BatchTableColumn[] columns = createColumns();

		contents = new JobParameterContents(
				"Job Parameter", JobParameterPreferencePage.JOB_PARAMETER_PREFERENCE_STORE_NAME, true); //$NON-NLS-1$ //$NON-NLS-2$
		contents.setTableColumnProperty(columns);
		contents.createTableViewerContents(control);
	}

	/**
	 * Job Parameter TableViewer 컬럼 생성
	 * 
	 * @return
	 */
	private BatchTableColumn[] createColumns() {
		ArrayList<BatchTableColumn> columns = new ArrayList<BatchTableColumn>();
		columns.add(new BatchTableColumn(
				BatchTestMessages.JobParameterInfoToWizardDialog_TABLE_COLUMN_PARAMETER_NAME,
				175, SWT.LEFT));
		columns.add(new BatchTableColumn(
				BatchTestMessages.JobParameterInfoToWizardDialog_TABLE_COLUMN_VALUE,
				150, SWT.LEFT));
		columns.add(new BatchTableColumn(
				BatchTestMessages.JobParameterInfoToWizardDialog_TABLE_COLUMN_DATA_TYPE,
				122, SWT.LEFT));

		return columns.toArray(new BatchTableColumn[0]);
	}

	@Override
	protected void okPressed() {
		CheckboxTableViewer chTableViewer = (CheckboxTableViewer) contents
				.getTableViewer();

		Object[] object = chTableViewer.getCheckedElements();
		info = new ArrayList<JobParameterInfo>();
		for (int i = 0; i < object.length; i++) {
			info.add((JobParameterInfo) object[i]);
		}
		super.okPressed();
	}

}
