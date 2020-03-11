package egovframework.bdev.imp.batch.wizards.jobcreation.pages;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import egovframework.bdev.imp.batch.common.BatchMessages;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.BatchJobBeanIDList;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.BatchPageBeanIDList;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.BatchXMLFileBeanIDList;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.JobVo;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.StepAndDecisionVo;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.StepVo;
import egovframework.bdev.imp.batch.wizards.jobcreation.views.BatchListenerLabelProvider;
import egovframework.bdev.imp.confmngt.preferences.listeners.model.JobListenerInfo;
import egovframework.bdev.imp.confmngt.preferences.listeners.model.ListenerInfo;
import egovframework.bdev.imp.confmngt.preferences.readwrite.model.JobRWInfo;
import egovframework.dev.imp.core.utils.BatchTableColumn;
import egovframework.dev.imp.core.utils.NullUtil;
import egovframework.dev.imp.core.utils.StringUtil;
/**
 * BatchJobCreationCustomizePage에서 Job Info 화면을 그림
 * 
 * @author 배치개발환경 개발팀 조용현
 * @since 2012.06.28
 * @version 1.0
 * @see <pre>
 *  &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *    
 * 수정일	  	수정자	  수정내용
 * -----------	------	----------------
 * 2012.09.18	조용현	최초생성
 * 
 * 
 * </pre>
 */
public class JobInfoContentsConstructor extends InfoContentsConstructor{
	
	/** JobInfo 화면의 입력값을 가지고 다니는 임시 JobVo */
	private JobVo tempJobVo = null;
	
	/** Job List에 있는 JobVO들의 name List */
	private String[] jobVoNameLists = null;

	/**
	 * JobInfoContentsConstructor 생성자
	 * 
	 * @param currentPage
	 * @param infoControl
	 * @param applyButton
	 * @param batchXMLFileBeanIDList
	 * @param projectBeanIDList
	 *
	 */
	public JobInfoContentsConstructor(
			WizardPage currentPage, Composite infoControl,
			Button applyButton, BatchXMLFileBeanIDList batchXMLFileBeanIDList, List<String> projectBeanIDList) {
		this.currentPage = currentPage;
		this.infoControl = infoControl;
		this.applyButton = applyButton;
		this.batchXMLFileBeanIDList = batchXMLFileBeanIDList;
		this.projectBeanIDList = projectBeanIDList;
	}

	/**
	 * Job Info 생성
	 * 
	 * @param jobVo
	 * @param jobVoNameList
	 */
	public void createJobInfoContents(JobVo jobVo, String[] jobVoNameList) {
		tempJobVo = new JobVo();
		tempJobVo.copyValues(jobVo);
		
		this.jobVoNameLists = jobVoNameList;
		
		invalidBatchXMLFileBeanIDList = getInvalidBeanList(tempJobVo);
		
		createJobIDControl(infoControl);
		
		createRestartControl(infoControl);
		
		createJobListenerTableViewer(infoControl);
	}
	
	/**
	 * 중복검사를 위해 현재 Job Info page의 BeanID list를 제외한 나머지 BeanID list를 가져온다.
	 * 
	 * @param jobVo
	 * @return
	 */
	protected BatchXMLFileBeanIDList getInvalidBeanList(JobVo jobVo){	
		BatchXMLFileBeanIDList invalidList = batchXMLFileBeanIDList.clone();
		BatchJobBeanIDList beanIDList = invalidList.getJobBeanIDList(jobVo.getJobName());
		
		if(!NullUtil.isNull(beanIDList)){
			beanIDList.removeJobBeanPageToAvailableList();
		}

		return invalidList;
	}
	
