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
package egovframework.dev.imp.ide.natures;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

/**
 * eGovFramework 네이처 클래스
 * @author 개발환경 개발팀 이흥주
 * @since 2009.06.01
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.03.20  이흥주          최초 생성
 * 
 * 
 * </pre>
 */
public class EgovNature implements IProjectNature {
	
	public static final String EGOV_NAME_ID = "egovframework.dev.imp.ide.natures.egovnature";
    /** 프로젝트 */
    private IProject project;

    /** 네이처 적용 */
    public void configure() throws CoreException {

    }

    /** 네이처 해제 */
    public void deconfigure() throws CoreException {

    }

    /** 프로젝트 가져오기 */
    public IProject getProject() {
        return this.project;
    }

    /** 프로젝트 설정 */
    public void setProject(IProject project) {
        this.project = project;

    }
    
    public static boolean isEgovNatureEnabled(IProject project) throws CoreException{
    	return project.isNatureEnabled(EGOV_NAME_ID);
    }

}
