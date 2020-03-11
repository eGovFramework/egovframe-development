package egovframework.dev.imp.commngt.wizards.pages;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.datatools.sqltools.core.profile.NoSuchProfileException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import egovframework.dev.imp.commngt.common.ComMngtMessages;
import egovframework.dev.imp.commngt.common.CommngtLog;
import egovframework.dev.imp.commngt.util.CreateTableUtil;
import egovframework.dev.imp.commngt.util.FindingScriptinZipUtil;
import egovframework.dev.imp.commngt.util.HandlingPropertiesUtil;
import egovframework.dev.imp.commngt.util.SettingSqlStatementUtil;
import egovframework.dev.imp.commngt.wizards.model.IComponentElement;
import egovframework.dev.imp.commngt.wizards.model.NewEgovCommngtContext;
import egovframework.dev.imp.commngt.wizards.model.SqlStatementModel;
import egovframework.dev.imp.core.common.DataToolsPlatformUtil;
import egovframework.dev.imp.core.utils.EgovProperties;
import egovframework.dev.imp.core.utils.NullUtil;

/**
 * 사용자 DB환경 셋팅 및 테이블 생성에 관한 마법사페이지 클래스
 * 
 * @author 개발환경 개발팀 최서윤
 * @since 2011.06.13
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2011.06.13  최서윤          최초 생성
 * 
 * 
 * </pre>
 */

public class CustomizeTableCreationPage extends WizardPage {

	/** 존재하는 DB 목록 */
	private Combo selectDBCombo = null;
	/** 사용할 DB의 db type 정보 */
	private Combo dbTypeCombo = null;
	/** 사용할 DB의 db type 정보 */
	private Text driverClassNameField = null;
	/** 사용할 DB의 url 정보 */
	private Text urlField = null;
	/** 사용할 DB의 user 정보 */
	private Text usernameField = null;
	/** 사용할 DB의 password 정보 */
	private Text passwordField = null;
	/** Connection test 버튼 */
	private Button connectionBtn = null;
	/** Create Table 버튼 */
	private Button createTableBtn = null;
	/** 화면 Table 구성 */
	private TableViewer viewer = null;

	/** 공통컴포넌트 컨텍스트 */
	private final NewEgovCommngtContext context;
	/** 선택 컴포넌트 목록 */
	private static java.util.List<IComponentElement> checkedComponent;

	/** 해당 페이지로 넘어 왔음을 체크 */
	public String checkLastPage = null;

	/** 화면용 SqlStatementModel */
	private final List<SqlStatementModel> ssm = new ArrayList<SqlStatementModel>();
	/** 로직용 SqlStatementModel */
	private final List<SqlStatementModel> latestSsm = new ArrayList<SqlStatementModel>();
	/** 로직용 SqlStatementModel2 */
	private final List<SqlStatementModel> innerModel = new ArrayList<SqlStatementModel>();
	/** Zip파일 안에서 찾은 Create, Insert */
	private HashMap<String, String> haMap = null;
	/** Globals.properties파일의 존재 유무 */
	private boolean isExist = false;
	/** Create Table 성공 여부 */
	private boolean successCreate = false;

	/** 사용자가 입력한 DbType */
	private String dbType = null;
	/** 사용자가 입력한 driverClassName */
	private String driverClassName = null;
	/** 사용자가 입력한 url */
	private String url = null;
	/** 사용자가 입력한 userName */
	private String username = null;
	/** 사용자가 입력한 password */
	private String password = null;
	
	
	/** 사용자가 선택한 datasource 정보 번호*/
	public static int selectedDatasourceNum;

	/**
	 * 생성자
	 * 
	 * @param pageName
	 * @param context
	 */
	public CustomizeTableCreationPage(String pageName,
			NewEgovCommngtContext context) {
		super(pageName);
		this.context = context;
		setTitle(ComMngtMessages.customizeTableCreationPagetitle);
	}

	/**
	 * page 완성 여부 check
	 * 
	 * @return 완성 여부
	 */
	public boolean isPageComplete() {
		boolean flag = urlField.getText().length() != 0
				&& driverClassNameField.getText().length() != 0
				&& getErrorMessage() == null
				&& successCreate;
		return flag;
	}
	
