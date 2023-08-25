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
package egovframework.bdev.imp.ide.com.wizards.operation;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.IProfileRegistry;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.ui.ProvisioningUI;
import org.eclipse.jdt.core.IAccessRule;
import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.m2e.core.MavenPlugin;

import egovframework.bdev.imp.ide.EgovBatchIdePlugin;
import egovframework.bdev.imp.ide.common.BatchIdeLog;
import egovframework.bdev.imp.ide.common.BatchIdeMessages;
import egovframework.bdev.imp.ide.common.BatchIdeUtils;
import egovframework.bdev.imp.ide.common.Policy;
import egovframework.bdev.imp.ide.common.ProjectFacetConstants;
import egovframework.bdev.imp.ide.common.ResourceConstants;
import egovframework.bdev.imp.ide.common.ResourceUtils;
import egovframework.bdev.imp.ide.job.wizards.model.NewBatchProjectContext;
import egovframework.bdev.imp.ide.scheduler.wizards.model.NewBatchWebProjectContext;

/**
 * Batch eGovFramework 프로젝트 생성 오퍼레이션 추상 클래스
 * 
 * @author 배치개발환경 개발팀
 * @since 2012.07.02
 * @version 1.0
 * @see
 * 
 *      <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *	수정일      	수정자           수정내용
 *  -------			--------    ---------------------------
 *	2012.07.02	배치개발환경 개발팀    최초 생성
 *
 * 
 *      </pre>
 */
public abstract class BatchProjectCreationOperation implements IRunnableWithProgress, EgovBatchProject {

	/** 메이븐 클래스 패스 엔트리 속성 값 */
	private static final String MAVEN_CLASSPATHENTRY_ATTRIBUTE_VALUE = "/WEB-INF/lib";
	/** 메이븐 클래스 패스 엔트리 속성 명 */
	private static final String MAVEN_CLASSPATHENTRY_ATTRIBUTE_NAME = "org.eclipse.jst.component.dependency";

	/** 모바일 컨텍스트 */
	protected NewBatchProjectContext context;

	/** pre 자바 네이처 */
	protected abstract void preJavaNature(IProgressMonitor monitor) throws CoreException;

	/** post 자바 네이처 */
	protected abstract void postJavaNature(IProgressMonitor monitor) throws CoreException;

	/** default 리소스 생성 */
	protected abstract void createDefaultResource(IProgressMonitor monitor) throws CoreException;

	/** configure 클래스패스 */
	protected abstract void configureClasspath(IProgressMonitor monitor) throws CoreException;

	/**
	 * 생성자
	 * 
	 * @param context
	 */
	public BatchProjectCreationOperation(NewBatchProjectContext context) {
		this.context = context;
	}

	/**
	 * 예제 템플릿 생성
	 * 
	 * @param monitor
	 * @throws CoreException
	 */
	private void createExamples(IProgressMonitor monitor) throws CoreException {
		if (!context.isCreateExample())
			return;

		if (context.getOptionalExampleFile() != null) {
			for (String exampleFile : context.getOptionalExampleFile()) {
				createExample2(monitor, exampleFile);
			}
		}
		createExample2(monitor, context.getDefaultExampleFile());

	}

	/**
	 * 예제 템플릿 생성
	 * 
	 * @param monitor
	 * @throws CoreException
	 */
	@SuppressWarnings("unchecked")
	private void createExample2(IProgressMonitor monitor, String exampleFile) throws CoreException {

		try {
			Path path = new Path(EgovBatchIdePlugin.getDefault().getInstalledPath());
			String zipFileName = path.append(ResourceConstants.EXAMPLES_PATH + exampleFile).toOSString();
			ZipFile zipFile = new ZipFile(zipFileName, "UTF-8");
			Enumeration<? extends ZipEntry> enumeration = zipFile.getEntries();

			IFile file;

			ZipEntry entry;
			BufferedInputStream is = null;
			String entryName = "";
			while (enumeration.hasMoreElements()) {
				entry = (ZipEntry) enumeration.nextElement();
				entryName = entry.getName();
				monitor.subTask(entryName);

				is = new BufferedInputStream(zipFile.getInputStream(entry));

				if (entry.isDirectory())
					continue;

				ResourceUtils.ensureFolderExists(getProject(), entryName);

				if (entryName.equals(ResourceConstants.POM_FILENAME)) {
					updatePomFile(getProject(), is);
				} else if (entryName.equals("src/main/java/egovframework/rte/cmmn/web/ImagePaginationRenderer.java")
						|| entryName.equals("src/main/webapp/WEB-INF/web.xml")) {
					updateContextFile(getProject(), is, entryName);
				} else {
					file = getProject().getFile(new Path(entryName));
					if (file.exists()) {
						file.delete(true, Policy.subMonitorFor(monitor, 1));
					} else {
						file.create(is, true, Policy.subMonitorFor(monitor, 1));
					}
				}
				is.close();
			}
			zipFile.close();

		} catch (Exception ex) {
			BatchIdeLog.logError(ex);
		}
	}

