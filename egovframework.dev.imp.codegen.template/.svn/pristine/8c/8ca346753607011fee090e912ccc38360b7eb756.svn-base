/*
 * Copyright 2008-2009 MOSPA(Ministry of Security and Public Administration).
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
package egovframework.dev.imp.codegen.template.util;

import org.eclipse.core.resources.IProject;

/**
 * 
 * Velocity 템플릿에서 사용되어지는 유틸리티 클래스
 * <p><b>NOTE:</b> Velocity 템플릿내에서 사용되어지는 유용한 유틸리티
 * 
 * @author 개발환경 개발팀 이흥주
 * @since 2009.08.03
 * @version 1.0
 * @see
 *
 * <pre>
 *  == 개정이력(Modification Information) ==
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.08.03  이흥주          최초 생성
 *
 * </pre>
 */
public class TemplateUtil {
    
    /** 프로젝트 */
    private final IProject project;
    
    /**
     * 
     * 생성자
     *
     * @param project
     */
    public TemplateUtil(IProject project) {
        this.project = project;
    }
    
    /**
     * 
     * 전체 경로에서 소스 패스를 제거
     *
     * @param path
     * @return
     */
    public String removeSourcePath(String path){
        
        String result = path;
        
        String[] sourcePaths = ResourceUtils.getSourcePath(project);
        for(String sourcePath : sourcePaths){
            if (path.indexOf(sourcePath) == 0) {
                result = path.substring(sourcePath.length());
            }
        }        
        return result;
    }
}
