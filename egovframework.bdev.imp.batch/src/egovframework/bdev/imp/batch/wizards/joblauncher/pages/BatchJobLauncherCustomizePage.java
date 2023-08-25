package egovframework.bdev.imp.batch.wizards.joblauncher.pages;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.jface.window.Window;
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
import org.eclipse.swt.widgets.Text;

import egovframework.bdev.imp.batch.common.BatchMessages;
import egovframework.bdev.imp.batch.wizards.com.FindXMLFileBeanIdValueUtil;
import egovframework.bdev.imp.batch.wizards.joblauncher.model.BatchJobLauncherContext;
import egovframework.dev.imp.core.common.DataToolsPlatformUtil;
import egovframework.dev.imp.core.utils.NullUtil;
import egovframework.dev.imp.core.utils.StringUtil;

/**
 * Batch Job Execution 생성 및 속성을 정의하는 Page
 * 
 * @author 배치개발환경 개발팀 조용현
 * @since 2012.06.28
 * @version 1.0
 * @see
 * 
 *      <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *수정일	  	수정자	  수정내용
 *-----------	------	----------------
 *2012.07.24	조용현	최초생성
 *
 * 
 *      </pre>
 */
public class BatchJobLauncherCustomizePage extends WizardPage {
	/** Database Type */
	private static String[] dataTypes = { "mySql", "Oracle", "Altibase", "Tibero", "hsql" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

	/** EgovNewBatchJobExecutionWizard의 context */
	private BatchJobLauncherContext context;

	/** 기존에 JobRepository가 있는지 검사(T:DB,Memory 비활성화) */
	private boolean isJobRepositoryIDExist;

	/** 기존에 driverclass설정을 가진 datasourceREF 설정이 있는지 검사(T:DB(Ref)비활성화) */
	private boolean isDatasourceRefExist;

	/** 기존에 TransactionManager설정이 있는지 검사(T:Memory 비활성화) */
	private boolean isTransactionManagerExist;

	/** DB의 존재 여부 */
	private boolean isDBExist;

	/** 현재 존재하는 DataBase를 선택하는 ComboBox */
	private Combo datasourceNameCombo;

	/** selectDBCombo에서 선택한 Database의 DB Type(disable) */
	private Combo dbTypeCombo;

	/** Launcher ID를 입력하는 Text */
	private Text launcherIdField;

	/** Operator ID를 입력하는 Text */
	private Text operatorIdField;

	/** Explorer ID를 입력하는 Text */
	private Text explorerIdField;

	/** Register ID를 입력하는 Text */
	private Text registerIdField;

	/** Driver Class Name을 입력하는 Text(disable) */
	private Text driverClassNameField;

	/** URL을 입력하는 Text(disable) */
	private Text urlField;

	/** User Name을 입력하는 Text(disable) */
	private Text usernameField;

	/** Password를 입력하는 Text(disable) */
	private Text passwordField;

	/** Sync Mode에서 Synchronous을 설정하는 Radio Button */
	private Button sync;

	/** Sync Mode에서 Asynchronous를 설정하는 Radio Button */
	private Button async;

	/** Repository에서 DB(Reference)를 설정하는 Radio Button */
	private Button dbRefType;

	/** Repository에서 DB(New)를 설정하는 Radio Button */
	private Button dbNewType;

	/** Repository에서 Memory를 설정하는 Radio Button */
	private Button memoryType;

	/** Datasource Bean ID Label, Text, Button이 생성되는 Composite */
	private Composite datasourceBeanIDControl = null;

	/** Datasource Bean ID값이 입력되는 Text */
	private Text datasourceBeanIDText = null;

	/** Datasource Bean ID를 선택하는 Dialog를 여는 Button */
	private Button datasourceBrowseButton = null;

	/**
	 * BatchJobExecutorCustomizePage 생성자
	 * 
	 * @param pageName
	 * @param context
	 *
	 */
	public BatchJobLauncherCustomizePage(String pageName, BatchJobLauncherContext context) {
		super(pageName);
		this.context = context;

		setTitle(BatchMessages.BatchJobLauncherCustomizePage_TITLE);
		setDescription(BatchMessages.BatchJobLauncherCustomizePage_DESCRIPTION);
	}

	public void createControl(Composite parent) {

		Composite control = new Composite(parent, SWT.None);
		control.setLayout(new GridLayout(2, false));
		control.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		launcherIdField = createIDLine(control, BatchMessages.BatchJobLauncherCustomizePage_LAUNCHER_ID_LABEL);

		createSyncMode(control);

		operatorIdField = createIDLine(control, BatchMessages.BatchJobLauncherCustomizePage_OPERATOR_ID_LABEL);

		explorerIdField = createIDLine(control, BatchMessages.BatchJobLauncherCustomizePage_EXPLORER_ID_LABEL);

		registerIdField = createIDLine(control, BatchMessages.BatchJobLauncherCustomizePage_REGISTER_ID_LABEL);

		createRepositoryType(control);

		setControl(control);
	}

	/**
	 * Database, Datasource Control 생성
	 * 
	 * @param parent
	 */
	private void createDatabaseControl(Composite parent) {

		GridData spanHorizontal = new GridData(GridData.FILL_HORIZONTAL);
		spanHorizontal.horizontalSpan = 3;

		Group dataBaseGroup = new Group(parent, SWT.None);
		dataBaseGroup.setLayout(new GridLayout());
		dataBaseGroup.setLayoutData(spanHorizontal);
		dataBaseGroup.setText(BatchMessages.BatchJobLauncherCustomizePage_DATABASE_GROUP);

		datasourceBeanIDControl = new Composite(dataBaseGroup, SWT.None);
		datasourceBeanIDControl.setLayout(new GridLayout(3, false));
		datasourceBeanIDControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		clearAndCreateDatasourceBeanIDControlWithBrowseButton();

		createDatasourceGroup(dataBaseGroup);
	}

	/**
	 * Database ComboBox(selectDBCombo) 생성, Listener 생성
	 * 
	 * @param parent
	 */
	private void clearAndCreateDatasourceBeanIDControl() {
		clearDatasourceBeanIDControl();

		Label beanIDLabel = new Label(datasourceBeanIDControl, SWT.None);
		beanIDLabel.setText(BatchMessages.BatchJobLauncherCustomizePage_DATASOURCE_BEAN_ID_LABEL);

		datasourceBeanIDText = new Text(datasourceBeanIDControl, SWT.BORDER);
		datasourceBeanIDText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		datasourceBeanIDText.addListener(SWT.Modify, validation);
	}

	/** DatasourceBeanIDControl의 항목을 모두 제거한다. */
	private void clearDatasourceBeanIDControl() {
		Control[] children = datasourceBeanIDControl.getChildren();

		for (int i = 0; i < children.length; i++) {
			children[i].dispose();
		}
	}

	/** DatasourceBeanIDControl의 항목을 모두 제거하고 Button을 제외한 Label, Text를 생성한다 */
	private void clearAndCreateDatasourceBeanIDControlWithoutBrowseButton() {
		clearAndCreateDatasourceBeanIDControl();

		datasourceBeanIDControl.getParent().layout(true, true);
	}

	/** DatasourceBeanIDControl의 항목을 모두 제거하고 Label, Text, Button을 생성한다. */
	private void clearAndCreateDatasourceBeanIDControlWithBrowseButton() {
		clearAndCreateDatasourceBeanIDControl();

		datasourceBrowseButton = new Button(datasourceBeanIDControl, SWT.PUSH);
		datasourceBrowseButton.setText(BatchMessages.BatchJobLauncherCustomizePage_BROWSE_BUTTON);
		datasourceBrowseButton.addListener(SWT.Selection, browseListener);

		datasourceBeanIDControl.getParent().layout(true, true);
	}

	/**
	 * DB입력 정보(dbTypeCombo, driverClassName, url, username, password) 생성
	 * 
	 * @param parent
	 */
	private void createDatasourceGroup(Composite parent) {

		Group descriptionGrp = new Group(parent, SWT.FILL);
		descriptionGrp.setText(BatchMessages.BatchJobLauncherCustomizePage_DATASOURCE_GROUP);
		descriptionGrp.setLayout(new GridLayout(2, false));
		descriptionGrp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		GridData labelGridData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		labelGridData.widthHint = 120;
		GridData fieldGridData = new GridData(GridData.FILL_HORIZONTAL);

		Label selectDBLabel = new Label(descriptionGrp, SWT.RIGHT);
		selectDBLabel.setText(BatchMessages.BatchJobLauncherCustomizePage_DATASOURCE_LABEL);
		selectDBLabel.setLayoutData(labelGridData);

		datasourceNameCombo = new Combo(descriptionGrp, SWT.READ_ONLY | SWT.FILL);
		datasourceNameCombo.setItems(getAvailableDBProfileNames());
		datasourceNameCombo.setLayoutData(fieldGridData);
		datasourceNameCombo.addListener(SWT.Selection, selectDBListener);
		datasourceNameCombo.addListener(SWT.Selection, validation);

		/* DB Type */
		Label dbTypeLabel = new Label(descriptionGrp, SWT.RIGHT);
		dbTypeLabel.setText(BatchMessages.BatchJobLauncherCustomizePage_DB_TYPE_LABEL);
		dbTypeLabel.setLayoutData(labelGridData);

		dbTypeCombo = new Combo(descriptionGrp, SWT.READ_ONLY | SWT.FILL);
		dbTypeCombo.setItems(dataTypes);
		dbTypeCombo.setLayoutData(fieldGridData);
		dbTypeCombo.setEnabled(false);
		dbTypeCombo.addListener(SWT.Selection, validation);

		/* Driver Class Name */
		Label driverClassNameLabel = new Label(descriptionGrp, SWT.RIGHT);
		driverClassNameLabel.setText(BatchMessages.BatchJobLauncherCustomizePage_DRIVER_CLASS_NAME_LABEL);
		driverClassNameLabel.setLayoutData(labelGridData);

		driverClassNameField = new Text(descriptionGrp, SWT.BORDER);
		driverClassNameField.setLayoutData(fieldGridData);
		driverClassNameField.setEnabled(false);
		driverClassNameField.addListener(SWT.Modify, validation);

		/* URL 입력 */
		Label urlLabel = new Label(descriptionGrp, SWT.RIGHT);
		urlLabel.setText(BatchMessages.BatchJobLauncherCustomizePage_URL_LABEL);
		urlLabel.setLayoutData(labelGridData);

		urlField = new Text(descriptionGrp, SWT.BORDER);
		urlField.setLayoutData(fieldGridData);
		urlField.setEnabled(false);
		urlField.addListener(SWT.Modify, validation);

		/* USERNAME 입력 */
		Label usernameLabel = new Label(descriptionGrp, SWT.RIGHT);
		usernameLabel.setText(BatchMessages.BatchJobLauncherCustomizePage_USER_NAME_LABEL);
		usernameLabel.setLayoutData(labelGridData);

		usernameField = new Text(descriptionGrp, SWT.BORDER);
		usernameField.setLayoutData(fieldGridData);
		usernameField.setEnabled(false);
		usernameField.addListener(SWT.Modify, validation);

		/* PASSWORD 입력 */
		Label passwordLabel = new Label(descriptionGrp, SWT.RIGHT);
		passwordLabel.setText(BatchMessages.BatchJobLauncherCustomizePage_PASSWORD_LABEL);
		passwordLabel.setLayoutData(labelGridData);

		passwordField = new Text(descriptionGrp, SWT.PASSWORD | SWT.BORDER);
		passwordField.setLayoutData(fieldGridData);
		passwordField.setEnabled(false);
		passwordField.addListener(SWT.Modify, validation);
	}

	/**
	 * 사용 가능한 Data type의 DBProfile을 가져온다.
	 * 
	 * @return
	 */
	private String[] getAvailableDBProfileNames() {
		String[] profileNames = DataToolsPlatformUtil.getProfileNames();
		ArrayList<String> availableNames = new ArrayList<String>();

		String dataType = null;
		String profileName = null;
		Map<String, String> dBProfile = null;
		String rawDBType = null;

		for (int i = 0; i < profileNames.length; i++) {
			profileName = profileNames[i];
			dBProfile = DataToolsPlatformUtil.getProperty(profileName);
			rawDBType = dBProfile.get("driverClass"); //$NON-NLS-1$

			for (int j = 0; j < dataTypes.length; j++) {
				dataType = dataTypes[j];
				if (rawDBType.toLowerCase().indexOf(dataType.toLowerCase()) > -1) {
					availableNames.add(profileName);
					break;
				}
			}
		}

		String[] result = availableNames.toArray(new String[] {});

		return result;
	}

	/**
	 * <pre>
	 * Launcher ID, Repository ID, Operator ID, Explorer ID, Register ID의
	 * Label, Text, Button 생성
	 * </pre>
	 * 
	 * @param control
	 * @param iDString
	 * @return text
	 */
	private Text createIDLine(Composite control, String iDString) {

		Label iDLabel = new Label(control, SWT.None);
		iDLabel.setText(iDString + " : "); //$NON-NLS-1$

		Text text = new Text(control, SWT.BORDER);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.addListener(SWT.Modify, validation);

		return text;
	}

	/**
	 * jobRepository의 존재 유무를 확인 및 사용하는 class 정보를 찾아주는 함수
	 */
	private void setIsJobRepositoryIDExist() {

		LinkedHashMap<String, String> beanList = FindXMLFileBeanIdValueUtil.findingWantedBeanInXMLFiles(context,
				"/beans/bean", "id", "jobRepository", "class", false); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		if (beanList != null && beanList.size() != 0) {
			// jobRepository는 프로젝트당 하나
			context.setJobRepositoryClass(beanList.keySet().toArray()[0].toString());
			isJobRepositoryIDExist = true;
		} else {
			isJobRepositoryIDExist = false;
		}

		if (isJobRepositoryIDExist) {
			LinkedHashMap<String, String> beanDataSourceRefList = FindXMLFileBeanIdValueUtil
					.findingWantedBeanInXMLFiles(context, "/beans/bean", "id", "jobRepository", "p:dataSource-ref", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
							false);
			if (beanDataSourceRefList.size() != 0) {
				// 생성할 런쳐파일에 p:dataSource-ref의 값을 넣을 꺼니깐 저장해둬
				context.setDataSourceRef(beanDataSourceRefList.keySet().toArray()[0].toString());
			} else {
				context.setDataSourceRef(""); //$NON-NLS-1$
			}
		}

		LinkedHashMap<String, String> beanValueList = FindXMLFileBeanIdValueUtil.findingWantedBeanInXMLFiles(context,
				"/beans/bean/property", "name", "driverClassName", "id", true); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		if (beanValueList.size() != 0) {
			// 떙겨쓸 참조 DB 설정 존재 (Ref, New, Memory 비활성화)
			isDatasourceRefExist = true;
		} else {
			// 참조 DB 존재하지 않음 (New, Memory만 활성화(jobRepository가 있어도활성화??다른 파일에서 ref해서 쓰는거면?))
			isDatasourceRefExist = false;
		}

		LinkedHashMap<String, String> beanValueTMList = FindXMLFileBeanIdValueUtil.findingWantedBeanInXMLFiles(context,
				"/beans/bean", "id", "transactionManager", "id", false); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		if (beanValueTMList.size() != 0) {
			// transactionManager 설정 존재 (Memory 비활성화)
			isTransactionManagerExist = true;
		} else {
			// transactionManager 존재하지 않음 (다른 조건에 따름(별 영향 없음))
			isTransactionManagerExist = false;
		}
		context.setIsTransactionManagerExist(isTransactionManagerExist);
	}

	/**
	 * Sync Mode Control 생성
	 * 
	 * @param control
	 */
	private void createSyncMode(Composite control) {
		Label syncModeLabel = new Label(control, SWT.None);
		syncModeLabel.setText(BatchMessages.BatchJobLauncherCustomizePage_EXECUTION_TYPE_LABEL);

		GridData gData = new GridData(GridData.FILL_HORIZONTAL);
		gData.horizontalSpan = 2;

		Composite syncModeControl = new Composite(control, SWT.None);
		syncModeControl.setLayout(new GridLayout(2, true));
		syncModeControl.setLayoutData(gData);

		sync = new Button(syncModeControl, SWT.RADIO);
		sync.setLayoutData(new GridData());
		sync.setText(BatchMessages.BatchJobLauncherCustomizePage_SYNCHRONOUS_RADIO_BUTTON);
		sync.addListener(SWT.Selection, validation);

		async = new Button(syncModeControl, SWT.RADIO);
		async.setLayoutData(new GridData());
		async.setText(BatchMessages.BatchJobLauncherCustomizePage_ASYNCHRONOUS_RADIO_BUTTON);
		async.addListener(SWT.Selection, validation);
	}

	/**
	 * Repository Type Control 생성 밑 Listener 생성
	 * 
	 * @param control
	 */
	private void createRepositoryType(Composite control) {
		GridData gData = new GridData(GridData.FILL_HORIZONTAL);
		gData.horizontalSpan = 3;

		GridLayout gLayout = new GridLayout(3, false);
		gLayout.horizontalSpacing = 20;

		Group repositoryTypeGroup = new Group(control, SWT.None);
		repositoryTypeGroup.setLayout(gLayout);
		repositoryTypeGroup.setLayoutData(gData);
		repositoryTypeGroup.setText(BatchMessages.BatchJobLauncherCustomizePage_REPOSITORY_TYPE_GROUP);

		dbRefType = new Button(repositoryTypeGroup, SWT.RADIO);
		dbRefType.setText(BatchMessages.BatchJobLauncherCustomizePage_DB_REFERENCE_RADIO_BUTTON);
		dbRefType.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				clearAndCreateDatasourceBeanIDControlWithBrowseButton();

				setDisableDatasourceControl();
			}
		});

		dbNewType = new Button(repositoryTypeGroup, SWT.RADIO);
		dbNewType.setText(BatchMessages.BatchJobLauncherCustomizePage_DB_NEW_RADIO_BUTTON);
		dbNewType.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				clearAndCreateDatasourceBeanIDControlWithoutBrowseButton();

				setEnableDatasourceControl();
			}
		});

		memoryType = new Button(repositoryTypeGroup, SWT.RADIO);
		memoryType.setText(BatchMessages.BatchJobLauncherCustomizePage_MEMORY_RADIO_BUTTON);
		memoryType.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				clearAndCreateDatasourceBeanIDControlWithoutBrowseButton();

				datasourceBeanIDText.setEnabled(false);

				setDisableDatasourceControl();
			}
		});

		createDatabaseControl(repositoryTypeGroup);
	}

	/**
	 * Page에서 입력한 값을 context에 입력
	 * 
	 * @return context
	 */
	private BatchJobLauncherContext saveValuesToContext() {

		context.setJobLauncerId(launcherIdField.getText());
		context.setSyncMode(sync.getSelection() == true ? true : false);
		context.setOperatorId(operatorIdField.getText());
		context.setExplorerId(explorerIdField.getText());
		context.setRegisterId(registerIdField.getText());

		if (!isJobRepositoryIDExist) {
			if (dbRefType.getSelection()) {
				context.setRepositoryType(BatchJobLauncherContext.DB_REFERENCE);
				context.setDatasourceBeanID(datasourceBeanIDText.getText());
			} else if (dbNewType.getSelection()) {
				context.setRepositoryType(BatchJobLauncherContext.DB_NEW);
				context.setDatasourceBeanID(datasourceBeanIDText.getText());
			} else if (memoryType.getSelection()) {
				context.setRepositoryType(BatchJobLauncherContext.MEMORY);
			}

			context.setSelectDB(datasourceNameCombo.getText());
			context.setDbType(dbTypeCombo.getText());
			context.setDriverClassName(driverClassNameField.getText());
			context.setUrl(urlField.getText());
			context.setUserName(usernameField.getText());
			context.setPasswd(passwordField.getText());
		}

		return context;

	}

	@Override
	public void setVisible(boolean visible) {
		if (visible) {
			setPageComplete(true);

			setIsJobRepositoryIDExist();

			Point size = null;

			size = new Point(800, 1000);

			getShell().setMinimumSize(size);
			getShell().setSize(size);

			refreshPage();
			saveValuesToContext();
		}
		super.setVisible(visible);
	}

	/** Page 초기화 */
	private void refreshPage() {
		launcherIdField.setText("jobLauncher"); //$NON-NLS-1$

		sync.setSelection(true);
		async.setSelection(false);

		operatorIdField.setText("jobOperator"); //$NON-NLS-1$
		explorerIdField.setText("jobExplorer"); //$NON-NLS-1$
		registerIdField.setText("jobRegistry"); //$NON-NLS-1$

		if (!isJobRepositoryIDExist) {
			isDBExist = !NullUtil.isEmpty(getAvailableDBProfileNames());

			datasourceBeanIDText.setText("");

			if (!isDBExist) {
				removeDBNewTypeListeners();
				addNoDBListenerToDBNewTypeButton();
			}

			if (isDatasourceRefExist) {
				dbRefType.setEnabled(true);
				dbRefType.setSelection(true);
				dbRefType.notifyListeners(SWT.Selection, null);

				dbNewType.setSelection(false);
			} else {
				dbRefType.setEnabled(false);
				dbRefType.setSelection(false);

				dbNewType.setSelection(true);
				dbNewType.notifyListeners(SWT.Selection, null);
			}

			dbNewType.setEnabled(true);

			if (isTransactionManagerExist) {
				memoryType.setEnabled(false);
			} else {
				memoryType.setEnabled(true);
			}
			memoryType.setSelection(false);

		} else {
			dbRefType.setEnabled(false);
			dbNewType.setEnabled(false);
			memoryType.setEnabled(false);

			datasourceBeanIDText.setEnabled(false);
			datasourceBrowseButton.setEnabled(false);
			datasourceNameCombo.setEnabled(false);
		}

		setErrorMessage(null);
	}

	/** DB(New) Radio Button의 리스너를 모두 제거한다. */
	private void removeDBNewTypeListeners() {
		Listener[] listeners = dbNewType.getListeners(SWT.Selection);
		for (int i = 0; i < listeners.length; i++) {
			dbNewType.removeListener(SWT.Selection, listeners[i]);
		}
	}

	/** DB가 없을 경우 DB(New) Radio Button에 설정할 리스너 */
	private void addNoDBListenerToDBNewTypeButton() {
		dbNewType.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				clearAndCreateDatasourceBeanIDControlWithoutBrowseButton();

				if (dbNewType.getSelection()) {
					datasourceBeanIDText.setEnabled(false);

					datasourceNameCombo.setEnabled(false);
					datasourceNameCombo.deselectAll();

					setErrorMessage(null);
					setMessage(BatchMessages.BatchJobLauncherCustomizePage_NO_DATABASE, INFORMATION);
					setPageComplete(false);
				} else {
					setMessage(null, INFORMATION);
				}
			}
		});
	}

	/**
	 * 현재 Page에서 사용 불가능한 BeanID 목록을 가져온다.
	 * 
	 * @param currentID
	 * @return
	 */
	private String getDuplicateBeanID() {
		ArrayList<String> beanIDList = new ArrayList<String>();

		beanIDList.add("async"); //$NON-NLS-1$

		if (!dbRefType.getSelection()) {
			beanIDList.add("datasource"); //$NON-NLS-1$
		}

		beanIDList.add("transactionManager"); //$NON-NLS-1$

		beanIDList.add("jobHandler"); //$NON-NLS-1$

		beanIDList.add("jdbcTemplate"); //$NON-NLS-1$

		if (!checkAndAddBeanID(beanIDList, launcherIdField.getText())) {
			return BatchMessages.BatchJobLauncherCustomizePage_DUPLICATE_LAUNCHER_ID;
		}

		if (!checkAndAddBeanID(beanIDList, operatorIdField.getText())) {
			return BatchMessages.BatchJobLauncherCustomizePage_DUPLICATE_OPERATOR_ID;
		}

		if (!checkAndAddBeanID(beanIDList, explorerIdField.getText())) {
			return BatchMessages.BatchJobLauncherCustomizePage_DUPLICATE_EXPLORER_ID;
		}

		if (!checkAndAddBeanID(beanIDList, registerIdField.getText())) {
			return BatchMessages.BatchJobLauncherCustomizePage_DUPLICATE_REGISTER_ID;
		}

		if (!checkAndAddBeanID(beanIDList, datasourceBeanIDText.getText())) {
			return BatchMessages.BatchJobLauncherCustomizePage_DUPLICATE_DATASOURCE_BEAN_ID;
		}

		return ""; //$NON-NLS-1$
	}

	/**
	 * Bean ID가 기존에 존재하는 지 확인하고 존재하지 않으면 추가한다.
	 * 
	 * @param beanIDList
	 * @param beanID
	 * @return
	 */
	private boolean checkAndAddBeanID(ArrayList<String> beanIDList, String beanID) {
		if (NullUtil.isEmpty(beanIDList)) {
			beanIDList = new ArrayList<String>();
			return beanIDList.add(beanID);
		} else {
			if (beanIDList.contains(beanID)) {
				return false;
			} else {
				return beanIDList.add(beanID);
			}
		}
	}

	/** datasourceNameCombo를 선택 가능하게 설정한다. */
	private void setEnableDatasourceControl() {
		datasourceNameCombo.setEnabled(true);
		datasourceNameCombo.select(0);
		datasourceNameCombo.notifyListeners(SWT.Selection, new Event());
	}

	/** datasourceNameCombo를 선택 불가능하게 설정한다 */
	private void setDisableDatasourceControl() {
		datasourceNameCombo.setEnabled(false);
		datasourceNameCombo.deselectAll();

		dbTypeCombo.deselectAll();

		driverClassNameField.setText(""); //$NON-NLS-1$

		urlField.setText(""); //$NON-NLS-1$

		usernameField.setText(""); //$NON-NLS-1$

		passwordField.setText(""); //$NON-NLS-1$
	}

	/** 각 입력값에 대한 Validation */
	Listener validation = new Listener() {

		public void handleEvent(Event event) {

			if (!NullUtil.isNull(dbNewType) && dbNewType.getSelection() && !isDBExist) {
				return;
			}

			setPageComplete(false);

			String errorMessage = null;

			errorMessage = getBeanIDTextErrorMessage(BatchMessages.BatchJobLauncherCustomizePage_ERROR_LAUNCHER_ID,
					launcherIdField.getText());
			if (!NullUtil.isEmpty(errorMessage)) {
				setErrorMessage(errorMessage);
				return;
			}

			errorMessage = getBeanIDTextErrorMessage(BatchMessages.BatchJobLauncherCustomizePage_ERROR_OPERATOR_ID,
					operatorIdField.getText());
			if (!NullUtil.isEmpty(errorMessage)) {
				setErrorMessage(errorMessage);
				return;
			}

			errorMessage = getBeanIDTextErrorMessage(BatchMessages.BatchJobLauncherCustomizePage_ERROR_EXPLORER_ID,
					explorerIdField.getText());
			if (!NullUtil.isEmpty(errorMessage)) {
				setErrorMessage(errorMessage);
				return;
			}

			errorMessage = getBeanIDTextErrorMessage(BatchMessages.BatchJobLauncherCustomizePage_ERROR_REGISTER_ID,
					registerIdField.getText());
			if (!NullUtil.isEmpty(errorMessage)) {
				setErrorMessage(errorMessage);
				return;
			}

			if (!isJobRepositoryIDExist) {

				if (!memoryType.getSelection()) {
					errorMessage = getBeanIDTextErrorMessage(
							BatchMessages.BatchJobLauncherCustomizePage_ERROR_DATASOURCE_BEAN_ID,
							datasourceBeanIDText.getText());
					if (!NullUtil.isEmpty(errorMessage)) {
						setErrorMessage(errorMessage);
						return;
					}
				}

				if (isDBExist && dbNewType.getSelection()) {
					if (NullUtil.isEmpty(datasourceNameCombo.getText())) {
						setErrorMessage(BatchMessages.BatchJobLauncherCustomizePage_DESELECT_DB);
						return;
					} else {
						if (NullUtil.isEmpty(driverClassNameField)) {
							setErrorMessage(BatchMessages.BatchJobLauncherCustomizePage_EMPTY_DRIVER_CLASS_NAME);
							return;
						}
						if (NullUtil.isEmpty(urlField)) {
							setErrorMessage(BatchMessages.BatchJobLauncherCustomizePage_EMPTY_URL);
							return;
						}
						if (NullUtil.isEmpty(usernameField)) {
							setErrorMessage(BatchMessages.BatchJobLauncherCustomizePage_EMPTY_USER_NAME);
							return;
						}
					}

				}
			}

			saveValuesToContext();

			setErrorMessage(null);
			setPageComplete(true);
		}

		/**
		 * Bean ID에 관련된 ErrorMessage 생성
		 * 
		 * @param beanIDName
		 * @param beanIDValue
		 * @return
		 */
		private String getBeanIDTextErrorMessage(String beanIDName, String beanIDValue) {
			if (NullUtil.isEmpty(beanIDValue)) {
				return beanIDName + BatchMessages.BatchJobLauncherCustomizePage_EMPTY_VALUE;
			} else {

				if (!StringUtil.isBatchJobBeanIDAvailable(beanIDValue)) {
					return beanIDName + BatchMessages.BatchJobLauncherCustomizePage_INVALID_VALUE;
				}

				String duplicateBeanID = getDuplicateBeanID();
				if (!NullUtil.isEmpty(duplicateBeanID)) {
					return BatchMessages.BatchJobLauncherCustomizePage_DUPLICATE_BEAN_ID_1 + duplicateBeanID
							+ BatchMessages.BatchJobLauncherCustomizePage_DUPLICATE_BEAN_ID_2;
				}
			}

			return null;
		}
	};

	/** selectDBCombo 선택시 Listener */
	Listener selectDBListener = new Listener() {

		public void handleEvent(Event event) {
			if (!NullUtil.isEmpty(datasourceNameCombo)) {
				Map<String, String> dBProfile = DataToolsPlatformUtil.getProperty(datasourceNameCombo.getText());
				if (!dBProfile.isEmpty()) {
					usernameField.setText(dBProfile.get("username")); //$NON-NLS-1$
					if (dBProfile.get("password") != null) { //$NON-NLS-1$
						passwordField.setText(dBProfile.get("password")); //$NON-NLS-1$
					} else {
						passwordField.setText(""); //$NON-NLS-1$
					}
					driverClassNameField.setText(dBProfile.get("driverClass")); //$NON-NLS-1$
					urlField.setText(dBProfile.get("url")); //$NON-NLS-1$

					String rawDBType = dBProfile.get("driverClass"); //$NON-NLS-1$
					for (String item : dataTypes) {
						if (rawDBType.toLowerCase().indexOf(item.toLowerCase()) > -1) {
							dbTypeCombo.setText(item);
							break;
						}
					}
				}
			}
		}
	};

	/**
	 * datasource Bean ID (ref) 선택하기
	 * 
	 */
	Listener browseListener = new Listener() {

		public void handleEvent(Event event) {

			String selectedBeanID = ""; //$NON-NLS-1$
			GetRefDatasourceBeanIDDialog dialog = new GetRefDatasourceBeanIDDialog(getShell(), context, selectedBeanID);
			dialog.setTitle(BatchMessages.BatchJobLauncherCustomizePage_GET_REF_DATASOURCE_BEAN_ID_DIALOG_TITLE);
			if (dialog.open() == Window.OK) {

				selectedBeanID = (String) dialog.getRefDatasourceBeanID();
				datasourceBeanIDText.setText(selectedBeanID);
			}
		}
	};
}
