package egovframework.bdev.imp.batch.wizards.jobcreation.pages;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import egovframework.bdev.imp.batch.EgovBatchPlugin;
import egovframework.bdev.imp.batch.common.BatchMessages;
import egovframework.bdev.imp.batch.wizards.com.FindXMLFileBeanIdValueUtil;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.BatchJobBeanIDList;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.BatchJobCreationContext;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.BatchXMLFileBeanIDList;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.DecisionVo;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.JobVo;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.NextVo;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.SharedValueVo;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.StepAndDecisionVo;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.StepVo;
import egovframework.bdev.imp.batch.wizards.jobcreation.views.ListProvider;
import egovframework.dev.imp.core.utils.BatchTableColumn;
import egovframework.dev.imp.core.utils.NullUtil;

/**
 * Batch Job, Step, Decision의 생성 및 속성을 정의하는 Page
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
public class BatchJobCreationCustomizePage extends WizardPage {

	private final String DEFAULT = "선택"; //$NON-NLS-1$

	/** stepAndDecisionCombo에 넣은 문자열(Step / Decision) */
	private final String STEP = "Step"; //$NON-NLS-1$

	/** stepAndDecisionCombo에 넣은 문자열(Step / Decision) */
	private final String DECISION = "Decision"; //$NON-NLS-1$

	/** Batch Job의 정보를 담을 Context */
	private BatchJobCreationContext context = null;

	private List<String> projectBeanIDList = null;

	/** Apply한 JobVo 및 Step, Decision Vo들의 Bean ID list */
	private BatchXMLFileBeanIDList batchXMLFileBeanIDList = null;

	/** Job의 List를 보여주는 TableViewer */
	private TableViewer jobListTable = null;

	/** Job List 밑의 Add 버튼 */
	private Button jobAddButton = null;

	/** Job List 밑의 Remove 버튼 */
	private Button jobRemoveButton = null;

	/** Step과 Decision의 List를 보여주는 TableViewer */
	private TableViewer stepAndDecisionListTable = null;

	/** Step/Decision List 밑의 Add 버튼 */
	private Button stepAndDecisionAdd = null;

	/** Step/Decision List 밑의 Remove 버튼 */
	private Button stepAndDecisionRemove = null;

	/** Job의 Info Control을 생성하는 객체 */
	JobInfoContentsConstructor jobInfoControl = null;

	/** Step의 Info Control을 생성하는 객체 */
	StepInfoContentsConstructor stepInfoControl = null;

	/** Chunk의 Info Control을 생성하는 객체 */
	DecisionInfoContentsConstructor decisionInfoControl = null;

	private String currentInfo = DEFAULT;

	/**
	 * <pre>
	 * job, step, decision 에 의해서 바뀌는 Label
	 * 
	 * ex) Job -> Job Info Step -> Step Info Decision -> Decision Info
	 */
	private Label infoLabel = null;

	/** Step / Decision을 선택할 수 있는 ComboBox */
	private Combo stepAndDecisionCombo = null;

	/**
	 * <pre>
	 * Job info, Step info, Decision info의 화면을 그릴 Composite
	 * 
	 * <pre>
	 */
	private Composite infoContents = null;

	/** Retore Default 버튼 */
	private Button restoreDefaultsButton = null;

	/** Apply 버튼 */
	private Button applyButton = null;

	/**
	 * BatchJobCreationCustomizePage 생성자
	 * 
	 * @param pageName
	 * @param context
	 */
	public BatchJobCreationCustomizePage(String pageName, BatchJobCreationContext context) {
		super(pageName);
		this.context = context;
		setTitle(BatchMessages.BatchJobCreationCustomizePage_TITLE);
		setDescription(BatchMessages.BatchJobCreationCustomizePage_DESCRIPTION);
	}

	public void createControl(Composite parent) {
		Group outerGroup = new Group(parent, SWT.None);
		outerGroup.setLayout(new GridLayout(3, false));
		outerGroup.setLayoutData(new GridData());
		removeMarginOfGridLayout(outerGroup);

		jobPartControl(outerGroup);

		stepPartControl(outerGroup);

		infoPartControl(outerGroup);

		setPageComplete(false);
		setControl(outerGroup);
	}

	/**
	 * Job List, Button 생성
	 * 
	 * @param control
	 */
	private void jobPartControl(Composite control) {

		Group innerPart = new Group(control, SWT.None);
		innerPart.setLayout(new GridLayout());
		innerPart.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		innerPart.setText(BatchMessages.BatchJobCreationCustomizePage_JOB_LIST_GROUP);

		jobListTable = createAndGetListTable(innerPart, jobListTableListener);

		createJobListTableButtonsControl(innerPart);
	}

	/**
	 * Step List, Button 생성
	 * 
	 * @param control
	 */
	private void stepPartControl(Composite control) {

		Group innerPart = new Group(control, SWT.NONE);
		innerPart.setLayout(new GridLayout());
		innerPart.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		innerPart.setText(BatchMessages.BatchJobCreationCustomizePage_STEP_DECISION_LIST_GROUP);

		stepAndDecisionListTable = createAndGetListTable(innerPart, stepAndDecisionListTableListener);

		createStepDecisionListTableButtonsControl(innerPart);
	}

	/**
	 * Job, Step/Deicision List Table을 생성하여 리턴한다.
	 * 
	 * @param control
	 * @param tableListener
	 * @return
	 */
	private TableViewer createAndGetListTable(Composite control, Listener tableListener) {
		TableViewer tableViewer = new TableViewer(control,
				SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);

		Table table = tableViewer.getTable();

		table.setHeaderVisible(false);
		table.setLinesVisible(false);
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new ListProvider());

		BatchTableColumn column = new BatchTableColumn("NO_COLUMN_NAME", 120, SWT.LEFT);
		column.setColumnToTable(table);

		GridData gData = new GridData(GridData.FILL_VERTICAL);
		gData.widthHint = 105;

		tableViewer.getControl().setLayoutData(gData);

		table.addListener(SWT.Selection, tableListener);

		return tableViewer;
	}

	/**
	 * Job List Table의 버튼을 생성한다.
	 * 
	 * @param control
	 */
	private void createJobListTableButtonsControl(Composite control) {
		Composite buttonControl = new Composite(control, SWT.NONE);
		buttonControl.setLayout(new GridLayout(2, true));
		buttonControl.setLayoutData(new GridData());
		removeMarginOfGridLayout(buttonControl);

		jobAddButton = new Button(buttonControl, SWT.PUSH);
		jobAddButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		jobAddButton.setText(BatchMessages.BatchJobCreationCustomizePage_ADD_BUTTON);
		jobAddButton.addListener(SWT.Selection, jobAddButtonListener);

		jobRemoveButton = new Button(buttonControl, SWT.PUSH);
		jobRemoveButton.setText(BatchMessages.BatchJobCreationCustomizePage_REMOVE_BUTTON);
		jobRemoveButton.addListener(SWT.Selection, jobRemoveButtonListener);
	}

	/**
	 * Step/Decision List Table의 버튼을 생성한다.
	 * 
	 * @param control
	 */
	private void createStepDecisionListTableButtonsControl(Composite control) {
		Composite buttonControl = new Composite(control, SWT.NONE);
		buttonControl.setLayout(new GridLayout(2, true));
		buttonControl.setLayoutData(new GridData());
		removeMarginOfGridLayout(buttonControl);

		stepAndDecisionAdd = new Button(buttonControl, SWT.PUSH);
		stepAndDecisionAdd.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		stepAndDecisionAdd.setText(BatchMessages.BatchJobCreationCustomizePage_ADD_BUTTON);
		stepAndDecisionAdd.addListener(SWT.Selection, stepAndDecisionAddButtonListener);

		stepAndDecisionRemove = new Button(buttonControl, SWT.PUSH);
		stepAndDecisionRemove.setText(BatchMessages.BatchJobCreationCustomizePage_REMOVE_BUTTON);
		stepAndDecisionRemove.addListener(SWT.Selection, stepAndDecisionRemoveTableViewerListener);
	}

	/**
	 * Job, Step, Decision info가 그려지는 Control 생성
	 * 
	 * @param parent
	 */
	public void infoPartControl(Composite parent) {

		Group innerPart = new Group(parent, SWT.NONE);
		innerPart.setLayout(new GridLayout());
		innerPart.setLayoutData(new GridData(GridData.FILL_BOTH));
		removeMarginOfGridLayout(innerPart);
		innerPart.setText(BatchMessages.BatchJobCreationCustomizePage_INFO_GROUP);

		createInfoLabelAndDecisionComboControl(innerPart);

		createEmptyInfoContentsControl(innerPart);

		createInfoButtonsControl(innerPart);
	}

	/**
	 * Info Label과 Step/Decision Combo를 생성
	 * 
	 * @param control
	 */
	private void createInfoLabelAndDecisionComboControl(Composite control) {
		Composite labelAndDecisionControl = new Composite(control, SWT.None);
		labelAndDecisionControl.setLayout(new GridLayout(2, false));
		labelAndDecisionControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		removeMarginOfGridLayout(labelAndDecisionControl);

		Composite labelControl = new Composite(labelAndDecisionControl, SWT.None);
		labelControl.setLayout(new GridLayout());
		labelControl.setLayoutData(new GridData());
		removeMarginOfGridLayout(labelControl);

		infoLabel = new Label(labelControl, SWT.None);

		Composite decisionControl = new Composite(labelAndDecisionControl, SWT.None);
		decisionControl.setLayout(new GridLayout());
		decisionControl.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.FILL_HORIZONTAL));
		removeMarginOfGridLayout(decisionControl);

		stepAndDecisionCombo = new Combo(decisionControl, SWT.BORDER | SWT.READ_ONLY);
		stepAndDecisionCombo.setItems(new String[] { DEFAULT, STEP, DECISION });
	}

	/**
	 * Job, Step, Decision info Item이 생성되는 Control 생성
	 * 
	 * @param control
	 */
	private void createEmptyInfoContentsControl(Composite control) {
		Group infoContentsGroup = new Group(control, SWT.NONE);
		infoContentsGroup.setLayout(new GridLayout());
		infoContentsGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		removeMarginOfGridLayout(infoContentsGroup);

		infoContents = new Composite(infoContentsGroup, SWT.None);
		infoContents.setLayout(new GridLayout());
		infoContents.setLayoutData(new GridData(GridData.FILL_BOTH));
		removeMarginOfGridLayout(infoContents);
	}

	/**
	 * Info의 버튼(Restore, Apply) Control 생성
	 * 
	 * @param control
	 */
	private void createInfoButtonsControl(Composite control) {
		Composite buttonControl = new Composite(control, SWT.NONE);
		buttonControl.setLayout(new GridLayout(2, true));
		buttonControl.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.FILL_HORIZONTAL));
		removeMarginOfGridLayout(buttonControl);

		restoreDefaultsButton = new Button(buttonControl, SWT.PUSH);
		restoreDefaultsButton.setText(BatchMessages.BatchJobCreationCustomizePage_RESTORE_DEFAULT_BUTTON);
		restoreDefaultsButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		applyButton = new Button(buttonControl, SWT.PUSH);
		applyButton.setText(BatchMessages.BatchJobCreationCustomizePage_APPLY_BUTTON);
		applyButton.setEnabled(false);
		applyButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}

	/** 화면에서 수정된 항목 반영 */
	private void refreshInfoControl() {
		getControl().getParent().layout(true, true);
	}

	/**
	 * <pre>
	 * Info 부분의 child member dispose 및
	 * Apply, Restore Default Button의 Listener 제거
	 * </pre>
	 */
	private void clearInfoContentsAndButtonListenerAndMessage() {
		Control[] child = infoContents.getChildren();

		if (child != null && child.length > 0) {
			for (Control data : child) {
				data.dispose();
			}
		}

		Listener[] applyButtonListener = applyButton.getListeners(SWT.Selection);
		Listener[] restoreButtonListener = restoreDefaultsButton.getListeners(SWT.Selection);

		if (applyButtonListener != null & restoreButtonListener != null) {
			for (int i = 0; i < applyButtonListener.length; i++) {
				applyButton.removeListener(SWT.Selection, applyButtonListener[i]);
				restoreDefaultsButton.removeListener(SWT.Selection, restoreButtonListener[i]);
			}
		}

		setErrorMessage(null);
	}

	@Override
	public void setVisible(boolean visible) {
		if (visible) {
			Point size = new Point(1150, 1200);
			getShell().setMinimumSize(size);
			getShell().setSize(size);
			batchXMLFileBeanIDList = new BatchXMLFileBeanIDList();

			projectBeanIDList = getProjectBeanIDList();

			jobInfoControl = new JobInfoContentsConstructor(this, infoContents, applyButton, batchXMLFileBeanIDList,
					projectBeanIDList);
			stepInfoControl = new StepInfoContentsConstructor(this, infoContents, applyButton, batchXMLFileBeanIDList,
					projectBeanIDList, context);
			decisionInfoControl = new DecisionInfoContentsConstructor(this, infoContents, applyButton,
					batchXMLFileBeanIDList, projectBeanIDList);

			refreshPage();

		}
		super.setVisible(visible);
	}

	/** Page 초기화 */
	private void refreshPage() {

		setMessage(null);

		jobAddButton.setEnabled(true);
		jobRemoveButton.setEnabled(false);

		jobListTable.setInput(null);

		stepAndDecisionAdd.setEnabled(false);
		stepAndDecisionRemove.setEnabled(false);

		stepAndDecisionListTable.setInput(null);

		infoLabel.setText(""); //$NON-NLS-1$
		stepAndDecisionCombo.deselectAll();
		stepAndDecisionCombo.setVisible(false);

		clearInfoContentsAndButtonListenerAndMessage();

		String message = BatchMessages.BatchJobCreationCustomizePage_INFO_INFORMATION;

		Label firstLabel = new Label(infoContents, SWT.None);
		firstLabel.setText(message);

		createInitialInfoContents();

		refreshInfoControl();
	}

	/**
	 * 페이지가 생성되었을 때 최초 Info 화면을 구성한다.
	 * 
	 */
	private void createInitialInfoContents() {
		// choise
		Label emptyLabel = new Label(infoContents, SWT.None);
		emptyLabel.setText("");
		Label infoImageLabel = new Label(infoContents, SWT.ICON | SWT.CENTER);
		infoImageLabel.setImage(EgovBatchPlugin.getDefault().getImage(EgovBatchPlugin.IMG_BATCH_JOB_ADD));
	}

	/**
	 * Project에 있는 Bean ID 목록을 가져온다.
	 * 
	 * @return
	 */
	private List<String> getProjectBeanIDList() {
		List<String> findingNode = new ArrayList<String>();
		findingNode.add("/beans/bean"); //$NON-NLS-1$
		findingNode.add("/beans/job"); //$NON-NLS-1$
		findingNode.add("/beans/job/step"); //$NON-NLS-1$

		return FindXMLFileBeanIdValueUtil.findXMLFiles(context, findingNode, "id", 3); //$NON-NLS-1$
	}

	/**
	 * Finish 버튼의 enable 여부 확인
	 * 
	 * @return
	 */
	private boolean canFinishButtonEnable() {
		boolean canFinishButtonEnable = false;
		TableItem[] items = jobListTable.getTable().getItems();

		if (items.length > 0) {
			canFinishButtonEnable = true;
			for (int i = 0; i < items.length; i++) {
				JobVo jobVo = (JobVo) items[i].getData();
				StepAndDecisionVo[] stepAndDecisionVo = jobVo.getStepAndDecisionVoList();
				if (NullUtil.isEmpty(stepAndDecisionVo)) {
					canFinishButtonEnable = false;
					break;
				}
			}
		}

		return canFinishButtonEnable;
	}

	/**
	 * Job List에 있는 JobVO의 목록을 가져온다.
	 * 
	 * @return
	 */
	public JobVo[] getResultBatchJobVos() {
		TableItem[] items = jobListTable.getTable().getItems();
		JobVo[] data = new JobVo[items.length];

		if (items.length > 0) {
			for (int i = 0; i < items.length; i++) {
				data[i] = (JobVo) items[i].getData();
			}
		}
		return data;
	}

	/**
	 * Job List에 있는 Job의 이름을 Array형식으로 return
	 * 
	 * @return String[] JobVoNameList
	 */
	public String[] getJobVoNameList() {
		TableItem[] data = jobListTable.getTable().getItems();
		String[] jobVoNameList = null;

		if (data.length > 0) {
			JobVo selectJobVo = getJobListSelection();
			String selectJobVoName = null;

			if (!NullUtil.isNull(selectJobVo)) {
				jobVoNameList = new String[data.length - 1];
				selectJobVoName = selectJobVo.getJobName();
			} else {
				jobVoNameList = new String[data.length];
				selectJobVoName = ""; //$NON-NLS-1$
			}

			int j = 0;

			for (int i = 0; i < data.length; i++) {
				JobVo vo = (JobVo) data[i].getData();
				String jobVoName = vo.getJobName();

				if (selectJobVoName.equals(jobVoName)) {
					continue;
				}

				jobVoNameList[j] = jobVoName;
				j++;
			}
		}

		return jobVoNameList;
	}

	/**
	 * 넘어온 String을 가진 Job을 Job List에서 찾아서 Selection 설정
	 * 
	 * @param jobVoName
	 */
	public void selectJobVo(String jobVoName) {

		TableItem[] data = jobListTable.getTable().getItems();

		if (data.length > 0) {
			for (TableItem item : data) {
				JobVo jobVo = (JobVo) item.getData();
				if (jobVoName.equals(jobVo.getJobName())) {
					jobListTable.setSelection(new StructuredSelection(jobVo));
					break;
				}
			}
		}
	}

	/**
	 * 넘어온 String을 가진 Step또는 Decision을 Step/Decision List에서 찾아서 Selection 설정
	 * 
	 * @param sdVoName
	 */
	public void selectStepAndDecisionVo(String sdVoName) {

		TableItem[] data = stepAndDecisionListTable.getTable().getItems();

		if (data.length > 0) {
			for (TableItem item : data) {
				StepAndDecisionVo sdVo = (StepAndDecisionVo) item.getData();
				if (sdVoName.equals(sdVo.getName())) {
					stepAndDecisionListTable.setSelection(new StructuredSelection(sdVo));
					break;
				}
			}
		}
	}

	/**
	 * 넘어온 control의 marginHeight를 0으로 설정
	 * 
	 * @param control
	 */
	public void removeMarginOfGridLayout(Composite control) {
		((GridLayout) control.getLayout()).marginHeight = 0;
	}

	private ArrayList<StepAndDecisionVo> getReferenceList(JobVo jobSelection,
			StepAndDecisionVo stepAndDecisionSelection) {
		ArrayList<StepAndDecisionVo> refMember = new ArrayList<StepAndDecisionVo>();

		if (!NullUtil.isNull(jobSelection) && !NullUtil.isNull(stepAndDecisionSelection)) {
			StepAndDecisionVo[] list = jobSelection.getStepAndDecisionVoList();

			if (!NullUtil.isEmpty(list)) {
				String standard = stepAndDecisionSelection.getName();

				for (StepAndDecisionVo sdVo : list) {
					if (isStepAndDecisionVoReference(sdVo, standard)) {
						refMember.add(sdVo);
					}
				}
			}
		}

		return refMember;
	}

	/**
	 * Step, Decision Vo가 특정 StepVO(standard)를 참조하는지 여부를 확인
	 * 
	 * @param sdVo
	 * @param standard
	 * @return
	 */
	private boolean isStepAndDecisionVoReference(StepAndDecisionVo sdVo, String standard) {
		if (sdVo instanceof StepVo) {
			return isStepVoReference((StepVo) sdVo, standard);
		} else {
			return isDecisionVoReference((DecisionVo) sdVo, standard);
		}
	}

	/**
	 * StepVo가 특정 StepVO(standard)를 참조하는지 여부를 확인
	 * 
	 * @param stepVo
	 * @param standard
	 * @return
	 */
	private boolean isStepVoReference(StepVo stepVo, String standard) {
		String nextStepString = stepVo.getNextStep();

		if (!NullUtil.isEmpty(nextStepString)) {
			if (nextStepString.equals(standard)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Decision Vo가 특정 StepVO(standard)를 참조하는지 여부를 확인
	 * 
	 * @param decisionVo
	 * @param standard
	 * @return
	 */
	private boolean isDecisionVoReference(DecisionVo decisionVo, String standard) {
		NextVo[] nextVos = decisionVo.getNextVo();

		if (!NullUtil.isEmpty(nextVos)) {
			for (NextVo nextVo : nextVos) {
				if (nextVo.getNextTo().equals(standard)) {
					return true;
				}
			}
		}

		String stopRestart = decisionVo.getStopRestart();
		if (!NullUtil.isEmpty(stopRestart)) {
			if (stopRestart.equals(standard)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * <pre>
	 * 다른 Step, Decision Vo를 참조하는 Vo들의 목록을 이용해
	 * Confirm Dialog의 메세지 생성
	 * </pre>
	 * 
	 * @param refMember
	 * @return
	 */
	private String createReferenceMessage(ArrayList<StepAndDecisionVo> refMember) {
		int lastMemberIndex = refMember.size() - 1;
		String refMemberString = ""; //$NON-NLS-1$

		if (!NullUtil.isEmpty(refMember)) {
			for (int i = 0; i < lastMemberIndex; i++) {
				StepAndDecisionVo member = refMember.get(i);
				refMemberString = refMemberString + member.getName() + ", "; //$NON-NLS-1$
			}
			StepAndDecisionVo member = refMember.get(lastMemberIndex);
			refMemberString = refMemberString + member.getName();
		}

		return refMemberString;
	}

	/**
	 * 다른 Step/Decision에 참조되는 Step/Decision을 수정시 표시할 String을 return
	 * 
	 * @param jobSelection
	 * @param stepAndDecisionSelection
	 * @return
	 */
	private String createRefDialogModifyString(String preID, ArrayList<StepAndDecisionVo> refMemberList) {
		String refMemeberString = createReferenceMessage(refMemberList);

		if (NullUtil.isEmpty(refMemeberString)) {
			return BatchMessages.BatchJobCreationCustomizePage_CONFIRM_UPDATE;
		}

		String result = preID + BatchMessages.BatchJobCreationCustomizePage_UPDATE_MESSAGE_1;
		result += refMemeberString + "\n\n"; //$NON-NLS-1$
		result += BatchMessages.BatchJobCreationCustomizePage_UPDATE_MESSAGE_2;

		return result;
	}

	/**
	 * 다른 Step/Decision에 참조되는 Step/Decision을 삭제시 표시할 String을 return
	 * 
	 * @param jobSelection
	 * @param stepAndDecisionSelection
	 * @return
	 */
	private String createRefDialogRemoveString(String preID, ArrayList<StepAndDecisionVo> refMemberList) {
		String refMemeberString = createReferenceMessage(refMemberList);

		if (NullUtil.isEmpty(refMemeberString)) {
			return BatchMessages.BatchJobCreationCustomizePage_CONFIRM_DELETE;
		}

		String result = preID + BatchMessages.BatchJobCreationCustomizePage_DELETE_MESSAGE_1;
		result += refMemeberString + "\n\n"; //$NON-NLS-1$
		result += preID + BatchMessages.BatchJobCreationCustomizePage_DELETE_MESSAGE_2;

		return result;
	}

	/**
	 * Step, Decision Vo의 목록을 가져온다.
	 * 
	 * @return
	 */
	private StepAndDecisionVo[] getStepAndDecisionList() {
		TableItem[] items = stepAndDecisionListTable.getTable().getItems();
		StepAndDecisionVo[] dSVos = new StepAndDecisionVo[] {};

		if (items.length > 0) {
			dSVos = new StepAndDecisionVo[items.length];
			for (int i = 0; i < dSVos.length; i++) {
				dSVos[i] = (StepAndDecisionVo) items[i].getData();
			}
		}

		return dSVos;
	}

	/** stepAndDecisionCombo의 Listener 제거 */
	private void removeStepAndDecisionComboListener() {
		Listener[] listeners = stepAndDecisionCombo.getListeners(SWT.Selection);
		if (!NullUtil.isEmpty(listeners)) {
			for (Listener listener : listeners) {
				stepAndDecisionCombo.removeListener(SWT.Selection, listener);
			}
		}
	}

	/**
	 * <pre>
	 * Step/Decision List에 아무것도 없을 때 
	 * Add 버튼 클릭시 stepAndDecisionCombo의 Listener 설정
	 * 
	 * Step만 선택 가능
	 * Decision 선택시 errorMessage 설정
	 * </pre>
	 */
	private void atFirstStepComboListener() {
		stepAndDecisionCombo.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				String postString = stepAndDecisionCombo.getText();

				if (DEFAULT.equals(postString)) {
					stepAndDecisionAddButtonListener.handleEvent(new Event());

				} else if (STEP.equals(postString)) {
					setMessage(BatchMessages.BatchJobCreationCustomizePage_ADD_NEW_STEP);

					refreshInfoToStep(getJobListSelection(), null);

					restoreDefaultsButton.setEnabled(true);

				} else {
					setErrorMessage(BatchMessages.BatchJobCreationCustomizePage_STEP_ONLY_AT_FIRST);

					restoreDefaultsButton.setEnabled(false);

					stepAndDecisionCombo.setText(currentInfo);

				}
			}
		});
	}

	/**
	 * <pre>
	 * stepAndDecisionCombo를 변경할 수 있을때 Listener 설정
	 * 
	 * Step 선택시 step info 생성
	 * Decision 선택시 decision info 생성
	 * </pre>
	 */
	private void setEnableStepAndDecisionComboListener() {
		stepAndDecisionCombo.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				JobVo selectJobVo = getJobListSelection();

				if (stepAndDecisionCombo.getText().equals(DEFAULT)) {
					stepAndDecisionAddButtonListener.handleEvent(new Event());

				} else {
					if (stepAndDecisionCombo.getText().equals(DECISION)) {
						setMessage(BatchMessages.BatchJobCreationCustomizePage_ADD_NEW_DECISION);
						refreshInfoToDecision(selectJobVo, null);

					} else if (stepAndDecisionCombo.getText().equals(STEP)) {
						setMessage(BatchMessages.BatchJobCreationCustomizePage_ADD_NEW_STEP);
						refreshInfoToStep(selectJobVo, null);

					}

					restoreDefaultsButton.setEnabled(true);
				}
			}
		});

	}

	/**
	 * <pre>
	 * Apply button과 Restore Default Button의
	 * Listener(SWT.Selection) 설정
	 * 
	 * </pre>
	 * 
	 * @param applyButtonListener
	 * @param restoreButtonListener
	 */
	private void setApplyAndRestoreButtonListener(Listener applyButtonListener, Listener restoreButtonListener) {
		applyButton.addListener(SWT.Selection, applyButtonListener);
		restoreDefaultsButton.addListener(SWT.Selection, restoreButtonListener);
	}

	/** Step/Decision List의 Add Button 클릭시 Info 화면 설정 */
	private void createStepDecisionAddButtonSelectInfoContents() {
		currentInfo = DEFAULT;

		Label infoLabel = new Label(infoContents, SWT.None);
		infoLabel.setText(BatchMessages.BatchJobCreationCustomizePage_STEP_DECISION_ADD_BUTTON_INFORMATION_MESSAGE);

		// choise
		Label emptyLabel = new Label(infoContents, SWT.None);
		emptyLabel.setText(""); //$NON-NLS-1$
		Label infoImageLabel = new Label(infoContents, SWT.ICON | SWT.CENTER);
		infoImageLabel.setImage(EgovBatchPlugin.getDefault().getImage(EgovBatchPlugin.IMG_BATCH_STEPDECISION_ADD));

	}

	/** Job List의 Selection을 가져온다. */
	private JobVo getJobListSelection() {
		IStructuredSelection selection = (IStructuredSelection) jobListTable.getSelection();

		if (!selection.isEmpty()) {
			JobVo jobVo = (JobVo) selection.getFirstElement();
			return jobVo;
		} else {
			return null;
		}
	}

	/** Step/Decision List의 Selection을 가져온다. */
	private StepAndDecisionVo getStepDecisionListSelection() {
		IStructuredSelection stepAndDecisionSelection = (IStructuredSelection) stepAndDecisionListTable.getSelection();

		if (!stepAndDecisionSelection.isEmpty()) {
			StepAndDecisionVo selectStepAndDecisionVo = (StepAndDecisionVo) stepAndDecisionSelection.getFirstElement();
			return selectStepAndDecisionVo;
		} else {
			return null;
		}

	}

	/** Step/Decision List의 Selection을 해제한다. */
	private void deselectStepDecisionList() {
		stepAndDecisionListTable.setSelection(new StructuredSelection());
	}

	/** Job의 Add Button 클릭시 작동하는 Listener */
	Listener jobAddButtonListener = new Listener() {

		public void handleEvent(Event event) {
			setMessage(BatchMessages.BatchJobCreationCustomizePage_SHOW_NEW_JOB_INFO);

			// Job List, Step/Decision List의 selection해제 및 Step/Decision List의
			// input 제거
			jobListTable.setSelection(new StructuredSelection());

			stepAndDecisionListTable.setSelection(new StructuredSelection());
			stepAndDecisionListTable.setInput(null);

			refreshInfoToJob(null);

			// Job, Step/Decision의 add, remove 버튼 disable
			jobAddButton.setEnabled(false);
			jobRemoveButton.setEnabled(false);

			stepAndDecisionAdd.setEnabled(false);
			stepAndDecisionRemove.setEnabled(false);

			// apply버튼 disable
			applyButton.setEnabled(false);
		}
	};

	/** Job의 Remove Button 클릭시 작동하는 Listener */
	Listener jobRemoveButtonListener = new Listener() {

		public void handleEvent(Event event) {
			JobVo selectJobVo = getJobListSelection();

			if (!NullUtil.isNull(selectJobVo)) {
				if (!NullUtil.isEmpty(selectJobVo.getStepAndDecisionVoList())) {
					String message = selectJobVo.getJobName()
							+ BatchMessages.BatchJobCreationCustomizePage_CONFIRM_DELETE_JOB_HAVING_STEP_DECISION;
					if (!openConfirmDialog(BatchMessages.BatchJobCreationCustomizePage_DELETE_BUTTON_DIALOG_TITLE,
							message)) {
						return;
					}
				} else {
					String message = selectJobVo.getJobName()
							+ BatchMessages.BatchJobCreationCustomizePage_CONFIRM_DELETE_JOB_WITHOUT_STEP_DECISION;
					if (!openConfirmDialog(BatchMessages.BatchJobCreationCustomizePage_DELETE_BUTTON_DIALOG_TITLE,
							message)) {
						return;
					}
				}

				batchXMLFileBeanIDList.removeJobBeanIDList(selectJobVo.getJobName());

				jobListTable.remove(selectJobVo);

				jobAddButton.notifyListeners(SWT.Selection, new Event());

				setPageComplete(canFinishButtonEnable());
			}
		}
	};

	/** Job의 List의 항목을 클릭시 작동하는 Listener */
	Listener jobListTableListener = new Listener() {
		public void handleEvent(Event event) {
			JobVo selectJobVo = getJobListSelection();
			deselectStepDecisionList();

			setMessage(BatchMessages.BatchJobCreationCustomizePage_SELECT_JOB);

			if (!NullUtil.isNull(selectJobVo)) {
				stepAndDecisionListTable.setInput(selectJobVo.getStepAndDecisionVoList());

				refreshInfoToJob(selectJobVo);

				jobAddButton.setEnabled(true);
				jobRemoveButton.setEnabled(true);

				stepAndDecisionAdd.setEnabled(true);
				stepAndDecisionRemove.setEnabled(false);

				applyButton.setEnabled(true);
			}

			setPageComplete(canFinishButtonEnable());
		}
	};

	/**
	 * Job Info 생성 및 해당하는 Listener, InfoLabel 및 StepAndDecisionCombo 설정
	 * 
	 * @param jobVo
	 */
	private void refreshInfoToJob(JobVo jobVo) {
		clearInfoContentsAndButtonListenerAndMessage();

		infoLabel.setText(BatchMessages.BatchJobCreationCustomizePage_JOB_INFO);

		jobInfoControl.createJobInfoContents(jobVo, getJobVoNameList());

		setApplyAndRestoreButtonListener(jobApplyButtonListener, jobRestoreButtonListener);

		stepAndDecisionCombo.setVisible(false);

		refreshInfoControl();
	}

	/** Job의 Apply Button 클릭시 작동하는 Listener */
	Listener jobApplyButtonListener = new Listener() {
		public void handleEvent(Event event) {
			JobVo selectJobVo = getJobListSelection();
			JobVo newJobVo = jobInfoControl.getJobVo();

			if (!NullUtil.isNull(selectJobVo)) {
				if (openConfirmDialog(BatchMessages.BatchJobCreationCustomizePage_APPLY_BUTTON_DIALOG_TITLE,
						BatchMessages.BatchJobCreationCustomizePage_CONFIRM_UPDATE)) {

					updateJobVoAndBeanIDList(selectJobVo, newJobVo);

				}
			} else {
				addNewJobVoAndBeanIDList(newJobVo);

			}

			jobListTableListener.handleEvent(new Event());

			setPageComplete(canFinishButtonEnable());
		}
	};

	/**
	 * JobVO를 List에 반영하고 BeanID를 갱신
	 * 
	 * @param selectJobVo
	 * @param newJobVo
	 */
	private void updateJobVoAndBeanIDList(JobVo selectJobVo, JobVo newJobVo) {
		batchXMLFileBeanIDList.updateJobVoBeanIDList(selectJobVo.getJobName(), newJobVo);
		selectJobVo.copyValues(newJobVo);
		jobListTable.update(selectJobVo, null);
	}

	/**
	 * JobVO를 List에 추가하고 BeanID를 추가
	 * 
	 * @param newJobVo
	 */
	private void addNewJobVoAndBeanIDList(JobVo newJobVo) {
		jobListTable.add(newJobVo);
		selectJobVo(newJobVo.getJobName());

		batchXMLFileBeanIDList.addNewJobVoBeanIDList(newJobVo);

		setMessage(BatchMessages.BatchJobCreationCustomizePage_ADD_NEW_JOB);
	}

	/** Job의 Restore Default Button 클릭시 작동하는 Listener */
	Listener jobRestoreButtonListener = new Listener() {

		public void handleEvent(Event event) {
			String message = BatchMessages.BatchJobCreationCustomizePage_RESTORE_DEFAULT_CONFIRM_MESSAGE;
			if (openConfirmDialog(BatchMessages.BatchJobCreationCustomizePage_RESTORE_DEFAULT_BUTTON_DIALOG_TITLE,
					message)) {

				if (NullUtil.isNull(getJobListSelection())) {
					jobAddButtonListener.handleEvent(new Event());

				} else {
					jobListTableListener.handleEvent(new Event());

				}
			}
		}
	};

	/** Step/Decision List의 항목을 클릭시 작동하는 Listener */
	Listener stepAndDecisionListTableListener = new Listener() {

		public void handleEvent(Event event) {
			JobVo selectJobVo = getJobListSelection();
			StepAndDecisionVo selectSDVo = getStepDecisionListSelection();

			if (!NullUtil.isNull(selectSDVo)) {
				if (selectSDVo instanceof StepVo) {
					setMessage(BatchMessages.BatchJobCreationCustomizePage_SELETE_STEP);

					refreshInfoToStep(selectJobVo, (StepVo) selectSDVo);
				} else if (selectSDVo instanceof DecisionVo) {
					setMessage(BatchMessages.BatchJobCreationCustomizePage_SELECT_DECISION);

					refreshInfoToDecision(selectJobVo, (DecisionVo) selectSDVo);
				}

				stepAndDecisionCombo.setVisible(true);
				stepAndDecisionCombo.setEnabled(false);

				jobRemoveButton.setEnabled(false);

				stepAndDecisionRemove.setEnabled(true);

				restoreDefaultsButton.setEnabled(true);
			}

			setPageComplete(canFinishButtonEnable());
		}
	};

	/**
	 * Step Info 생성 및 해당하는 Listener, InfoLabel 및 StepAndDecisionCombo 설정
	 * 
	 * @param jobVo
	 * @param stepVo
	 */
	private void refreshInfoToStep(JobVo jobVo, StepVo stepVo) {
		currentInfo = STEP;

		clearInfoContentsAndButtonListenerAndMessage();

		stepAndDecisionCombo.setText(STEP);

		infoLabel.setText(BatchMessages.BatchJobCreationCustomizePage_STEP_INFO_LABEL);

		stepInfoControl.createStepInfoContents(jobVo, stepVo);

		setApplyAndRestoreButtonListener(stepAndDecisionApplyButtonListener, stepAndDecisionRestoreButtonListener);

		refreshInfoControl();
	}

	/**
	 * Decision Info 생성 및 해당하는 Listener, InfoLabel 및 StepAndDecisionCombo 설정
	 * 
	 * @param jobVo
	 * @param decisionVo
	 */
	private void refreshInfoToDecision(JobVo jobVo, DecisionVo decisionVo) {
		currentInfo = DECISION;

		clearInfoContentsAndButtonListenerAndMessage();

		stepAndDecisionCombo.setText(DECISION);

		infoLabel.setText(BatchMessages.BatchJobCreationCustomizePage_DECISION_INFO_LABEL);

		decisionInfoControl.createDecisionInfoContents(jobVo, decisionVo);

		setApplyAndRestoreButtonListener(stepAndDecisionApplyButtonListener, stepAndDecisionRestoreButtonListener);

		refreshInfoControl();
	}

	/** Step/Decision의 Apply Button을 클릭시 작동하는 Listener */
	Listener stepAndDecisionApplyButtonListener = new Listener() {
		public void handleEvent(Event event) {
			JobVo selectJobVo = getJobListSelection();
			StepAndDecisionVo selectSDVo = getStepDecisionListSelection();

			if (DECISION.equals(stepAndDecisionCombo.getText())) {
				applyDecisionAndBeanIDList(selectJobVo, selectSDVo);

			} else if (STEP.equals(stepAndDecisionCombo.getText())) {
				applyStepAndBeanIDList(selectJobVo, selectSDVo);

			}

			stepAndDecisionCombo.setEnabled(false);

			setPageComplete(canFinishButtonEnable());

			stepAndDecisionListTableListener.handleEvent(null);
		}

		/**
		 * DecisionVo을 Apply 및 BeanID 등록
		 * 
		 * @param selectJobVo
		 * @param selectSDVo
		 */
		private void applyDecisionAndBeanIDList(JobVo selectJobVo, StepAndDecisionVo selectSDVo) {
			DecisionVo newDecisionVo = decisionInfoControl.getDecisionVo();

			if (NullUtil.isNull(selectSDVo)) {
				openNewDecisionApplyInformDialog();

				addNewDecisionVOAndbeanIDList(selectJobVo, newDecisionVo);

			} else {
				updateDecisionVoAndBeanIDList(selectJobVo, (DecisionVo) selectSDVo, newDecisionVo);

			}
		}

		/** 새로운 DecisionVo가 Apply됐을 경우 InfomMessage Dialog 생성 */
		private void openNewDecisionApplyInformDialog() {
			String informMessage = BatchMessages.BatchJobCreationCustomizePage_INFORM_AFTER_ADD_DICISION;
			MessageDialog.openInformation(getShell(),
					BatchMessages.BatchJobCreationCustomizePage_INFORM_AFTER_ADD_DICISION_DIALOG_TITLE, informMessage);
		}

		/**
		 * 새로운 DecisionVo을 Apply 및 BeanID 등록
		 * 
		 * @param selectJobVo
		 * @param newDecisionVo
		 */
		private void addNewDecisionVOAndbeanIDList(JobVo selectJobVo, DecisionVo newDecisionVo) {
			addNewStepAndDecisionVo(selectJobVo, newDecisionVo);

			batchXMLFileBeanIDList.addDecisionVoBeanIDList(selectJobVo, newDecisionVo);

			setMessage(BatchMessages.BatchJobCreationCustomizePage_ADD_DECISION);
		}

		/**
		 * 새로운 Step, DecisionVo을 List에 반영 및 JobVo에 추가
		 * 
		 * @param selectJobVo
		 * @param newSDVo
		 */
		private void addNewStepAndDecisionVo(JobVo selectJobVo, StepAndDecisionVo newSDVo) {
			stepAndDecisionListTable.add(newSDVo);

			selectJobVo.setStepAndDecisionVoList(getStepAndDecisionList());

			selectStepAndDecisionVo(newSDVo.getName());
		}

		/**
		 * 기존 DecisionVo을 Apply 및 BeanID 갱신
		 * 
		 * @param selectJobVo
		 * @param selectSDVo
		 * @param newDecisionVo
		 */
		private void updateDecisionVoAndBeanIDList(JobVo selectJobVo, DecisionVo selectSDVo, DecisionVo newDecisionVo) {
			String preDecisionID = selectSDVo.getName();
			String postDecisionID = newDecisionVo.getName();

			if (preDecisionID.equals(postDecisionID)) {
				if (openConfirmDialog(BatchMessages.BatchJobCreationCustomizePage_APPLY_BUTTON_DIALOG_TITLE,
						BatchMessages.BatchJobCreationCustomizePage_UPDATE_MESSAGE_2)) {
					updateDecisionVoAndBeanIDListWhenIDNotChanged(selectJobVo, selectSDVo, newDecisionVo);
				}

			} else {
				updateDecisionVoAndBeanIDListWhenIDChanged(selectJobVo, selectSDVo, newDecisionVo, preDecisionID,
						postDecisionID);

			}
		}

		/**
		 * DecisionVo의 ID가 변경되지 않았을 때 DecisionVo, BeanIDList 갱신
		 * 
		 * @param selectJobVo
		 * @param selectSDVo
		 * @param newDecisionVo
		 */
		private void updateDecisionVoAndBeanIDListWhenIDNotChanged(JobVo selectJobVo, DecisionVo selectSDVo,
				DecisionVo newDecisionVo) {
			DecisionVo selectDecisionVo = (DecisionVo) selectSDVo;

			batchXMLFileBeanIDList.updateDecisionVoBeanIDList(selectDecisionVo, selectJobVo, newDecisionVo);

			selectDecisionVo.copyValues(newDecisionVo);

			stepAndDecisionListTable.update(selectDecisionVo, null);
		}

		/**
		 * DecisionVo의 ID가 변경되었을 때 DecisionVo, BeanIDList 갱신
		 * 
		 * @param selectJobVo
		 * @param selectSDVo
		 * @param newDecisionVo
		 * @param preDecisionID
		 * @param postDecisionID
		 */
		private void updateDecisionVoAndBeanIDListWhenIDChanged(JobVo selectJobVo, DecisionVo selectSDVo,
				DecisionVo newDecisionVo, String preDecisionID, String postDecisionID) {
			ArrayList<StepAndDecisionVo> refList = getReferenceList(selectJobVo, selectSDVo);

			String message = createRefDialogModifyString(selectSDVo.getName(), refList);

			if (openConfirmDialog(BatchMessages.BatchJobCreationCustomizePage_APPLY_BUTTON_DIALOG_TITLE, message)) {
				updateDecisionVoAndBeanIDListWhenIDNotChanged(selectJobVo, selectSDVo, newDecisionVo);

				updateStepDecisionIDToRefList(preDecisionID, postDecisionID, refList);
			}
		}

		/**
		 * 참조되는 Step, Decision의 ID가 변경될 경우 참조하는 Step, Decision의 정보도 수정해준다.
		 * 
		 * @param preID
		 * @param newID
		 * @param refList
		 */
		private void updateStepDecisionIDToRefList(String preID, String newID, ArrayList<StepAndDecisionVo> refList) {
			if (!NullUtil.isEmpty(refList)) {
				for (StepAndDecisionVo vo : refList) {
					if (vo instanceof StepVo) {
						updateStepDecisionIDToStepVo(preID, newID, (StepVo) vo);

					} else if (vo instanceof DecisionVo) {
						updateStepDecisionIDToDecisionVo(preID, newID, (DecisionVo) vo);

					}
				}
			}
		}

		/**
		 * 참조되는 Step, Decision의 ID가 변경될 경우 참조하는 Decision의 정보도 수정해준다.
		 * 
		 * @param preStepID
		 * @param newStepID
		 * @param refDecisionVo
		 */
		private void updateStepDecisionIDToDecisionVo(String preStepID, String newStepID, DecisionVo refDecisionVo) {
			NextVo[] nextVos = refDecisionVo.getNextVo();
			if (!NullUtil.isEmpty(nextVos)) {
				for (NextVo nextVo : nextVos) {
					if (nextVo.getNextTo().equals(preStepID)) {
						nextVo.setNextTo(newStepID);

					}
				}
			}

			String stopRestart = refDecisionVo.getStopRestart();
			if (!NullUtil.isEmpty(stopRestart)) {
				if (stopRestart.equals(preStepID)) {
					refDecisionVo.setStopRestart(newStepID);

				}
			}
		}

		/**
		 * 참조되는 Step, Decision의 ID가 변경될 경우 참조하는 Step의 정보도 수정해준다.
		 * 
		 * @param preStepID
		 * @param newStepID
		 * @param refStepVo
		 */
		private void updateStepDecisionIDToStepVo(String preStepID, String newStepID, StepVo refStepVo) {
			String nextStepID = refStepVo.getNextStep();
			if (!NullUtil.isEmpty(nextStepID)) {
				if (nextStepID.equals(preStepID)) {
					refStepVo.setNextStep(newStepID);

				}
			}
		}

		/**
		 * StepVo을 Apply 및 BeanID 갱신
		 * 
		 * @param selectJobVo
		 * @param selectSDVo
		 */
		private void applyStepAndBeanIDList(JobVo selectJobVo, StepAndDecisionVo selectSDVo) {
			StepVo newStepVo = stepInfoControl.getStepVo();

			if (NullUtil.isNull(selectSDVo)) {
				if (stepAndDecisionListTable.getTable().getItemCount() > 0) {
					openMoreTwoStepApplyInformDialog();
				}

				addNewStepVoAndBeanIDList(selectJobVo, newStepVo);

			} else {
				updateStepVoAndBeanIDList(selectJobVo, (StepVo) selectSDVo, newStepVo);

			}
		}

		/** 2번째 StepVo 이상 Apply 할 경우 InformMessaage Dialog 생성 */
		private void openMoreTwoStepApplyInformDialog() {
			MessageDialog.openInformation(getShell(),
					BatchMessages.BatchJobCreationCustomizePage_INFORM_AFTER_ADD_MORE_TWO_STEPS_DIALOG_TITLE,
					BatchMessages.BatchJobCreationCustomizePage_INFORM_AFTER_ADD_MORE_TWO_STEPS);
		}

		/**
		 * 새로운 StepVo을 Apply 및 BeanID 추가
		 * 
		 * @param selectJobVo
		 * @param newStepVo
		 */
		private void addNewStepVoAndBeanIDList(JobVo selectJobVo, StepVo newStepVo) {
			addNewStepAndDecisionVo(selectJobVo, newStepVo);

			batchXMLFileBeanIDList.addStepVoBeanIDList(selectJobVo, newStepVo);

			selectJobVo.setSharedValues(stepInfoControl.applyStepIDToSharedVoAndGetList(newStepVo.getName()));

			setMessage(BatchMessages.BatchJobCreationCustomizePage_ADD_STEP);
		}

		/**
		 * 기존 StepVo을 Apply 및 BeanID 갱신
		 * 
		 * @param selectJobVo
		 * @param selectSDVo
		 * @param newStepVo
		 */
		private void updateStepVoAndBeanIDList(JobVo selectJobVo, StepVo selectSDVo, StepVo newStepVo) {
			String preStepID = selectSDVo.getName();
			String postStepID = newStepVo.getName();

			if (preStepID.equals(postStepID)) {
				if (openConfirmDialog(BatchMessages.BatchJobCreationCustomizePage_APPLY_BUTTON_DIALOG_TITLE,
						BatchMessages.BatchJobCreationCustomizePage_UPDATE_MESSAGE_2)) {
					updateStepVoAndBeanIDListWhenIDNotChanged(selectJobVo, selectSDVo, newStepVo, preStepID,
							postStepID);
				}

			} else {
				updateStepVoAndBeanIDListWhenIDChanged(selectJobVo, selectSDVo, newStepVo, preStepID, postStepID);

			}
		}

		/**
		 * StepVo의 ID가 변경되지 않았을 때 StepVo, BeanIDList 갱신
		 * 
		 * @param selectJobVo
		 * @param selectSDVo
		 * @param newStepVo
		 * @param preStepID
		 * @param postStepID
		 */
		private void updateStepVoAndBeanIDListWhenIDNotChanged(JobVo selectJobVo, StepVo selectSDVo, StepVo newStepVo,
				String preStepID, String postStepID) {
			StepVo selectStepVo = (StepVo) selectSDVo;

			batchXMLFileBeanIDList.updateStepVoBeanIDList(selectStepVo, selectJobVo, newStepVo);

			selectStepVo.copyValues(newStepVo);

			stepAndDecisionListTable.update(selectStepVo, null);

			selectJobVo.setSharedValues(stepInfoControl.updateStepIDToSharedValueVoAndGetList(preStepID, postStepID));
		}

		/**
		 * StepVo의 ID가 변경되었을 때 StepVo, BeanIDList 갱신
		 * 
		 * @param selectJobVo
		 * @param selectSDVo
		 * @param newStepVo
		 * @param preStepID
		 * @param postStepID
		 */
		private void updateStepVoAndBeanIDListWhenIDChanged(JobVo selectJobVo, StepVo selectSDVo, StepVo newStepVo,
				String preStepID, String postStepID) {

			ArrayList<StepAndDecisionVo> refList = getReferenceList(selectJobVo, selectSDVo);

			String message = createRefDialogModifyString(selectSDVo.getName(), refList);

			if (openConfirmDialog(BatchMessages.BatchJobCreationCustomizePage_APPLY_BUTTON_DIALOG_TITLE, message)) {

				updateStepVoAndBeanIDListWhenIDNotChanged(selectJobVo, selectSDVo, newStepVo, preStepID, postStepID);

				updateStepDecisionIDToRefList(preStepID, newStepVo.getName(), refList);
			}
		}

	};

	/** Step의 Restore Default Button을 클릭시 작동하는 Listener */
	Listener stepAndDecisionRestoreButtonListener = new Listener() {

		public void handleEvent(Event event) {
			String message = BatchMessages.BatchJobCreationCustomizePage_RESTORE_DEFAULT_CONFIRM_MESSAGE;
			if (!openConfirmDialog(BatchMessages.BatchJobCreationCustomizePage_RESTORE_DEFAULT_BUTTON_DIALOG_TITLE,
					message)) {
				return;
			}

			StepAndDecisionVo selectSDVo = getStepDecisionListSelection();
			if (NullUtil.isNull(selectSDVo)) {
				stepAndDecisionCombo.notifyListeners(SWT.Selection, new Event());
			} else {
				stepAndDecisionListTableListener.handleEvent(new Event());
			}
		}
	};

	/** Step/Decision의 Add Button을 클릭시 작동하는 Listener */
	Listener stepAndDecisionAddButtonListener = new Listener() {

		public void handleEvent(Event event) {
			setPageComplete(false);

			deselectStepDecisionList();

			setMessage(BatchMessages.BatchJobCreationCustomizePage_SHOW_NEW_STEP_DECISION_INFO, NONE);

			infoLabel.setText("");

			clearInfoContentsAndButtonListenerAndMessage();

			createStepDecisionAddButtonSelectInfoContents();

			refreshInfoControl();

			stepAndDecisionCombo.setVisible(true);
			stepAndDecisionCombo.setEnabled(true);
			stepAndDecisionCombo.setText(DEFAULT);

			if (stepAndDecisionListTable.getTable().getItemCount() > 0) {
				removeStepAndDecisionComboListener();

				setEnableStepAndDecisionComboListener();

			} else {
				removeStepAndDecisionComboListener();

				atFirstStepComboListener();

			}

			jobRemoveButton.setEnabled(false);

			stepAndDecisionRemove.setEnabled(false);

			applyButton.setEnabled(false);

			restoreDefaultsButton.setEnabled(false);

			stepAndDecisionListTable.setSelection(null);
		}
	};

	/** Step/Decision의 Remove Button을 클릭시 작동하는 Listener */
	Listener stepAndDecisionRemoveTableViewerListener = new Listener() {

		public void handleEvent(Event event) {
			JobVo selectJobVo = getJobListSelection();
			StepAndDecisionVo selectStepAndDecisionVo = getStepDecisionListSelection();

			ArrayList<StepAndDecisionVo> refMemberList = getReferenceList(selectJobVo, selectStepAndDecisionVo);

			String message = createRefDialogRemoveString(selectStepAndDecisionVo.getName(), refMemberList);

			if (!openConfirmDialog(BatchMessages.BatchJobCreationCustomizePage_REMOVE_STEP_DECISION_DIALOG_TITLE,
					message)) {
				return;
			}

			if (!NullUtil.isNull(selectStepAndDecisionVo)) {
				BatchJobBeanIDList jobBeanList = batchXMLFileBeanIDList.getJobBeanIDList(selectJobVo.getJobName());
				jobBeanList.removeStepDecision(selectStepAndDecisionVo.getName());

				removeStepDecisionIDToRefList(selectStepAndDecisionVo.getName(), refMemberList);

				stepAndDecisionListTable.remove(selectStepAndDecisionVo);

				selectJobVo.setStepAndDecisionVoList(getStepAndDecisionList());

				stepAndDecisionRemove.setEnabled(false);

				if (selectStepAndDecisionVo instanceof StepVo) {
					StepVo removedStepVo = (StepVo) selectStepAndDecisionVo;
					updateSharedValueVo(selectJobVo, removedStepVo.getName());
				}

				stepAndDecisionAdd.notifyListeners(SWT.Selection, new Event());
			}

			setPageComplete(canFinishButtonEnable());
		}

		/**
		 * 참조되는 Step, Decision의 ID가 삭제될 경우 참조하는 Step, Decision의 정보도 삭제해준다.
		 * 
		 * @param preID
		 * @param newID
		 * @param refList
		 */
		private void removeStepDecisionIDToRefList(String preID, ArrayList<StepAndDecisionVo> refList) {
			if (!NullUtil.isEmpty(refList)) {
				for (StepAndDecisionVo vo : refList) {
					if (vo instanceof StepVo) {
						updateRefStepVo(preID, (StepVo) vo);

					} else if (vo instanceof DecisionVo) {
						updateRefDecisionVo(preID, (DecisionVo) vo);

					}
				}
			}
		}

		/**
		 * 참조되는 Step, Decision의 ID가 삭제될 경우 참조하는 Decision의 정보도 삭제해준다.
		 * 
		 * @param removedID
		 * @param newStepID
		 * @param refDecisionVo
		 */
		private void updateRefDecisionVo(String removedID, DecisionVo refDecisionVo) {
			NextVo[] nextVosExceptRefID = getNextVoListExceptRefID(removedID, refDecisionVo);
			refDecisionVo.setNextVo(nextVosExceptRefID);

			updateStopOnTo(removedID, refDecisionVo);
		}

		/**
		 * 삭제된 VO의 ID를 참조하는 NextVo를 제외한 나머지 NextVoList를 반환한다.
		 * 
		 * @param preStepID
		 * @param refDecisionVo
		 * @return
		 */
		private NextVo[] getNextVoListExceptRefID(String preStepID, DecisionVo refDecisionVo) {
			ArrayList<NextVo> updateNextVos = new ArrayList<NextVo>();
			NextVo[] nextVos = refDecisionVo.getNextVo();
			if (!NullUtil.isEmpty(nextVos)) {
				for (NextVo nextVo : nextVos) {
					if (!nextVo.getNextTo().equals(preStepID)) {
						updateNextVos.add(nextVo);
					}
				}
			}

			return updateNextVos.toArray(new NextVo[0]);
		}

		/**
		 * Stop Restart가 삭제된 VO의 ID를 참조하는 경우 Stop On, Restart의 값을 제거한다.
		 * 
		 * @param removedID
		 * @param refDecisionVo
		 */
		private void updateStopOnTo(String removedID, DecisionVo refDecisionVo) {
			String stopRestart = refDecisionVo.getStopRestart();
			if (!NullUtil.isEmpty(stopRestart)) {
				if (stopRestart.equals(removedID)) {
					refDecisionVo.setStopOn("");
					refDecisionVo.setStopRestart("");

				}
			}
		}

		/**
		 * 참조되는 Step, Decision의 ID가 삭제될 경우 참조하는 Step의 정보도 삭제해준다.
		 * 
		 * @param preStepID
		 * @param newStepID
		 * @param refStepVo
		 */
		private void updateRefStepVo(String preStepID, StepVo refStepVo) {
			String nextStepID = refStepVo.getNextStep();
			if (!NullUtil.isEmpty(nextStepID)) {
				if (nextStepID.equals(preStepID)) {
					refStepVo.setNextStep("");

				}
			}
		}

		/**
		 * Job의 SharedValueVoList를 삭제된 StepID를 제외한 SharedValueVoList로 갱신한다.
		 * 
		 * @param selectJobVo
		 * @param removedStepID
		 */
		private void updateSharedValueVo(JobVo selectJobVo, String removedStepID) {
			SharedValueVo[] sharedValueVoList = selectJobVo.getSharedValues();
			ArrayList<SharedValueVo> afterRemovedSVVoList = new ArrayList<SharedValueVo>();

			if (!NullUtil.isEmpty(sharedValueVoList)) {
				for (SharedValueVo svVo : sharedValueVoList) {
					String svVoStepID = svVo.getStepId();
					if (!svVoStepID.equals(removedStepID)) {
						afterRemovedSVVoList.add(svVo);
					}
				}
			}

			sharedValueVoList = afterRemovedSVVoList.toArray(new SharedValueVo[0]);
			selectJobVo.setSharedValues(sharedValueVoList);
		}

	};

	/**
	 * 넘겨진 String을 표시하는 ConfirmMessage Dialog 생성
	 * 
	 * @param title
	 * @param message
	 * @return
	 */
	private boolean openConfirmDialog(String title, String message) {
		return MessageDialog.openConfirm(getShell(), title, message);
	}
}