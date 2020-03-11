package egovframework.bdev.tst.wizards.pages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.junit.JUnitCore;
import org.eclipse.jdt.junit.TestRunListener;
import org.eclipse.jdt.junit.launcher.JUnitLaunchShortcut;
import org.eclipse.jdt.junit.model.ITestCaseElement;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.w3c.dom.NodeList;

import egovframework.bdev.imp.batch.wizards.jobcreation.model.JobVo;
import egovframework.bdev.imp.confmngt.preferences.parameter.model.JobParameterInfo;
import egovframework.bdev.imp.confmngt.preferences.parameter.model.JobParameterLabelProvider;
import egovframework.bdev.tst.EgovBatchTestPlugin;
import egovframework.bdev.tst.common.BatchTestLog;
import egovframework.bdev.tst.common.BatchTestMessages;
import egovframework.bdev.tst.util.FindBeanIdValueUtil;
import egovframework.bdev.tst.util.FindFilesinProjectUtil;
import egovframework.bdev.tst.util.GenerateJobTestFileUtil;
import egovframework.bdev.tst.wizards.model.BatchJobTestContext;
import egovframework.bdev.tst.wizards.views.JobLauncherListLabelProvider;
import egovframework.bdev.tst.wizards.views.JobListLabelProvider;
import egovframework.bdev.tst.wizards.views.JobTestFilesListLabelProvider;
import egovframework.dev.imp.core.utils.BatchTableColumn;
import egovframework.dev.imp.core.utils.NullUtil;
import egovframework.dev.imp.core.utils.StringUtil;
import egovframework.dev.imp.core.utils.XmlUtil;
/**
 * 배치 테스트 사용자 정의 마법사 페이지 클래스
 * @author 조용현
 * @since 2012.07.24
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *수정일	  	수정자	  수정내용
 *-----------	------	----------------
 *2012.07.24	조용현	최초생성
 *
 * 
 * </pre>
 */
public class BatchJobTestCustomizePage extends WizardPage {
	
	/** Page Size */
	Point size = new Point(650,800);
	
	/** Batch Job Test Context */
	BatchJobTestContext context;
	
	/** 선택한 Project 이름*/
	Label projectName;
	
	/** Project가 가지는 Job 목록*/
	TableViewer jobList;
	
	/** Job Parameter의 TableViewer*/
	TableViewer jobParameterList;
	
	/** Job Parameter TableViewer의 Add Button*/
	Button addJobParameterButton;
	
	/** Job Parameter TableViewer의 Remove Button*/
	Button removeJobParameterButton;
	
	/** Test 실행 Button*/
	Button testButton = null;
	
	/** Folder 경로 설정 및 파일명 입력 Dialog를 여는 Button*/
	Button generateFileButton = null;
	
	/** Test 실행 결과 창 Text */
	Text resultConsoleText = null;
	
	/** Test 결과 */
	String result = ""; //$NON-NLS-1$
	
	/** Test 결과 이미지*/
	Label signalLight = null;
	
	/** Job을 Test 할 수 있는지 여부*/
	private Boolean isJobTestAble = false;
	
	/** Job Executor의 TableViewer*/
	private TableViewer jobLauncherTableViewer;
	
	/** 선택한 jobID가 존재하는 파일 저장*/
	private LinkedHashMap<String, String> jobIDExistFileList = new LinkedHashMap<String, String>();
	
	/** 이미 생성한 Test 파일*/
	private HashMap<String, IFile> testJavaFiles = new HashMap<String, IFile>();
	
	/** New, Reuse Radio Button에 따른 항목이 생성되는 Composite */
	private Group contentsGroup = null;
	
	/** Reuse 에서 기존 Test File 목록이 있는 TableViewer */
	private TableViewer testFilesTableViewer = null;
	
	/** reuse 선택시 사용하는 param 확인 메세지 */
	private String messageParam = "";  //$NON-NLS-1$
	
	/** reuse 선택시 사용하는 job name 확인 메세지 */
	private String messageJobName = ""; //$NON-NLS-1$
	
	/** New Test Radio Button */
	private Button newTestButton = null;
	
	/** Reuse Test Radio Test Button */
	private Button reuseTestButton = null;

	/** reuse 파일내 job name 정보 */
	private ArrayList<String> testFileJobName = new ArrayList<String>();
	
	/**
	 * BatchJobTestCustomizePage의 생성자
	 * 
	 * @param pageName
	 * @param context
	 */
	public BatchJobTestCustomizePage(String pageName, BatchJobTestContext context) {
		super(pageName);
		this.context = context;
		
		setTitle(BatchTestMessages.BatchJobTestCustomizePage_TITLE);
		setDescription(BatchTestMessages.BatchJobTestCustomizePage_DESCRIPTION);
	}

	public void createControl(Composite parent) {
		Composite control = new Composite(parent, SWT.None);
		control.setLayout(new GridLayout());
		control.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		createProjectNameLabel(control);
		
		GridData testContentsGroupGData = new GridData(GridData.FILL_HORIZONTAL);
		
		Group testContentsGroup = new Group(control, SWT.None);
		testContentsGroup.setLayout(new GridLayout(2, false));
		testContentsGroup.setLayoutData(testContentsGroupGData);
		testContentsGroup.setText(BatchTestMessages.BatchJobTestCustomizePage_SELECT_TEST_TYPE);
		
		createTestTypeControl(testContentsGroup);
		
		GridData contentsGroupGData = new GridData(GridData.FILL_HORIZONTAL);
		contentsGroupGData.heightHint = 320;
		
		contentsGroup = new Group(control, SWT.None);
		contentsGroup.setLayout(new GridLayout(2, false));
		contentsGroup.setLayoutData(contentsGroupGData);
		contentsGroup.setText(BatchTestMessages.BatchJobTestCustomizePage_NEW_TEST_GROUP);
		
		createNewContentsControl(contentsGroup);
		
		createTestButtons(control);
		
		createTestResult(control);

		setControl(control);
	}
	
