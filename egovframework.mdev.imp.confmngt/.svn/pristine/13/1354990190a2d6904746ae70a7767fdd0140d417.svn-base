/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package egovframework.mdev.imp.confmngt.properties;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.datatools.sqltools.core.profile.NoSuchProfileException;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;

import egovframework.dev.imp.core.common.DataToolsPlatformUtil;
import egovframework.dev.imp.core.utils.EgovProperties;
import egovframework.dev.imp.core.utils.NullUtil;
import egovframework.mdev.imp.confmngt.common.MobileConfMngtLog;
import egovframework.mdev.imp.confmngt.common.MobileConfMngtMessages;
import egovframework.mdev.imp.ide.common.ResourceUtils;

/**
 * 공통컴포넌트의 설정정보를 관리하는 Property Page
 * 
 * @author 개발환경 개발팀 조윤정
 * @since 2011.06.13
 * @version 1.0
 * @see <pre>
 *      개정이력(Modification Information)
 *   
 *   수정일      수정자           수정내용
 *  ------- -------- ---------------------------
 *  2011.06.13  조윤정     최초 생성
 * 
 * 
 * </pre>
 */

public class MobileProjectConfPropertyPage extends PropertyPage implements IWorkbenchPropertyPage {

	/** Properties 생성 */
	private static Properties properties = new Properties();

	/** Local IP Field */
	private Text localIpField;
	/** Main Page Field */
	private Text mainPageField;
	/** OS Type Combo Field */
	private Combo osTypeCombo;

	/** DB 사용에 대한 선택 */
	private Label checkDB;
	private Combo selectDbTypeCombo;
	private Combo dbTypeCombo;
	private Text driverClassNameField;
	private Text urlField;
	private Text usernameField;
	private Text passwordField;
	private Button connectionBtn;
	private boolean isExist;

	private String osType;
	private String mainPage;
	private String localIp;

	private String dbType;
	private String driverClassName;
	private String url;
	private String username;
	private String password;

	public String getPassword() {
		return password;
	}

	private static Object resource = null;
	private static IAdaptable element = null;
	private static File propertiesFile = null;

	private int selectedDatasourceNum;

	/**
	 * ConfPropertyPage의 생성자.
	 */
	public MobileProjectConfPropertyPage() {
		setDescription(MobileConfMngtMessages.mobileConfPropertyPageDESC);
	}

	/**
	 * 해당 Property Page에 입력한 값을 저장할 프로젝트를 리턴함
	 */
	private IProject getSelectedProject() {
		element = getElement();
		if (element instanceof IProject) {
			return (IProject) element;
		}
		resource = element.getAdapter(IResource.class);
		if (resource instanceof IProject) {
			return (IProject) resource;
		}
		return null;
	}

	private File getPropertiesFile() {
		propertiesFile = EgovProperties.getPropertiesFile(JavaCore.create(getSelectedProject().getProject()));
		return propertiesFile;
	}

	/**
	 * 
	 */
	private void setFileIsExist() {
		File globalsFile = getPropertiesFile();
		if (globalsFile == null) {
			isExist = false;
		} else {
			if (globalsFile.getName().indexOf("globals.properties") >= 0) { //$NON-NLS-1$
				isExist = true;
			}
		}
	}

	/**
	 * @return globals.properties 파일 경로
	 */
	private String getFilePath() {
		IPath prjPath = getSelectedProject().getProject().getLocation();
		String[] prjPathWithoutName = prjPath.toString().split(getSelectedProject().getProject().getName());

		String propertiesFilePath = prjPathWithoutName[0] + getPropertiesFile();
		return propertiesFilePath;
	}

