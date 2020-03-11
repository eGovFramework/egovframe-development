/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package egovframework.hdev.imp.ide.pages;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
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
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import egovframework.hdev.imp.ide.common.DeviceAPIIdeLog;
import egovframework.hdev.imp.ide.common.DeviceAPIMessages;
import egovframework.hdev.imp.ide.common.NullUtil;
import egovframework.hdev.imp.ide.model.DeviceAPIContext;
import egovframework.hdev.imp.ide.model.FilesTableViewerLabelProvider;
import egovframework.hdev.imp.ide.wizards.examples.DeviceAPITemplateInfo;
/**
 * 예제 소스 생성 Wizard Page
 * 
 * @since 2012.07.24
 * @author 디바이스 API 개발환경 팀 조용현
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
public class GenerateTemplatePage extends WizardPage {

	/** DeviceAPIContext */
	DeviceAPIContext context;
	
	/** Generate Template CheckBox Button */
	private Button generateTemplateButton = null;
	
	/** File의 TableViewer */
	private TableViewer filesTableViewer = null;
	
	/** Wizard의 Finish Button의 Enable/Disable 설정 */
	private boolean isEnableFinishButton = true;
	
	/** URL 체크 */
	private Button urlCheckButton = null;
	
	/** Web 프로젝트 생성 */
	private Button createWebProjectButton = null;
	
	/** url 필드 */
	private Text urlField = null;
	
	/** "Note : " String을 가지는 Label */
	@SuppressWarnings("unused")
	private Label noteName;
	
	/** Note의 내용을 담는 Label */
	@SuppressWarnings("unused")
	private Label noteContent;
	
	/** generateText의 내용을 담는 Text */
	private Text generateText;
	
	/** generateWebText 내용을 담는 Text */
	private Text generateWebText;

	/**
	 * GenerateTemplatePage의 생성자
	 * 
	 * @param pageName
	 */
	public GenerateTemplatePage(String pageName, DeviceAPIContext context) {
		super(pageName);
		
		this.context = context;
		
		setTitle(DeviceAPIMessages.GENERATE_TEMPLATE_WIZARD_PAGE_TITLE);
		setDescription(DeviceAPIMessages.GENERATE_TEMPLATE_WIZARD_PAGE_DESCRIPTION);
	}
	
	/**
	 * @return the isEnableFinishButton
	 */
	public boolean isEnableFinishButton() {
		return isEnableFinishButton;
	}

	/**
	 * @param GenerateTemplatePage
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {

		Composite control = new Composite(parent, SWT.None);
		control.setLayout(new GridLayout());
		control.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		createNoteControl(control);
		
		generateTemplateButton = new Button(control, SWT.CHECK);
		generateTemplateButton.setText(DeviceAPIMessages.FILES_TABLE_VIEWER_CONTROL_CHECKBOX);
		generateTemplateButton.addListener(SWT.Selection, generateTemplateButtonListener);
		

		Group tableViewerGroup = new Group(control, SWT.None);
		tableViewerGroup.setLayout(new GridLayout());
		tableViewerGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		tableViewerGroup.setText(DeviceAPIMessages.FILES_TABLE_VIEWER_GROUP_TEXT);

		filesTableViewer = new TableViewer(tableViewerGroup,
				SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);

		Table filesTableViewerTable = filesTableViewer.getTable();
		filesTableViewerTable.setLinesVisible(true);
		filesTableViewerTable.setHeaderVisible(true);
		filesTableViewerTable.setLayoutData(new GridData(GridData.FILL_BOTH));
		filesTableViewerTable.setEnabled(false);
		filesTableViewerTable.addListener(SWT.Selection, validation);
		filesTableViewerTable.addListener(SWT.Selection, filesTableViewerListener);

		String[] columnName = new String[] {DeviceAPIMessages.FILES_TABLE_VIEWER_SOURCE_NAME_COLUMN, DeviceAPIMessages.FILES_TABLE_VIEWER_SOURCE_DESCRIPTION_COLUMN };
		int[] columnAlignment = new int[] { SWT.LEFT, SWT.LEFT };
		int[] columnWidth = new int[] { 150, 300};

		for (int i = 0; i < columnName.length; i++) {
			TableColumn column = new TableColumn(filesTableViewerTable,
					columnAlignment[i]);
			column.setText(columnName[i]);
			column.setWidth(columnWidth[i]);
		}

		filesTableViewer.setContentProvider(new ArrayContentProvider());
		filesTableViewer.setLabelProvider(new FilesTableViewerLabelProvider());
		
		createWebNoteControl(control);
		
		createWebContextControl(control);

		setPageComplete(false);
		setControl(control);
	}
	
	/**
	 * 설명 컴포넌트 생성
	 * @param SelectProjectPage
	 * @param control void
	 */
	private void createNoteControl(Composite control){
		
		Group generateGroup = new Group(control, SWT.None);
		generateGroup.setLayout(new GridLayout());
		generateGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		generateGroup.setText("Generate Guide Program");
		
		generateText = new Text(generateGroup, SWT.READ_ONLY | SWT.MULTI);
		generateText.setLayoutData(new GridData(GridData.FILL_BOTH));
		generateText.setText(DeviceAPIMessages.SELECT_TEMPLATE_NOTE_CONTENTS);
		
	}
	
	/**
	 * Web Project 생성 설명 컴포넌트 생성
	 * @param SelectProjectPage
	 * @param control void
	 */
	private void createWebNoteControl(Composite control){
		
		Group generateGroup = new Group(control, SWT.None);
		generateGroup.setLayout(new GridLayout());
		generateGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		generateGroup.setText("Web Project 생성");
		
		generateWebText = new Text(generateGroup, SWT.READ_ONLY | SWT.MULTI);
		generateWebText.setLayoutData(new GridData(GridData.FILL_BOTH));
		generateWebText.setText(DeviceAPIMessages.SELECT_TEMPLATE_NOTE_WEB_CONTENTS);
		
	}

	/** filesTableViewer에 넣을 Input 생성*/
	private ArrayList<String[]> getTableInputItems(){
		ArrayList<String[]> items = new ArrayList<String[]>();
		String[] fileNames = new String[] {DeviceAPITemplateInfo.template1name, DeviceAPITemplateInfo.template2name, DeviceAPITemplateInfo.template3name, 
				DeviceAPITemplateInfo.template4name, DeviceAPITemplateInfo.template5name, DeviceAPITemplateInfo.template6name, DeviceAPITemplateInfo.template7name, 
				DeviceAPITemplateInfo.template8name, DeviceAPITemplateInfo.template9name, DeviceAPITemplateInfo.template10name, DeviceAPITemplateInfo.template11name, 
				DeviceAPITemplateInfo.template12name, DeviceAPITemplateInfo.template13name, DeviceAPITemplateInfo.template14name, DeviceAPITemplateInfo.template15name,
				DeviceAPITemplateInfo.template16name, DeviceAPITemplateInfo.template17name, DeviceAPITemplateInfo.template18name, DeviceAPITemplateInfo.template19name,
				DeviceAPITemplateInfo.template20name, DeviceAPITemplateInfo.template21name, DeviceAPITemplateInfo.template22name, DeviceAPITemplateInfo.template23name,
				DeviceAPITemplateInfo.template24name, DeviceAPITemplateInfo.template25name
};

		for(int i = 0 ; i < fileNames.length; i++){
			
			try {
				
				items.add(new String[]{fileNames[i], (String)DeviceAPITemplateInfo.getTemplateInfo(fileNames[i]).get("desc"), "경로"});
			} catch (Exception e) {
				
				DeviceAPIIdeLog.logError(e);
			}
		}
		
		return items;
	}
	
	
    /**
     * Web 설정 Context 구성
     * @param control 
     * @return void
     */
    private void createWebContextControl(Composite control){  	
    	Group urlControl = new Group(control, SWT.None);
		urlControl.setLayout(new GridLayout(4, false));
		urlControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		urlControl.setText(DeviceAPIMessages.URL_GROUP_TEXT);
		
		GridData gData = new GridData(GridData.FILL_HORIZONTAL);
		gData.horizontalSpan = 4;
		
		createWebProjectButton = new Button(urlControl, SWT.CHECK);
		createWebProjectButton.setText(DeviceAPIMessages.CREATE_WEB_PROJECT_CHECKBOX_BUTTON_TEXT);
		createWebProjectButton.setEnabled(false);
		createWebProjectButton.addListener(SWT.Selection, createWebProjectButtonListener);
		
		urlCheckButton = new Button(urlControl, SWT.CHECK);
		urlCheckButton.setText(DeviceAPIMessages.URL_LABEL_TEXT);
		urlCheckButton.setEnabled(false);
		urlCheckButton.setLayoutData(gData);
		urlCheckButton.addListener(SWT.Selection, urlCheckButtonListner);
		
		urlField = new Text(urlControl, SWT.BORDER);
		urlField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		urlField.setEnabled(false);
		urlField.setText(getNetworkIpAddress());
		urlField.addListener(SWT.Modify, validation);

    }
    
    /**
     * 로컬 IP 생성
     * @param GenerateTemplatePage
     * @return String
     */
    protected String getNetworkIpAddress() {
    	
    	String address = null;
    	
    	try {
    		
    		address = "http://" + InetAddress.getLocalHost().getHostAddress() + ":8080";
		} catch (Exception e) {
			
			DeviceAPIIdeLog.logError(e);
		}
		
		return address;
    }
	
	/**
	 * <pre>
	 * Finish Button 비활성화
	 * PageComplete는 parameter인 isPageComplete에 의해 결정
	 * </pre>
	 * @param isPageComplete
	 */
	private void setDisableFinishButton(boolean isPageComplete){
		isEnableFinishButton = false;
		setPageComplete(isPageComplete);
		
		getContainer().updateButtons();
	}

	/** Finish Button 활성화 */
	private void setEnableFinishButton(){
		isEnableFinishButton = true;
		setPageComplete(false);
		
		getContainer().updateButtons();
	}
	
	/**
	 * ExampleFile 설정
	 * 
	 * @param templateName
	 * @throws Exception
	 */
	private void setExampleFile(String templateName) throws Exception {
		HashMap<String, String> templateMap = DeviceAPITemplateInfo.getTemplateInfo(templateName);
		
		context.setDeviceapiExampleFile(templateMap.get("example"));
		context.setTemplateType(templateMap.get("name"));
		
		context.setWebExampleFile(DeviceAPITemplateInfo.webexample);
	}

	/** GenerateTemplate Button의 Listener */
	Listener generateTemplateButtonListener = new Listener() {

		@Override
		public void handleEvent(Event event) {
			setCreateWebProjectButtonDisable();
			
			if (generateTemplateButton.getSelection()) {
				filesTableViewer.setInput(getTableInputItems());
				filesTableViewer.getTable().setEnabled(true);
				setDisableFinishButton(false);
				
			} else {
				filesTableViewer.setInput(null);
				filesTableViewer.getTable().setEnabled(false);
				filesTableViewer.setSelection(new StructuredSelection());
				setEnableFinishButton();
				
				setServerURLBoxDisable();
			}
		}
	};
	
	/**
	 * 테이블 변경 이벤트 리스너
	 */
	Listener filesTableViewerListener = new Listener() {
		@Override
		public void handleEvent(Event event) {
			IStructuredSelection selection = (IStructuredSelection)filesTableViewer.getSelection();
			if(!selection.isEmpty()){
				Object object = selection.getFirstElement();
				String[] item = (String[])object;
				
				if(DeviceAPITemplateInfo.template1name.equals(item[0])){
					setServerURLBoxDisable();
					
					setCreateWebProjectButtonDisable();
				}else{
					urlCheckButton.setEnabled(true);
					
					createWebProjectButton.setEnabled(true);
				}
				
				try {
					setExampleFile(item[0]);
				} catch (Exception e) {
					DeviceAPIIdeLog.logError(e);
				}
				
				if(!createWebProjectButton.getSelection()){
					setEnableFinishButton();
				}

			}
		}
	};
	
	/**
	 * 버튼 변경
	 * @param GenerateTemplatePage 
	 * @return void
	 */
	private void setCreateWebProjectButtonDisable(){
		createWebProjectButton.setSelection(false);
		createWebProjectButton.setEnabled(false);
		createWebProjectButton.notifyListeners(SWT.Selection, null);
	}
	
	
	/**
	 * 버튼 이벤트
	 */
	Listener createWebProjectButtonListener = new Listener() {
		
		@Override
		public void handleEvent(Event event) {
			if(createWebProjectButton.getSelection()){
				
				setDisableFinishButton(true);
				context.setIsTemplate(true);
			}else{
				
				setEnableFinishButton();
				context.setIsTemplate(false);
			}
		}
	};
	
	/**
	 * urlCheckButton 이벤트 리스너
	 */
	Listener urlCheckButtonListner = new Listener() {
		
		@Override
		public void handleEvent(Event event) {
			if(urlCheckButton.getSelection()){
				
				urlField.setEnabled(true);
				urlField.notifyListeners(SWT.Modify, null);
				
				context.setIsWebContextUse(true);
				context.setServerURL(urlField.getText());
			}else{
				
				urlField.setText(getNetworkIpAddress());
				urlField.setEnabled(false);
				
				context.setIsWebContextUse(false);
				context.setServerURL("");
			}
		}
	};
	
	/**
	 * 서버 URL 박스 컨트롤
	 * @return void
	 */
	private void setServerURLBoxDisable(){
		urlCheckButton.setEnabled(false);
		urlCheckButton.setSelection(false);
		urlCheckButton.notifyListeners(SWT.Selection, null);
	}
	
	/**
	 * Validation 이벤트 리스너
	 */
	Listener validation = new Listener() {
		
		@Override
		public void handleEvent(Event event) {
			if(urlCheckButton.getSelection()){
				String serverURL = urlField.getText();
				setDisableFinishButton(false);
				
				if(NullUtil.isEmpty(serverURL)){
					setErrorMessage(DeviceAPIMessages.GENERATE_TEMPLATE_WIZARD_PAGE_ERROR_EMPTY_URL);
					return;
				}
				
				String pattern = "^http://[0-9]{1,3}[.][0-9]{1,3}[.][0-9]{1,3}[.][0-9]{1,3}([:][0-9]+)?([/]?|[/][0-9a-zA-Z_]*[/]?)?";
				if (!Pattern.matches(pattern, serverURL)){
					setErrorMessage(DeviceAPIMessages.GENERATE_TEMPLATE_WIZARD_PAGE_ERROR_INVALID_URL);
					return;
				}
				
				context.setServerURL(serverURL);
			} else {
				
				context.setServerURL("");
			}
			
			setErrorMessage(null);
			createWebProjectButton.notifyListeners(SWT.Selection, null);
		}
	};
	
	/**
	 * @param GenerateTemplatePage
	 * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		if(visible){
			getShell().setSize(533, 659);
		}
		super.setVisible(visible);
	}
}
