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

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import egovframework.dev.imp.codegen.template.CodeGenLog;

/**
 * 
 * 리소스 관리 유틸리티 클래스
 * <p><b>NOTE:</b> 리소스 생성시 폴더 존재 여부 확인과 폴더 생성을 위한 유틸리티
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
public class ResourceUtils {
    /**
     * 폴더 존재 여부 확인하여 없으면 생성
     * @param folder
     * @param monitor
     * @throws CoreException
     */
    public static void ensureFolderExists(IFolder folder,
            IProgressMonitor monitor) throws CoreException {
        int ticks = folder.getProjectRelativePath().segmentCount();
        IProgressMonitor subMonitor = Policy.monitorFor(monitor);
        subMonitor.beginTask(null, ticks);
        IContainer parent;
        String[] segs;
        int i;
        IFolder tmp;
        try {
            if (folder.exists())
                return;
        } finally {
            subMonitor.done();
        }
        parent = folder.getProject();
        segs = folder.getProjectRelativePath().segments();
        for (i = 0; i < segs.length; i++) {
            tmp = parent.getFolder(new Path(segs[i]));
            if ((tmp != null) && (!tmp.exists()))
                tmp.create(true, true, Policy.subMonitorFor(monitor, 1));
            // else
            // monitor.worked(1);
            parent = tmp;
        }

        return;
    }

    /**
     * 파일명을 기반으로 폴더 존재 여부 확인하여 없으면 생성
     * @param project
     * @param fileName
     * @throws CoreException
     */
    public static void ensureFolderExists(IProject project, String fileName)
            throws CoreException {
        String path = ""; //$NON-NLS-1$

        if (fileName.lastIndexOf("/") < 0) //$NON-NLS-1$
            return;

        path = fileName.substring(0, fileName.lastIndexOf("/")); //$NON-NLS-1$

        IFolder folder = project.getFolder(path);
        ResourceUtils.ensureFolderExists(folder, null);
    }
    
    /**
     * 
     * 자바 프로젝트 로부터 소스 경로 가져오기
     *
     * @param project
     * @return
     */
    @SuppressWarnings("unused")
	public static String[] getSourcePath(IProject project) {
        ArrayList<String> path = new ArrayList<String>();
        
        IJavaProject javaProject = JavaCore.create(project);
        if ((javaProject == null) || (!javaProject.exists()))
            return null;
        try {
            IClasspathEntry[] classpath;
            ArrayList<IClasspathEntry> entries;
            classpath = javaProject.getRawClasspath();
            entries =
                new ArrayList<IClasspathEntry>(Arrays.asList(classpath));
            for(int i=0; i< classpath.length; i++){
                if (classpath[i].getEntryKind() == IClasspathEntry.CPE_SOURCE){
                    path.add(classpath[i].getPath().toString());
                }
                
            }
        }
        catch(Exception ex)        {
            CodeGenLog.logError(ex);
        }
        
        return (String[])path.toArray(new String[path.size()]);
    }
}