	/**
	 * Select DB를 선택한 경우(Data Source Explorer에 연결되어 있는 Datasource를 사용할 경우),
	 * 선택한 Datasource에 대한 정보를 가져옴
	 *
	 * */
	SelectionListener dbSelectionListener = new SelectionListener() {

		public void widgetSelected(SelectionEvent e) {
			selectedDatasourceNum = selectDbTypeCombo.getSelectionIndex();

			Map<String, String> profile = DataToolsPlatformUtil.getProperty(selectDbTypeCombo.getText().trim());

			String driverClass = (String) profile.get(MobileConfMngtMessages.mobileConfPropertyPageDRIVERCLASS);

			driverClassNameField.setText(driverClass);
			connectionBtn.setEnabled(true);
			setMessage(null);
			setValid(true);

			if (driverClass.toLowerCase().indexOf(MobileConfMngtMessages.mobileConfPropertyPageMYSQL) > -1) {
				dbTypeCombo.setText(MobileConfMngtMessages.mobileConfPropertyPageMYSQL);
			} else if (driverClass.toLowerCase().indexOf(MobileConfMngtMessages.mobileConfPropertyPageORACLE) > -1) {
				dbTypeCombo.setText(MobileConfMngtMessages.mobileConfPropertyPageORACLE);

			} else if (driverClass.toLowerCase().indexOf(MobileConfMngtMessages.mobileConfPropertyPageALTIBASE) > -1) {
				dbTypeCombo.setText(MobileConfMngtMessages.mobileConfPropertyPageALTIBASE);
			} else if (driverClass.toLowerCase().indexOf(MobileConfMngtMessages.mobileConfPropertyPageTIBERO) > -1) {
				dbTypeCombo.setText(MobileConfMngtMessages.mobileConfPropertyPageTIBERO);
			} else {
				dbTypeCombo.setText("");
				setMessage(MobileConfMngtMessages.mobileConfPropertyPageWARN, WARNING);
				connectionBtn.setEnabled(false);
				//					setValid(false);
			}

			urlField.setText(NullUtil.isNone((String) profile.get(MobileConfMngtMessages.mobileConfPropertyPageURL)) ? "" //$NON-NLS-2$
					: (String) profile.get(MobileConfMngtMessages.mobileConfPropertyPageURL));
			usernameField
					.setText(NullUtil.isNone((String) profile.get(MobileConfMngtMessages.mobileConfPropertyPageUSERNAME)) ? "" : (String) profile.get(MobileConfMngtMessages.mobileConfPropertyPageUSERNAME)); //$NON-NLS-2$
			passwordField
					.setText(NullUtil.isNone((String) profile.get(MobileConfMngtMessages.mobileConfPropertyPagePASSWORD)) ? "" : (String) profile.get(MobileConfMngtMessages.mobileConfPropertyPagePASSWORD)); //$NON-NLS-2$
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			widgetSelected(e);
		}
	};

	/**
	 * Connection Test
	 */
	SelectionListener connectionTestListener = new SelectionListener() {
		boolean isConnect;
		Connection conn = null;

		public void widgetSelected(SelectionEvent e) {

			Runnable runnable = new Runnable() {
				public void run() {
					try {
						conn = DataToolsPlatformUtil.getConnection(DataToolsPlatformUtil.getDatabaseIdentifier(DataToolsPlatformUtil.getConnectionInfo(DataToolsPlatformUtil
								.getProfileNames()[selectedDatasourceNum])));

					} catch (SQLException e) {
						MobileConfMngtLog.logError(e);
					} catch (NoSuchProfileException e) {
						MobileConfMngtLog.logError(e);
					} finally {
						if (conn != null) {
							try {
								isConnect = true;
								conn.close();
							} catch (SQLException e) {
								MobileConfMngtLog.logError(e);
							}
						} else {
							isConnect = false;
						}
					}
				}
			};

			BusyIndicator.showWhile(connectionBtn.getDisplay(), runnable);

			//				MessageDialog.openInformation(getShell(),
			//						MobileConfMngtMessages.mobileConfPropertyPageDBCONN,
			//						isConnect ? MobileConfMngtMessages.mobileConfPropertyPageSUCCESS
			//								: MobileConfMngtMessages.mobileConfPropertyPageFAIL + DataSource.getErrorMessage());

			if (isConnect) {
				if (dbType.indexOf("mysql") > -1 || dbType.indexOf("oracle") > -1 || dbType.indexOf("altibase") > -1 || dbType.indexOf("tibero") > -1) {
					connectionBtn.setEnabled(true);
				} else {
					setMessage("설치된 공통 컴포넌트는 MySql, Oracle, Tibero,\n" + "Altibase기준으로 제공됩니다.", WARNING);
				}

				MessageDialog.openInformation(getShell(), "Database Connection", "Connection Succeeded!");
				//				connectionBtn.setEnabled(true);
			}
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			widgetSelected(e);
		}
	};