	/**
	 * Datasource Explorer 값을 읽어와서 Properties 파일과 비교
	 * */
	private void getJDBCInfo(){
		
		if(isExist){
			dbType = EgovProperties.getProperty(HandlingPropertiesUtil.getFilePath(), ComMngtMessages.customizeTableCreationPage99); //$NON-NLS-1$
			driverClassName = EgovProperties.getProperty(HandlingPropertiesUtil.getFilePath(),ComMngtMessages.customizeTableCreationPage10);
			url = EgovProperties.getProperty(HandlingPropertiesUtil.getFilePath(),ComMngtMessages.customizeTableCreationPage19);
			username = EgovProperties.getProperty(HandlingPropertiesUtil.getFilePath(),ComMngtMessages.customizeTableCreationPage24);
			password = EgovProperties.getProperty(HandlingPropertiesUtil.getFilePath(),ComMngtMessages.customizeTableCreationPage29);
			
			if(NullUtil.isNone(dbType)){
				dbType = "";
			}
			if(NullUtil.isNone(driverClassName)){
				driverClassName = "";
			}
			if(NullUtil.isNone(url)){
				url = "";
			}
			if(NullUtil.isNone(username)){
				username = "";
			}
			if(NullUtil.isNone(password)){
				password = "";
			}
			
			String[] profileNames = DataToolsPlatformUtil.getProfileNames();
			for (int i = 0; i < profileNames.length; i++) {

				setMessage(null);
				
				Map<String, String> profile = DataToolsPlatformUtil.getProperty(profileNames[i]);
				
				//datasource의 내용과 프로퍼티파일의 내용이 같다면
				if(username.equals((String) profile.get(ComMngtMessages.customizeTableCreationPage68)) &&
				url.equals((String) profile.get(ComMngtMessages.customizeTableCreationPage65)) &&
				driverClassName.equals((String) profile.get(ComMngtMessages.customizeTableCreationPage53))){
					//화면에 뿌려줌
					
					selectedDatasourceNum = i;
					
					if (driverClassName.toLowerCase().indexOf(ComMngtMessages.customizeTableCreationPagedbType1) > -1) {
						dbType = ComMngtMessages.customizeTableCreationPagedbType1;
					} else if (driverClassName.toLowerCase().indexOf(ComMngtMessages.customizeTableCreationPagedbType2) > -1) {
						dbType = ComMngtMessages.customizeTableCreationPagedbType2;
					} else if (driverClassName.toLowerCase().indexOf(ComMngtMessages.customizeTableCreationPagedbType3) > -1) {
						dbType = ComMngtMessages.customizeTableCreationPagedbType3;
					} else if (driverClassName.toLowerCase().indexOf(ComMngtMessages.customizeTableCreationPagedbType4) > -1) {
						dbType = ComMngtMessages.customizeTableCreationPagedbType4;
					} else if (driverClassName.toLowerCase().indexOf(ComMngtMessages.customizeTableCreationPagedbType5) > -1) {
						dbType = ComMngtMessages.customizeTableCreationPagedbType5;
					} else if (driverClassName.toLowerCase().indexOf(ComMngtMessages.customizeTableCreationPagedbType6) > -1) {
						dbType = ComMngtMessages.customizeTableCreationPagedbType6;
					} else if (driverClassName.toLowerCase().indexOf(ComMngtMessages.customizeTableCreationPagedbType7) > -1) {
						dbType = ComMngtMessages.customizeTableCreationPagedbType7;						
					} else{
						dbType = ""; //$NON-NLS-1$
					}
					
					if(selectDBCombo != null && dbType != ""){ //dbType이 "" 이거면 selectDBCombo 선택 안해주는 로직 추가 //$NON-NLS-1$
						selectDBCombo.setText(profileNames[i]);
					}
						
					dbTypeCombo.setText(dbType);
					driverClassNameField.setText(NullUtil.isNone((String) profile.get(ComMngtMessages.customizeTableCreationPage53)) ? "" : (String) profile.get(ComMngtMessages.customizeTableCreationPage53)); //$NON-NLS-1$
					urlField.setText(NullUtil.isNone((String) profile.get(ComMngtMessages.customizeTableCreationPage65)) ? "" : (String) profile.get(ComMngtMessages.customizeTableCreationPage65)); //$NON-NLS-1$
					usernameField.setText(NullUtil.isNone((String) profile.get(ComMngtMessages.customizeTableCreationPage68)) ? "" : (String) profile.get(ComMngtMessages.customizeTableCreationPage68)); //$NON-NLS-1$
					passwordField.setText(NullUtil.isNone((String) profile.get(ComMngtMessages.customizeTableCreationPage71)) ? "" : (String) profile.get(ComMngtMessages.customizeTableCreationPage71)); //$NON-NLS-1$
					
				} else {
					
					setMessage(ComMngtMessages.customizeTableCreationPageDSEXPLORER, INFORMATION);
					
					if(dbType != ""){
						setMessage(null);
					}
					dbType = ""; //$NON-NLS-1$
				}
			}
			
		} else {
			dbType = ""; //$NON-NLS-1$
			driverClassName = ""; //$NON-NLS-1$
			url = ""; //$NON-NLS-1$
			username = ""; //$NON-NLS-1$
			password = ""; //$NON-NLS-1$
		}
	}
	