	/**
	 * 예제 템플릿 생성
	 * 
	 * @throws CoreException
	 */
	@SuppressWarnings("unchecked")
	private void createExample() throws CoreException {

		if (!context.isCreateExample())
			return;

		try {
			Path path = new Path(EgovBatchIdePlugin.getDefault().getInstalledPath());
			String zipFileName = path.append(context.getDefaultExampleFile()).toOSString();
			ZipFile zipFile = new ZipFile(zipFileName, "UTF-8");
			Enumeration<? extends ZipEntry> enumeration = zipFile.getEntries();
			IPath targetPath = getLocationPath().append(getProjectName());

			ZipEntry entry;
			BufferedOutputStream dest = null;
			BufferedInputStream is = null;
			int BUFFER = 2048;
			while (enumeration.hasMoreElements()) {
				entry = (ZipEntry) enumeration.nextElement();

				is = new BufferedInputStream(zipFile.getInputStream(entry));
				int count;
				byte data[] = new byte[BUFFER];

				if (entry.isDirectory())
					continue;

				ResourceUtils.ensureFolderExists(getProject(), entry.getName());

				if (entry.getName().equals(ResourceConstants.POM_FILENAME)) {
					updatePomFile(getProject(), is);
				} else {
					FileOutputStream fos = new FileOutputStream(targetPath.append(entry.getName()).toOSString());

					dest = new BufferedOutputStream(fos, BUFFER);
					while ((count = is.read(data, 0, BUFFER)) != -1) {
						dest.write(data, 0, count);
					}
					dest.flush();
					dest.close();
					fos.close();
				}
				is.close();
			}
			zipFile.close();

		} catch (Exception ex) {
			BatchIdeLog.logError(ex);
		}
	}

	/**
	 * POM 파일 변경
	 * 
	 * @param project
	 * @param is
	 * @throws CoreException
	 */
	private void updatePomFile(IProject project, BufferedInputStream is) throws CoreException {

		IFile file = project.getFile(new Path(ResourceConstants.POM_FILENAME));
		if (file.exists()) {
			file.delete(true, null);
		}
		try {
			String document = stream2string(is, context.getGroupId(), context.getArtifactId(), context.getVersion(),
					context.getPackageName());
			ByteArrayInputStream stream = new ByteArrayInputStream(document.getBytes());

			file.create(stream, true, null);
		} catch (IOException e) {
			BatchIdeLog.logError(e);
		}
	}

	/**
	 * 컨텍스트 설정 후 파일 생성
	 * 
	 * @param project
	 * @param is
	 * @param fileName
	 * @throws CoreException
	 */
	private void updateContextFile(IProject project, BufferedInputStream is, String fileName) throws CoreException {

		IFile file = project.getFile(new Path(fileName));
		if (file.exists()) {
			file.delete(true, null);
		}
		try {
			String document = stream2string(is, context.getGroupId(), context.getArtifactId(), context.getVersion(),
					context.getPackageName());
			ByteArrayInputStream stream = new ByteArrayInputStream(document.getBytes());

			file.create(stream, true, null);
		} catch (IOException e) {
			BatchIdeLog.logError(e);
		}
	}