	private GridData createDefaultGridData() {
		GridData gridData = new GridData();
		gridData.horizontalSpan = 2;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		return gridData;
	}

	/**
	 * Project Configuration PropertyPage을 구성하는 메소드
	 * 
	 * @param parent
	 * @return
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createContents(Composite parent) {
		noDefaultAndApplyButton();

		GridLayout layout;
		GridData gridData;

		Composite container = new Composite(parent, SWT.NONE);

		layout = new GridLayout();
		layout.numColumns = 2;
		container.setLayout(layout);

		gridData = new GridData();
		gridData.horizontalSpan = 2;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		container.setLayoutData(gridData);

		setFileIsExist();
		createOsTypeGrp(container, createDefaultGridData(), layout);
		createMainPageGrp(container, createDefaultGridData(), layout);
		createLocalIpGrp(container, createDefaultGridData(), layout);
		createDatabaseInfo(container, createDefaultGridData(), layout);

		dbTypeCombo.setEnabled(false);
		driverClassNameField.setEnabled(false);
		urlField.setEnabled(false);
		usernameField.setEnabled(false);
		passwordField.setEnabled(false);

		if (isExist) {
			osTypeCombo.setEnabled(true);
			localIpField.setEnabled(true);
			mainPageField.setEnabled(true);

		} else {
			osTypeCombo.setEnabled(false);
			localIpField.setEnabled(false);
			mainPageField.setEnabled(false);
			selectDbTypeCombo.setEnabled(false);
			setErrorMessage(MobileConfMngtMessages.mobileConfPropertyPageFILENOTEXIST);
			//			setValid(false);
		}

		return container;
	}

	/**
	 * @param container
	 * @param gridData
	 * @param layout
	 */
	private void createOsTypeGrp(Composite container, GridData gridData, GridLayout layout) {
		// Operating System Type
		Group osTypeGrp = new Group(container, SWT.NULL);
		osTypeGrp.setText(MobileConfMngtMessages.mobileConfPropertyPageOSDESC);
		osTypeGrp.setLayout(layout);
		osTypeGrp.setLayoutData(gridData);

		new Label(osTypeGrp, SWT.NONE).setText(MobileConfMngtMessages.mobileConfPropertyPageOSTYPE);
		osTypeCombo = new Combo(osTypeGrp, SWT.NONE | SWT.READ_ONLY);
		osTypeCombo.setItems(new String[] { MobileConfMngtMessages.mobileConfPropertyPageWINDOWS, MobileConfMngtMessages.mobileConfPropertyPageUNIX });

		if (isExist && NullUtil.notNone(EgovProperties.getProperty(getFilePath(), MobileConfMngtMessages.mobileConfPropertyPageGLOBALSOSTYPE))) {
			osType = EgovProperties.getProperty(getFilePath(), MobileConfMngtMessages.mobileConfPropertyPageGLOBALSOSTYPE);
		} else {
			osType = ""; //$NON-NLS-1$
		}
		osTypeCombo.setText(osType);
		osTypeCombo.setEnabled(false);

		GridData osTypeLayoutData = new GridData(GridData.FILL_HORIZONTAL);
		osTypeLayoutData.horizontalSpan = 2;
		Label osTypeLabel = new Label(osTypeGrp, SWT.NONE);
		osTypeLabel.setLayoutData(osTypeLayoutData);
		osTypeLabel.setText(MobileConfMngtMessages.mobileConfPropertyPageOSNOTE);
	}

