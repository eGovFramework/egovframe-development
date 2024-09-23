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
/*******************************************************************************
#   * Copyright (c) 2000-2007 IBM Corporation and others.
#   * All rights reserved. This program and the accompanying materials
#   * are made available under the terms of the Eclipse Public License v1.0
#   * which accompanies this distribution, and is available at
#   * http://www.eclipse.org/legal/epl-v10.html
#   *
#   * Contributors:
#   * IBM Corporation - initial API and implementation
#   *******************************************************************************/

package egovframework.bdev.imp.ide.job.wizards.operation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jst.common.project.facet.core.JavaFacetInstallConfig;
import org.eclipse.jst.j2ee.web.project.facet.IWebFacetInstallDataModelProperties;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.IActionDefinition;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IFacetedProject.Action;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.common.project.facet.core.runtime.IRuntime;
import org.eclipse.wst.common.project.facet.core.runtime.RuntimeManager;

import egovframework.bdev.imp.ide.com.wizards.operation.BatchProjectCreationOperation;
import egovframework.bdev.imp.ide.common.BatchIdeLog;
import egovframework.bdev.imp.ide.common.BatchIdeUtils;
import egovframework.bdev.imp.ide.common.Policy;
import egovframework.bdev.imp.ide.common.ProjectFacetConstants;
import egovframework.bdev.imp.ide.common.ResourceConstants;
import egovframework.bdev.imp.ide.common.ResourceUtils;
import egovframework.bdev.imp.ide.job.wizards.model.NewBatchProjectContext;
import egovframework.bdev.imp.ide.scheduler.wizards.model.NewBatchWebProjectContext;

/**
 * Batch eGovFramework 웹 프로젝트 생성 오퍼레이션 추상 클래스
 * @author 배치개발환경 개발팀
 * @since 2012.07.02
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *	수정일      	수정자           수정내용
 *  -------			--------    ---------------------------
 *	2012.07.02	배치개발환경 개발팀    최초 생성
 *
 * 
 * </pre>
 */
public class BatchWebProjectCreationOperation extends BatchProjectCreationOperation {

    /**
     * 생성자
     * @param context
     */
    public BatchWebProjectCreationOperation(NewBatchProjectContext context) {
        super(context);
    }

    /** 서블릿 버전 */
    private String getServletVersion() {
        return ((NewBatchWebProjectContext) context).getServletVersion();
    }

    /** 런타임 이름 */
    private String getRuntimeName() {
        return ((NewBatchWebProjectContext) context).getRuntimeName();
    }

    /**
     * 패싯 런타임 찾기
     * @param runtime
     * @return
     */
    private IRuntime findFacetRuntime(IRuntime runtime) {
		String runtimeName = getRuntimeName();
		if (runtimeName == null) {
			return null;
		}
		if (runtime != null) {
			runtimeName = runtime.getName();
		}
		Set<IRuntime> set = RuntimeManager.getRuntimes();
		Iterator<IRuntime> iterator = set.iterator();
		while (iterator.hasNext()) {
			IRuntime r = (IRuntime) iterator.next();

			if (runtimeName.equals(r.getName())) {
				return r;
			}
		}
		return null;
    }

    /**
     * 프로젝트 패싯 추가
     * @param monitor
     * @throws CoreException
     */
    @SuppressWarnings( {"deprecation" })
    private void addProjectFacets(IProgressMonitor monitor)
            throws CoreException {
		IProject project = getProject();

		IFacetedProject facetedProject = ProjectFacetsManager.create(project.getProject(), true, null);
		IProjectFacet javaFacet = ProjectFacetsManager.getProjectFacet(ProjectFacetConstants.JAVA_FACET_ID);
		IProjectFacetVersion javaFacetVersion = javaFacet.getVersion(ProjectFacetConstants.DEFAULT_JAVA_VERSION);
		/*
		//        IProjectFacetVersion javaFacetVersion =
		//            javaFacet.getVersion(ProjectFacetConstants.DEFAULT_JAVA_VERSION);
		String javaVersion = System.getProperty("java.version").substring(0, 3);

		if (javaVersion != null && javaVersion.indexOf("1.5") > -1)
			javaVersion = "5.0";
		IProjectFacetVersion javaFacetVersion = javaFacet.getVersion(javaVersion);
		*/
		Set<Action> facetActions = new HashSet<Action>(2);
		for (IActionDefinition def : javaFacetVersion.getActionDefinitions(IFacetedProject.Action.Type.INSTALL)) {
			Object object = def.createConfigObject(javaFacetVersion, project.getProject().getName());
			JavaFacetInstallConfig config = (JavaFacetInstallConfig) object;
			config.setSourceFolder(ResourceConstants.SOURCE_FOLDER);
			config.setDefaultOutputFolder(ResourceConstants.DEFAULT_OUTPUT_FOLDER);

			facetActions.add(new Action(Action.Type.INSTALL, javaFacetVersion, config));
		}

		IProjectFacet webFacet = ProjectFacetsManager.getProjectFacet(ProjectFacetConstants.WEB_FACET_ID);
		IProjectFacetVersion webFacetVersion = webFacet.getVersion(getServletVersion());
		for (IActionDefinition def : webFacetVersion.getActionDefinitions(IFacetedProject.Action.Type.INSTALL)) {
			Object object = def.createConfigObject(webFacetVersion, project.getProject().getName());
			IDataModel model = (IDataModel) object;
			model.setStringProperty(IWebFacetInstallDataModelProperties.SOURCE_FOLDER, ResourceConstants.SOURCE_FOLDER.toString());
			model.setStringProperty(IWebFacetInstallDataModelProperties.CONFIG_FOLDER, ResourceConstants.WEB_ROOT.toString());
			model.setStringProperty(IWebFacetInstallDataModelProperties.CONTEXT_ROOT, getArtifactId());
			facetActions.add(new Action(Action.Type.INSTALL, webFacetVersion, model));
		}
		facetedProject.modify(facetActions, Policy.subMonitorFor(monitor, 1));
    }

