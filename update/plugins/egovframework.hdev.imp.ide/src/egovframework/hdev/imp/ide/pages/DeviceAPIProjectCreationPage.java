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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.util.TextProcessor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import egovframework.dev.imp.core.utils.IdeUIUtil;
import egovframework.hdev.imp.ide.common.DeviceAPIMessages;
import egovframework.hdev.imp.ide.common.StringUtil;
import egovframework.hdev.imp.ide.model.DeviceAPIContext;

/**
 * Device API Project 생성 마법사 페이지 클래스
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
abstract public class DeviceAPIProjectCreationPage extends DeviceAPIBaseProjectCreationPage {

    /** 위젯 */
    protected Text groupIdText;
    protected Text artifactIdText;
    protected Combo versionCombo;
    protected Text packageText;
    protected DeviceAPIContext context;

	/**
     * 생성자
     * @param pageName
     * @param context
     */
    public DeviceAPIProjectCreationPage(String pageName, DeviceAPIContext context) {
        super(pageName);
        this.context = context;
        
        setTitle(DeviceAPIMessages.PROJECT_CREATION_PAGE_TITLE);
        setDescription(DeviceAPIMessages.PROJECT_CREATION_PAGE_DESCRIPTION);
    }

    /**
     * 프로젝트명 변경 이벤트 리스너
     */
    private final Listener projectNameModifyListener = new Listener() {
        public void handleEvent(Event e) {
            updateArtifactId();
            boolean valid = validatePage();
            setPageComplete(valid);
        }
    };

    /**
     * Web 설정을 위한 위젯
     * @param container
     */
    private void createWebSettingGroup(Composite container) {

        // 그룹
        Group mavenGroup = new Group(container, SWT.NONE);
        mavenGroup.setText(DeviceAPIMessages.PROJECT_CREATION_MAVEN_GROUP_TEXT);
        GridLayout dirLayout = new GridLayout(3, false);
        mavenGroup.setLayout(dirLayout);
        mavenGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        // 그룹 아이디
        groupIdText =
            IdeUIUtil.createTextField(mavenGroup,
            		DeviceAPIMessages.PROJECT_CREATION_GROUP_ID_LABEL);
        groupIdText.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                setPageComplete(validatePage());
            }
        });

        // 아티팩트 아이디
        artifactIdText =
            IdeUIUtil.createTextField(mavenGroup,
            		DeviceAPIMessages.PROJECT_CREATION_ARTIFACT_ID_LABEL);
        artifactIdText.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                setPageComplete(validatePage());
            }
        });

        // 버젼
        versionCombo =
            IdeUIUtil.createComboField(mavenGroup,
            		DeviceAPIMessages.PROJECT_CREATION_VERSION_LABEL);
        versionCombo.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                setPageComplete(validatePage());
            }
        });
    }

    /**
     * 프로젝트 명에 따라 그룹아이디,아티팩아이디 자동 설정
     */
    private void updateArtifactId() {
        String projectName = getDeviceapiProjectName();
        String groupId = "";  //$NON-NLS-1$
        String artifactId = "";  //$NON-NLS-1$

        if ((projectName.length() > 0)
            && (".".equals(projectName.substring(projectName.length() - 1)))) {  //$NON-NLS-1$
            projectName = projectName.substring(0, projectName.length() - 1);
        }

        artifactId = projectName;

        if (projectName.lastIndexOf(".") > 0) {  //$NON-NLS-1$
            groupId = projectName.substring(0, projectName.lastIndexOf("."));  //$NON-NLS-1$
            groupIdText.setText(TextProcessor.process(groupId));
            artifactId =
                projectName.substring(projectName.lastIndexOf(".") + 1);  //$NON-NLS-1$
        }

        artifactIdText.setText(TextProcessor.process(artifactId));
    }

    /**
     * 위젯 생성
     */
    @Override
    protected void createControls(Composite parent) {

        createWebSettingGroup(parent);

        addProjectNameListener(projectNameModifyListener);  
    }
    
    /**
     * 검증 페이지
     */
    @Override
    protected boolean validatePage() {
    	String groupID = null;
    	String artifactID = null;
    	String version = null;
    	
        if (!super.validatePage()) {
            return false;
        }
        
        groupID = getGroupId();

        if (groupID.length() < 1) {  //$NON-NLS-1$
        	setErrorMessage(DeviceAPIMessages.PROJECT_CREATION_ERROR_GROUP_ID_EMPTY);
        	return false;
        }
        
        if (!StringUtil.isProjectElementNameAvailable(groupID)) {  //$NON-NLS-1$
            setErrorMessage(DeviceAPIMessages.PROJECT_CREATION_ERROR_GROUP_ID_INVALID);
            return false;
        }
        
        artifactID = getArtifactId();

        if (artifactID.length() < 1) {  //$NON-NLS-1$
        	setErrorMessage(DeviceAPIMessages.PROJECT_CREATION_ERROR_ARTIFACT_ID_EMPTY);
        	return false;
        }
        
        if (!StringUtil.isProjectElementNameAvailable(artifactID)) {  //$NON-NLS-1$
            setErrorMessage(DeviceAPIMessages.PROJECT_CREATION_ERROR_ARTIFACT_ID_INVALID);
            return false;
        }
        
        version = getVersion();
        
        if (version.length() < 1) { //$NON-NLS-1$
        	setErrorMessage(DeviceAPIMessages.PROJECT_CREATION_ERROR_VERSION_EMPTY);
        	return false;
        }
        
        if (!StringUtil.isProjectElementNameAvailable(version)) { //$NON-NLS-1$
        	setErrorMessage(DeviceAPIMessages.PROJECT_CREATION_ERROR_VERSION_INVALID);
        	return false;
        }

        return true;
    }
    
    /**
     * 프로젝트 핸들
     * @return
     */
    public IProject getWebProjectHandle() {
        return ResourcesPlugin.getWorkspace().getRoot().getProject(
            getWebProjectName());
    }
    
    /**
     * 프로젝트명 가져오기
     * @return
     */
    protected String getWebProjectName() {
    	if (projectNameText == null) {
            return ""; //$NON-NLS-1$
        }

        return projectNameText.getText();
    }
    
    // 선택된 workspace 체크
    private boolean isInWorkspace() {
        return useDefaultWorkspaceLocationButton.getSelection();
    }
    
    /**
     * get path
     * @return
     */
    public IPath getWebLocationPath() {

        String path = "";  //$NON-NLS-1$
        if (isInWorkspace()) {
            path = Platform.getLocation().toOSString();

        } else {
            path = locationPathText.getText();
        }

        return new Path(path);
    }
    
    //getter
    public String getWebPackage() {
        if (packageText == null) {
            return "";  //$NON-NLS-1$
        }
        return packageText.getText();
    }
    
    //getter
    public String getGroupId() {
        if (groupIdText == null) {
            return "";  //$NON-NLS-1$
        }

        return groupIdText.getText();
    }
    //getter
    public String getArtifactId() {
        if (artifactIdText == null) {
            return "";  //$NON-NLS-1$
        }

        return artifactIdText.getText();
    }
    //getter
    public String getVersion() {
        if (versionCombo == null) {
            return "";  //$NON-NLS-1$
        }

        return versionCombo.getText();
    }

    // 화면 생성
    @Override
    public void createControl(Composite parent) {
        super.createControl(parent);
    }
}