	/**
	 * @param container
	 * @param gridData
	 * @param layout
	 */
	private void createMainPageGrp(Composite container, GridData gridData, GridLayout layout) {
		// Main Page
		Group mainPageGrp = new Group(container, SWT.NULL);
		mainPageGrp.setText(MobileConfMngtMessages.mobileConfPropertyPageMAINPAGEGRP);
		mainPageGrp.setLayout(layout);
		mainPageGrp.setLayoutData(gridData);

		new Label(mainPageGrp, SWT.NONE).setText(MobileConfMngtMessages.mobileConfPropertyPageMAINPAGE);
		mainPageField = new Text(mainPageGrp, 2052);
		mainPageField.setLayoutData(new GridData(768));

		if (isExist && NullUtil.notNone(EgovProperties.getProperty(getFilePath(), MobileConfMngtMessages.mobileConfPropertyPageGLOBALSMAINPAGE))) {
			mainPage = EgovProperties.getProperty(getFilePath(), MobileConfMngtMessages.mobileConfPropertyPageGLOBALSMAINPAGE);
		} else {
			mainPage = "";
		}
		mainPageField.setText(mainPage);
		mainPageField.setEnabled(false);

		GridData mainPageLayoutData = new GridData(GridData.FILL_HORIZONTAL);
		mainPageLayoutData.horizontalSpan = 2;
		Label mainPageLabel = new Label(mainPageGrp, SWT.NONE);
		mainPageLabel.setLayoutData(mainPageLayoutData);
		mainPageLabel.setText(MobileConfMngtMessages.mobileConfPropertyPageMAINPAGENOTE);

	}

	/**
	 * @param container
	 * @param gridData
	 * @param layout
	 */
	private void createLocalIpGrp(Composite container, GridData gridData, GridLayout layout) {
		// Local IP
		Group localIpGrp = new Group(container, SWT.NULL);
		localIpGrp.setText(MobileConfMngtMessages.mobileConfPropertyPageIPSETTINGGRP);
		localIpGrp.setLayout(layout);
		localIpGrp.setLayoutData(gridData);

		new Label(localIpGrp, SWT.NONE).setText(MobileConfMngtMessages.mobileConfPropertyPageIPTITLE);
		localIpField = new Text(localIpGrp, 2052);
		localIpField.setLayoutData(new GridData(768));

		if (isExist && NullUtil.notNone(EgovProperties.getProperty(getFilePath(), MobileConfMngtMessages.mobileConfPropertyPageGLOBALSIP))) {
			localIp = EgovProperties.getProperty(getFilePath(), MobileConfMngtMessages.mobileConfPropertyPageGLOBALSIP);
		} else {
			localIp = ""; //$NON-NLS-1$
		}

		localIpField.setText(localIp);
		localIpField.setEnabled(false);

		GridData localIpLayoutData = new GridData(GridData.FILL_HORIZONTAL);
		localIpLayoutData.horizontalSpan = 2;
		Label localIpLabel = new Label(localIpGrp, SWT.NONE);
		localIpLabel.setLayoutData(localIpLayoutData);
		localIpLabel.setText(MobileConfMngtMessages.mobileConfPropertyPageIPNOTE);
	}