	/**
	 * Test Type Control 생성
	 * 
	 * @param control
	 */
	private void createTestTypeControl(Composite control){
		Composite testTypeControl = new Composite(control, SWT.None);
		testTypeControl.setLayout(new GridLayout(2, true));
		testTypeControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		GridData gData = new GridData();
		gData.widthHint = 150;
		
		newTestButton = new Button(testTypeControl, SWT.RADIO);
		newTestButton.setText(BatchTestMessages.BatchJobTestCustomizePage_NEW_TEST_RADIO_BUTTON);
		newTestButton.setLayoutData(gData);
		newTestButton.setSelection(true);
		newTestButton.addListener(SWT.Selection, new Listener() {
			
			public void handleEvent(Event event) {
				if(newTestButton.getSelection()){
					clearContentsGroup();
					
					setErrorMessage(null);
					
					createNewContentsControl(contentsGroup);
					
					testButton.setEnabled(false);
				}
			}
		});
		
		reuseTestButton = new Button(testTypeControl, SWT.RADIO);
		reuseTestButton.setText(BatchTestMessages.BatchJobTestCustomizePage_REUSE_TEST_RADIO_BUTTON);
		reuseTestButton.addListener(SWT.Selection, new Listener() {
			
			public void handleEvent(Event event) {
				if(reuseTestButton.getSelection()){
					clearContentsGroup();
					
					setErrorMessage(null);
					
					createReuseContentsControl(contentsGroup);
					
					testButton.setEnabled(false);
				}
			}
		});
	}
	
	/** ContentsGroup내 항목 제거 */
	private void clearContentsGroup(){
		Control[] children = contentsGroup.getChildren();
		
		if(!NullUtil.isEmpty(children)){
			for(Control child : children){
				child.dispose();
			}
		}
	}
	
	/**
	 * New Radio Button선택시 Contents내 항목 생성
	 * 
	 * @param contentsGroup
	 */
	private void createNewContentsControl(Group contentsGroup){
		contentsGroup.setText(BatchTestMessages.BatchJobTestCustomizePage_NEW_TEST_GROUP);
		
		createJobList(contentsGroup);
		
		createJobLauncher(contentsGroup);
		
		createJobParameterList(contentsGroup);
		
		createGenerateFileButtons(contentsGroup);
		
		setNewTestItemStatus();
		
		contentsGroup.getParent().layout(true, true);
	}
	
	/** New Contents Control 생성시 초기값 및 Listener와 같은 상태 설정 */
	private void setNewTestItemStatus(){
		setInputTotableViewers();
		
		setJobAndLauncherStatus();
	}
	
	/** JobList, JobLauncherTableViewer에 Input값 설정 */
	private void setInputTotableViewers(){
		jobList.setInput(makeJobVoArray());
		
		jobLauncherTableViewer.setInput(makeJobExecVoArray());
	}
	
	/** JobList, JobLauncherTableViewer의 Listener 및 상태 설정 */
	private void setJobAndLauncherStatus(){
		TableItem[] jobListItems = jobList.getTable().getItems();
		TableItem[] jobLauncherListItems = jobLauncherTableViewer.getTable().getItems();
		if(NullUtil.isEmpty(jobListItems)){
			jobList.removeSelectionChangedListener(validation);
			jobLauncherTableViewer.removeSelectionChangedListener(validation);
			addJobParameterButton.setEnabled(false);
			setErrorMessage(BatchTestMessages.BatchJobTestCustomizePage_EMPTY_JOB_LIST);
			
		}else if(NullUtil.isEmpty(jobLauncherListItems)){
			jobList.removeSelectionChangedListener(validation);
			jobLauncherTableViewer.removeSelectionChangedListener(validation);
			addJobParameterButton.setEnabled(false);
			setErrorMessage(BatchTestMessages.BatchJobTestCustomizePage_EMPTY_JOB_LAUNCHER);
			
		}else if(isJobTestAble){
			jobList.removeSelectionChangedListener(validation);
			jobLauncherTableViewer.removeSelectionChangedListener(validation);
			addJobParameterButton.setEnabled(false);
			setErrorMessage(BatchTestMessages.BatchJobTestCustomizePage_DUPLICATE_BEAN_ID);
			
		}else{
			jobList.addSelectionChangedListener(validation);
			jobLauncherTableViewer.addSelectionChangedListener(validation);
			addJobParameterButton.setEnabled(true);
			setErrorMessage(null);
			
		}
	}
	