	/**
	 * DB 입력정보를 가지고 connection test
	 * */
	SelectionListener connectionTestListener = new SelectionListener() {
		boolean isConnect = false;
		Connection conn = null;
		public void widgetSelected(SelectionEvent e) {

			Runnable runnable = new Runnable() {
					
				public void run() {
					try {
						 conn = DataToolsPlatformUtil.getConnection(DataToolsPlatformUtil.getDatabaseIdentifier(
								 DataToolsPlatformUtil.getConnectionInfo(DataToolsPlatformUtil.getProfileNames()[selectedDatasourceNum])));
							 
					} catch (SQLException e) {
						CommngtLog.logError(e);
					} catch (NoSuchProfileException e) {
						CommngtLog.logError(e);
					}finally {
						if (conn != null) {
							try {
								isConnect = true;
								conn.close();
							} catch (SQLException e) {
								CommngtLog.logError(e);
							}
						}else{
							isConnect = false;
						}
					}
				}
			};
				
			BusyIndicator.showWhile(connectionBtn.getDisplay(), runnable);
				
			if (isConnect) {
				
				createTableBtn.setEnabled(true);
				connectionBtn.setEnabled(true);
				
				MessageDialog.openInformation(getShell(),
						ComMngtMessages.customizeTableCreationPage47,
						ComMngtMessages.customizeTableCreationPage48);
			} else{
				createTableBtn.setEnabled(false);
			}
			
			//finish 여부 다시 체크
			isPageComplete();
			updatePageComplete();
			
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			widgetSelected(e);
		}
	};