	/**
	 * Database 정보
	 */
	private void createDatabaseInfo(Composite container, GridData gridData, GridLayout layout) {

		Group datasourceGrp = new Group(container, SWT.NONE);
		datasourceGrp.setText(MobileConfMngtMessages.mobileConfPropertyPageDBSETTINGGRP);
		datasourceGrp.setLayout(layout);
		datasourceGrp.setLayoutData(gridData);

		GridData dbCheckButton = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
		checkDB = new Label(datasourceGrp, SWT.NONE);
		checkDB.setLayoutData(dbCheckButton);
		checkDB.setText(MobileConfMngtMessages.mobileConfPropertyPageSELECTDB);

		selectDbTypeCombo = new Combo(datasourceGrp, SWT.READ_ONLY);
		selectDbTypeCombo.setItems(DataToolsPlatformUtil.getProfileNames());

		if (selectDbTypeCombo.getItems().length == 0) {
			setMessage("The specified Database Information does not exist in Data Source Explorer.", SWT.BOLD);
			selectDbTypeCombo.setEnabled(false);
		} else {
			selectDbTypeCombo.setEnabled(true);
		}

		GridData selectDBComboData = new GridData();
		selectDBComboData.horizontalAlignment = SWT.FILL;

		selectDbTypeCombo.setLayoutData(selectDBComboData);
		selectDbTypeCombo.addSelectionListener(dbSelectionListener);

		createDatasourcePropertyGrp(datasourceGrp);

	}

