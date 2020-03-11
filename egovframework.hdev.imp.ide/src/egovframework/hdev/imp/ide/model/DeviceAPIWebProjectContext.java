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
 * @Class Name : DeviceAPIWebProjectContext
 * @Description : DeviceAPIWebProjectContext Class
 * @Modification Information  
 * @
 * @  수정일			수정자		수정내용
 * @ ---------		---------	-------------------------------
 * @ 2012. 9. 17.		이율경		최초생성
 * 
 * @author 디바이스 API 개발환경 팀
 * @since 2012. 9. 17.
 * @version 1.0
 * @see
 * 
 */
public class DeviceAPIWebProjectContext {
	
	/** 템플릿 Type */
	private String templateType;
	
    /** 프로젝트 */
    private IProject webProject;
    /** 프로젝트 이름 */
    private String webProjectName;
    /** 위치 경로 */
    private IPath webLocationPath;

    /** 그룹 아이디 */
    private String groupId;
    /** 아티팩트 아이디 */
    private String artifactId;
    /** 버젼 */
    private String version;
    /** 패키지명 */
    private String webPackageName;
    
    /** 메이븐 설정 파일 명 */
    private String webPomFileName;
    
    /** 예제 파일 명 */
    private String webExampleFile;
    
    /** 서블릿 버젼 */
    private String servletVersion;
    
    /** 서버 런타임 명 */
    private String runtimeName;
    
    /** Server Info 사용 */
    private boolean isWebContextUse;
    
    /**
     * @param DeviceAPIWebProjectContext
     * @return IPath
     */
    public IPath getWebLocationPath() {
		return webLocationPath;
	}
    
	/**
	 * @param DeviceAPIWebProjectContext
	 * @return String
	 */
	public String getTemplateType() {
		return templateType;
	}

	/**
	 * @param DeviceAPIWebProjectContext
	 * @param templateType void
	 */
	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

    /**
     * @param DeviceAPIWebProjectContext
     * @return String
     */
    public String getRuntimeName() {
        return runtimeName;
    }

    /**
     * @param DeviceAPIWebProjectContext
     * @param runtimeName void
     */
    public void setRuntimeName(String runtimeName) {
        this.runtimeName = runtimeName;
    }

    /**
     * @param DeviceAPIWebProjectContext
     * @return String
     */
    public String getServletVersion() {
        return servletVersion;
    }

    /**
     * @param DeviceAPIWebProjectContext
     * @param servletVersion void
     */
    public void setServletVersion(String servletVersion) {
        this.servletVersion = servletVersion;
    }

	/**
	 * @param DeviceAPIWebProjectContext
	 * @return String
	 */
	public String getWebExampleFile() {
		return webExampleFile;
	}
	//setter
	public void setWebExampleFile(String webExampleFile) {
		this.webExampleFile = webExampleFile;
	}
	
    /**
     * @param DeviceAPIWebProjectContext
     * @return String
     */
    public String getWebPomFileName() {
        return webPomFileName;
    }
    
    /**
     * @param DeviceAPIWebProjectContext
     * @param webPomFileName void
     */
    public void setWebPomFileName(String webPomFileName) {
        this.webPomFileName = webPomFileName;
    }
    
    /**
     * @param DeviceAPIWebProjectContext
     * @param webLocationPath void
     */
    public void setWebLocationPath(IPath webLocationPath) {
        this.webLocationPath = webLocationPath;
    }
    
    /**
     * @param DeviceAPIWebProjectContext
     * @return String
     */
    public String getWebProjectName() {
        return webProjectName;
    }
    
    /**
     * @param DeviceAPIWebProjectContext
     * @param projectName void
     */
    public void setWebProjectName(String projectName) {
        this.webProjectName = projectName;
    }
    
    /**
     * @param DeviceAPIWebProjectContext
     * @return String
     */
    public String getGroupId() {
        return groupId;
    }
    
    /**
     * @param DeviceAPIWebProjectContext
     * @param groupId void
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    
    /**
     * @param DeviceAPIWebProjectContext
     * @return String
     */
    public String getArtifactId() {
        return artifactId;
    }
    
    /**
     * @param DeviceAPIWebProjectContext
     * @param artifactId void
     */
    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }
    
    /**
     * @param DeviceAPIWebProjectContext
     * @return String
     */
    public String getVersion() {
        return version;
    }
    
    /**
     * @param DeviceAPIWebProjectContext
     * @param version void
     */
    public void setVersion(String version) {
        this.version = version;
    }
    
    /**
     * @param DeviceAPIWebProjectContext
     * @return String
     */
    public String getWebPackageName() {
        return webPackageName;
    }
    
    /**
     * @param DeviceAPIWebProjectContext
     * @param webPackageName void
     */
    public void setWebPackageName(String webPackageName) {
        this.webPackageName = webPackageName;
    }
    
    /**
     * @param DeviceAPIWebProjectContext
     * @return IProject
     */
    public IProject getWebProject() {
        return webProject;
    }
    
    /**
     * @param DeviceAPIWebProjectContext
     * @param webProject void
     */
    public void setWebProject(IProject webProject) {
        this.webProject = webProject;
    }

	/**
	 * @param DeviceAPIWebProjectContext
	 * @return boolean
	 */
	public boolean getIsWebContextUse() {
		return isWebContextUse;
	}

	/**
	 * @param DeviceAPIWebProjectContext
	 * @param isWebContextUse void
	 */
	public void setIsWebContextUse(boolean isWebContextUse) {
		this.isWebContextUse = isWebContextUse;
	}
}
