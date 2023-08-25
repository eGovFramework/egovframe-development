package egovframework.bdev.imp.batch.wizards.jobcreation.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;

import egovframework.bdev.imp.batch.common.BatchMessages;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.BatchJobCreationContext;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.BatchPageBeanIDList;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.BatchXMLFileBeanIDList;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.TypeCSqlKeyValueVo;
import egovframework.bdev.imp.confmngt.preferences.readwrite.JobRWTabContents;
import egovframework.bdev.imp.confmngt.preferences.readwrite.model.JobRWInfo;
import egovframework.dev.imp.core.utils.NullUtil;
import egovframework.dev.imp.core.utils.StringUtil;

/**
 * <pre>
 * BatchJobCreationCustomizePage에서 
 * Job Reader / Writer TableViewer에 추가, 수정할때 여는 Dialog
 * </pre>
 * 
 * @author 배치개발환경 개발팀 조용현
 * @since 2012.06.28
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
abstract public class JobRWDialog extends StatusDialog {

	/** 선택된 Job Reader / Writer를 가지는 Vo */
	private JobRWInfo selectJobRWInfo = null;

	/** Job Reader / Writer TableViewer를 생성 */
	private JobRWTabContents contents = null;

	/** Apply한 JobVo 및 Step, Decision Vo들의 Bean ID list */
	private BatchXMLFileBeanIDList batchXMLFileBeanIDList = null;

	/** Info 페이지에서 입력된 Bean ID를 저장하는 Bean ID list */
	private BatchPageBeanIDList pageBeanIDs = null;

	/** 중간 Title */
	private String semiTitle = null;

	/** 중간 Title의 설명 */
	private String semiDescription = null;

	/** 노트 내용 */
	@SuppressWarnings("unused")
	private String noteContent = null;

	/** Job RW의 상세 정보 항목을 생성 */
	private JobRWDetailInfoControlConstructor detailInfoControlConstructor = null;

	/** Partition Type 여부 */
	final private boolean isPartitionType;

	/** Job RW 상세 정보 항목이 생성되는 Composite */
	private ScrolledComposite detailInfoControl = null;

	/** Job RW의 상세 정보 항목의 Error를 넘겨 받는 Label */
	private Label errorSettingLabel = null;

	/** Job RW의 상세 정보 Context */
	private Map<String, String> detailContext = null;

	/** SqlPagingQueryJdbcItemReader에서 입력한 Key, Value값 목록 */
	private List<TypeCSqlKeyValueVo> sqlKeyValueVo = new ArrayList<TypeCSqlKeyValueVo>();

	/** Wizard의 Context */
	private BatchJobCreationContext context = null;

	/** 초기 Finish 버튼 비활성화 설정시 String */
	final private static String INITAIL_DISABLE_FINISH_BUTTON = "INITAIL_DISABLE_FINISH_BUTTON";

	final private String previousSelectedJobRW;

	/**
	 * detailContext의 값을 가져온다
	 *
	 * @return the detailContext
	 */
	public Map<String, String> getDetailContext() {
		return detailContext;
	}

	/**
	 * sqlKeyValueVo의 값을 가져온다
	 *
	 * @return the sqlKeyValueVo
	 */
	public List<TypeCSqlKeyValueVo> getSqlKeyValueVo() {
		return sqlKeyValueVo;
	}

	/**
	 * noteContent의 값을 설정한다.
	 * 
	 * @param noteContent
	 */
	public void setNoteContent(String noteContent) {
		this.noteContent = noteContent;
	}

	/**
	 * contents의 값을 설정한다.
	 * 
	 * @param contents
	 */
	public void setContents(JobRWTabContents contents) {
		this.contents = contents;
	}

	/**
	 * semiTitle의 값을 설정한다.
	 * 
	 * @param semiTitle
	 */
	public void setSemiTitle(String semiTitle) {
		this.semiTitle = semiTitle;
	}

	/**
	 * description의 값을 설정한다.
	 * 
	 * @param description
	 */
	public void setsemiDescription(String semiDescription) {
		this.semiDescription = semiDescription;
	}

	/**
	 * info의 값을 가져온다
	 * 
	 * @return the info
	 */
	public JobRWInfo getInfo() {
		return selectJobRWInfo;
	}

	/**
	 * <pre>
	 * JobRWInfoToWizardDialog 생성자
	 * type 1: Job Reader 
	 * type 2: Job Writer
	 * </pre>
	 * 
	 * @param parent
	 * @param type
	 */
	public JobRWDialog(Shell parent, BatchXMLFileBeanIDList batchXMLFileBeanIDList, BatchPageBeanIDList pageBeanIDs,
			JobRWInfo info, boolean isPartitionType, BatchJobCreationContext context, Map<String, String> detailContext,
			List<TypeCSqlKeyValueVo> sqlKeyValueVo) {
		super(parent);
		this.batchXMLFileBeanIDList = batchXMLFileBeanIDList;
		this.pageBeanIDs = pageBeanIDs;
		this.selectJobRWInfo = info;
		this.isPartitionType = isPartitionType;
		this.context = context;
		this.detailContext = detailContext;

		if (NullUtil.isNull(info)) {
			this.previousSelectedJobRW = "";
		} else {
			this.previousSelectedJobRW = selectJobRWInfo.getName();
		}

		this.sqlKeyValueVo = sqlKeyValueVo;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		GridData tableGData = new GridData(GridData.FILL_HORIZONTAL);
		tableGData.heightHint = 450;

		Composite control = new Composite(parent, SWT.NONE);
		control.setLayout(new GridLayout());
		control.setLayoutData(tableGData);

		Label semiTitleLabel = new Label(control, SWT.None);
		semiTitleLabel.setText(semiTitle);
		StringUtil.setLabelStringBold(semiTitleLabel);

		Label semiDescriptionLabel = new Label(control, SWT.None);
		semiDescriptionLabel.setText(semiDescription);

		Link link = new Link(control, SWT.RIGHT);
		link.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		link.setText(BatchMessages.JobRWDialog_LINK_MESSAGE);
		link.addListener(SWT.MouseUp, new Listener() {

			public void handleEvent(Event event) {
				PreferencesUtil.createPreferenceDialogOn(
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
						"egovframework.bdev.imp.confmngt.preferences.jobreaderwriterpreperencepage", //$NON-NLS-1$
						new String[] { "egovframework.bdev.imp.confmngt.preferences.jobreaderwriterpreperencepage" }, //$NON-NLS-1$
						null).open();
				contents.refreshInputData();

				setTableViewerSelection(selectJobRWInfo);

				if (isPartitionType) {
					changeTableInputToPartitionTypeTableInput();
				}
			}
		});

		contents = createJobRWContents();
		contents.createTableViewerContents(control);

		contents.getTableViewer().getTable().addListener(SWT.Selection, validation);
		contents.getTableViewer().getTable().addListener(SWT.Selection, contentsListner);

		if (isPartitionType) {
			changeTableInputToPartitionTypeTableInput();
		}

		GridData gData = new GridData(GridData.FILL_HORIZONTAL);
		gData.heightHint = 180;

		Group sqlContentsGroup = new Group(control, SWT.SHADOW_ETCHED_IN);
		sqlContentsGroup.setLayout(new GridLayout(3, false));
		sqlContentsGroup.setLayoutData(gData);

		detailInfoControl = new ScrolledComposite(sqlContentsGroup, SWT.V_SCROLL);
		GridData gInnerData = new GridData(GridData.FILL_HORIZONTAL);
		gInnerData.heightHint = 160;
		detailInfoControl.setExpandVertical(true);
		detailInfoControl.setLayout(new GridLayout());
		detailInfoControl.setLayoutData(gInnerData);

		createErrorSettingLabel(control);

		setTableViewerSelection(selectJobRWInfo);

		validation.handleEvent(null);

		return control;
	}

	/** 실제 Job RW의 Contents(TableViewer를 생성한다. */
	abstract protected JobRWTabContents createJobRWContents();

	/**
	 * ErrorSettingLabel Control 생성
	 * 
	 * @param control
	 */
	private void createErrorSettingLabel(Composite control) {
		errorSettingLabel = new Label(control, SWT.BORDER);
		errorSettingLabel.setVisible(false);
		errorSettingLabel.addListener(SWT.Modify, validation);

		errorSettingLabel.setText(INITAIL_DISABLE_FINISH_BUTTON);
	}

	/** Dialog의 Validation Listener */
	Listener validation = new Listener() {

		public void handleEvent(Event event) {
			IStructuredSelection selection = (IStructuredSelection) contents.getTableViewer().getSelection();
			StatusInfo status = new StatusInfo();

			if (selection.isEmpty()) {
				status.setError(getEmptyErrorMessage());
				updateStatus(status);
				return;
			}

			selectJobRWInfo = (JobRWInfo) selection.getFirstElement();
			if (!validateBeanID(selectJobRWInfo.getName())) {
				status.setError(BatchMessages.JobRWDialog_DUPLICATE_BEAN_ID_ERROR_MESSAGE);
				updateStatus(status);
				return;
			}

			String errorMessage = errorSettingLabel.getText();
			if (!NullUtil.isEmpty(errorMessage)) {
				status.setError(errorMessage);
				updateStatus(status);
				return;
			}

			status.setOK();
			updateStatus(status);
		}

	};

	/**
	 * 입력된 항목이 없을때의 Error Message를 가져온다. 자식 클래스에서 구현.
	 * 
	 * @return
	 */
	abstract protected String getEmptyErrorMessage();

	/**
	 * Bean ID의 유효성을 확인한다.
	 * 
	 * @param beanID
	 * @return
	 */
	private boolean validateBeanID(String beanID) {
		if (pageBeanIDs.isBeanIDExist(beanID)) {
			return false;
		}

		if (batchXMLFileBeanIDList.isBeanIDExistExceptJobRWBeanList(beanID)) {
			return false;
		}

		return true;
	}

	/**
	 * Contents TableViewer의 listener 생성
	 * 
	 */
	Listener contentsListner = new Listener() {

		@SuppressWarnings("deprecation")
		public void handleEvent(Event event) {
			IStructuredSelection selection = (IStructuredSelection) contents.getTableViewer().getSelection();
			selectJobRWInfo = (JobRWInfo) selection.getFirstElement();

			createDetailInfoControl(selectJobRWInfo, isPartitionType);

			String errorMessage = errorSettingLabel.getText();
			if (!NullUtil.isEmpty(errorMessage)) {
				errorSettingLabel.setText("");
				errorSettingLabel.notifyListeners(SWT.Modify, null);

				Button okButton = getOKButton();
				okButton.setEnabled(false);
			}
		}
	};

	/**
	 * Job RW의 상세 정보항목들을 생성한다.
	 * 
	 * @param info
	 * @param isPartitionType
	 */
	private void createDetailInfoControl(JobRWInfo info, boolean isPartitionType) {

		if (!NullUtil.isNull(info)) {
			String resourceType = info.getResourceType();

			if (JobRWInfo.DB.equals(resourceType)) {
				JobRWDBDetailInfoControlConstructor detailInfoControlConstructor = new JobRWDBDetailInfoControlConstructor(
						detailInfoControl, errorSettingLabel, context, detailContext, sqlKeyValueVo,
						isPreviousSelectedJobRW(info.getName()));
				detailInfoControlConstructor.createDetailInfoControl(info);

				this.detailInfoControlConstructor = detailInfoControlConstructor;
			} else if (JobRWInfo.FILE.equals(resourceType)) {
				JobRWFileDetailInfoControlConstructor detailInfoControlConstructor = new JobRWFileDetailInfoControlConstructor(
						detailInfoControl, errorSettingLabel, isPartitionType, context, detailContext,
						isPreviousSelectedJobRW(info.getName()));
				detailInfoControlConstructor.createDetailInfoControl(info);

				this.detailInfoControlConstructor = detailInfoControlConstructor;
			}

		}
	}

	private boolean isPreviousSelectedJobRW(String selectedJobRWName) {
		return previousSelectedJobRW.equals(selectedJobRWName);
	}

	/**
	 * Contents의 TableViewer에서 Selection을 설정한다.
	 * 
	 * @param info
	 */
	private void setTableViewerSelection(JobRWInfo info) {
		if (!NullUtil.isNull(info)) {
			TableViewer tableViewer = contents.getTableViewer();
			TableItem[] items = tableViewer.getTable().getItems();

			for (int i = 0; i < items.length; i++) {
				JobRWInfo item = (JobRWInfo) items[i].getData();
				if (info.getName().equals(item.getName())) {
					tableViewer.setSelection(new StructuredSelection(item));
					tableViewer.getTable().notifyListeners(SWT.Selection, null);
				}
			}
		}
	}

	/**
	 * Partition Type일때 Contents Table에 입력되는 input을 변경한다.
	 * 
	 */
	private void changeTableInputToPartitionTypeTableInput() {
		TableItem[] inputItems = contents.getTableViewer().getTable().getItems();

		ArrayList<JobRWInfo> newInputItems = new ArrayList<JobRWInfo>();

		for (TableItem item : inputItems) {
			JobRWInfo jobRWInfo = (JobRWInfo) item.getData();

			if (JobRWInfo.FILE.equals(jobRWInfo.getResourceType())) {
				if (!jobRWInfo.getItemType().contains("MultiResource")) {
					newInputItems.add(jobRWInfo);
				}
			}
		}

		contents.getTableViewer().setInput(newInputItems);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(600, 650);
	}

	@Override
	protected void okPressed() {
		detailContext = detailInfoControlConstructor.getDetailContext();

		sqlKeyValueVo = detailInfoControlConstructor.getSqlKeyValueList();

		super.okPressed();
	}

	@Override
	protected boolean isResizable() {
		return true;
	}
}