	/**
	 * @param datasourceGrp
	 */
	private void createDatasourcePropertyGrp(Group datasourceGrp) {
		/* 4가지 정보에 대한 group 생성 */
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;

		Group descriptionGrp = new Group(datasourceGrp, SWT.FILL);
		descriptionGrp.setText(MobileConfMngtMessages.mobileConfPropertyPageDB);
		GridData gdGroup = new GridData(GridData.FILL);
		descriptionGrp.setLayoutData(gdGroup);
		descriptionGrp.setLayout(gridLayout);
		gdGroup.horizontalAlignment = SWT.FILL;
		gdGroup.grabExcessHorizontalSpace = true;
		gdGroup.horizontalSpan = 4;

		/* DB Type */
		GridData dbLabelData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		Label dbTypeLabel = new Label(descriptionGrp, SWT.NONE);
		dbTypeLabel.setText(MobileConfMngtMessages.mobileConfPropertyPageDBTYPETITLE);
		dbTypeLabel.setLayoutData(dbLabelData);
		GridData dbTypeInfoData = new GridData(SWT.FILL);

		dbTypeCombo = new Combo(descriptionGrp, SWT.READ_ONLY | SWT.FILL);
		String items[] = { MobileConfMngtMessages.mobileConfPropertyPageMYSQL, MobileConfMngtMessages.mobileConfPropertyPageORACLE,
				MobileConfMngtMessages.mobileConfPropertyPageALTIBASE, MobileConfMngtMessages.mobileConfPropertyPageTIBERO, "" };
		dbTypeCombo.setItems(items);
		dbTypeCombo.setLayoutData(dbTypeInfoData);

		dbTypeInfoData.horizontalAlignment = SWT.FILL;
		dbTypeInfoData.grabExcessHorizontalSpace = true;
		dbTypeInfoData.horizontalSpan = 1;

		// Driver Class Name
		GridData driverClassNameData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		Label driverClassNameLabel = new Label(descriptionGrp, SWT.NONE);
		driverClassNameLabel.setText(MobileConfMngtMessages.mobileConfPropertyPageDRIVERCLASSTITLE);
		driverClassNameLabel.setLayoutData(driverClassNameData);

		GridData driverClassNameData1 = new GridData();
		driverClassNameField = new Text(descriptionGrp, SWT.BORDER);
		driverClassNameField.setLayoutData(driverClassNameData1);
		driverClassNameData1.horizontalAlignment = SWT.FILL;
		driverClassNameData1.grabExcessHorizontalSpace = true;
		driverClassNameData1.horizontalSpan = 1;

		driverClassNameField.setEnabled(false);

		/* URL 입력 */
		GridData urlLabelData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		Label urlLabel = new Label(descriptionGrp, SWT.NONE);
		urlLabel.setText(MobileConfMngtMessages.mobileConfPropertyPageURLTITLE);
		urlLabel.setLayoutData(urlLabelData);
		GridData urlTextData = new GridData();
		urlField = new Text(descriptionGrp, SWT.BORDER);

		urlField.setEnabled(false);

		urlField.setLayoutData(urlTextData);
		urlTextData.horizontalAlignment = SWT.FILL;
		urlTextData.grabExcessHorizontalSpace = true;
		urlTextData.horizontalSpan = 1;

		/* USERNAME 입력 */
		GridData userNameLabelData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		Label usernameLabel = new Label(descriptionGrp, SWT.NONE);
		usernameLabel.setText(MobileConfMngtMessages.mobileConfPropertyPageUSERNMTITLE);
		usernameLabel.setLayoutData(userNameLabelData);
		GridData userNameTextData = new GridData();
		usernameField = new Text(descriptionGrp, SWT.BORDER);
		usernameField.setLayoutData(userNameTextData);
		userNameTextData.horizontalAlignment = SWT.FILL;
		userNameTextData.grabExcessHorizontalSpace = true;
		userNameTextData.horizontalSpan = 1;

		usernameField.setEnabled(false);

		/* PASSWORD 입력 */
		GridData passwordLabelData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		Label passwordLabel = new Label(descriptionGrp, SWT.NONE);
		passwordLabel.setText(MobileConfMngtMessages.mobileConfPropertyPagePASSWORDTITLE);
		passwordLabel.setLayoutData(passwordLabelData);
		GridData passWordTextData = new GridData();
		passwordField = new Text(descriptionGrp, SWT.PASSWORD | SWT.BORDER);
		passwordField.setLayoutData(passWordTextData);
		passWordTextData.horizontalAlignment = SWT.FILL;
		passWordTextData.grabExcessHorizontalSpace = true;
		passWordTextData.horizontalSpan = 1;

		passwordField.setEnabled(false);

		GridData cData = new GridData();
		cData.grabExcessHorizontalSpace = true;
		cData.horizontalSpan = 3;
		cData.horizontalAlignment = SWT.END;

		connectionBtn = new Button(datasourceGrp, SWT.CENTER);
		connectionBtn.setEnabled(false);
		connectionBtn.setLayoutData(cData);
		connectionBtn.setText(MobileConfMngtMessages.mobileConfPropertyPageCONNTEST);
		connectionBtn.addSelectionListener(connectionTestListener);

		if (isExist) {
			dbType = EgovProperties.getProperty(getFilePath(), MobileConfMngtMessages.mobileConfPropertyPageGLOBALSDBTYPE);
			driverClassName = EgovProperties.getProperty(getFilePath(), MobileConfMngtMessages.mobileConfPropertyPageGLOBALSDRIVERCLASSNAME);
			url = EgovProperties.getProperty(getFilePath(), MobileConfMngtMessages.mobileConfPropertyPageGLOBALSURL);
			username = EgovProperties.getProperty(getFilePath(), MobileConfMngtMessages.mobileConfPropertyPageGLOBALSUSERNAME);
			password = EgovProperties.getProperty(getFilePath(), MobileConfMngtMessages.mobileConfPropertyPageGLOBALSPASSWORD);

			if (NullUtil.isNone(dbType)) {
				dbType = "";
			}
			if (NullUtil.isNone(driverClassName)) {
				driverClassName = "";
			}
			if (NullUtil.isNone(url)) {
				url = "";
			}
			if (NullUtil.isNone(username)) {
				username = "";
			}
			if (NullUtil.isNone(password)) {
				password = "";
			}

			String[] profileNames = DataToolsPlatformUtil.getProfileNames();
			if (profileNames.length > 0) {
				for (int i = 0; i < profileNames.length; i++) {

					setMessage(null);

					Map<String, String> profile = DataToolsPlatformUtil.getProperty(profileNames[i]);

					// datasource의 내용과 프로퍼티파일의 내용이 같다면
					if (username.equals((String) profile.get("username")) && url.equals((String) profile.get("url")) && driverClassName.equals((String) profile.get("driverClass"))) {
						// 화면에 뿌려줌
						if (selectDbTypeCombo != null) {
							selectDbTypeCombo.setText(profileNames[i]);
						}
						if (driverClassName.toLowerCase().indexOf("mysql") > -1) {
							dbType = "mysql";
						} else if (driverClassName.toLowerCase().indexOf("oracle") > -1) {
							dbType = "oracle";
						} else if (driverClassName.toLowerCase().indexOf("altibase") > -1) {
							dbType = "altibase";
						} else if (driverClassName.toLowerCase().indexOf("tibero") > -1) {
							dbType = "tibero";
						} else {
							dbType = "";
						}

						dbTypeCombo.setText(dbType);
						driverClassNameField.setText(NullUtil.isNone((String) profile.get("driverClass")) ? "" : (String) profile.get("driverClass"));
						urlField.setText(NullUtil.isNone((String) profile.get("url")) ? "" : (String) profile.get("url"));
						usernameField.setText(NullUtil.isNone((String) profile.get("username")) ? "" : (String) profile.get("username"));
						passwordField.setText(NullUtil.isNone((String) profile.get("password")) ? "" : (String) profile.get("password"));

						connectionBtn.setEnabled(true);
						setValid(true);
					} else {
						//						setValid(false);
						setMessage(MobileConfMngtMessages.mobileConfPropertyPageDSEXPLORER, INFORMATION);

						if (dbType != "") {
							setMessage(null);
						}
						dbType = "";

					}
				}
			} else {
				setErrorMessage(MobileConfMngtMessages.mobileConfPropertyPageNODSEXPLORER);
				//				setValid(false);
			}

		} else {
			dbType = "";
			driverClassName = "";
			url = "";
			username = "";
		}
	}

