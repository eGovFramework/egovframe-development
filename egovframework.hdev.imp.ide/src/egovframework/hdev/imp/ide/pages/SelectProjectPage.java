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

import java.util.ArrayList;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import egovframework.dev.imp.core.utils.EgovJavaElementUtil;
import egovframework.hdev.imp.ide.EgovDeviceAPIIdePlugin;
import egovframework.hdev.imp.ide.common.DeviceAPIMessages;
import egovframework.hdev.imp.ide.common.ProjectFacetConstants;
import egovframework.hdev.imp.ide.model.ClassTableViewerLabelProvider;
import egovframework.hdev.imp.ide.model.DeviceAPIContext;
import egovframework.hdev.imp.ide.wizards.examples.DeviceAPITemplateInfo;
/**
 * Device API Project 선택 Wizard Page
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
public class SelectProjectPage extends WizardPage {
	
	/** Job Test Context */
	DeviceAPIContext context;
	
	@SuppressWarnings("unused")
	private IContainer selectedContainer;
	
	/** Nature를 가진 선택 가능한 Project의 ListViewer*/
	ListViewer projectTableViewer;
	
	/** Project 이름을 입력하는 Text */
	private Text containerNameField;
	
	/** containerNameField 옆의 설명 Label */
	private Label containerNameLabel;
	
	/** "Note : " String을 가지는 Label */
	private Label noteName;
	
	/** Note의 내용을 담는 Label */
	private Label noteContent;
	
	/** Nature를 가진 선택 가능한 Project의 목록 */
	private ArrayList<IProject> eGovProjects;

	/**
	 * SelectProjectPage 생성자
	 * 
	 * @param pageName
	 * @param context
	 */
	public SelectProjectPage(String pageName, DeviceAPIContext context) {
		super(pageName);

		this.context = context;
		
		setTitle(DeviceAPIMessages.SELECT_PROJECT_PAGE_TITLE);
		setDescription(DeviceAPIMessages.SELECT_PROJECT_PAGE_DESCRIPTION);
		
		// 수정요망
		eGovProjects = getNatureProject();
	}

	/**
	 * @param SelectProjectPage
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {

		Composite control = new Composite(parent, SWT.NONE);
		control.setLayout(new GridLayout());
		control.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.GRAB_HORIZONTAL));

		containerNameLabel = new Label(control, SWT.NONE);
		containerNameLabel.setText(DeviceAPIMessages.SELECT_PROJECT_PAGE_CONTAINER_NAME_LABEL_TEXT);

		containerNameField = new Text(control, SWT.BORDER);
		containerNameField
				.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		containerNameField.addListener(SWT.Modify, validation);

		createTreeTable(control);

		createNoteControl(control);

		setPageComplete(false);
		setControl(control);
	}

	/**
	 * 선택 가능한 Project의 목록을 보여주는 ListTable 생성
	 * 
	 * @param control
	 */
	private void createTreeTable(Composite control) {
		GridData spec = new GridData(SWT.FILL, SWT.FILL, true, true);
		spec.widthHint = 320;
		spec.heightHint = 300;
		
		projectTableViewer = new ListViewer(control, SWT.BORDER | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION);

		projectTableViewer.getControl().setLayoutData(spec);
		projectTableViewer.setContentProvider(new ArrayContentProvider());
		projectTableViewer.setLabelProvider(new ClassTableViewerLabelProvider());
		projectTableViewer.setInput(eGovProjects);
		projectTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection)projectTableViewer.getSelection();
				
				if(selection.isEmpty()){
					return;
				}
				
				IProject selectProject = (IProject)selection.getFirstElement();
				containerNameField.setText(selectProject.getName());
			}
		});
	}
	
	/**
	 * 설명 컴포넌트 생성
	 * @param SelectProjectPage
	 * @param control void
	 */
	private void createNoteControl(Composite control){
		Composite noteControl = new Composite(control, SWT.None);
		noteControl.setLayout(new GridLayout(2, false));
		noteControl.setLayoutData(new GridData());

		noteName = new Label(noteControl, SWT.BOLD | SWT.TOP);
		noteName.setFont(JFaceResources.getFontRegistry().getBold(
				JFaceResources.DIALOG_FONT));
		noteName.setText(DeviceAPIMessages.SELECT_PROJECT_PAGE_NOTE);

		noteContent = new Label(noteControl, SWT.BOLD | SWT.TOP);
		noteContent.setText(DeviceAPIMessages.SELECT_PROJECT_PAGE_NOTE_CONTENTS);
	}
	
	/** Nature를 가진 Project 목록 추출 */
	private ArrayList<IProject> getNatureProject(){
		ArrayList<IProject> result = new ArrayList<IProject>();

		IProject[] allProjects = ResourcesPlugin.getWorkspace().getRoot()
				.getProjects();
		try {
			for (IProject project : allProjects) {
				if (EgovJavaElementUtil.isJavaProject(project) && project.isNatureEnabled(ProjectFacetConstants.ANDROID_NATURE_ID)
						&& (project.isNatureEnabled(EgovDeviceAPIIdePlugin.ID_NATURE) == false)) {
					result.add(project);
				}
			}
		} catch (CoreException e) {

		}
		return result;
	}
	
	/** containerNameField의 validation Listener */
	Listener validation = new Listener() {

		public void handleEvent(Event event) {
			
			IProject project = null;
			boolean canFlipToNextPage = false;
			String containerClassName = containerNameField.getText();
			
			for (int i = 0; i < eGovProjects.size(); i++) {
				project = eGovProjects.get(i);

				// class명이 같으면 true
				if (project.getName().equals(containerClassName)) {
					canFlipToNextPage = true;
					break;
				}
			}
			
			if(!canFlipToNextPage){
				setErrorMessage(DeviceAPIMessages.SELECT_PROJECT_PAGE_ERROR_INVALID_PROJECT_NAME);
			}

			setPageComplete(canFlipToNextPage);
			
			if(canFlipToNextPage){
				setErrorMessage(null);
				
				context.setDeviceapiProject(project);
				context.setDeviceapiProjectName(project.getName());
				context.setDeviceapiLocationPath(Platform.getLocation());
				
				context.setDeviceapiExampleFile(DeviceAPITemplateInfo.deviceapiExampleFile);
			}
		}
	};
}
