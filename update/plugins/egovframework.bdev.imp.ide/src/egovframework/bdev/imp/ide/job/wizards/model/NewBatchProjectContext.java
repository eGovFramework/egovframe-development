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
package egovframework.bdev.imp.ide.job.wizards.model;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;

/**
 * 신규 배치 프로젝트 컨택스트 클래스
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
public class NewBatchProjectContext {
    /** 프로젝트 */
    private IProject project;
    
    /** 프로젝트 이름 */
    private String projectName;
    
    /** 위치 경로 */
    private IPath locationPath;

    /** 그룹 아이디 */
    private String groupId;
    
    /** 아티팩트 아이디 */
    private String artifactId;
    
    /** 버젼 */
    private String version;
    
    /** 패키지명 */
    private String packageName;
    
    /** 예제 생성 여부 */
    private boolean isCreateExample;
    
    /** 기본 예제 파일 명 */
    private String defaultExampleFile;
    
    /** 선택 예제 파일 명 */
    private String[] optionalExampleFile;
    
    /** 메이븐 설정 파일 명 */
    private String pomFileName;
    
    /** 템플릿 프로젝트 생성 타이틀 */
    private String templateProjectDescription;
    
    /** 프로젝트 생성 타입 */
	private String creationType;
    
	/** 프로젝트 실행 타입 */
    private String executionType;
    
    /**
	 * project의 값을 가져온다
	 *
	 * @return the project
	 */
	public IProject getProject() {
		return project;
	}

	/**
	 * project의 값을 설정한다.
	 *
	 * @param project the project to set
	 */
	public void setProject(IProject project) {
		this.project = project;
	}

	/**
	 * projectName의 값을 가져온다
	 *
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * projectName의 값을 설정한다.
	 *
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * locationPath의 값을 가져온다
	 *
	 * @return the locationPath
	 */
	public IPath getLocationPath() {
		return locationPath;
	}

	/**
	 * locationPath의 값을 설정한다.
	 *
	 * @param locationPath the locationPath to set
	 */
	public void setLocationPath(IPath locationPath) {
		this.locationPath = locationPath;
	}
	
	/**
	 * groupId의 값을 가져온다
	 *
	 * @return the groupId
	 */
	public String getGroupId() {
		return groupId;
	}

	/**
	 * groupId의 값을 설정한다.
	 *
	 * @param groupId the groupId to set
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	/**
	 * artifactId의 값을 가져온다
	 *
	 * @return the artifactId
	 */
	public String getArtifactId() {
		return artifactId;
	}

	/**
	 * artifactId의 값을 설정한다.
	 *
	 * @param artifactId the artifactId to set
	 */
	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	/**
	 * version의 값을 가져온다
	 *
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * version의 값을 설정한다.
	 *
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * packageName의 값을 가져온다
	 *
	 * @return the packageName
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * packageName의 값을 설정한다.
	 *
	 * @param packageName the packageName to set
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	
	/**
	 * isCreateExample의 값을 가져온다
	 *
	 * @return the isCreateExample
	 */
	public boolean isCreateExample() {
		return isCreateExample;
	}

	/**
	 * isCreateExample의 값을 설정한다.
	 *
	 * @param isCreateExample the isCreateExample to set
	 */
	public void setCreateExample(boolean isCreateExample) {
		this.isCreateExample = isCreateExample;
	}

	/**
	 * defaultExampleFile의 값을 가져온다
	 *
	 * @return the defaultExampleFile
	 */
	public String getDefaultExampleFile() {
		return defaultExampleFile;
	}

	/**
	 * defaultExampleFile의 값을 설정한다.
	 *
	 * @param defaultExampleFile the defaultExampleFile to set
	 */
	public void setDefaultExampleFile(String defaultExampleFile) {
		this.defaultExampleFile = defaultExampleFile;
	}

	/**
	 * optionalExampleFile의 값을 가져온다
	 *
	 * @return the optionalExampleFile
	 */
	public String[] getOptionalExampleFile() {
		return optionalExampleFile;
	}

	/**
	 * optionalExampleFile의 값을 설정한다.
	 *
	 * @param optionalExampleFile the optionalExampleFile to set
	 */
	public void setOptionalExampleFile(String[] optionalExampleFile) {
		this.optionalExampleFile = optionalExampleFile;
	}

	/**
	 * pomFileName의 값을 가져온다
	 *
	 * @return the pomFileName
	 */
	public String getPomFileName() {
		return pomFileName;
	}

	/**
	 * pomFileName의 값을 설정한다.
	 *
	 * @param pomFileName the pomFileName to set
	 */
	public void setPomFileName(String pomFileName) {
		this.pomFileName = pomFileName;
	}

	/**
	 * templateProjectDescription의 값을 가져온다
	 *
	 * @return the templateProjectDescription
	 */
	public String getTemplateProjectDescription() {
		return templateProjectDescription;
	}

	/**
	 * templateProjectDescription의 값을 설정한다.
	 *
	 * @param templateProjectDescription the templateProjectDescription to set
	 */
	public void setTemplateProjectDescription(String templateProjectDescription) {
		this.templateProjectDescription = templateProjectDescription;
	}

	/**
	 * creationType의 값을 가져온다
	 *
	 * @return the creationType
	 */
	public String getCreationType() {
		return creationType;
	}

	/**
	 * creationType의 값을 설정한다.
	 *
	 * @param creationType the creationType to set
	 */
	public void setCreationType(String creationType) {
		this.creationType = creationType;
	}

	/**
	 * executionType의 값을 가져온다
	 *
	 * @return the executionType
	 */
	public String getExecutionType() {
		return executionType;
	}

	/**
	 * executionType의 값을 설정한다.
	 *
	 * @param executionType the executionType to set
	 */
	public void setExecutionType(String executionType) {
		this.executionType = executionType;
	}

	/** NewBatchProjectContext 생성자 */
    public NewBatchProjectContext() {

    }
   
}