	//	/**
	//	 * Properties 파일에 수정된 정보를 저장하는 메소드
	//	 */
	//	private void storeProperties() {
	//		try {
	//			String encoding = MobileConfMngtMessages.mobileConfPropertyPageCOMMENT;
	//			String toISO_8859 = stringToUnicode(encoding);
	//
	//			properties.store(new FileOutputStream(getFilePath()), toISO_8859); //$NON-NLS-1$
	//		} catch (IOException e) {
	//			MobileConfMngtLog.logError(e);
	//			setErrorMessage(e.getMessage());
	//		}
	//	}
	//	
	//	private static String stringToUnicode(String text) {
	//		char[] chBuffer = text.toCharArray();
	//		
	//		StringBuffer buffer = new StringBuffer();
	//	     
	//		for (int i = 0; i < chBuffer.length; i++) {
	//			if(!Pattern.matches("[^가-힣ㄱ-ㅎㅏ-ㅣ]", String.valueOf(chBuffer[i]))){
	//				buffer.append("\\u");
//				buffer.append(Integer.toHexString((int) chBuffer[i]));
	//			} else {
	//				buffer.append(String.valueOf(chBuffer[i]));
	//			}
	//			 
	//	      
	//		}
	//		
	//		return buffer.toString();
	//	}