	/**
	 * Data Source Explorer에 연결되어 있는 Datasource를 사용할 경우 선택한 Datasource에 대한 정보를 가져옴
	 * */
	SelectionListener dbSelectionListener = new SelectionListener() {

		public void widgetSelected(SelectionEvent e) {
			
			setMessage(null);
			
			selectedDatasourceNum = selectDBCombo.getSelectionIndex();
			
			Map<String, String> profile = DataToolsPlatformUtil.getProperty(selectDBCombo.getText().trim());

			String driverClass = (String) profile.get(ComMngtMessages.customizeTableCreationPage53);

			driverClassNameField.setText(driverClass);
			setErrorMessage(null);
			connectionBtn.setEnabled(true);
					
			//없을 경우 그냥 뿌려준다
			if (driverClass.toLowerCase().indexOf(ComMngtMessages.customizeTableCreationPagedbType1) > -1) {
				dbTypeCombo.setText(ComMngtMessages.customizeTableCreationPagedbType1);
				dbType = ComMngtMessages.customizeTableCreationPagedbType1;
			} else if (driverClass.toLowerCase().indexOf(ComMngtMessages.customizeTableCreationPagedbType2) > -1) {
				dbTypeCombo.setText(ComMngtMessages.customizeTableCreationPagedbType2);
				dbType = ComMngtMessages.customizeTableCreationPagedbType2;
			} else if (driverClass.toLowerCase().indexOf(ComMngtMessages.customizeTableCreationPagedbType3) > -1) {
				dbTypeCombo.setText(ComMngtMessages.customizeTableCreationPagedbType3);
				dbType = ComMngtMessages.customizeTableCreationPagedbType2;
			} else if (driverClass.toLowerCase().indexOf(ComMngtMessages.customizeTableCreationPagedbType4) > -1) {
				dbTypeCombo.setText(ComMngtMessages.customizeTableCreationPagedbType4);
				dbType = ComMngtMessages.customizeTableCreationPagedbType4;
			} else if (driverClass.toLowerCase().indexOf(ComMngtMessages.customizeTableCreationPagedbType5) > -1) {
				dbTypeCombo.setText(ComMngtMessages.customizeTableCreationPagedbType5);
				dbType = ComMngtMessages.customizeTableCreationPagedbType5;
			} else if (driverClass.toLowerCase().indexOf(ComMngtMessages.customizeTableCreationPagedbType6) > -1) {
				dbTypeCombo.setText(ComMngtMessages.customizeTableCreationPagedbType6);
				dbType = ComMngtMessages.customizeTableCreationPagedbType6;
			} else if (driverClass.toLowerCase().indexOf(ComMngtMessages.customizeTableCreationPagedbType7) > -1) {
				dbTypeCombo.setText(ComMngtMessages.customizeTableCreationPagedbType7);
				dbType = ComMngtMessages.customizeTableCreationPagedbType7;				
			} else{
				dbTypeCombo.setText(""); //$NON-NLS-1$
				dbType = ""; //$NON-NLS-1$
				setErrorMessage(ComMngtMessages.customizeTableCreationPageDatasourceError);
				connectionBtn.setEnabled(false);
			}
					
			if (NullUtil.isNone((String) profile.get(ComMngtMessages.customizeTableCreationPage65))) {
				urlField.setText(""); //$NON-NLS-1$
			} else {
				
				urlField.setText((String) profile.get(ComMngtMessages.customizeTableCreationPage65));
			}
					
			if (NullUtil.isNone((String) profile.get(ComMngtMessages.customizeTableCreationPage68))) {
				usernameField.setText(""); //$NON-NLS-1$
			} else {
				usernameField.setText((String) profile.get(ComMngtMessages.customizeTableCreationPage68));
			}
					
			if (NullUtil.isNone((String) profile.get(ComMngtMessages.customizeTableCreationPage71))) {
				passwordField.setText(""); //$NON-NLS-1$
			} else {
				passwordField.setText((String) profile.get(ComMngtMessages.customizeTableCreationPage71));
			}
			
			context.setDBType(driverClassNameField.getText());
			context.setUrl(urlField.getText());
			context.setUsername(usernameField.getText());
			context.setPassword(passwordField.getText());
			
			createTableBtn.setEnabled(false);
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			widgetSelected(e);
		}
	};

