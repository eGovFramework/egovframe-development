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
/*******************************************************************************
#   * Copyright (c) 2000-2007 IBM Corporation and others.
#   * All rights reserved. This program and the accompanying materials
#   * are made available under the terms of the Eclipse Public License v1.0
#   * which accompanies this distribution, and is available at
#   * http://www.eclipse.org/legal/epl-v10.html
#   *
#   * Contributors:
#   * IBM Corporation - initial API and implementation
#   *******************************************************************************/
package egovframework.hdev.imp.ide.pages;

import java.net.URI;

import org.eclipse.core.filesystem.URIUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.TextProcessor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.ide.IIDEHelpContextIds;
import org.eclipse.ui.internal.ide.filesystem.FileSystemConfiguration;
import org.eclipse.ui.internal.ide.filesystem.FileSystemSupportRegistry;

import egovframework.hdev.imp.ide.common.DeviceAPIMessages;
import egovframework.hdev.imp.ide.common.StringUtil;

/**
 * Device API Project 생성 마법사 페이지 기반 추상 클래스
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
@SuppressWarnings("restriction")
public abstract class DeviceAPIBaseProjectCreationPage extends WizardPage {

    /** 사용자 지정 경로 */
    private String userPath = ""; //$NON-NLS-1$

    /** 프로젝트 명 */
    private String projectName = ""; //$NON-NLS-1$

    /** 프로젝트 Name Text*/
    protected Text projectNameText;
    protected Button useDefaultWorkspaceLocationButton;
    protected Label locationLabel;
    protected Text locationPathText;
    protected Button locationBrowseButton;
    protected IDialogSettings dialogSettings;
    private static final int SIZING_TEXT_FIELD_WIDTH = 250;
    private static final String BLANK_STRING = "";

    /**
     * 생성자
     * @param pageName
     */
    public DeviceAPIBaseProjectCreationPage(String pageName) {
        super(pageName);
        setPageComplete(false);
    }

    /**
     * 프로젝트명 변경 이벤트 리스너
     */
    private final Listener projectNameModifyListener = new Listener() {
        public void handleEvent(Event e) {
            setDeviceapiLocationForSelection();
            boolean valid = validatePage();
            setPageComplete(valid);

        }
    };

    /**
     * 프로젝트 이름 가져오기
     * @return String
     */
    public String getDeviceapiProjectName() {
        if (projectNameText == null) {
            return ""; //$NON-NLS-1$
        }

        return projectNameText.getText();
    }

    /**
     * 프로젝트 화면 이름 출력
     * @return String
     */
    private String getDefaultPathDisplayString() {

        return Platform.getLocation().append(projectName).toOSString();

    }

    /**
     * 프로젝트명 변경
     * @param projectName void
     */
    private void updateDeviceapiProjectName(String projectName) {
        this.projectName = projectName;
        if (isInWorkspace()) {
            locationPathText.setText(TextProcessor.process(getDefaultPathDisplayString()));
        }
    }

    /**
     * 선택된 프로젝트명으로 Set
     * @param DeviceAPIBaseProjectCreationPage void
     */
    private void setDeviceapiLocationForSelection() {
        updateDeviceapiProjectName(getDeviceapiProjectName());
    }

    /**
     * 선택된 workspace 체크
     * @return boolean
     */
    private boolean isInWorkspace() {
        return useDefaultWorkspaceLocationButton.getSelection();
    }

    /**
     * 프로젝트 이름 그룹 위젯 생성
     * @param parent
     */
    private final void createProjectNameGroup(Composite parent) {

        // 그룹
        Composite projectGroup = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        projectGroup.setLayout(layout);
        projectGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        // 신규 프로젝트 라벨
        Label projectLabel = new Label(projectGroup, SWT.NONE);
        projectLabel.setText(DeviceAPIMessages.PROJECT_CREATION_PROJECT_NAME_LABEL);
        projectLabel.setFont(parent.getFont());

        // 신규 프로젝트명 텍스트
        projectNameText = new Text(projectGroup, SWT.BORDER);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = SIZING_TEXT_FIELD_WIDTH;
        projectNameText.setLayoutData(data);
        projectNameText.setFont(parent.getFont());

        // 리스너 설정
        projectNameText.addListener(SWT.Modify, projectNameModifyListener);
    }

    /**
     * 프로젝트 위치 그룹 위젯 생성
     * @param parent
     */
    private final void createProjectLocationGroup(Composite parent) {

        // 그룹
        Group group = new Group(parent, SWT.NONE);
        group.setText(DeviceAPIMessages.PROJECT_CREATION_CONTENTS_GROUP_TEXT);
        GridLayout dirLayout = new GridLayout();

        group.setLayout(dirLayout);
        group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Composite container = new Composite(group, SWT.NONE);
        container.setLayout(new GridLayout(3, false));
        container.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        // 기본 워크스페이스 사용여부 체크 버튼
        useDefaultWorkspaceLocationButton = new Button(container, SWT.CHECK);
        GridData useDefaultWorkspaceLocationButtonData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
        useDefaultWorkspaceLocationButton.setLayoutData(useDefaultWorkspaceLocationButtonData);
        useDefaultWorkspaceLocationButton.setText(DeviceAPIMessages.PROJECT_CREATION_USE_DEFAULT_WORKSPACE_LOCATION_BUTTON_TEXT);
        useDefaultWorkspaceLocationButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                boolean inWorkspace = isInWorkspace();
                locationLabel.setEnabled(!inWorkspace);
                locationPathText.setEnabled(!inWorkspace);
                locationBrowseButton.setEnabled(!inWorkspace);
                if (isInWorkspace()) {
                    userPath = locationPathText.getText();
                    setDeviceapiLocationForSelection();
                } else {
                    locationPathText.setText(TextProcessor
                        .process(userPath));
                }
                
                setPageComplete(validatePage());
            }
        });
        useDefaultWorkspaceLocationButton.setSelection(true);

        // 경로 라벨
        locationLabel = new Label(container, SWT.NONE);
        GridData locationLabelData = new GridData();
        locationLabelData.horizontalIndent = 10;
        locationLabel.setLayoutData(locationLabelData);
        locationLabel
            .setText(DeviceAPIMessages.PROJECT_CREATION_LOCATION_LABEL);
        locationLabel.setEnabled(false);

        // 경로 텍스트
        locationPathText = new Text(container, SWT.BORDER);
        GridData locationComboData = new GridData(GridData.FILL_HORIZONTAL); // new
                                                                             // GridData(SWT.FILL,
                                                                             // SWT.CENTER,
                                                                             // true,
                                                                             // false);
        locationPathText.setLayoutData(locationComboData);
        updateDeviceapiProjectName("");  //$NON-NLS-1$
        locationPathText.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                validatePage();
            }
        });
        locationPathText.setEnabled(false);

        // 경로 선택 버튼
        locationBrowseButton = new Button(container, SWT.NONE);
        GridData locationBrowseButtonData = new GridData();
        locationBrowseButton.setLayoutData(locationBrowseButtonData);
        locationBrowseButton
            .setText(DeviceAPIMessages.PROJECT_CREATION_LOCATION_BROWSE_BUTTON_TEXT);
        locationBrowseButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                DirectoryDialog dialog = new DirectoryDialog(getShell());
                dialog
                    .setText(DeviceAPIMessages.PROJECT_CREATION_LOCATION_BROWSE_DIALOG_TITLE);

                String path = locationPathText.getText();
                if (path.length() == 0) {
                    path =
                        ResourcesPlugin.getWorkspace().getRoot().getLocation()
                            .toPortableString();
                }
                dialog.setFilterPath(path);

                String selectedDir = dialog.open();
                if (selectedDir != null) {
                    locationPathText.setText(selectedDir);
                    useDefaultWorkspaceLocationButton.setSelection(false);
                    validatePage();
                }
            }
        });
        locationBrowseButton.setEnabled(false);

    }

    /**
     * 그밖의 컨트롤들 추가를 위한 추상 메소드
     * @param parent
     */
    protected abstract void createControls(Composite parent);

    /**
     * 프로젝트 이름 변경 이벤트 리스너 추가.
     * @param listener
     */
    protected void addProjectNameListener(Listener listener) {
        projectNameText.addListener(SWT.Modify, listener);
    }

    /**
     * 페이지 검사
     * @return boolean
     */
    protected boolean validatePage() {
        IWorkspace workspace = IDEWorkbenchPlugin.getPluginWorkspace();

        String projectName = getDeviceapiProjectName();
        if (projectName.equals(BLANK_STRING)) { 
            setErrorMessage(null);
            setMessage(DeviceAPIMessages.PROJECT_CREATION_ERROR_PROJECT_NAME_EMPTY); 
            return false;
        }
        
        if(!StringUtil.isProjectElementNameAvailable(projectName)) { //$NON-NLS-1$
        	setErrorMessage(DeviceAPIMessages.PROJECT_CREATION_ERROR_PROJECT_NAME_INVALID);
        	return false;
        }
        
        if(projectName.length() > 165) {
        	setErrorMessage(DeviceAPIMessages.PROJECT_CREATION_ERROR_PROJECT_NAME_TOOLONG);
        	return false;
        }
        
        IStatus status = workspace.validateName(projectName, IResource.PROJECT);
        if (!status.isOK()) {
            setErrorMessage(status.getMessage());
            return false;
        }

        IProject project = getDeviceapiProjectHandle();
        
        if (project.exists()) {
            setErrorMessage(DeviceAPIMessages.PROJECT_CREATION_ERROR_PROJECT_NAME_ALREADY_EXIST); 
            return false;
        }

        if (!isValidProjectLocation(project)) {
            setErrorMessage(DeviceAPIMessages.PROJECT_CREATION_ERROR_WORKSPACE_LOCATION_NOT_VALID);
            return false;
        }

        setErrorMessage(null);
        setMessage(null);
        return true;
    }

    /**
     * get path
     * @return IPath
     */
    public IPath getDeviceapiLocationPath() {

        String path = "";  //$NON-NLS-1$
        if (isInWorkspace()) {
            path = Platform.getLocation().toOSString();

        } else {
            path = locationPathText.getText();
        }

        return new Path(path);
    }

    /**
     * 프로젝트 핸들
     * @return IProject
     */
    public IProject getDeviceapiProjectHandle() {
        return ResourcesPlugin.getWorkspace().getRoot().getProject(
            getDeviceapiProjectName());
    }

    /**
     * get 프로젝트 uri
     * @return URI
     */
    public URI getDeviceapiProjectLocationURI() {

        FileSystemConfiguration configuration =
            FileSystemSupportRegistry.getInstance().getDefaultConfiguration();
        if (configuration == null) {
            return null;
        }
        
        return configuration.getContributor().getURI("test");
    }

    /**
     * 프로젝트 위치 확인
     * @param existingProject
     * @return boolean
     */
    public boolean isValidProjectLocation(IProject existingProject) {

        String locationFieldContents = locationPathText.getText();
        if (locationFieldContents.length() == 0) {
            return false;
        }

        URI newPath = getDeviceapiProjectLocationURI();
        if (newPath == null) {
            return false;
        }

        if (existingProject != null) {
            URI projectPath = existingProject.getLocationURI();
            if (projectPath != null && URIUtil.equals(projectPath, newPath)) {
                return false;
            }
        }

        if (!isInWorkspace()) {
            IStatus locationStatus =
                ResourcesPlugin.getWorkspace().validateProjectLocationURI(
                    existingProject, newPath);

            if (!locationStatus.isOK()) {
                return false;
            }
        }

        return true;
    }

    /**
     * UI 컨트롤 생성
     */
    @Override
    public void createControl(Composite parent) {
 
    	Composite composite = new Composite(parent, SWT.NULL);
        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        composite.layout(true);

        initializeDialogUnits(parent);

        PlatformUI.getWorkbench().getHelpSystem().setHelp(composite,
            IIDEHelpContextIds.NEW_PROJECT_WIZARD_PAGE);
        
        createProjectNameGroup(composite);

        createProjectLocationGroup(composite);

        createControls(composite);

        setErrorMessage(null);
        setMessage(null);
        setControl(composite);
        Dialog.applyDialogFont(composite);
    }
}
