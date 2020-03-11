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
package egovframework.dev.imp.codegen.template.impl;

import net.sf.abstractplugin.core.EclipseProjectUtils;

import org.eclipse.core.runtime.Path;
import org.eclipse.eclipsework.core.exception.EWException;
import org.eclipse.eclipsework.core.impl.interfaces.resources.ResourceManager;

import egovframework.dev.imp.codegen.template.CodeGenLog;


/**
 * 
 * 리소스 관리 클래스
 * <p><b>NOTE:</b> 코드젠 결과 리소스를 생성 관리하는 클래스 
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
public class CodeGenResourceManager extends ResourceManager {

    /**
     * 
     * 리소스 생성
     *
     * @param file 
     * @param override 
     * @return
     * @throws EWException
     */
    @Override
    public boolean createResource(org.eclipse.eclipsework.core.interfaces.resources.IFile file, boolean override) throws EWException {

        CodeGenLog.logInfo("Create Resource : " + file.getPath());
        String path = file.getPath().substring(0, file.getPath().lastIndexOf("/"));
        
        
        if (path.lastIndexOf("/") > 0) {

            String folderPath = file.getPath().substring(0, file.getPath().lastIndexOf("/"));
            org.eclipse.core.resources.IFolder f = EclipseProjectUtils.getSelectedJavaProject().getProject().getParent().getFolder(new Path(folderPath));
            try { 
                if (!f.exists()) {
                    f.create(true, true, null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return super.createResource(file, override);
    }
}