	/**
	 * Create Table 실행
	 * */
	SelectionListener createTableListener = new SelectionListener() {

		public void widgetSelected(SelectionEvent event) {

			context.setDBType(driverClassNameField.getText());
			context.setUrl(urlField.getText());
			context.setUsername(usernameField.getText());
			context.setPassword(passwordField.getText());

			try {

				getContainer().run(true, true, new IRunnableWithProgress() {

					public void run(IProgressMonitor monitor)
							throws InvocationTargetException,
							InterruptedException {

						monitor.beginTask(
								ComMngtMessages.customizeTableCreationPage96,
								checkedComponent.size() * 2);

						IProgressMonitor nullMonitor = new NullProgressMonitor();

						String fileName = null;
						List<SqlStatementModel> sList = null;
						innerModel.clear();

						for (int i = 0; i < checkedComponent.size(); i++) {

							fileName = ComMngtMessages.customizeTableCreationPage38
									+ checkedComponent.get(i).getFileName();

							if (checkedComponent.get(i).getUseTable().length() != 0) {

								monitor.subTask(ComMngtMessages.customizeTableCreationPage97);
								monitor.worked(1);

								// Zip 파일에서 선택한 컴포넌트의 table script를 반환한다
								haMap = FindingScriptinZipUtil
										.getScriptFileinZip(nullMonitor,
												fileName,
												HandlingPropertiesUtil
														.findDbType(context));

								if (!haMap.isEmpty()) {

									String ddlContents = null;
									String dmlContents = null;

									// 파일 목록에서 create문이 있는 파일과 insert문이 있는 파일로
									// 나눠서 각각 넣는다
									ddlContents = haMap
											.get(ComMngtMessages.customizeTableCreationPage39);

									if (haMap
											.get(ComMngtMessages.customizeTableCreationPage40) != null) {
										dmlContents = haMap
												.get(ComMngtMessages.customizeTableCreationPage40);
									}

									monitor.subTask(ComMngtMessages.customizeTableCreationPage98);
									monitor.worked(1);

									// create를 실시한다.
									sList = CreateTableUtil.createTableMethod(
											context, ddlContents, dmlContents,
											ssm);

									latestSsm.clear();
									for (int j = 0; j < sList.size(); j++) {
										latestSsm.add(sList.get(j));
									}

									SqlStatementModel latestModel = null;

									for (int j = 0; j < sList.size(); j++) {
										if(!(null == sList.get(j).getComponent() || sList.get(j).getComponent().equals(""))) {
											latestModel = new SqlStatementModel();
											latestModel.setComponent(sList.get(j)
													.getComponent());
											latestModel.setStmtName(sList.get(j)
													.getStmtName().toUpperCase());
											latestModel.setStmtType(sList.get(j)
													.getStmtType().toUpperCase());
											latestModel.setResultMessage(sList.get(
													j).getResultMessage());
	
											if (sList.get(j).getErrorCode() != null) {
												latestModel.setErrorCode(sList.get(
														j).getErrorCode());
											}
											innerModel.add(latestModel);
										}
									}
								}
							}

						}// 컴포넌트 갯수만큼 for끝

						ssm.clear();

						HashMap<String, SqlStatementModel> sqlHashMap = new HashMap<String, SqlStatementModel>();
						for (int i = 0; i < innerModel.size(); i++) {

							if(innerModel.get(i).getComponent() != null && !innerModel.get(i).getStmtType().equalsIgnoreCase(ComMngtMessages.customizeTableCreationPageindex)) {

								if (!sqlHashMap.containsKey(innerModel.get(i)
										.getStmtName())) {
									
									ssm.add(innerModel.get(i));
								}
	
								sqlHashMap.put(innerModel.get(i).getStmtName(),
										innerModel.get(i));
								setErrorMessage(null);
							}
						}

						if (monitor.isCanceled()) {
							throw new InterruptedException();
						}

						monitor.done();

					}

				});
				if (innerModel.size() == 0) {
					setErrorMessage(null);
					setErrorMessage(ComMngtMessages.customizeTableCreationPage100);
				}
				viewer.setLabelProvider(new CheckedComponentTableLableProvider());
				if(!NullUtil.isNone(innerModel.get(0).getResultMessage())){
					
					if (innerModel.get(0).getResultMessage()
							.indexOf(ComMngtMessages.customizeTableCreationPagebefore) < 0) {
						// 테이블 생성이 정상 종료된 경우
						successCreate = true;
					}
				}

				//finish 여부 다시 체크
				isPageComplete();
				updatePageComplete();

			} catch (InvocationTargetException e) {
				CommngtLog.logError(e);
			} catch (InterruptedException e) {
				CommngtLog.logError(e);
			}

		}

		public void widgetDefaultSelected(SelectionEvent arg0) {
			widgetSelected(arg0);
		}
	};
	
	/**
	 * 화면단 컨트롤 생성
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {

		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 4;

		selectDbCombo(container); // selectDb
		insertDatasourceGrp(container); // db info & connection test Btn &

		//jdbc파일을 읽어서 프로퍼티파일과 비교한후 값 셋팅
		getJDBCInfo();

		createGrp(container);// description

		container.setLayout(layout);
		setControl(container);
	}
	

	/**
	 * selectDB 체크박스와 Datasource 정보가 담긴 Combo
	 * 
	 * @param parent
	 * */
	private void selectDbCombo(Composite parent) {
		
		GridData selectDBLabel = new GridData();
		Label selectDBLb = new Label(parent, SWT.NONE);
		selectDBLb.setLayoutData(selectDBLabel);
		selectDBLb.setText(ComMngtMessages.customizeTableCreationPage2);

		GridData selectDBComboData = new GridData(GridData.FILL_BOTH);
		selectDBCombo = new Combo(parent, SWT.READ_ONLY | SWT.FILL);
		selectDBCombo.setItems(DataToolsPlatformUtil.getProfileNames());
		selectDBCombo.setLayoutData(selectDBComboData);
		selectDBCombo.addSelectionListener(dbSelectionListener);
		selectDBComboData.horizontalAlignment = SWT.FILL;
		selectDBComboData.grabExcessHorizontalSpace = true;
		selectDBComboData.horizontalSpan = 3;

	}
	