	/**
	 * JobI ID Control 생성
	 * 
	 * @param control
	 */
	private void createJobIDControl(Composite control){
		final Text jobIDField = createJobIDField(control, tempJobVo);
		jobIDField.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event event) {
				tempJobVo.setJobName(jobIDField.getText());
			}
		});
		
		jobIDField.addListener(SWT.Modify, validation());
		jobIDField.forceFocus();
	}
	
	/**
	 * Restart RadioButton Control 생성
	 * 
	 * @param control
	 */
	private void createRestartControl(Composite control){
		Group restartableGroup = new Group(control, SWT.NONE);
		restartableGroup.setLayout(new GridLayout(2, true));
		restartableGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		restartableGroup.setText(BatchMessages.JobInfoContentsConstructor_RESTART_GROUP_TITLE);

		Button restartableTrue = new Button(restartableGroup, SWT.RADIO);
		restartableTrue.setText(BatchMessages.JobInfoContentsConstructor_RESTART_TRUE_BUTTON_TEXT);
		restartableTrue.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				tempJobVo.setRestartable(true);
			}
		});

		Button restartableFalse = new Button(restartableGroup, SWT.RADIO);
		restartableFalse.setText(BatchMessages.JobInfoContentsConstructor_RESTART_FALSE_BUTTON_TEXT);
		restartableFalse.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				tempJobVo.setRestartable(false);
			}
		});
		
		restartableTrue.setSelection(tempJobVo.isRestartable());
		restartableFalse.setSelection(!tempJobVo.isRestartable());
	}
	
	/**
	 * Job Listener Table Viewer 생성
	 * 
	 * @param control
	 */
	private void createJobListenerTableViewer(Composite control){
		Group jobListenersGroup = new Group(control, SWT.None);
		jobListenersGroup.setLayout(new GridLayout());
		jobListenersGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		jobListenersGroup.setText(BatchMessages.JobInfoContentsConstructor_JOB_LISTENER_GROUP_TITLE);

		final TableViewer jobListenersTableViewer = new TableViewer(jobListenersGroup, SWT.BORDER
				| SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);

		Table table = jobListenersTableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		BatchTableColumn[] columns = createColumns();

		for (int i = 0; i < columns.length; i++) {
			columns[i].setColumnToTable(table);
		}
	
		jobListenersTableViewer.setContentProvider(new ArrayContentProvider());
		jobListenersTableViewer.setLabelProvider(new BatchListenerLabelProvider());
		jobListenersTableViewer.setInput(tempJobVo.getJobListenerInfoList());
		jobListenersTableViewer.getControl().setLayoutData(
				new GridData(GridData.FILL_BOTH));
		
		createTableButtonControl(jobListenersGroup, jobListenersTableViewer);
	}
	
	/**
	 * Job Listener Table Viewer의 Column 생성
	 * 
	 * @return
	 */
	private BatchTableColumn[] createColumns(){
		ArrayList<BatchTableColumn> columns = new ArrayList<BatchTableColumn>();
		columns.add(new BatchTableColumn(BatchMessages.JobInfoContentsConstructor_JOB_LISTENER_TABLE_COLUMN_NAME, 200, SWT.LEFT));
		columns.add(new BatchTableColumn(BatchMessages.JobInfoContentsConstructor_JOB_LISTENER_TABLE_COLUMN_CLASS, 200, SWT.LEFT));
		
		return columns.toArray(new BatchTableColumn[0]);
	}
	
	/**
	 * Job Listener Table Viewer의 Button 생성
	 * 
	 * @param control
	 * @param tableViewer
	 */
	private void createTableButtonControl(Composite control, final TableViewer tableViewer){
		Composite buttonControl = new Composite(control, SWT.None);
		buttonControl.setLayout(new GridLayout(2, true));
		buttonControl
				.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));

		Button addJobListenerButton = new Button(buttonControl, SWT.PUSH);
		addJobListenerButton.setText(BatchMessages.JobInfoContentsConstructor_JOB_LISTENER_ADD_BUTTON);
		
		addJobListenerButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		final Button removeJobListenerButton = new Button(buttonControl, SWT.PUSH);
		removeJobListenerButton.setText(BatchMessages.JobInfoContentsConstructor_JOB_LISTENER_REMOVE_BUTTON);
		removeJobListenerButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		removeJobListenerButton.setEnabled(false);
		removeJobListenerButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				IStructuredSelection selection = getSelection(tableViewer);

				if (!NullUtil.isNull(selection)) {
					Object[] selections = selection.toArray();
					
					tableViewer.remove(selections);
					removeJobListenerButton.setEnabled(false);
					
					TableItem[] items = tableViewer.getTable().getItems();
					JobListenerInfo[] data = new JobListenerInfo[items.length];
					if(!NullUtil.isEmpty(items)){
						for(int i =0; i< items.length; i++){
							data[i] = (JobListenerInfo)items[i].getData();
						}
					}
					
					tempJobVo.setJobListenerInfoList(data);
				}
			}
		});
		
		tableViewer.getTable().addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				removeJobListenerButton.setEnabled(true);
			}
		});

		addJobListenerButton.addListener(SWT.Selection, listenerTableListener(tableViewer, removeJobListenerButton));
	}
	
	/**
	 * Job ID Control 생성
	 * 
	 * @param control
	 * @param jobVo
	 */
	private Text createJobIDField(Composite control, JobVo jobVo) {
		Composite jobIdControl = new Composite(control, SWT.NONE);
		jobIdControl.setLayout(new GridLayout(2, false));
		jobIdControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label jobId = new Label(jobIdControl, SWT.NONE);
		jobId.setText(BatchMessages.JobInfoContentsConstructor_JOB_ID_LABEL);
		
		String jobName = jobVo.getJobName();

		Text jobIDField = new Text(jobIdControl, SWT.BORDER);
		jobIDField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		jobIDField.setText(StringUtil.returnEmptyStringIfNull(jobName));
		
		return jobIDField;
	}
	
	protected void validateItems() throws InfoValidationException {
		validateDuplicationBeanID();
		
		validateJobID();
	}
	
	/** Bean ID의 중복 검사*/
	private void validateDuplicationBeanID() throws InfoValidationException{
		if(!validateJobPageBeanID()){
			throwInfoValidationException(BatchMessages.JobInfoContentsConstructor_DUPLICATE_BEAN_ID_ERROR_MESSAGE);
			
		}
	}
	
	/** Job ID 유효성 검사*/
	private void validateJobID() throws InfoValidationException{
		String jobName = tempJobVo.getJobName();

		if (NullUtil.isEmpty(jobName)) {
			throwInfoValidationException(BatchMessages.JobInfoContentsConstructor_EMPTY_JOB_ID_ERROR_MESSAGE);
			
		}
		
		if (!StringUtil.isBatchJobBeanIDAvailable(jobName)) {
			throwInfoValidationException(BatchMessages.JobInfoContentsConstructor_INVALID_JOB_ID_ERROR_MESSAGE);
			
		}
		
		if (!NullUtil.isEmpty(jobVoNameLists)) {
			for (int i = 0; i < jobVoNameLists.length; i++) {
				if (jobName.equals(jobVoNameLists[i])) {
					throwInfoValidationException(BatchMessages.JobInfoContentsConstructor_DUPLICATE_JOB_ID_ERROR_MESSAGE);
					
				}
			}
		}
		
		if(isBeanIDDuplicate(invalidBatchXMLFileBeanIDList, jobName)){
			throwInfoValidationException(BatchMessages.JobInfoContentsConstructor_DUPLICATE_BEAN_ID_ERROR_MESSAGE);

		}
		
	}
	
	/** Job Info 내 Item간의 중복 검사 */
	private boolean validateJobPageBeanID(){
		return setJobPageBeanID(new BatchPageBeanIDList());
	}
	
	/** tempJobVo에 있는 BeanID들을 추출해서 저장 */
	private boolean setJobPageBeanID(BatchPageBeanIDList pageBeanIDs){
		return pageBeanIDs.setJobInfoPageBeanIDs(tempJobVo);
	}
	
	/** 해당 TableViewer의 Selection을 가져온다. */
	private IStructuredSelection getSelection(TableViewer tableViewer){
		IStructuredSelection selection = (IStructuredSelection)tableViewer.getSelection();
		if(selection.isEmpty()){
			return null;
		}else{
			return selection;
		}
	}
	
	/**
	 * Job Listener의 TableViewer Listener 생성
	 * 
	 * @param tableviewer
	 * @param removeButton
	 * @return
	 */
	private Listener listenerTableListener(final TableViewer tableviewer, final Button removeButton){
		return new Listener() {
			public void handleEvent(Event event) {
				TableItem[] items = tableviewer.getTable().getItems();
				String[] listenerNames = getListenerNameFromTableItem(items);
				
				BatchPageBeanIDList pageBeanIDs = new BatchPageBeanIDList();
				setJobPageBeanID(pageBeanIDs);
				pageBeanIDs.removeBeanID(listenerNames);
				
				ListenerDialog dialog = new JobListenerDialog(infoControl.getShell(),
						listenerNames, invalidBatchXMLFileBeanIDList, pageBeanIDs);
				if (dialog.open() == org.eclipse.jface.window.Window.OK) {
					ListenerInfo[] data = dialog.getInfo();
					tableviewer.setInput(data);

					JobListenerInfo[] jobData = new JobListenerInfo[data.length];
					for(int i=0; i< data.length; i++){
						jobData[i] = (JobListenerInfo)data[i];
					}
					tempJobVo.setJobListenerInfoList(jobData);

					removeButton.setEnabled(false);
				}
			}
		};
	}
	
	/**
	 * <pre>
	 * Job, Step, Chunk TableViewer의 TableItem Array를
	 * ListenerInfo 타입의 Array로 변환
	 * </pre>
	 * @param items
	 * @return
	 */
	private String[] getListenerNameFromTableItem(TableItem[] items){
		String[] result = new String[items.length];
		for(int i =0; i< items.length; i++){
			result[i] = ((ListenerInfo)items[i].getData()).getName();
		}
		
		return result;
	}
	
	/**
	 * <pre>
	 * Job info에 입력된 정보를 임시 JobVo에 저장한다.
	 * Validation은 실시하지 않는다.
	 * </pre>
	 * @return jobVo
	 */
	public JobVo getJobVo() {
		updateJobNameAndJobRWFullNameInStepDecisionVo(tempJobVo);
		
		return tempJobVo;
	}
	
	/**
	 * JobVo 변경시 해당 JobVo에 속한 Step, DecisionVo의 jobName 변경
	 * 
	 * @param jobVo
	 */
	private void updateJobNameAndJobRWFullNameInStepDecisionVo(JobVo jobVo){
		StepAndDecisionVo[] sdVos = jobVo.getStepAndDecisionVoList();
		if(!NullUtil.isEmpty(sdVos)){
			for(int i = 0; i < sdVos.length; i++){
				sdVos[i].setJobName(jobVo.getJobName());
				
				if(sdVos[i] instanceof StepVo){
					StepVo stepVo = (StepVo)sdVos[i];
					stepVo.setJobReaderFullName(getJobReaderFullName(stepVo));
					stepVo.setJobWriterFullName(getJobWriterFullName(stepVo));
				}
			}
		}
	}
	
	/**
	 * Job Reader의 Full Name 생성
	 * 
	 * @param stepVo
	 * @return
	 */
	private String getJobReaderFullName(StepVo stepVo){
		return getJobRWFullName(stepVo.getJobName(), stepVo.getName(), stepVo.getJobReaderInfo());
	}
	
	/**
	 *  Job Writer의 Full Name 생성
	 * 
	 * @param stepVo
	 * @return
	 */
	private String getJobWriterFullName(StepVo stepVo){
		return getJobRWFullName(stepVo.getJobName(), stepVo.getName(), stepVo.getJobWriterInfo());
	}
	
	/**
	 *  Job Reader/ Writer의 Full Name 생성
	 * 
	 * @param jobID
	 * @param stepID
	 * @param jobRW
	 * @return
	 */
	private String getJobRWFullName(String jobID, String stepID, JobRWInfo jobRW){
		if(!NullUtil.isNull(jobRW)){
			return jobID+"."+stepID+"."+jobRW.getName(); //$NON-NLS-1$ //$NON-NLS-2$
		}else{
			return null;
		}
	}
}
