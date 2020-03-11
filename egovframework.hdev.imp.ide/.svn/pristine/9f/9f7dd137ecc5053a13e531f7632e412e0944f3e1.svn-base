package egovframework.hdev.imp.ide.pages;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.sqltools.core.profile.NoSuchProfileException;
import org.eclipse.datatools.sqltools.core.profile.ProfileUtil;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import egovframework.dev.imp.core.common.DataToolsPlatformUtil;
import egovframework.hdev.imp.ide.common.DatabaseUtil;
import egovframework.hdev.imp.ide.common.DeviceAPIIdeLog;
import egovframework.hdev.imp.ide.common.DeviceAPIMessages;
import egovframework.hdev.imp.ide.common.NullUtil;
import egovframework.hdev.imp.ide.model.DeviceAPIContext;
import egovframework.hdev.imp.ide.wizards.examples.DeviceAPITemplateInfo;

/**  
 * @Class Name : CustomizeTableCreationPage
 * @Description : CustomizeTableCreationPage Class
 * @Modification Information  
 * @
 * @  수정일			수정자		수정내용
 * @ ---------		---------	-------------------------------
 * @ 2012. 9. 17.		조용현		최초생성
 * 
 * @author 디바이스 API 개발환경 팀
 * @since 2012. 9. 17.
 * @version 1.0
 * @see
 * 
 */
public class CustomizeTableCreationPage extends WizardPage {

	/** DeviceAPIContext */
	DeviceAPIContext context;

	/** Table 목록 */
	TableViewer tableList;

	/** DB 선택 Combo */
	private Combo selectDBCombo;

	/** DB type Combo */
	private Combo dbTypeCombo;

	/** Driver Class Name Text */
	private Text driverClassNameField;

	/** URL Text */
	private Text urlField;

	/** User Name Text */
	private Text usernameField;

	/** Password Text */
	private Text passwordField;

	/** Database이 연결된 경우 연결된 Connection 객체 */
	private Connection conn;

	/** Table 생성 Button(SWT.Push) */
	private Button createTableButton;

	/** 사용자가 선택한 datasource 정보 번호 */
	public static int selectedDatasourceNum;

	/**
	 * CustomizeTableCreation 생성자
	 * @param pageName
	 * @param context
	 */
	public CustomizeTableCreationPage(String pageName, DeviceAPIContext context) {
		super(pageName);

		this.context = context;

		setTitle(DeviceAPIMessages.CUSTOMIZE_TABLE_CREATION_PAGE_TITLE);
		setDescription(DeviceAPIMessages.CUSTOMIZE_TABLE_CREATION_PAGE_DESCRIPTION);
	}

