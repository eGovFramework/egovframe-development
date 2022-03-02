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
package egovframework.boot.dev.imp.ide.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;


/**
 * 유틸리티 클래스
 * @author 표준프레임워크센터 이정은
 * @since 2021.12.21
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2021.12.21  이정은          최초 생성
 * 
 * 
 * </pre>
 */
public class BootIdeUtils {

    /**
     * 프로젝트에 네이처 추가
     * @param project
     * @param natureId
     * @param monitor
     * @throws CoreException
     */
    public static void addNatureToProject(IProject project, String natureId,
            IProgressMonitor monitor)

    throws CoreException {
    	
		System.out.println("#################################################");
		System.out.println("##### BootIdeUtils addNatureToProject start ... ");
		System.out.println("#################################################");
		
        if (!project.hasNature(natureId)) {
        	
        	System.out.println("#################################################");
			System.out.println("##### BootIdeUtils addNatureToProject natureId >>> " + natureId);
			System.out.println("#################################################");
			
			
            IProjectDescription description = project.getDescription();
            String[] prevNatures = description.getNatureIds();
            String[] newNatures = new String[prevNatures.length + 1];
            System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
            newNatures[prevNatures.length] = natureId;
            description.setNatureIds(newNatures);
            project.setDescription(description, Policy
                .subMonitorFor(monitor, 1));
        }

        return;
    }

    /**
     * 클래스패스 컨테이너 적용
     * @param project
     * @param containerPath
     * @param append
     * @throws CoreException
     */
    public static void assignClasspathContainerToJavaProject(IProject project,
            IPath containerPath, boolean append) throws CoreException {
        IClasspathEntry sdkEntry = JavaCore.newContainerEntry(containerPath);
        assignClasspathEntryToJavaProject(project, sdkEntry, append);
    }

    /**
     * 클래스패스 엔트리 적용
     * @param classpathEntry
     * @param append
     * @throws CoreException
     */
    public static void assignClasspathEntryToJavaProject(IProject project,
            IClasspathEntry classpathEntry, boolean append)
            throws CoreException {
        IJavaProject javaProject = JavaCore.create(project);
        if ((javaProject == null) || (!javaProject.exists()))
            return;
        try {

            IClasspathEntry[] classpath;
            ArrayList<IClasspathEntry> entries;
            if (append) {
                classpath = javaProject.getRawClasspath();
                entries =
                    new ArrayList<IClasspathEntry>(Arrays.asList(classpath));
            } else {
                entries = new ArrayList<IClasspathEntry>();
            }

            entries.add(classpathEntry);

            classpath = entries.toArray(new IClasspathEntry[entries.size()]);
            javaProject.setRawClasspath(classpath, null);
        } catch (JavaModelException e) {
            BootIdeLog.logError(e);
        }
    }

    /**
     * 클래스패스 엔트리 적용
     * @param project
     * @param classpathEntrys
     * @param append
     * @throws CoreException
     */
    public static void assignClasspathEntryToJavaProject(IProject project,
            IClasspathEntry[] classpathEntrys, boolean append)
            throws CoreException {
        assignClasspathEntryToJavaProject(project, Arrays
            .asList(classpathEntrys), append);
    }
    
    /**
     * 클래스패스 엔트리 적용
     * @param project
     * @param collection
     * @param append
     * @throws CoreException
     */
    public static void assignClasspathEntryToJavaProject(IProject project,
            Collection<IClasspathEntry> collection, boolean append)
            throws CoreException {
        IJavaProject javaProject = JavaCore.create(project);
        if ((javaProject == null) || (!javaProject.exists()))
            return;
        try {
            IClasspathEntry[] classpath;
            ArrayList<IClasspathEntry> entries;
            if (append) {
                classpath = javaProject.getRawClasspath();
                entries =
                    new ArrayList<IClasspathEntry>(Arrays.asList(classpath));
            } else {
                entries = new ArrayList<IClasspathEntry>();
            }

            entries.addAll(collection);

            classpath = entries.toArray(new IClasspathEntry[entries.size()]);
            javaProject.setRawClasspath(classpath, null);
        } catch (JavaModelException e) {
            BootIdeLog.logError(e);
        }
    }

    public static void assignOutputLocationToJavaProject(IProject project,
            IPath outputPath) throws CoreException {
        IJavaProject javaProject = JavaCore.create(project);
        javaProject.setOutputLocation(outputPath, null);
    }

    /**
     * 클래스패스 엔트리 정렬
     * @param project
     * @throws CoreException
     */
    @SuppressWarnings("unchecked")
	public static void sortClasspathEntry(IProject project)
            throws CoreException {
        IJavaProject javaProject = JavaCore.create(project);

        if ((javaProject == null) || (!javaProject.exists()))
            return;
        try {
            IClasspathEntry[] classpath;
            ArrayList<IClasspathEntry> entries;

            classpath = javaProject.getRawClasspath();
            entries = new ArrayList<IClasspathEntry>(Arrays.asList(classpath));

            BootIdeUtils utils = new BootIdeUtils();
            ClasspathComparator classpathComparator =
                utils.new ClasspathComparator();
            Collections.sort(entries, classpathComparator);

            classpath = entries.toArray(new IClasspathEntry[entries.size()]);
            javaProject.setRawClasspath(classpath, null);
        } catch (JavaModelException e) {
            BootIdeLog.logError(e);
        }
    }

    /**
     * 클래스패스 비교
     * @author JeDi
     *
     */
    @SuppressWarnings("rawtypes")
	public class ClasspathComparator implements Comparator {
        public int compare(Object s1, Object s2) {

            // ascending 정렬
            return ((IClasspathEntry) s1).getPath().toString().compareTo(((IClasspathEntry) s2).getPath().toString());
        }
    }

}
