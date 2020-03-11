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
package egovframework.hdev.imp.ide.common;

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
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.IProfileRegistry;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.ui.ProvisioningUI;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;


/**  
 * IDE용 유틸리티 클래스
 * @Class Name : DeviceAPIIdeUtils
 * @Description : DeviceAPIIdeUtils Class
 * @Modification Information  
 * @
 * @  수정일			수정자		수정내용
 * @ ---------		---------	-------------------------------
 * @ 2012. 7. 12.		이율경		최초생성
 * 
 * @author 디바이스 API 개발환경 팀
 * @since 2012. 7. 12.
 * @version 1.0
 * @see
 * 
 */
public class DeviceAPIIdeUtils {

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
        if (!project.hasNature(natureId)) {
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
            DeviceAPIIdeLog.logError(e);
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
            DeviceAPIIdeLog.logError(e);
        }
    }

    /**
     * 프로젝트의 위치를 설정한다.
     * @param project
     * @param outputPath
     * @throws CoreException
     */
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

            DeviceAPIIdeUtils utils = new DeviceAPIIdeUtils();
            ClasspathComparator classpathComparator =
                utils.new ClasspathComparator();
            Collections.sort(entries, classpathComparator);

            classpath = entries.toArray(new IClasspathEntry[entries.size()]);
            javaProject.setRawClasspath(classpath, null);
        } catch (JavaModelException e) {
            DeviceAPIIdeLog.logError(e);
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
            return ((IClasspathEntry) s1).getPath().toString().compareTo(
                ((IClasspathEntry) s2).getPath().toString());
        }
    }
    
    /**
     * 클래스패스 엔트리 정렬
     * @param project
     * @throws CoreException
     */
    public static void removeClasspathEntry(IProject project, IClasspathEntry classpathEntry)
            throws CoreException {
        IJavaProject javaProject = JavaCore.create(project);

        if ((javaProject == null) || (!javaProject.exists()))
            return;
        
        try {
            IClasspathEntry[] classpath;
            ArrayList<IClasspathEntry> entries;

            classpath = javaProject.getRawClasspath();
            entries = new ArrayList<IClasspathEntry>(Arrays.asList(classpath));
            
            for (int i = 0; i < entries.size(); i++) {
				
            	if(entries.get(i).getPath().toString().compareTo(classpathEntry.getPath().toString()) == 0) {
            		
            		entries.remove(i);
            	}
			}
            
            classpath = entries.toArray(new IClasspathEntry[entries.size()]);
            javaProject.setRawClasspath(classpath, null);
        } catch (JavaModelException e) {
            DeviceAPIIdeLog.logError(e);
        }
    }
    
    /**
     * Maven3 존재 유무
     */
    public static boolean isMaven3Version() {
		ProvisioningUI provisioningUI = ProvisioningUI.getDefaultUI();
    	ProvisioningSession session = provisioningUI.getSession();
    	IProfileRegistry profileRegistry = (IProfileRegistry)session.getProvisioningAgent().getService(IProfileRegistry.SERVICE_NAME);
    	IProfile[] profiles = profileRegistry.getProfiles();
    	
    	for (int idx = 0; idx < profiles.length; idx++) {
            IQueryResult<IInstallableUnit> queryResult = profiles[idx].query(QueryUtil.createIUQuery(ProjectFacetConstants.MAVEN2_FEATURE_ID), null);
            if (!queryResult.isEmpty()){
            	
            	return false;
            } else {
            	IQueryResult<IInstallableUnit> queryResult2 = profiles[idx].query(QueryUtil.createIUQuery(ProjectFacetConstants.MAVEN3_FEATURE_ID), null);
            	if (!queryResult2.isEmpty()){
            		
            		return true;
            	}
            }
    	}
    	
    	return false;
	}
    
    /**
     * Maven3 존재 유무
     */
    public static boolean isM2EToAndroid() {
		ProvisioningUI provisioningUI = ProvisioningUI.getDefaultUI();
    	ProvisioningSession session = provisioningUI.getSession();
    	IProfileRegistry profileRegistry = (IProfileRegistry)session.getProvisioningAgent().getService(IProfileRegistry.SERVICE_NAME);
    	IProfile[] profiles = profileRegistry.getProfiles();
    	
    	for (int idx = 0; idx < profiles.length; idx++) {
            IQueryResult<IInstallableUnit> queryResult = profiles[idx].query(QueryUtil.createIUQuery(ProjectFacetConstants.M2E_ANDROID_FEATURE_ID), null);
            if (!queryResult.isEmpty()){
            	
            	return true;
            } 
    	}
    	
    	return false;
	}
    
    /**
     * ADT 존재 유무
     */
    public static boolean isAndroidDevelopmentTool() {
		ProvisioningUI provisioningUI = ProvisioningUI.getDefaultUI();
    	ProvisioningSession session = provisioningUI.getSession();
    	IProfileRegistry profileRegistry = (IProfileRegistry)session.getProvisioningAgent().getService(IProfileRegistry.SERVICE_NAME);
    	IProfile[] profiles = profileRegistry.getProfiles();
    	
    	for (int idx = 0; idx < profiles.length; idx++) {
            IQueryResult<IInstallableUnit> queryResult = profiles[idx].query(QueryUtil.createIUQuery(ProjectFacetConstants.ANDROID_FEATURE_ID), null);
            if (!queryResult.isEmpty()){
            	
            	return true;
            } 
    	}
    	
    	return false;
	}
    
}