	/**
	 * 프로젝트 생성
	 * 
	 * @param monitor
	 * @throws CoreException
	 */
	private void createProject(IProgressMonitor monitor) throws CoreException {
		IProject project = getProject();
		IProjectDescription desc = project.getWorkspace().newProjectDescription(project.getName());
		IPath locationPath = getLocationPath();
		if (Platform.getLocation().equals(locationPath))
			locationPath = null;
		desc.setLocation(locationPath);
		project.create(desc, Policy.subMonitorFor(monitor, 1));

		if (!project.isOpen())
			project.open(Policy.subMonitorFor(monitor, 1));
	}

	// private void updateJavaNature(IProgressMonitor
	// monitor) throws CoreException {
	// IdeUtils.addNatureToProject(getProject(),
	// JavaCore.NATURE_ID, monitor);
	//
	// assignClasspathEntryToJavaProject(PreferenceConstants.getDefaultJRELibrary());
	// }

	/**
	 * 스프링 네이처 추가
	 */
	private void createSpringNature(IProgressMonitor monitor) throws CoreException {
		BatchIdeUtils.addNatureToProject(getProject(), "org.springframework.ide.eclipse.core.springnature", monitor);
	}

	/**
	 * eGovFramework 네이처 추가
	 * 
	 * @param monitor
	 * @throws CoreException
	 */
	private void createEgovNature(IProgressMonitor monitor) throws CoreException {
		BatchIdeUtils.addNatureToProject(getProject(), EgovBatchIdePlugin.ID_NATURE, monitor);
	}

	private void createMavenNature(IProgressMonitor monitor) throws CoreException {
		ProvisioningUI provisioningUI = ProvisioningUI.getDefaultUI();
		ProvisioningSession session = provisioningUI.getSession();
		IProfileRegistry profileRegistry = (IProfileRegistry) session.getProvisioningAgent()
				.getService(IProfileRegistry.SERVICE_NAME);
		IProfile[] profiles = profileRegistry.getProfiles();

		for (int idx = 0; idx < profiles.length; idx++) {
			IQueryResult<IInstallableUnit> queryResult = profiles[idx]
					.query(QueryUtil.createIUQuery(ProjectFacetConstants.MAVEN2_FEATURE_ID), null);
			if (!queryResult.isEmpty()) {
				BatchIdeUtils.addNatureToProject(getProject(), ProjectFacetConstants.MAVEN2_NATURE_ID, monitor);
			} else {
				IQueryResult<IInstallableUnit> queryResult2 = profiles[idx]
						.query(QueryUtil.createIUQuery(ProjectFacetConstants.MAVEN3_FEATURE_ID), null);
				if (!queryResult2.isEmpty()) {
					BatchIdeUtils.addNatureToProject(getProject(), ProjectFacetConstants.MAVEN3_NATURE_ID, monitor);
				}
			}
		}
	}

	/**
	 * Maven ContainerPath 설정
	 * 
	 * @throws CoreException
	 */
	private IPath createMavenContainerPath() throws CoreException {

		ProvisioningUI provisioningUI = ProvisioningUI.getDefaultUI();
		ProvisioningSession session = provisioningUI.getSession();
		IProfileRegistry profileRegistry = (IProfileRegistry) session.getProvisioningAgent()
				.getService(IProfileRegistry.SERVICE_NAME);
		IProfile[] profiles = profileRegistry.getProfiles();

		for (int idx = 0; idx < profiles.length; idx++) {
			IQueryResult<IInstallableUnit> queryResult = profiles[idx]
					.query(QueryUtil.createIUQuery(ProjectFacetConstants.MAVEN2_FEATURE_ID), null);
			if (!queryResult.isEmpty()) {
				return new Path(ProjectFacetConstants.MAVEN2_CLASSPATH_CONTAINER_ID);
			} else {
				IQueryResult<IInstallableUnit> queryResult2 = profiles[idx]
						.query(QueryUtil.createIUQuery(ProjectFacetConstants.MAVEN3_FEATURE_ID), null);
				if (!queryResult2.isEmpty()) {
					return new Path(ProjectFacetConstants.MAVEN3_CLASSPATH_CONTAINER_ID);
				}
			}
		}

		return new Path(ProjectFacetConstants.MAVEN2_CLASSPATH_CONTAINER_ID);
	}

