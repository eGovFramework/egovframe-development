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
package egovframework.hdev.imp.ide.model;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;


/**  
 * @Class Name : DeviceAPIContext
 * @Description : DeviceAPIContext Class
 * @Modification Information  
 * @
 * @  수정일			수정자		수정내용
 * @ ---------		---------	-------------------------------
 * @ 2012. 8. 24.		이율경		최초생성
 * 
 * @author 디바이스 API 개발환경 팀
 * @since 2012. 8. 24.
 * @version 1.0
 * @see
 * 
 */
public class DeviceAPIContext extends DeviceAPIWebProjectContext {

	/** 프로젝트 */
    private IProject deviceapiProject;
    
    /** 프로젝트 이름 */
    private String deviceapiProjectName;
    
    /** 위치 경로 */
    private IPath deviceapiLocationPath;
    
    /** 패키지명 */
    private String deviceapiPackageName;
    
    /** 메이븐 설정 파일 명 */
    private String deviceapiPomFileName;
    
    /** 예제 파일 명 */
    private String deviceapiExampleFile;
    
    /** 디바이스API 메이븐 그룹  */
    private String deviceapiGroupId;
    
    /** 디바이스API 메이븐 Artifact  */
    private String deviceapiArtifactId;
    
    /** 디바이스API 메이븐 Version */
    private String deviceapiVersion;
    
    /** 템플릿 생성여부 */
    private boolean isTemplate;
    
    /** 디바이스API serverURL */
    private String serverURL;

	/**
	 * @param DeviceAPIContext
	 * @return IProject
	 */
	public IProject getDeviceapiProject() {
		return deviceapiProject;
	}

	/**
	 * @param DeviceAPIContext
	 * @param deviceapiProject void
	 */
	public void setDeviceapiProject(IProject deviceapiProject) {
		this.deviceapiProject = deviceapiProject;
	}

	/**
	 * @param DeviceAPIContext
	 * @return String
	 */
	public String getDeviceapiProjectName() {
		return deviceapiProjectName;
	}

	/**
	 * @param DeviceAPIContext
	 * @param deviceapiProjectName void
	 */
	public void setDeviceapiProjectName(String deviceapiProjectName) {
		this.deviceapiProjectName = deviceapiProjectName;
	}

	/**
	 * @param DeviceAPIContext
	 * @return IPath
	 */
	public IPath getDeviceapiLocationPath() {
		return deviceapiLocationPath;
	}

	/**
	 * @param DeviceAPIContext
	 * @param deviceapiLocationPath void
	 */
	public void setDeviceapiLocationPath(IPath deviceapiLocationPath) {
		this.deviceapiLocationPath = deviceapiLocationPath;
	}

	/**
	 * @param DeviceAPIContext
	 * @return String
	 */
	public String getDeviceapiPomFileName() {
		return deviceapiPomFileName;
	}

	/**
	 * @param DeviceAPIContext
	 * @param deviceapiPomFileName void
	 */
	public void setDeviceapiPomFileName(String deviceapiPomFileName) {
		this.deviceapiPomFileName = deviceapiPomFileName;
	}

	/**
	 * @param DeviceAPIContext
	 * @return String
	 */
	public String getDeviceapiExampleFile() {
		return deviceapiExampleFile;
	}

	/**
	 * @param DeviceAPIContext
	 * @param deviceapiExampleFile void
	 */
	public void setDeviceapiExampleFile(String deviceapiExampleFile) {
		this.deviceapiExampleFile = deviceapiExampleFile;
	}

	/**
	 * @param DeviceAPIContext
	 * @return String
	 */
	public String getDeviceapiPackageName() {
		return deviceapiPackageName;
	}

	/**
	 * @param DeviceAPIContext
	 * @param deviceapiPackageName void
	 */
	public void setDeviceapiPackageName(String deviceapiPackageName) {
		this.deviceapiPackageName = deviceapiPackageName;
	}

	/**
	 * @param DeviceAPIContext
	 * @return boolean
	 */
	public boolean getIsTemplate() {
		return isTemplate;
	}

	/**
	 * @param DeviceAPIContext
	 * @param isTemplate void
	 */
	public void setIsTemplate(boolean isTemplate) {
		this.isTemplate = isTemplate;
	}

	/**
	 * @param DeviceAPIContext
	 * @return String
	 */
	public String getDeviceapiGroupId() {
		return deviceapiGroupId;
	}

	/**
	 * @param DeviceAPIContext
	 * @param deviceapiGroupId void
	 */
	public void setDeviceapiGroupId(String deviceapiGroupId) {
		this.deviceapiGroupId = deviceapiGroupId;
	}

	/**
	 * @param DeviceAPIContext
	 * @return String
	 */
	public String getDeviceapiArtifactId() {
		return deviceapiArtifactId;
	}

	/**
	 * @param DeviceAPIContext
	 * @param deviceapiArtifactId void
	 */
	public void setDeviceapiArtifactId(String deviceapiArtifactId) {
		this.deviceapiArtifactId = deviceapiArtifactId;
	}

	/**
	 * @param DeviceAPIContext
	 * @return String
	 */
	public String getDeviceapiVersion() {
		return deviceapiVersion;
	}

	/**
	 * @param DeviceAPIContext
	 * @param deviceapiVersion void
	 */
	public void setDeviceapiVersion(String deviceapiVersion) {
		this.deviceapiVersion = deviceapiVersion;
	}

	/**
	 * @param DeviceAPIContext
	 * @return String
	 */
	public String getServerURL() {
		return serverURL;
	}

	/**
	 * @param DeviceAPIContext
	 * @param serverURL void
	 */
	public void setServerURL(String serverURL) {
		this.serverURL = serverURL;
	}
	
}
