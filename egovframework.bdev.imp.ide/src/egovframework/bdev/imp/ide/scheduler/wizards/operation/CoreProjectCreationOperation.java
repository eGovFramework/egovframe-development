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
package egovframework.bdev.imp.ide.scheduler.wizards.operation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jst.common.project.facet.core.JavaFacetInstallConfig;
import org.eclipse.wst.common.project.facet.core.IActionDefinition;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IFacetedProject.Action;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;

import egovframework.bdev.imp.ide.com.wizards.operation.BatchProjectCreationOperation;
import egovframework.bdev.imp.ide.common.BatchIdeUtils;
import egovframework.bdev.imp.ide.common.Policy;
import egovframework.bdev.imp.ide.common.ProjectFacetConstants;
import egovframework.bdev.imp.ide.common.ResourceConstants;
import egovframework.bdev.imp.ide.common.ResourceUtils;
import egovframework.bdev.imp.ide.job.wizards.model.NewBatchProjectContext;

/**
 * 기본 프로젝트 생성 오퍼레이션 클래스
 * @author 배치개발환경 개발팀
 * @since 2012.07.02
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2012.07.02  배치개발환경 개발팀    최초 생성
 *   2017.01.18  장동한                  facts 주석처리
 * 
 * 
 * </pre>
 */
public class CoreProjectCreationOperation extends BatchProjectCreationOperation {

    /**
     * 생성자
     * @param context
     */
    public CoreProjectCreationOperation(NewBatchProjectContext context) {
        super(context);
    }

    /**
     * 클래스 패스 설정
     */
    @Override
    protected void configureClasspath(IProgressMonitor monitor)
            throws CoreException {

        IPath projectPath = getProject().getFullPath();

        List<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();

        entries.add(JavaCore.newSourceEntry(projectPath
            .append(ResourceConstants.SOURCE_FOLDER), new IPath[0]));

        entries.add(JavaCore.newSourceEntry(projectPath
            .append(ResourceConstants.RESOURCE_FOLDER), new IPath[]{ResourceConstants.EXCLUDING_PATH},
            projectPath.append(ResourceConstants.DEFAULT_OUTPUT_FOLDER)));

        entries.add(JavaCore.newSourceEntry(projectPath
            .append(ResourceConstants.TEST_SOURCE_FOLDER), new IPath[0],
            projectPath.append(ResourceConstants.TEST_OUTPUT_FOLDER)));

        entries.add(JavaCore.newSourceEntry(projectPath
            .append(ResourceConstants.TEST_RESOURCE_FOLDER), new IPath[]{ResourceConstants.EXCLUDING_PATH},
            projectPath.append(ResourceConstants.TEST_OUTPUT_FOLDER)));

        entries.add(JavaRuntime.getDefaultJREContainerEntry());

        IClasspathEntry[] classpathEntrys =
            (IClasspathEntry[]) entries.toArray(new IClasspathEntry[entries
                .size()]);

        BatchIdeUtils.assignClasspathEntryToJavaProject(getProject(), classpathEntrys, false);

        // Java Facet
        // IPath outputLocation =
        // projectPath.append(ResourceConstants.outputPath);
        // assignOutputLocationToJavaProject(outputLocation);

    }

    /**
     * 기본 리소스 생성
     */
    @Override
    protected void createDefaultResource(IProgressMonitor monitor)
            throws CoreException {

        for (int i = 0; i < ResourceConstants.SYSTEM_FOLDERS.length; i++) {
            IFolder folder =
                getProject().getFolder(ResourceConstants.SYSTEM_FOLDERS[i]);
            ResourceUtils.ensureFolderExists(folder, Policy.subMonitorFor(
                monitor, 1));
        }

    }

    /**
     * 자바 네이처 추가 이후 작업 처리
     */
    @Override
    protected void postJavaNature(IProgressMonitor monitor)
            throws CoreException {
        //R
    }

    /**
     * 자바 네이처 추가 이전 작업 처리
     */
    @Override
    protected void preJavaNature(IProgressMonitor monitor) throws CoreException {
    	//2017-01-18 modify by jdh
    	//java core 프로젝트에서 팻싱처리가 무의미 하여  제거
        //addProjectFacets(monitor);
    }

    /**
     * 프로젝트 패싯 추가
     * @param monitor
     * @throws CoreException
     */
    @SuppressWarnings("deprecation")
    private void addProjectFacets(IProgressMonitor monitor)
            throws CoreException {

        IProject project = getProject();

        IFacetedProject facetedProject =
            ProjectFacetsManager.create(project.getProject(), true, null);
        IProjectFacet javaFacet =
            ProjectFacetsManager
                .getProjectFacet(ProjectFacetConstants.JAVA_FACET_ID);
        IProjectFacetVersion javaFacetVersion = javaFacet.getVersion(ProjectFacetConstants.DEFAULT_JAVA_VERSION);
/*        
//        IProjectFacetVersion javaFacetVersion =
//            javaFacet.getVersion(System.getProperty("java.version").substring(0,3));
        String javaVersion = System.getProperty("java.version").substring(0,3);
        
        if(javaVersion != null && javaVersion.indexOf("1.5")>-1) javaVersion = "5.0";
        IProjectFacetVersion javaFacetVersion = javaFacet.getVersion(javaVersion);
*/
        Set<Action> facetActions = new HashSet<Action>(1);
        for (IActionDefinition def : javaFacetVersion
            .getActionDefinitions(IFacetedProject.Action.Type.INSTALL)) {
            Object object =
                def.createConfigObject(javaFacetVersion, project.getProject()
                    .getName());
            JavaFacetInstallConfig config = (JavaFacetInstallConfig) object;
            config.setSourceFolder(ResourceConstants.SOURCE_FOLDER);
            config
                .setDefaultOutputFolder(ResourceConstants.DEFAULT_OUTPUT_FOLDER);

            facetActions.add(new Action(Action.Type.INSTALL, javaFacetVersion,
                config));
        }
        
        facetedProject.modify(facetActions, Policy.subMonitorFor(monitor, 1));
    }

}