	/**
	 * @param CustomizeTableCreationPage
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		Composite control = new Composite(parent, SWT.None);
		control.setLayout(new GridLayout());
		control.setLayoutData(new GridData(GridData.FILL_BOTH));

		createSelectDbCombo(control);

		insertDatasourceGrp(control);

		createTableControl(control);

		setControl(control);
	}

	/**
	 * Database ComboBox(selectDBCombo) 생성, Listener 생성
	 * @param CustomizeTableCreationPage
	 * @param parent void
	 */
	private void createSelectDbCombo(Composite parent) {

		Composite control = new Composite(parent, SWT.None);
		control.setLayout(new GridLayout(2, false));
		control.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label selectDBLabel = new Label(control, SWT.None);
		selectDBLabel.setText(DeviceAPIMessages.SELECT_DB_LABEL);

		selectDBCombo = new Combo(control, SWT.FILL);
		selectDBCombo.setItems(DataToolsPlatformUtil.getProfileNames());
		selectDBCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL));
		selectDBCombo.addListener(SWT.Selection, selectDBListener);
	}

	/**
	 * DB입력 정보(dbTypeCombo, driverClassName, url, username, password) 생성
	 * Connection Test, Create Table 버튼 생성
	 * @param parent
	 */
	private void insertDatasourceGrp(Composite parent) {

		Group descriptionGrp = new Group(parent, SWT.FILL);
		descriptionGrp.setText(DeviceAPIMessages.DATASOURCE_GROUP_TEXT);
		descriptionGrp.setLayout(new GridLayout(2, false));
		descriptionGrp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		/* DB Type */
		GridData dbLabelData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		dbLabelData.widthHint = 120;
		GridData dbTypeInfoData = new GridData(GridData.FILL_HORIZONTAL);

		Label dbTypeLabel = new Label(descriptionGrp, SWT.RIGHT);
		dbTypeLabel.setText(DeviceAPIMessages.DBTYPE_LABEL);
		dbTypeLabel.setLayoutData(dbLabelData);

		dbTypeCombo = new Combo(descriptionGrp, SWT.READ_ONLY | SWT.FILL);
		String items[] = { DeviceAPIMessages.MYSQL_DBTYPE,
				DeviceAPIMessages.ORACLE_DBTYPE,
				DeviceAPIMessages.ALTIBASE_DBTYPE,
				DeviceAPIMessages.TIBERO_DBTYPE }; //$NON-NLS-1$
		dbTypeCombo.setItems(items);
		dbTypeCombo.setLayoutData(dbTypeInfoData);
		dbTypeCombo.setEnabled(false);
		dbTypeCombo.deselectAll();

		/* Driver Class Name */
		Label driverClassNameLabel = new Label(descriptionGrp, SWT.RIGHT);
		driverClassNameLabel.setText(DeviceAPIMessages.DRIVER_CLASS_NAME_LABEL);
		driverClassNameLabel.setLayoutData(dbLabelData);

		driverClassNameField = new Text(descriptionGrp, SWT.BORDER);
		driverClassNameField.setLayoutData(dbTypeInfoData);
		driverClassNameField.setEnabled(false);

		/* URL 입력 */
		Label urlLabel = new Label(descriptionGrp, SWT.RIGHT);
		urlLabel.setText(DeviceAPIMessages.URL_LABEL);
		urlLabel.setLayoutData(dbLabelData);

		urlField = new Text(descriptionGrp, SWT.BORDER);
		urlField.setLayoutData(dbTypeInfoData);
		urlField.setEnabled(false);

		/* USERNAME 입력 */
		Label usernameLabel = new Label(descriptionGrp, SWT.RIGHT);
		usernameLabel.setText(DeviceAPIMessages.USER_NAME_LABEL);
		usernameLabel.setLayoutData(dbLabelData);

		usernameField = new Text(descriptionGrp, SWT.BORDER);
		usernameField.setLayoutData(dbTypeInfoData);
		usernameField.setEnabled(false);

		/* PASSWORD 입력 */
		Label passwordLabel = new Label(descriptionGrp, SWT.RIGHT);
		passwordLabel.setText(DeviceAPIMessages.PASSWORD_LABEL);
		passwordLabel.setLayoutData(dbLabelData);

		passwordField = new Text(descriptionGrp, SWT.PASSWORD | SWT.BORDER);
		passwordField.setLayoutData(dbTypeInfoData);
		passwordField.setEnabled(false);

		createButtonControl(descriptionGrp);
	}

	/** Connection Test, Create Table 버튼 생성 및 Listener 할당 */
	private void createButtonControl(Composite control) {
		Composite buttonControl = new Composite(control, SWT.None);
		buttonControl.setLayout(new GridLayout(2, true));

		GridData gData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		gData.horizontalSpan = 2;

		buttonControl.setLayoutData(gData);

		Button testButton = new Button(buttonControl, SWT.PUSH);
		testButton.setText(DeviceAPIMessages.TEST_BUTTON_TEXT);
		testButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		testButton.addListener(SWT.Selection, testConn);

		createTableButton = new Button(buttonControl, SWT.PUSH);
		createTableButton.setText(DeviceAPIMessages.CREATE_TABLE_BUTTON_TEXT);
		createTableButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		createTableButton.setEnabled(false);
		createTableButton.addListener(SWT.Selection, createTable);
	}

	/**
	 * Table의 TableViewer 생성
	 */
	private void createTableControl(Composite control) {

		Group createTableGroup = new Group(control, SWT.None);
		createTableGroup.setLayout(new GridLayout());
		createTableGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		createTableGroup.setText(DeviceAPIMessages.CREATE_TABLE_GROUP_TEXT);

		tableList = new TableViewer(createTableGroup, SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);

		Table table = tableList.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));

		String[] columnNames = new String[] {
				DeviceAPIMessages.CREATE_TABLE_TABLEVIEWER_COLUMN_TABLENAME,
				DeviceAPIMessages.CREATE_TABLE_TABLEVIEWER_COLUMN_RESULT,
				DeviceAPIMessages.CREATE_TABLE_TABLEVIEWER_COLUMN_DESCRIPTION };
		int[] columnAlignment = new int[] { SWT.LEFT, SWT.LEFT, SWT.LEFT };
		int[] columnWidth = new int[] { 150, 150, 150 };

		for (int i = 0; i < columnNames.length; i++) {
			TableColumn column = new TableColumn(table, columnAlignment[i]);
			column.setText(columnNames[i]);
			column.setWidth(columnWidth[i]);
		}

		tableList.setContentProvider(new ArrayContentProvider());
		tableList.setLabelProvider(new DeviceAPITableLabelProvider());
	}

	/**
	 * @param CustomizeTableCreationPage
	 * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {

		try {

			if (visible) {

				DeviceAPITable apiTable = null;

				List<String> tableNameList = DeviceAPITemplateInfo.getTableList();
				HashMap<String, String> tableDescMap = DeviceAPITemplateInfo.getTableDesc();

				ArrayList<DeviceAPITable> tableContentList = new ArrayList<DeviceAPITable>();
				for (int i = 0; i < tableNameList.size(); i++) {

					String tableName = tableNameList.get(i);
					String tableDesc = tableDescMap.get(tableName);

					apiTable = new DeviceAPITable(tableName, "", tableDesc);

					tableContentList.add(apiTable);
				}

				tableList.setInput(tableContentList);
			}

			super.setVisible(visible);
		} catch (Exception e) {

			DeviceAPIIdeLog.logError(e);
		}
	}

	/**
	 * 라벨 제공자 클래스
	 */
	class DeviceAPITableLabelProvider extends LabelProvider implements
			ITableLabelProvider {

		@Override
		public String getColumnText(Object element, int columnIndex) {
			DeviceAPITable item = (DeviceAPITable) element;
			switch (columnIndex) {
			case 0:
				return item.getTableName();
			case 1:
				return item.getTableInstall();
			case 2:
				return item.getTableDesc();
			default:
				return "";
			}
		}

		@Override
		public Image getColumnImage(Object arg0, int columnIndex) {

			return null;
		}

	}

	/**
	 * 라벨 제공자 클래스
	 */
	class DeviceAPITable {

		private String tableName;
		private String tableInstall;
		private String tableDesc;

		public DeviceAPITable(String tableName, String tableInstall,
				String tableDesc) {

			this.tableName = tableName;
			this.tableInstall = tableInstall;
			this.tableDesc = tableDesc;
		}

		public String getTableName() {
			return tableName;
		}

		public void setTableName(String tableName) {
			this.tableName = tableName;
		}

		public String getTableInstall() {
			return tableInstall;
		}

		public void setTableInstall(String tableInstall) {
			this.tableInstall = tableInstall;
		}

		public String getTableDesc() {
			return tableDesc;
		}

		public void setTableDesc(String tableDesc) {
			this.tableDesc = tableDesc;
		}

	}

	/** selectDBCombo 선택시 Listener */
	Listener selectDBListener = new Listener() {

		public void handleEvent(Event event) {
			if (!NullUtil.isEmpty(selectDBCombo)) {
				Map<String, String> dBProfile = DataToolsPlatformUtil
						.getProperty(selectDBCombo.getText());
				if (!dBProfile.isEmpty()) {
					usernameField.setText(dBProfile.get("username"));
					passwordField.setText(dBProfile.get("password"));
					driverClassNameField.setText(dBProfile.get("driverClass"));
					urlField.setText(dBProfile.get("url"));

					selectedDatasourceNum = selectDBCombo.getSelectionIndex();

					String[] items = dbTypeCombo.getItems();
					String rawDBType = dBProfile.get("driverClass");
					for (String item : items) {
						if (rawDBType.toLowerCase().indexOf(item.toLowerCase()) > -1) {
							dbTypeCombo.setText(item);
							break;
						}
					}
				}
			}
		}
	};

	/** Test Connection 버튼 선택시 Listener */
	Listener testConn = new Listener() {

		public void handleEvent(Event event) {

			if (!NullUtil.isNull(conn)) {
				try {
					conn.close();
				} catch (SQLException e) {

				}
			}

			try {
				IConnectionProfile profile = ProfileUtil
						.getProfile(selectDBCombo.getText());
				conn = ProfileUtil.createConnection(profile,
						dbTypeCombo.getText());

			} catch (NoSuchProfileException e1) {
				setErrorMessage(DeviceAPIMessages.ERROR_CANNOT_CONNECT_DB);
				createTableButton.setEnabled(false);
				return;
			}

			if (NullUtil.isNull(conn)) {
				setErrorMessage(DeviceAPIMessages.ERROR_CANNOT_CONNECT_DB);
				createTableButton.setEnabled(false);
				return;
			}

			try {
				if (!conn.isClosed()) {
					setErrorMessage(null);
					setMessage(DeviceAPIMessages.SUCCESS_CONNECT_DB, INFORMATION);
					createTableButton.setEnabled(true);
				}
			} catch (SQLException e) {
				setErrorMessage(DeviceAPIMessages.ERROR_CANNOT_CONNECT_DB);
				createTableButton.setEnabled(false);
			}

		}
	};

	/** 테이블 생성 이벤트 리스너 */
	Listener createTable = new Listener() {

		@SuppressWarnings({ "unchecked", "rawtypes" })
		public void handleEvent(Event event) {

			try {

				DeviceAPITable table = null;

				String fileName = "examples/web/" + context.getWebExampleFile();
				String dbType = dbTypeCombo.getText().toLowerCase();

				ArrayList<DeviceAPITable> list = (ArrayList<DeviceAPITable>) tableList.getInput();
				ArrayList<DeviceAPITable> updateList = new ArrayList<CustomizeTableCreationPage.DeviceAPITable>();

				String fileContent = DatabaseUtil.getScriptFileinZip(fileName, dbType).get("create");
				String fileContentDml = DatabaseUtil.getScriptFileinZip(fileName, dbType).get("insert");
				HashMap<String, HashMap> mapDDL = DatabaseUtil.getSqlStatementFromDDL(fileContent);
				HashMap<String, String> mapDML = DatabaseUtil.setSqlStatementFromDML(fileContentDml);
				HashMap<String, String> createMap = mapDDL.get("TABLE");

				String sql = "";
				for (int i = 0; i < list.size(); i++) {

					table = list.get(i);

					String tableName = table.getTableName();
					try {

						if (DatabaseUtil.isExistTable(tableName.toUpperCase())) {

							table.setTableInstall(DeviceAPIMessages.CREATE_TABLE_TABLEVIEWER_COLUMN_RESULT_ALREADY_EXIST);
						} else {
							sql = createMap.get(tableName.toUpperCase());

							if (DatabaseUtil.excuteSQL(sql)) {
								
								String insertSQL = mapDML.get(tableName.toUpperCase());
								if(insertSQL != null && !"".equals(insertSQL)) {
									DatabaseUtil.excuteSQL(insertSQL);
								}
									
								table.setTableInstall(DeviceAPIMessages.CREATE_TABLE_TABLEVIEWER_COLUMN_RESULT_SUCCESS);
							} else {

								table.setTableInstall(DeviceAPIMessages.CREATE_TABLE_TABLEVIEWER_COLUMN_RESULT_FAILED);
							}
						}
					} catch (Exception e) {

						table.setTableInstall(DeviceAPIMessages.CREATE_TABLE_TABLEVIEWER_COLUMN_RESULT_FAILED);
					}

					updateList.add(table);
				}

				tableList.setInput(updateList);

			} catch (Exception e) {

				DeviceAPIIdeLog.logError(e);
			}
		}
	};

}