	/**
	 * DB입력 정보(db유형, driverClassName, url, username, password)
	 * 
	 * @param parent
	 */
	private void insertDatasourceGrp(Composite parent) {

		GridLayout glDescription = new GridLayout();
		glDescription.numColumns = 4;

		GridData gdGroup = new GridData(GridData.FILL_BOTH);
		gdGroup.horizontalAlignment = SWT.FILL;
		gdGroup.verticalAlignment = SWT.FILL;
		gdGroup.grabExcessHorizontalSpace = true;
		gdGroup.grabExcessVerticalSpace = true;
		gdGroup.horizontalSpan = 4;
		Group descriptionGrp = new Group(parent, SWT.FILL);
		descriptionGrp
				.setText(ComMngtMessages.customizeTableCreationPage3);
		descriptionGrp.setLayoutData(gdGroup);
		descriptionGrp.setLayout(glDescription);

		/* DB Type */
		GridData dbLabelData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		Label dbTypeLabel = new Label(descriptionGrp, SWT.NONE);
		dbTypeLabel.setText(ComMngtMessages.customizeTableCreationPage4);
		dbTypeLabel.setLayoutData(dbLabelData);
		GridData dbTypeInfoData = new GridData(SWT.FILL);

		dbTypeCombo = new Combo(descriptionGrp, SWT.READ_ONLY | SWT.FILL);
		String items[] = {
				ComMngtMessages.customizeTableCreationPagedbType1,
				ComMngtMessages.customizeTableCreationPagedbType2,
				ComMngtMessages.customizeTableCreationPagedbType3,
				ComMngtMessages.customizeTableCreationPagedbType4,
				ComMngtMessages.customizeTableCreationPagedbType5,
				ComMngtMessages.customizeTableCreationPagedbType6,
				ComMngtMessages.customizeTableCreationPagedbType7,
				""}; //$NON-NLS-1$
		dbTypeCombo.setItems(items);
		dbTypeCombo.setLayoutData(dbTypeInfoData);
		
		dbTypeCombo.setEnabled(false);
		dbTypeInfoData.horizontalAlignment = SWT.FILL;
		dbTypeInfoData.grabExcessHorizontalSpace = true;
		dbTypeInfoData.horizontalSpan = 3;

		/* Driver Class Name */
		GridData driverClassNameData = new GridData(
				GridData.HORIZONTAL_ALIGN_END);
		Label driverClassNameLabel = new Label(descriptionGrp, SWT.NONE);
		driverClassNameLabel.setText(ComMngtMessages.customizeTableCreationPage13);
		driverClassNameLabel.setLayoutData(driverClassNameData);
		GridData driverClassNameData1 = new GridData();
		driverClassNameField = new Text(descriptionGrp, SWT.BORDER);
		driverClassNameField.setLayoutData(driverClassNameData1);
		driverClassNameField.setEditable(false);
		driverClassNameData1.horizontalAlignment = SWT.FILL;
		driverClassNameData1.grabExcessHorizontalSpace = true;
		driverClassNameData1.horizontalSpan = 3;

		/* URL 입력 */
		GridData urlLabelData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		Label urlLabel = new Label(descriptionGrp, SWT.NONE);
		urlLabel.setText(ComMngtMessages.customizeTableCreationPage18);
		urlLabel.setLayoutData(urlLabelData);
		GridData urlTextData = new GridData();
		urlField = new Text(descriptionGrp, SWT.BORDER);
		urlField.setLayoutData(urlTextData);
		urlField.setEditable(false);
		urlTextData.horizontalAlignment = SWT.FILL;
		urlTextData.grabExcessHorizontalSpace = true;
		urlTextData.horizontalSpan = 3;

		/* USERNAME 입력 */
		GridData userNameLabelData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		Label usernameLabel = new Label(descriptionGrp, SWT.NONE);
		usernameLabel
				.setText(ComMngtMessages.customizeTableCreationPage23);
		usernameLabel.setLayoutData(userNameLabelData);
		GridData userNameTextData = new GridData();
		usernameField = new Text(descriptionGrp, SWT.BORDER);
		usernameField.setLayoutData(userNameTextData);
		usernameField.setEditable(false);
		userNameTextData.horizontalAlignment = SWT.FILL;
		userNameTextData.grabExcessHorizontalSpace = true;
		userNameTextData.horizontalSpan = 3;

		/* PASSWORD 입력 */
		GridData passWordLabelData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		Label passwordLabel = new Label(descriptionGrp, SWT.NONE);
		passwordLabel
				.setText(ComMngtMessages.customizeTableCreationPage28);
		passwordLabel.setLayoutData(passWordLabelData);
		GridData passWordTextData = new GridData();
		passwordField = new Text(descriptionGrp, SWT.PASSWORD | SWT.BORDER);
		passwordField.setLayoutData(passWordTextData);
		passwordField.setEditable(false);
		passWordTextData.horizontalAlignment = SWT.FILL;
		passWordTextData.grabExcessHorizontalSpace = true;
		passWordTextData.horizontalSpan = 3;

		/* Connection Test 버튼 */
		GridData gData = new GridData();
		connectionBtn = new Button(descriptionGrp, SWT.NONE);
		connectionBtn.setLayoutData(gData);
		connectionBtn.setEnabled(false);
		connectionBtn.setText(ComMngtMessages.customizeTableCreationPage32);
		connectionBtn.addSelectionListener(connectionTestListener);
		gData.grabExcessHorizontalSpace = true;
		gData.horizontalSpan = 3;
		gData.horizontalAlignment = SWT.END;

		/* Create Table 버튼 */
		GridData cData = new GridData();
		createTableBtn = new Button(descriptionGrp, SWT.NONE);
		createTableBtn.setLayoutData(cData);
		createTableBtn.setEnabled(false);
		createTableBtn.setText(ComMngtMessages.customizeTableCreationPage33);
		createTableBtn.addSelectionListener(createTableListener);
		cData.horizontalSpan = 1;
		cData.horizontalAlignment = SWT.END;

	}