	/**
	 *  기존 Properties 파일을 읽어와서 변경사항만 반영하는 메소드
	 *  @param filePath
	 * */
	@SuppressWarnings("resource")
	private static void fileReadAndWrite(String filePath) {

		try {

			String writeString = "";

			// 파일 읽어오기
			BufferedReader instrm = new BufferedReader(new FileReader(filePath));

			String tmpstr = "";
			while ((tmpstr = instrm.readLine()) != null) {
				if (tmpstr.indexOf("#") < 0 && tmpstr.length() > 0) {
					//#이 들어있다면
					String[] getMap = tmpstr.split("=");
					if (getMap.length >= 2 && !getMap[0].equals(" ")) {
						for (int i = 0; i < properties.size(); i++) {
							Set<Object> keys = properties.keySet();
							Iterator<Object> iterator = keys.iterator();
							while (iterator.hasNext()) {
								String key = (String) iterator.next();

								if (key.equals(getMap[0].trim())) {
									getMap[1] = properties.getProperty(key);

								}

							}

						}

						tmpstr = getMap[0] + "=" + getMap[1];
					} else {
						//key는 있지만 value가 null일 경우
						Set<Object> keys = properties.keySet();
						Iterator<Object> iterator = keys.iterator();
						while (iterator.hasNext()) {
							String key = (String) iterator.next();

							if (key.equals(getMap[0].trim())) {
								tmpstr = tmpstr + properties.getProperty(key);
							}
						}
					}
				}

				//기존의 파일을 읽어 새로운 파일에 쓰기위한 String을 만든다.
				writeString += tmpstr + "\n";
			}

			InputStream inStream = new ByteArrayInputStream(writeString.getBytes("UTF-8"));

			String pname = resource.toString();
			String[] pName = pname.split("/");
			String[] fname = propertiesFile.toString().split(pName[1]);

			ResourceUtils.createFolderFile((IProject) element.getAdapter(IResource.class), fname[1], inStream, null);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * PropertyPage에서 OK버튼 클릭시 수행되는 메소드
	 * 
	 * Page 등을 저장함
	 */
	@SuppressWarnings("unchecked")
	public boolean performOk() {

		ArrayList<?> a = EgovProperties.loadPropertyFile(getFilePath());

		for (int i = 0; i < a.size(); i++) {
			HashMap<?, ?> m = (HashMap<?, ?>) a.get(i);
			Iterator<?> iterator = m.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<?, String> entry = (Entry<?, String>) iterator.next();

				if (entry.getKey().equals(MobileConfMngtMessages.mobileConfPropertyPageGLOBALSOSTYPE) && NullUtil.notNone(osTypeCombo.getText())) {
					entry.setValue(osTypeCombo.getText());
				}
				if (entry.getKey().equals(MobileConfMngtMessages.mobileConfPropertyPageGLOBALSIP) && NullUtil.notNone(localIpField.getText())) {
					entry.setValue(localIpField.getText());
				}
				if (entry.getKey().equals(MobileConfMngtMessages.mobileConfPropertyPageGLOBALSMAINPAGE) && NullUtil.notNone(mainPageField.getText())) {
					entry.setValue(mainPageField.getText());
				}
				if (dbTypeCombo.getText().indexOf("mysql") > -1 || dbTypeCombo.getText().indexOf("oracle") > -1 || dbTypeCombo.getText().indexOf("altibase") > -1
						|| dbTypeCombo.getText().indexOf("tibero") > -1) {

					if (entry.getKey().equals(MobileConfMngtMessages.mobileConfPropertyPageGLOBALSDBTYPE) && NullUtil.notNone(dbTypeCombo.getText())) {
						entry.setValue(dbTypeCombo.getText());
					}
					if (entry.getKey().equals(MobileConfMngtMessages.mobileConfPropertyPageGLOBALSDRIVERCLASSNAME) && NullUtil.notNone(driverClassNameField.getText())) {
						entry.setValue(driverClassNameField.getText());
					}
					if (entry.getKey().equals(MobileConfMngtMessages.mobileConfPropertyPageGLOBALSURL) && NullUtil.notNone(urlField.getText())) {
						entry.setValue(urlField.getText());
					}
					if (entry.getKey().equals(MobileConfMngtMessages.mobileConfPropertyPageGLOBALSUSERNAME) && NullUtil.notNone(usernameField.getText())) {
						entry.setValue(usernameField.getText());
					}
					if (entry.getKey().equals(MobileConfMngtMessages.mobileConfPropertyPageGLOBALSPASSWORD)) {
						entry.setValue(passwordField.getText());
					}

				}
				properties.putAll(m);
			}
		}

		fileReadAndWrite(getFilePath());
		return super.performOk();
	}

}