	/**
	 * Reuse Contents Control 생성
	 * 
	 * @param contentsGroup
	 */
	private void createReuseContentsControl(Group contentsGroup){
		contentsGroup.setText(BatchTestMessages.BatchJobTestCustomizePage_REUSE_TEST_GROUP);
		
		String[] columnNames = new String[]{"TestFile", "Job", "JobLauncher", "JobParameters"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		int[] columnAlignment = new int[]{SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER};
		int[] columnWidth = new int[]{120, 100, 100, 260};
		
		testFilesTableViewer = new TableViewer(contentsGroup, SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		testFilesTableViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Table table = testFilesTableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		for(int i = 0; i < columnNames.length; i++){
			TableColumn column = new TableColumn(table, columnAlignment[i]);
			column.setText(columnNames[i]);
			column.setWidth(columnWidth[i]);
		}
		
		contentsGroup.getParent().layout(true, true);
		
		testFilesTableViewer.setContentProvider(new ArrayContentProvider());
		testFilesTableViewer.setLabelProvider(new JobTestFilesListLabelProvider());
		testFilesTableViewer.setInput(getExistTestFile());
		testFilesTableViewer.getTable().addListener(SWT.MouseUp, new Listener() {
			public void handleEvent(Event event) {

				IStructuredSelection reuseTestSelection = (IStructuredSelection)testFilesTableViewer.getSelection();
				if (!NullUtil.isNull(reuseTestSelection) && reuseTestSelection.size() > 0) {
					String filename = reuseTestSelection.getFirstElement().toString().split("::")[0]; //$NON-NLS-1$
					if(!NullUtil.isNull(testJavaFiles)){
						for(int i =0; i < testJavaFiles.keySet().toArray().length; i++){
							if(testJavaFiles.keySet().toArray()[i].equals(filename)){
								IFile testFile = testJavaFiles.get(testJavaFiles.keySet().toArray()[i]);
								context.setJobTestFile(testFile.getFullPath().toOSString());
								
								for(int j = 0; j < testFileJobName.size(); j++){
									if(testFileJobName.get(j).contains(testFile.toString())){
										String[] testJobNameSplit = testFileJobName.get(j).split("::"); //$NON-NLS-1$
										messageJobName = testJobNameSplit[testJobNameSplit.length-1];
										break;
									}
								}
								break;
							}
						}
					}
					
					String jobfilename = reuseTestSelection.getFirstElement().toString().split("::")[1]; //$NON-NLS-1$
					if(!NullUtil.isNull(jobfilename))
					{
						String[] tempJob = jobfilename.split("/"); //$NON-NLS-1$
						if(!NullUtil.isNull(tempJob)){
							context.setJobVo(new JobVo(tempJob[tempJob.length-1].toString(), false, null, null, null));
						}
					}

					String launcherfilename = reuseTestSelection.getFirstElement().toString().split("::")[2]; //$NON-NLS-1$
					if(!NullUtil.isNull(launcherfilename))
					{
						String[] tempJobLauncher = launcherfilename.split("/"); //$NON-NLS-1$
						if(!NullUtil.isNull(tempJobLauncher)){
						context.setJobExecName(tempJobLauncher[tempJobLauncher.length-1].toString());
						}
					}
					String param = reuseTestSelection.getFirstElement().toString().split("::")[3]; //$NON-NLS-1$
					if(param.contains("String_")){ //$NON-NLS-1$
						param = param.replace("String_", ""); //$NON-NLS-1$ //$NON-NLS-2$
					}
					if(param.contains("Long_")){ //$NON-NLS-1$
						param = param.replace("Long_", ""); //$NON-NLS-1$ //$NON-NLS-2$
					}
					if(param.contains("Double_")){ //$NON-NLS-1$
						param = param.replace("Double_", ""); //$NON-NLS-1$ //$NON-NLS-2$
					}
					if(param.contains("Date_")){ //$NON-NLS-1$
						param = param.replace("Date_", ""); //$NON-NLS-1$ //$NON-NLS-2$
					}

					messageParam = param;
					
					testButton.setEnabled(true);
				}

			}
		});
		
	}
	
	/**
	 * Project Name의 Composite 생성
	 * 
	 * @param control
	 */
	private void createProjectNameLabel(Composite control){		
		Composite labelControl = new Composite(control, SWT.None);
		labelControl.setLayout(new GridLayout(2, false));
		labelControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label projectNameLabel = new Label(labelControl, SWT.None);
		projectNameLabel.setText(BatchTestMessages.BatchJobTestCustomizePage_PROJECT_NAME_LABEL);
		
		projectName = new Label(labelControl, SWT.None);
		StringUtil.setLabelStringBold(projectName);
	}
	
	/**
	 * Project가 가지는 Job의 List Composite 생성
	 * 
	 * @param control
	 */
	private void createJobList(Composite control){
		
		GridData gData = new GridData();
		gData.widthHint = 140;
		gData.heightHint = 255;
		gData.verticalSpan = 2;
		
		Group listControl = new Group(control, SWT.None);
		listControl.setLayout(new GridLayout());
		listControl.setLayoutData(gData);
		listControl.setText(BatchTestMessages.BatchJobTestCustomizePage_SELECT_JOB_GROUP);
		
		jobList = new TableViewer(listControl, SWT.FILL | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE);
		jobList.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		
		jobList.setContentProvider(new ArrayContentProvider());
		jobList.setLabelProvider(new JobListLabelProvider());
	}
	
	/**
	 * Job Parameter의 TableViewer 생성
	 * 
	 * @param control
	 */
	private void createJobParameterList(Composite control){
		GridData gData = new GridData(GridData.FILL_HORIZONTAL);
		gData.heightHint = 100;
		
		Group jobParameterListControl = new Group(control, SWT.None);
		jobParameterListControl.setLayout(new GridLayout(2, false));
		jobParameterListControl.setLayoutData(gData);
		jobParameterListControl.setText(BatchTestMessages.BatchJobTestCustomizePage_JOB_PARAMETER_GROUP);
		
		jobParameterList = new TableViewer(jobParameterListControl, SWT.BORDER
				| SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);

		Table table = jobParameterList.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		setJobParameterTableColumns(table);

		jobParameterList.setContentProvider(new ArrayContentProvider());
		jobParameterList.setLabelProvider(new JobParameterLabelProvider());
		jobParameterList.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		jobParameterList.getTable().addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				removeJobParameterButton.setEnabled(true);
			}
		});
		