	/**
	 * 메이븐 네이처 추가
	 * 
	 * @param monitor
	 * @throws CoreException
	 */
	private void updateMavenNature(IProgressMonitor monitor) throws CoreException {

		IPath containerPath = createMavenContainerPath();

		IClasspathEntry sdkEntry = null;
		if (this.context instanceof NewBatchProjectContext || this.context instanceof NewBatchWebProjectContext) {
			IClasspathAttribute attribute = JavaCore.newClasspathAttribute(MAVEN_CLASSPATHENTRY_ATTRIBUTE_NAME,
					MAVEN_CLASSPATHENTRY_ATTRIBUTE_VALUE);
			sdkEntry = JavaCore.newContainerEntry(containerPath, new IAccessRule[0],
					new IClasspathAttribute[] { attribute }, false);

		} else {
			sdkEntry = JavaCore.newContainerEntry(containerPath);
		}

		BatchIdeUtils.assignClasspathEntryToJavaProject(getProject(), sdkEntry, true);

		// createMavenNature(monitor);
	}

	/**
	 * 메이븐 POM파일 생성
	 * 
	 * @param project
	 * @param monitor
	 * @throws CoreException
	 */
	private void createPomFile(IProject project, IProgressMonitor monitor) throws CoreException {
		if (context.isCreateExample())
			return;

		IFile file = project.getFile(new Path(ResourceConstants.POM_FILENAME));
		try {
			String document = stream2string(openPomContentStream(), context.getGroupId(), context.getArtifactId(),
					context.getVersion(), context.getPackageName());
			ByteArrayInputStream stream = new ByteArrayInputStream(document.getBytes());

			file.create(stream, true, monitor);
			openPomContentStream().close();
		} catch (IOException e) {
			BatchIdeLog.logError(e);
		}
	}