    /**
     * 기본 리소스 생성
     */
    @Override
    protected void createDefaultResource(IProgressMonitor monitor)
            throws CoreException {

		for (int i = 0; i < ResourceConstants.SYSTEM_FOLDERS.length; i++) {
			org.eclipse.core.resources.IFolder folder = getProject().getFolder(ResourceConstants.SYSTEM_FOLDERS[i]);
			ResourceUtils.ensureFolderExists(folder, Policy.subMonitorFor(monitor, 1));
		}
    }

    /**
     * 자바 네이처 추가 이후 작업 처리
     */
    @SuppressWarnings("deprecation")
    @Override
    protected void postJavaNature(IProgressMonitor monitor)
            throws CoreException {

		try {
			// if(wcco != null) wcco.execute(monitor,
			// null);
			IFacetedProject facetedProject = ProjectFacetsManager.create(getProject());
			IRuntime runtime = findFacetRuntime(null/* runtime */);
			facetedProject.setRuntime(runtime, monitor);
			if (runtime != null) {
				Set<IRuntime> set = new HashSet<IRuntime>();
				set.add(runtime);
				facetedProject.setTargetedRuntimes(set, monitor);
			}
		} catch (Exception e) {
            BatchIdeLog.logError(e);
        }

    }

    /**
     * 자바 네이처 추가 이전 작업
     */
    @Override
    protected void preJavaNature(IProgressMonitor monitor) throws CoreException {
        // createWTPNature(monitor);
        addProjectFacets(monitor);
    }

    /**
     * 클래스 패스 설정
     */
    @Override
    protected void configureClasspath(IProgressMonitor monitor)
            throws CoreException {

		IPath projectPath = getProject().getFullPath();

		List<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();

		entries.add(JavaCore.newSourceEntry(projectPath.append(ResourceConstants.RESOURCE_FOLDER), new IPath[] { ResourceConstants.EXCLUDING_PATH },
				projectPath.append(ResourceConstants.DEFAULT_OUTPUT_FOLDER)));

		entries.add(JavaCore.newSourceEntry(projectPath.append(ResourceConstants.TEST_SOURCE_FOLDER), new IPath[0], projectPath.append(ResourceConstants.TEST_OUTPUT_FOLDER)));

		entries.add(JavaCore.newSourceEntry(projectPath.append(ResourceConstants.TEST_RESOURCE_FOLDER), new IPath[] { ResourceConstants.EXCLUDING_PATH },
				projectPath.append(ResourceConstants.TEST_OUTPUT_FOLDER)));

		IClasspathEntry[] classpathEntrys = (IClasspathEntry[]) entries.toArray(new IClasspathEntry[entries.size()]);

        BatchIdeUtils.assignClasspathEntryToJavaProject(getProject(), classpathEntrys, false);

    }
    
//    @Override
//    protected void configureWebClasspath(IProgressMonitor monitor)
//            throws CoreException {
//
//    	 IPath projectPath = getProject().getFullPath();
//
//         List<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();
//
//         entries.add(JavaCore.newSourceEntry(projectPath
//             .append(ResourceConstants.RESOURCE_FOLDER), new IPath[]{ResourceConstants.EXCLUDING_PATH},
//             projectPath.append(ResourceConstants.DEFAULT_OUTPUT_FOLDER)));
//
//         entries.add(JavaCore.newSourceEntry(projectPath
//             .append(ResourceConstants.TEST_SOURCE_FOLDER), new IPath[0],
//             projectPath.append(ResourceConstants.TEST_OUTPUT_FOLDER)));
//
//         entries.add(JavaCore.newSourceEntry(projectPath
//             .append(ResourceConstants.TEST_RESOURCE_FOLDER), new IPath[]{ResourceConstants.EXCLUDING_PATH},
//             projectPath.append(ResourceConstants.TEST_OUTPUT_FOLDER)));
//
//         IClasspathEntry[] classpathEntrys =
//             (IClasspathEntry[]) entries.toArray(new IClasspathEntry[entries
//                 .size()]);
//
//         BatchIdeUtils.assignClasspathEntryToJavaProject(getProject(),
//             classpathEntrys, true);
//
//    }

}