	/**
	 * Create Table Viewer
	 * 
	 * @param parent
	 */
	private void createGrp(Composite parent) {

		GridData gData = new GridData(GridData.FILL_BOTH);
		gData.horizontalSpan = 4;
		gData.heightHint = 180;
		gData.widthHint = 100;

		Group descriptionGrp = new Group(parent, SWT.NONE);
		descriptionGrp.setLayout(new GridLayout());
		descriptionGrp
				.setText(ComMngtMessages.customizeTableCreationPage33);

		viewer = new TableViewer(descriptionGrp, SWT.V_SCROLL | SWT.READ_ONLY
				| SWT.CENTER | SWT.BORDER | SWT.FULL_SELECTION);
		viewer.getTable().setLinesVisible(true);
		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLayoutData(gData);

		// treeviewer에 selection이 존재할경우 aList에서 성공/실패 여부를 가져오고, 메세지 출력
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent e) {
				IStructuredSelection selection = (IStructuredSelection) viewer
						.getSelection();

				if (!NullUtil.isNull(selection) && selection.size() > 0) {
					SqlStatementModel sqlStatementModel = (SqlStatementModel) selection
							.getFirstElement();
					if (sqlStatementModel.getErrorCode() != null) {
						setErrorMessage(sqlStatementModel.getErrorCode());
					} else {
						setErrorMessage(null);
					}

				}
			}
		});

		TableColumn col1 = new TableColumn(viewer.getTable(), SWT.NONE);
		col1.setText(ComMngtMessages.customizeTableCreationPage35);
		col1.setWidth(170);
		TableColumn col2 = new TableColumn(viewer.getTable(), SWT.NONE);
		col2.setText(ComMngtMessages.customizeTableCreationPage36);
		col2.setWidth(120);
		TableColumn col3 = new TableColumn(viewer.getTable(), SWT.NONE);
		col3.setText(ComMngtMessages.customizeTableCreationPage37);
		col3.setWidth(160);

		viewer.setContentProvider(new IStructuredContentProvider() {

			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				// inputChanged
			}

			public void dispose() {
				// dispose
			}

			@SuppressWarnings("unchecked")
			public Object[] getElements(Object inputElement) {

				ArrayList<SqlStatementModel> list = (ArrayList<SqlStatementModel>) inputElement;
				return list.toArray();
			}

		});

		viewer.setLabelProvider(new CheckedComponentTableLableProvider());
		descriptionGrp.setLayoutData(gData);
		descriptionGrp.pack();
	}


	


	/**
	 * Finish버튼 클릭시 이벤트
	 * 
	 * @param monitor
	 * */
	public void performOk(IProgressMonitor monitor) {
		monitor.beginTask(ComMngtMessages.customizeTableCreationPage74, 1);

		HandlingPropertiesUtil.setContext(context);
//		HandlingPropertiesUtil.setProperties();
		HandlingPropertiesUtil.storeProperties(context);

		monitor.done();
	}

	/**
	 * 페이지 완성여부 update
	 * */
	private void updatePageComplete() {
		setPageComplete(false);
		// 페이지의 완결성 체크
		if (isPageComplete())
			return;
		// 페이지의 완결성 체크를 건너 뛰었다면 페이지를 완료상태로 변경
		setPageComplete(true);
	}

	/**
	 * 이 페이지로 넘어왔을 때 호출 앞에서 선택한 값 셋팅
	 * 
	 * @param visible
	 */
	public void setVisible(boolean visible) {

		if (visible) {
			// insertDatasourcePage로 넘어왔다는 것을 알려준다
			checkLastPage = ComMngtMessages.customizeTableCreationPage75;

			// 체크한 컴포넌트의 내용을 checkedComponent에 담는다
			checkedComponent = context.getComponent();

			// SqlStatementModel에 선택한 컴포넌트를 우선 set해준다
			ssm.clear();
			SettingSqlStatementUtil.setSqlStatementModel(checkedComponent, ssm);

			// Table view 에 보여 주기 위해 component 셋팅
			if (!checkedComponent.isEmpty()) {
				viewer.setInput(ssm);
			}
			
			selectDBCombo.deselectAll();
			dbTypeCombo.deselectAll();
			driverClassNameField.setText("");
			urlField.setText("");
			usernameField.setText("");
			passwordField.setText("");
			connectionBtn.setEnabled(false);
			createTableBtn.setEnabled(false);
			successCreate = false;
			isPageComplete();
			updatePageComplete();
			

			// 데이터소스 익스플로러 DB 정보 부재
			if (selectDBCombo.getItems().length == 0) {
				setMessage(ComMngtMessages.customizeTableCreationPage76, SWT.BOLD);
				selectDBCombo.setEnabled(false);
			}
			
			File globalsFile = HandlingPropertiesUtil.getPropertiesFile();
			if (globalsFile == null) {
				isExist = false;
			} else {
				if (globalsFile.getName().indexOf(ComMngtMessages.customizeTableCreationPage2) >= 0) {
					isExist = true;
				}
			}
						
			// 공통 컴포넌트 컴텍스트에 초기 값 셋팅
			if(dbType != null){
				context.setDBType(dbType);
			}else{
				context.setDBType(""); //$NON-NLS-1$
			}
			if(url != null){
				context.setUrl(url);
			}else{
				context.setUrl(""); //$NON-NLS-1$
			}
			if(username != null){
				context.setUsername(username);
			}else{
				context.setUsername(""); //$NON-NLS-1$
			}
			if(password != null){
				context.setPassword(password);
			}else{
				context.setPassword(""); //$NON-NLS-1$
			}
			
			//properties 유무 체크
			File globalsFileRe = HandlingPropertiesUtil.getPropertiesFile();
			if (globalsFileRe == null) {
				isExist = false;
			} else {
				if (globalsFile.getName().indexOf(ComMngtMessages.customizeTableCreationPage1) >= 0) {
					isExist = true;
				}
			}
			
			if(isExist){
				//프로퍼티 파일과 비교하는 로직
				getJDBCInfo();
				
				// connection 버튼 활성화 여부
				if (driverClassNameField.getText().length() > 0
						&& urlField.getText().length() > 0
						&& dbTypeCombo.getText().length() > 0) {
					connectionBtn.setEnabled(true);

				}
			}

		}

		super.setVisible(visible);
	}

	/**
	 * 테이블에 값 셋팅
	 */
	class CheckedComponentTableLableProvider extends LabelProvider implements
			ITableLabelProvider {

		/**
		 * @param element
		 * @param columnIndex
		 * @return null
		 */
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		/**
		 * @param element
		 * @param columnIndex
		 * @return 각 행 삽입내용
		 */
		public String getColumnText(Object element, int columnIndex) {

			if (element instanceof SqlStatementModel) {
				SqlStatementModel item = (SqlStatementModel) element;

				switch (columnIndex) {
				case 0:
					return item.getStmtName();
				case 1:
					return item.getResultMessage();
				case 2:
					return item.getComponent();
				default : return null;
				}

			}
			return null;
		}
	}

}