	/**
	 * POM 파일 스트림을 기본 데이터 적용하여 문자열로 변환
	 * 
	 * @param stream
	 * @param groupId
	 * @param artifactId
	 * @param version
	 * @param packageName
	 * @return
	 * @throws IOException
	 */
	private String stream2string(InputStream stream, String groupId, String artifactId, String version,
			String packageName) throws IOException {
		String lineSeparator = System.getProperty("line.separator");
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));
		StringBuffer sb = new StringBuffer();
		for (;;) {
			String line = br.readLine();
			if (line == null)
				break;
			line = line.replace("###GROUP_ID###", groupId);
			line = line.replace("###ARTIFACT_ID###", artifactId);
			line = line.replace("###VERSION###", version);
			line = line.replace("###PACKAGE_NAME###", packageName);
			line = line.replace("###NAME###", artifactId);
			line = line.replace("###URL###", "http://www.egovframe.go.kr"); //$NON-NLS-2$
			sb.append(line).append(lineSeparator);
		}
		br.close();
		return sb.toString();
	}

	/**
	 * POM 파일에서 스트림으로 로딩
	 * 
	 * @return
	 */
	private InputStream openPomContentStream() {
		return getClass().getClassLoader()
				.getResourceAsStream(ResourceConstants.POM_EXAMPLE_PATH + context.getPomFileName());
	}

	/**
	 * 프로젝트 가져오기
	 */
	protected IProject getProject() {
		return context.getProject();
	}

	/**
	 * 위치경로 가져오기
	 * 
	 * @return
	 */
	protected IPath getLocationPath() {
		return context.getLocationPath();
	}

	/**
	 * 프로젝트명 가져오기
	 * 
	 * @return
	 */
	protected String getProjectName() {
		return context.getProjectName();
	}

	/**
	 * 프로젝트 위치
	 * 
	 * @return
	 */
	protected String getProjectLocation() {
		return context.getLocationPath().toString();
	}

	/**
	 * 아티팩트 아이디 가져오기
	 * 
	 * @return
	 */
	protected String getArtifactId() {
		return context.getArtifactId();
	}

	/**
	 * build 폴더 삭제하기
	 */
	protected void buildDirDelete() throws CoreException {
		IProject project = getProject();

		IFolder folder = project.getFolder("build");

		folder.delete(true, null);
	}

	/**
	 * 실행
	 */
	public void run(IProgressMonitor pmonitor) throws InvocationTargetException, InterruptedException {

		IProgressMonitor nullMointor = new NullProgressMonitor();
		pmonitor.beginTask("Create Project", 9);

		try {

			if (this.context.getTemplateProjectDescription().equals(BatchIdeMessages.wizardPageBatchJobDBWebDescription)
					|| this.context.getTemplateProjectDescription()
							.equals(BatchIdeMessages.wizardPageBatchJobFileWebDescription)) {

				Thread.sleep(20);
				pmonitor.worked(2);

				pmonitor.subTask("create project");
				createProject(nullMointor);

				Thread.sleep(20);
				pmonitor.worked(2);

				pmonitor.subTask("create default resource");
				createDefaultResource(nullMointor);

				pmonitor.worked(2);
				pmonitor.subTask("create pom file");
				createPomFile(getProject(), nullMointor);// 2017-01-19 modify by jdh

				pmonitor.worked(1);
				createMavenNature(nullMointor);

				pmonitor.subTask("create pre javanature");
				preJavaNature(nullMointor);

				pmonitor.worked(1);

				pmonitor.subTask("create java nature");
				JavaCore.create(getProject());

				pmonitor.worked(1);

				postJavaNature(nullMointor);

				pmonitor.worked(1);

				pmonitor.subTask("create maven nature");
				updateMavenNature(nullMointor);
				// pmonitor.worked(1);

				pmonitor.subTask("create spring nature");
//				createSpringNature(nullMointor);
				pmonitor.worked(1);

				pmonitor.subTask("create egov nature");
				createEgovNature(nullMointor);
				pmonitor.worked(1);

				pmonitor.subTask("configure classpath");
				configureClasspath(nullMointor);
				pmonitor.worked(1);

				pmonitor.subTask("generate sample");
				if (getProject() != null)
					createExamples(pmonitor);
				else
					createExample();

				pmonitor.worked(1);

				BatchIdeUtils.sortClasspathEntry(getProject());

			} else {
				Thread.sleep(20);
				pmonitor.worked(2);

				pmonitor.subTask("create project");
				createProject(nullMointor);

				Thread.sleep(20);
				pmonitor.worked(2);

				pmonitor.subTask("create default resource");
				createDefaultResource(nullMointor);

				pmonitor.worked(2);

				pmonitor.subTask("create pom file");

				pmonitor.worked(1);

				createMavenNature(nullMointor);

				pmonitor.subTask("create pre javanature");
				preJavaNature(nullMointor);

				pmonitor.worked(1);

				pmonitor.subTask("create java nature");
				JavaCore.create(getProject());

				pmonitor.worked(1);

				postJavaNature(nullMointor);

				pmonitor.worked(1);

				pmonitor.subTask("configure classpath");
				configureClasspath(nullMointor);
				pmonitor.worked(1);

				pmonitor.subTask("create maven nature");
				updateMavenNature(nullMointor);
				pmonitor.worked(1);

				pmonitor.subTask("create spring nature");
//				createSpringNature(nullMointor);
				pmonitor.worked(1);

				pmonitor.subTask("create egov nature");
				createEgovNature(nullMointor);
				pmonitor.worked(1);

				pmonitor.subTask("generate sample");
				if (getProject() != null)
					createExamples(pmonitor);
				else
					createExample();

				pmonitor.worked(1);

				BatchIdeUtils.sortClasspathEntry(getProject());
			}

			try {
				// if (MavenPlugin.getDefault() != null) {
				if (MavenPlugin.getMaven() != null) {
					// MavenPlugin.getProjectConfigurationManager().updateProjectConfiguration(this.getProject(),
					// nullMointor);
					MavenPlugin.getProjectConfigurationManager().updateProjectConfiguration(this.getProject(),
							pmonitor);
				}

			} catch (Exception e) {
			}

			buildDirDelete();

		} catch (CoreException e) {
			BatchIdeLog.logError(e);
		} finally {
			pmonitor.done();
		}

	}

}