		GridLayout gLayout = new GridLayout();
		gLayout.marginWidth = 0;
		
		Composite buttonControl = new Composite(jobParameterListControl, SWT.None);
		buttonControl.setLayout(gLayout);
		buttonControl.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_CENTER));

		addJobParameterButton = new Button(buttonControl, SWT.PUSH);
		addJobParameterButton.setText(BatchTestMessages.BatchJobTestCustomizePage_ADD_JOB_PARAMETER_BUTTON);
		addJobParameterButton.addListener(SWT.MouseUp, addJobParameterListener);
		addJobParameterButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		removeJobParameterButton = new Button(buttonControl, SWT.PUSH);
		removeJobParameterButton.setText(BatchTestMessages.BatchJobTestCustomizePage_REMOVE_JOB_PARAMETER_BUTTON);
		removeJobParameterButton.setEnabled(false);
		removeJobParameterButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		removeJobParameterButton.addListener(SWT.MouseUp, new Listener() {

			public void handleEvent(Event event) {
				IStructuredSelection selection = (IStructuredSelection) jobParameterList
						.getSelection();

				if (!NullUtil.isNull(selection) && selection.size() > 0) {
					jobParameterList.remove(selection.toArray());
				}
				removeJobParameterButton.setEnabled(false);
			}
		});
	}
	
	/**
	 * Job Parameter의 TableViewer Column 생성
	 * 
	 * @param table
	 */
	private void setJobParameterTableColumns(Table table){
		BatchTableColumn name = new BatchTableColumn("Name", 100); //$NON-NLS-1$
		name.setColumnToTable(table);
		
		BatchTableColumn value = new BatchTableColumn("Value", 100); //$NON-NLS-1$
		value.setColumnToTable(table);
		
		BatchTableColumn dataType = new BatchTableColumn("Data Type", 80); //$NON-NLS-1$
		dataType.setColumnToTable(table);
	}
	
	/**
	 * Job Launcher Control 생성
	 * 
	 * @param control
	 */
	private void createJobLauncher(Composite control){
		Group jobExecutorGroup = new Group(control, SWT.None);
		jobExecutorGroup.setLayout(new GridLayout());
		jobExecutorGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		jobExecutorGroup.setText(BatchTestMessages.BatchJobTestCustomizePage_JOB_EXECUTOR_GROUP);
		
		GridData gData = new GridData(GridData.FILL_HORIZONTAL);
		gData.heightHint = 100;
		gData.widthHint = 300;
		
		jobLauncherTableViewer = new TableViewer(jobExecutorGroup, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.FULL_SELECTION);
		jobLauncherTableViewer.getControl().setLayoutData(gData);
		
		Table table = jobLauncherTableViewer.getTable();
		table.setLinesVisible(false);
		table.setHeaderVisible(true);
		
		setJobLauncherTableColumns(table);

		jobLauncherTableViewer.setContentProvider(new ArrayContentProvider());
		jobLauncherTableViewer.setLabelProvider(new JobLauncherListLabelProvider());
	}
	
	/**
	 * Job Launcher의 TableViewer Column 생성
	 * 
	 * @param table
	 */
	private void setJobLauncherTableColumns(Table table){
		BatchTableColumn fileName = new BatchTableColumn("File Name", 180); //$NON-NLS-1$
		fileName.setColumnToTable(table);
		
		BatchTableColumn fileLocation = new BatchTableColumn("File Location", 350); //$NON-NLS-1$
		fileLocation.setColumnToTable(table);
	}
	
	/**
	 * Generate File Button Control 생성
	 * 
	 * @param control
	 */
	private void createGenerateFileButtons(Composite control){
		GridData gData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		gData.horizontalSpan = 2;
		
		Composite buttonControl = new Composite(control, SWT.None);
		buttonControl.setLayout(new GridLayout());
		buttonControl.setLayoutData(gData);
		
		generateFileButton = new Button(buttonControl, SWT.PUSH);
		generateFileButton.setText(BatchTestMessages.BatchJobTestCustomizePage_GENERATE_FILE_BUTTON);
		generateFileButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		generateFileButton.addListener(SWT.Selection, generateFileButtonListener);
		generateFileButton.setEnabled(false);
	}
	
	/**
	 * Test Button 생성 및 Listener 할당
	 * 
	 * @param control
	 */
	private void createTestButtons(Composite control){
		Composite buttonControl = new Composite(control, SWT.None);
		buttonControl.setLayout(new GridLayout());
		buttonControl.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		
		GridData gData = new GridData();
		gData.widthHint = 100;
		
		testButton = new Button(buttonControl, SWT.PUSH);
		testButton.setText(BatchTestMessages.BatchJobTestCustomizePage_TEST_BUTTON);
		testButton.setLayoutData(gData);
		testButton.setEnabled(false);
		testButton.addListener(SWT.Selection, testButtonListener);
	}
	
	/**
	 * Test Result Control 생성
	 * 
	 * @param control
	 */
	private void createTestResult(Composite control) {
		Group testResultGroup = new Group(control, SWT.None);
		testResultGroup.setLayout(new GridLayout(2, false));
		testResultGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		testResultGroup.setText(BatchTestMessages.BatchJobTestCustomizePage_RESULT_GROUP);
		
		resultConsoleText = new Text(testResultGroup, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		resultConsoleText.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		signalLight = new Label(testResultGroup, SWT.None);
		signalLight.setImage(EgovBatchTestPlugin.getDefault().getImage(EgovBatchTestPlugin.IMG_BATCH_TST_RESULT_READY));
		
		//JUnitCore에 Listener추가
		JUnitCore.addTestRunListener(tListener);
	}
	
	@Override
	public void setVisible(boolean visible) {
		if(visible){
			getShell().setSize(size);
			
			FindFilesinProjectUtil.findXMLFiles(context, context.getSelection(), "<job"); //$NON-NLS-1$
			FindFilesinProjectUtil.findXMLFiles(context, context.getSelection(), "core.launch.support.*Launcher"); //$NON-NLS-1$
			
			newTestButton.setSelection(true);
			reuseTestButton.setSelection(false);
			newTestButton.notifyListeners(SWT.Selection, null);
			
			isJobTestAble = false;

			projectName.setText(context.getProjectName());
			setPageComplete(true);
			
			checkRepositoryDatasourceID();
			checkTransactionManagerID();
			
			getControl().getParent().layout(true, true);
		}
		super.setVisible(visible);
	}
	
	/** Project에 있는 Job을 가져와 Array에 저장 */
	private JobVo[] makeJobVoArray(){
		
		ArrayList<String> jobIDList = new ArrayList<String>();
		
		Set<String> foundFileskey = context.getFoundJobXMLfiles().keySet();
		Object[] keyArray = foundFileskey.toArray();
		IFile foundFile;
		NodeList jobList;
		String jobId;
		
		for(int i = 0; i < context.getFoundJobXMLfiles().size(); i++){
			foundFile = context.getFoundJobXMLfiles().get( (String)keyArray[i]);
			try {
				
				jobList = XmlUtil.getNodeList(XmlUtil.getRootNode(foundFile.getLocation().toOSString()), "/beans/job"); //$NON-NLS-1$
				for(int j = 0; jobList != null && j < jobList.getLength(); j++){
					jobId = jobList.item(j).getAttributes().getNamedItem("id").getNodeValue(); //$NON-NLS-1$

					if(jobIDList.size() != 0){
						for(int m = 0; m < jobIDList.size(); m++){
							if(jobIDList.get(m).contentEquals(jobId)){
								isJobTestAble = true;
								break;
							}
						}
					}
					jobIDList.add(jobId);
					jobIDExistFileList.put(jobId, foundFile.toString());
				}
			} catch (Exception e) {
				BatchTestLog.logError(e);
			}
		}

		if(jobIDList.size() != 0){
			ArrayList<String> sortedJobIDList = new ArrayList<String>();

			for(int i = 0; i<jobIDList.size(); i++){
				sortedJobIDList.add(jobIDList.get(i));
			}
			Collections.sort(sortedJobIDList);

			JobVo[] listOfJobs = new JobVo[sortedJobIDList.size()];

			for(int i = 0; i < sortedJobIDList.size(); i++){

				listOfJobs[i] = new JobVo();
				listOfJobs[i].setJobName(sortedJobIDList.get(i));
			}

			return listOfJobs;
		} else {
			return null;
		}
	}
	
	
	/** Project에 있는 JobExec을 가져와 Array에 저장 */
	private String[] makeJobExecVoArray(){
		
		ArrayList<String> jobExecFileList = new ArrayList<String>();

		Set<String> foundFileskey = context.getFoundJobExecXMLfiles().keySet();
		Object[] keyArray = foundFileskey.toArray();

		for(int i = 0; i < context.getFoundJobExecXMLfiles().size(); i++){
			String key = (String)keyArray[i];
			IFile foundFile = context.getFoundJobExecXMLfiles().get(key);
			String foundFileString = foundFile.toString();
			jobExecFileList.add(foundFileString.substring(foundFileString.indexOf(context.getProjectName())+context.getProjectName().length(), foundFileString.length()));
		}

		if(jobExecFileList.size() != 0){
			String[] listOfJobs = new String[jobExecFileList.size()];

			for(int i = 0; i < jobExecFileList.size(); i++){

				listOfJobs[i] = new String();
				listOfJobs[i] = jobExecFileList.get(i);
			}

			return listOfJobs;
		} else {
			return null;
		}
	}
	
	/** Test Button 클릭시 메세지 생성 */
	private String createTestMessage(){
		String foundFileString = context.getJobExecName();
		String jobLauncherName = foundFileString.substring(foundFileString.lastIndexOf("/")+1, foundFileString.length()); //$NON-NLS-1$
		String message = "현재 설정된 정보는 다음과 같습니다.\n\n"; //$NON-NLS-1$
		message += ("\n"); //$NON-NLS-1$
		message += ("- [Job Name] : "+messageJobName+"\n"); //$NON-NLS-1$ //$NON-NLS-2$
		message += "- [Job Launcher] : "+ jobLauncherName +"\n"; //$NON-NLS-1$ //$NON-NLS-2$
		message += "- [Job Parameter(s)] : "; //$NON-NLS-1$
		if(jobParameterList  != null && newTestButton.getSelection()) {
			TableItem[] items = jobParameterList.getTable().getItems();
			if(items.length < 1){
				message += "timestamp 파라메터 사용\n"; //$NON-NLS-1$
			}else{
				for(int i = 0; i < items.length - 1; i++){
					JobParameterInfo jobPI = (JobParameterInfo)items[i].getData();
					message += (jobPI.getParameterName()+", "); //$NON-NLS-1$
				}
				JobParameterInfo jobPI = (JobParameterInfo)items[items.length-1].getData();
				message += (jobPI.getParameterName()+"\n");	 //$NON-NLS-1$
			}
		} else {
			
			message += messageParam + "\n"; //$NON-NLS-1$
		}
		message += ("\n"); //$NON-NLS-1$
		message += "\n주의 : 운영환경의 데이터를 이용하는 경우 실데이터 반영으로 실제 업무에 영향을 줄 수 있습니다.\n\n계속 진행 하시겠습니까?"; //$NON-NLS-1$
		
		return message;
	}
	
	/** 선택한 프로젝트에서 jobRepository, datasource ID 들을 체크*/
	private void checkRepositoryDatasourceID(){
		
		if(context.getFoundJobExecXMLfiles() != null){
			//찾은 launcher 파일내에서 jobRepository ID를 가진 파일 검색
			Map<String, IFile> beanIDList = new LinkedHashMap<String, IFile>();
			beanIDList = FindBeanIdValueUtil.findBeanIDList(context.getFoundJobExecXMLfiles(), "/beans/bean", "jobRepository", "id"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			
			if(beanIDList != null && beanIDList.size() > 0){
				context.setJobRepositoryXMLFile(beanIDList.get(beanIDList.keySet().toArray()[0]));
				//찾은 launcher 파일내에서 jobRepositoryID 하위의 datasource ID를 가져오기
				Map<String, IFile> findDatasourceIDfile = new LinkedHashMap<String, IFile>();
				findDatasourceIDfile = FindBeanIdValueUtil.findBeanIDList(beanIDList, "/beans/bean", null, "p:dataSource-ref"); //$NON-NLS-1$ //$NON-NLS-2$
				
				if(findDatasourceIDfile != null && findDatasourceIDfile.size() > 0){
					Map<String, IFile> foundDatasourceIDfile = new LinkedHashMap<String, IFile>();
					foundDatasourceIDfile = FindBeanIdValueUtil.findBeanIDList(FindFilesinProjectUtil.findXMLFiles(context, "bean", "*.xml", true), "/beans/bean", (String)findDatasourceIDfile.keySet().toArray()[0], "id"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				
					if(foundDatasourceIDfile != null && foundDatasourceIDfile.size() > 0){
						context.setDatasourceXMLFile(foundDatasourceIDfile.get(foundDatasourceIDfile.keySet().toArray()[0]));
					} else {
					
						//alias 찾아주기
						foundDatasourceIDfile = FindBeanIdValueUtil.findBeanIDList(FindFilesinProjectUtil.findXMLFiles(context, "bean", "*.xml", true), "/beans/alias", (String)findDatasourceIDfile.keySet().toArray()[0], "alias"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
						
						if(foundDatasourceIDfile != null && foundDatasourceIDfile.size() > 0){
							context.setDatasourceXMLFile(foundDatasourceIDfile.get(foundDatasourceIDfile.keySet().toArray()[0]));
						}
					}
				}
			}
		}
	}
	
	/** 선택한 프로젝트에서 transactionManager ID 를 체크*/
	private void checkTransactionManagerID(){
		
		if(context.getFoundJobExecXMLfiles() != null){
			Map<String, IFile> foundTransactionManagerFile = FindFilesinProjectUtil.findXMLFiles(context, "bean id=\"transactionManager\"", "*.xml", true); //$NON-NLS-1$ //$NON-NLS-2$
			if(!NullUtil.isNull(foundTransactionManagerFile) && foundTransactionManagerFile.keySet().size() != 0){
				context.setTransactionManagerXMLFile(foundTransactionManagerFile.get(foundTransactionManagerFile.keySet().toArray()[0]));
			}
		}
	}
	
	/** Job Parameter TableViewer의 Add Button Listener */
	Listener addJobParameterListener = new Listener() {
		
		public void handleEvent(Event event) {
			
			TableItem[] items = jobParameterList.getTable().getItems();
			
			JobParameterInfoToWizardDialog dialog = new JobParameterInfoToWizardDialog(getShell(), items);
			if(dialog.open() == Window.OK){
				ArrayList<JobParameterInfo> data = dialog.getInfo();
				jobParameterList.setInput(data.toArray());
				items = jobParameterList.getTable().getItems();
				JobParameterInfo[] paramInfo = new JobParameterInfo[items.length];
				
				if (items.length > 0) {
					for (int i = 0; i < items.length; i++) {
						paramInfo[i] = new JobParameterInfo();
						paramInfo[i] = (JobParameterInfo) items[i].getData();
					}
				}
				context.setJobParameterList(paramInfo);
			}
			
			removeJobParameterButton.setEnabled(false);
		}
	};

	/** Job List TableViewer 클릭시 Listener */
	ISelectionChangedListener validation = new ISelectionChangedListener() {
		public void selectionChanged(SelectionChangedEvent event) {
			
			generateFileButton.setEnabled(false);
			
			IStructuredSelection jobSelection = (IStructuredSelection)jobList.getSelection();			
			if(jobSelection.isEmpty()){
				setErrorMessage(BatchTestMessages.BatchJobTestCustomizePage_EMPTY_JOB_SELECTION);
				return;
			}else{
				context.setJobVo((JobVo)jobSelection.getFirstElement());
			}
		
			IStructuredSelection jobExecutorSelection = (IStructuredSelection)jobLauncherTableViewer.getSelection();
			if(jobExecutorSelection.isEmpty()){
				setErrorMessage(BatchTestMessages.BatchJobTestCustomizePage_EMPTY_JOB_LAUNCHER_SELECTION);
				return;
			}else{
				context.setJobExecName(jobExecutorSelection.getFirstElement().toString());
			}
			
			generateFileButton.setEnabled(true);
			
			setErrorMessage(null);
		}
	};
	
	/** Test Button의 Listener */
	Listener testButtonListener = new Listener() {
		
		public void handleEvent(Event event) {			
			if(!MessageDialog.openQuestion(getShell(), BatchTestMessages.BatchJobTestCustomizePage_TEST_BUTTON_CONFIRM_DIALOG_TITLE, createTestMessage())){
				return;
			}
			
			testButton.setEnabled(false);
			if(!generateFileButton.isDisposed()){
				generateFileButton.setEnabled(false);
			}

			IFile newXMLFile = null;
			
			//context에 newJunitTestFile(생성한 파일)의 위치를 저장
//			Path path = new Path("/spring-batch-samples/src/test/java/org/springframework/batch/sample/JobStepFunctionalTests.java");
//			Path path = new Path("4TEST.1/src/test/java/EgovBatchJobRunTester.java");
			Path path = new Path(context.getJobTestFile());
			newXMLFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);

			Object ob = newXMLFile;
			
			JUnitLaunchShortcut junitShortcut = new JUnitLaunchShortcut();
			junitShortcut.launch(new StructuredSelection(ob), ILaunchManager.RUN_MODE);
			
			if(getShell()!= null) {

				signalLight.setImage(EgovBatchTestPlugin.getDefault().getImage(EgovBatchTestPlugin.IMG_BATCH_TST_RESULT_WAIT));
				Display display = getShell().getDisplay();
				result = "<eGovFramework Batch Test>\nBatch Execution Status .......................................................................... [ Wait ]"; //$NON-NLS-1$
			    display.syncExec( 
			      new Runnable() { 
			       public void run(){ 
			    	   resultConsoleText.setText(result);
			       } 
			      }
			    );
			}
		}
	};
	
	/** TestRunListener의 Method override */
	TestRunListener tListener = new TestRunListener() {
		
		@Override
		public void testCaseStarted(ITestCaseElement testCaseElement) {
			if(getShell()!= null) {
				Display display = getShell().getDisplay();
				result = "<eGovFramework Batch Test>\nBatch Execution Status .......................................................................... [ Started ]"; //$NON-NLS-1$
			    display.syncExec( 
			      new Runnable() { 
			       public void run(){ 
			    	   signalLight.setImage(EgovBatchTestPlugin.getDefault().getImage(EgovBatchTestPlugin.IMG_BATCH_TST_RESULT_START));
			    	   resultConsoleText.setText(result);
			       } 
			      }
			    );
			}
		    super.testCaseStarted(testCaseElement);
		}	
		
		
		@Override
		public void testCaseFinished(ITestCaseElement testCaseElement) {
			super.testCaseFinished(testCaseElement);
			
			if(getShell() != null) {
				if(testCaseElement.getFailureTrace() != null){
					result = "<eGovFramework Batch Test>\nBatch Execution Status .......................................................................... [ "+ testCaseElement.getTestResult(true).toString()+" ]" + "\n\nLog : " + testCaseElement.getFailureTrace().getTrace(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				} else {
					result = "<eGovFramework Batch Test>\nBatch Execution Status .......................................................................... [ "+ testCaseElement.getTestResult(true).toString()+" ]"; //$NON-NLS-1$ //$NON-NLS-2$
				}
			    Display display = getShell().getDisplay();
			    display.syncExec( 
			      new Runnable() { 
			       public void run(){ 
			    	   if(result.contains("Error")){ //$NON-NLS-1$
			    		   signalLight.setImage(EgovBatchTestPlugin.getDefault().getImage(EgovBatchTestPlugin.IMG_BATCH_TST_RESULT_FAILED));
			    	   } else {
			    		   signalLight.setImage(EgovBatchTestPlugin.getDefault().getImage(EgovBatchTestPlugin.IMG_BATCH_TST_RESULT_SUCCESS));
			    	   }
			    	   resultConsoleText.setText(result);
			    	   
			    	   testButton.setEnabled(true);
			    	   if(!generateFileButton.isDisposed()){
			    		   generateFileButton.setEnabled(true);
			    	   }
			       } 
			      }
			    );
			    
			} else {
				JUnitCore.removeTestRunListener(tListener);

				if(!testButton.isDisposed()){
					testButton.setEnabled(true);
				}
				if(!generateFileButton.isDisposed()){
					generateFileButton.setEnabled(true);
				}

			}
		}
	};
	
	/** GenerateFile Button의 Listener */
	Listener generateFileButtonListener = new Listener() {
		
		public void handleEvent(Event event) {
			IStructuredSelection selection = context.getSelection();
			Object selectObject = selection.getFirstElement();
			IResource selectResource = (IResource)selectObject;
			
			
			GenerateFileDialog dialog = new GenerateFileDialog(getShell(), context.getFolderPath(), context.getFileName(), selectResource.getProject());
			openGenTestFileDialog(dialog);
		}
	};
	
	/**
	 * Generate Test File하는 다이얼로그
	 * 
	 * @param dialog
	 * @throws UnsupportedEncodingException 
	 */
	private void openGenTestFileDialog(GenerateFileDialog dialog) {
		
		checkIsAsync();
		if(dialog.open() == Window.OK){
			context.setFolderPath(dialog.getFolderPath());
			context.setFileName(dialog.getFileName());
			
			/**
			 * context 안에 필요한 항목
			 * :: 폴더 지정 Path, 지정한 파일명, 사용할 JobParameter, 선택한 Execution파일경로, 선택한 잡파일 경로 
			 * */
			if(GenerateJobTestFileUtil.GenerateJobTestFile(context, jobIDExistFileList)){
				messageJobName = context.getJobVo().getJobName();
				
				testButton.setEnabled(true);
				setErrorMessage(null);
				MessageDialog.openInformation(getShell(), " eGovFrame Batch Job Test", "Test File has been created.\n\n- File Name: "+context.getFileName()+"\n- File Path: "+context.getFolderPath());				
			}else{
				testButton.setEnabled(false);
				MessageDialog.openError(getShell(), " eGovFrame Batch Job Test", "Please, Re-Confirm your File Name or File Path\n\n- File Name: "+context.getFileName()+"\n- File Path: "+context.getFolderPath());
				openGenTestFileDialog(dialog);
			}
			
		}
		
	}
	
	/** 선택한 Launcher 파일의 비동기 설정여부 체크*/
	private void checkIsAsync(){
		
		IFile selectedLauncherFile = null;
		for(int i=0; i < context.getFoundJobExecXMLfiles().size(); i++){
			Object[] key = context.getFoundJobExecXMLfiles().keySet().toArray();
			
			for(int j= 0; j < key.length; j ++){
				if(key[j].toString().contains(context.getJobExecName())){
					selectedLauncherFile = context.getFoundJobExecXMLfiles().get(key[j]);
					break;
				}
			}
		}
		
		Map<String, IFile> beanIDList = new LinkedHashMap<String, IFile>();
		beanIDList = FindBeanIdValueUtil.findBeanIDList(selectedLauncherFile, "/beans/bean", "org.springframework.core.task.SimpleAsyncTaskExecutor", "class"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		if(beanIDList == null){
			beanIDList = FindBeanIdValueUtil.findBeanIDList(selectedLauncherFile, "/beans/bean/property/bean", "org.springframework.core.task.SimpleAsyncTaskExecutor", "class"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		if(beanIDList != null){
			context.setIsAsync(true);
		}
	}
	
	/**
	 * 프로젝트내 생성된 test 파일을 재사용할 경우 목록 구성
	 * 
	 * @return realTemp
	 */
	private String[] getExistTestFile(){
		
		Map<String, IFile>  foundJavaFiles = FindFilesinProjectUtil.findXMLFiles(context, "@RunWith(SpringJUnit4ClassRunner.class)", "*.java", false); //$NON-NLS-1$ //$NON-NLS-2$
		
		ArrayList<String> resultFile = new ArrayList<String>();

		if(foundJavaFiles !=null){

			String[] realTemp;
			for(int i = 0; i < foundJavaFiles.keySet().toArray().length; i++){
				IFile testFile = foundJavaFiles.get(foundJavaFiles.keySet().toArray()[i]);
				String foundContents = ""; //$NON-NLS-1$
				try {
					InputStream input = testFile.getContents();
					BufferedReader in = new BufferedReader(new InputStreamReader(input));
					String inputLine;

					while ((inputLine = in.readLine()) != null) {
						//reuse tableviewer 채우기위한 test파일내 주석탐색
						if(inputLine.length() != 0 && inputLine.contains("Test File Information")){ //$NON-NLS-1$
							foundContents = testFile.getName() + "::\n"; //$NON-NLS-1$
							while(!inputLine.contains(" */")){ //$NON-NLS-1$
								inputLine = in.readLine();
								foundContents += inputLine.trim() + "::\n"; //$NON-NLS-1$
							}
							testJavaFiles.put(testFile.getName(), testFile);
						}
						//test파일내 등록된 Job name 탐색
						if(inputLine.length() != 0 && inputLine.contains("String jobName")){ //$NON-NLS-1$
							String[] jobNameSplit = inputLine.trim().split("\""); //$NON-NLS-1$
							if(!NullUtil.isNull(jobNameSplit) && jobNameSplit.length >= 2){
								testFileJobName.add(i, testFile + "::" + jobNameSplit[jobNameSplit.length-2]); //$NON-NLS-1$
							}
						}
					}
					in.close();
					if(foundContents != null && foundContents.length() > 0){
						resultFile.add(foundContents);
					}
					
				} catch (CoreException e) {
					BatchTestLog.logError(e);
				} catch (IOException e) {
					BatchTestLog.logError(e);
				}
			}

			realTemp = new String[resultFile.size()];
			for(int i = 0; i< resultFile.size(); i++){
				if(resultFile.size() > 0 && resultFile.get(i).contains("::")){ //$NON-NLS-1$
					String[] temp = resultFile.get(i).split("::"); //$NON-NLS-1$
					String forSplit = ""; //$NON-NLS-1$

					if(temp != null){
						for(int j = 0; j< temp.length; j++){
							if(temp[j].contains(".xml") || temp[j].contains(".java") || //$NON-NLS-1$ //$NON-NLS-2$
									temp[j].contains("Long") || temp[j].contains("Date") || temp[j].contains("Double") || temp[j].contains("String")){ //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
								forSplit += temp[j] + "::";  //$NON-NLS-1$
							}
						}
					}

					realTemp[i] = new String();
					Boolean isExist = false;
					for(int k = 0; k < realTemp.length; k++){
						if(realTemp[k] != null && realTemp[k].equals(forSplit)){
							isExist = true;
						}
					}
					if(!isExist){
						realTemp[i] = forSplit;
					}
				}
			}
			if(realTemp != null && realTemp.length > 0 && realTemp[0] != null){ 
				return realTemp;
			} 
		}
		return null;
	}
}