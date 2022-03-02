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
package egovframework.bdev.imp.ide.com.wizards.model;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;

/**
 * 신규 프로젝트 컨택스트 클래스
 * @author 개발환경 개발팀 이흥주
 * @since 2009.06.01
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.02.20  이흥주          최초 생성
 * 
 * 
 * </pre>
 */
public class NewProjectContext extends NewWebProjectContext{
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
    private String templateProjectTitle;
    
    //getter
    public String getTemplateProjectTitle() {
		return templateProjectTitle;
	}
    //setter
	public void setTemplateProjectTitle(String templateProjectTitle) {
		this.templateProjectTitle = templateProjectTitle;
	}
	//getter
    public String getPomFileName() {
        return pomFileName;
    }
    //setter
    public void setPomFileName(String pomFileName) {
        this.pomFileName = pomFileName;
    }
    //getter
    public boolean isCreateExample() {
        return isCreateExample;
    }
    //setter
    public void setCreateExample(boolean isCreateExample) {
        this.isCreateExample = isCreateExample;
    }
    //getter
    public IPath getLocationPath() {
        return locationPath;
    }
    //getter
    public String getDefaultExampleFile() {
        return defaultExampleFile;
    }
    //setter
    public void setDefaultExampleFile(String defaultExampleFile) {
        this.defaultExampleFile = defaultExampleFile;
    }
    //getter
    public String[] getOptionalExampleFile() {
        return optionalExampleFile;
    }
    //setter
    public void setOptionalExampleFile(String[] optionalExampleFile) {
        this.optionalExampleFile = optionalExampleFile;
    }
    //setter
    public void setLocationPath(IPath locationPath) {
        this.locationPath = locationPath;
    }
    //getter
    public String getProjectName() {
        return projectName;
    }
    //setter
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    //getter
    public String getGroupId() {
        return groupId;
    }
    //setter
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    //getter
    public String getArtifactId() {
        return artifactId;
    }
    //setter
    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }
    //getter
    public String getVersion() {
        return version;
    }
    //setter
    public void setVersion(String version) {
        this.version = version;
    }
    //getter
    public String getPackageName() {
        return packageName;
    }
    //setter
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    //생성자
    public NewProjectContext() {

    }
    //getter
    public IProject getProject() {
        return project;
    }
    //setter
    public void setProject(IProject project) {
        this.project = project;
    }
}
