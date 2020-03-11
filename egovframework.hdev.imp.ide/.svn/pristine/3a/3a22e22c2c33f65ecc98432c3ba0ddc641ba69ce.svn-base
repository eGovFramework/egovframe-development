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
package egovframework.hdev.imp.ide.wizards.operation;

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
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
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

import egovframework.hdev.imp.ide.EgovDeviceAPIIdePlugin;
import egovframework.hdev.imp.ide.common.DeviceAPIIdeLog;
import egovframework.hdev.imp.ide.common.DeviceAPIIdeUtils;
import egovframework.hdev.imp.ide.common.Policy;
import egovframework.hdev.imp.ide.common.ProjectFacetConstants;
import egovframework.hdev.imp.ide.common.ResourceConstants;
import egovframework.hdev.imp.ide.common.ResourceUtils;
import egovframework.hdev.imp.ide.model.DeviceAPIContext;

/**  
 * @Class Name : DeviceAPIProjectCreationOperation
 * @Description : DeviceAPIProjectCreationOperation Class
 * @Modification Information  
 * @
 * @  수정일			수정자		수정내용
 * @ ---------		---------	-------------------------------
 * @ 2012. 8. 22.		이율경		최초생성
 * 
 * @author 디바이스 API 개발환경 팀
 * @since 2012. 8. 22.
 * @version 1.0
 * @see
 * 
 */
public abstract class NewDeviceAPIWebProjectOperation implements 
		IRunnableWithProgress, EgovDeviceAPIWebProject {

    /** 메이븐 클래스 패스 엔트리 속성 값 */
    private static final String MAVEN_CLASSPATHENTRY_ATTRIBUTE_VALUE =
        "/WEB-INF/lib"; 
    /** 메이븐 클래스 패스 엔트리 속성 명 */
    private static final String MAVEN_CLASSPATHENTRY_ATTRIBUTE_NAME =
        "org.eclipse.jst.component.dependency"; 

    /** DeviceAPI 컨텍스트 */
    protected DeviceAPIContext context;

    /** pre 자바 네이처 */
    protected abstract void preJavaNature(IProgressMonitor monitor, IProject project)
            throws CoreException;

    /** post 자바 네이처 */
    protected abstract void postJavaNature(IProgressMonitor monitor)
            throws CoreException;

    /** default 리소스 생성 */
    protected abstract void createDefaultResource(IProgressMonitor monitor)
            throws CoreException;

    /** configure 클래스패스 */
    protected abstract void configureClasspath(IProgressMonitor monitor)
            throws CoreException;
    
    /** 실행 */
    public abstract void run(IProgressMonitor pmonitor) throws InvocationTargetException, InterruptedException;

    /**
     * 생성자
     * @param context
     */
    public NewDeviceAPIWebProjectOperation(DeviceAPIContext context) {
        this.context = context;
    }

    /**
     * 예제 템플릿 생성
     * @throws CoreException
     * @throws IOException 
     */
    @SuppressWarnings("unchecked")
	protected void createExample() throws CoreException {

    	BufferedInputStream is = null;
    	ZipFile zipFile = null;
    	FileOutputStream fos = null;
        try {
            Path path = new Path(EgovDeviceAPIIdePlugin.getDefault().getInstalledPath());
            String zipFileName = path.append("examples/web/").append(context.getWebExampleFile()).toOSString();
            zipFile = new ZipFile(zipFileName, "UTF-8");
            Enumeration<? extends ZipEntry> enumeration = zipFile.getEntries();
            IPath targetPath = getWebLocationPath();

            ZipEntry entry;
            BufferedOutputStream dest = null;
            int BUFFER = 2048;
            while (enumeration.hasMoreElements()) {
                entry = (ZipEntry) enumeration.nextElement();

                is = new BufferedInputStream(zipFile.getInputStream(entry));
                int count;
                byte data[] = new byte[BUFFER];

                if (entry.isDirectory())
                    continue;

                ResourceUtils.ensureFolderExists(getWebProject(), entry.getName());

                if (entry.getName().equals(ResourceConstants.WEB_POM_FILENAME)) {
                    updatePomFile(getWebProject(), is);
                } else {
                    fos = new FileOutputStream(targetPath.append(entry.getName())
                            .toOSString());

                    dest = new BufferedOutputStream(fos, BUFFER);
                    while ((count = is.read(data, 0, BUFFER)) != -1) {
                        dest.write(data, 0, count);
                    }
                    dest.flush();
                    dest.close();
                    fos.flush();
                    fos.close();
                }
                is.close();
            }

        } catch (Exception ex) {
        	
            DeviceAPIIdeLog.logError(ex);
        } finally {
        	
        	if(is != null) {
        		
        		try {
					is.close();
				} catch (IOException e) {
					
					DeviceAPIIdeLog.logError(e);
				}
        	}
        	
        	if(zipFile != null) {
        		try {
        			zipFile.close();
        		} catch (IOException e) {
					
					DeviceAPIIdeLog.logError(e);
				}
        	}
        	
        	if(fos != null) {
        		
        		try {
        			fos.close();
	        	} catch (IOException e) {
					
					DeviceAPIIdeLog.logError(e);
				}
        	}
        }
    }

    /**
     * POM 파일 변경
     * @param project
     * @param is
     * @throws CoreException
     */
    protected void updatePomFile(IProject project, BufferedInputStream is)
            throws CoreException {

        IFile file = project.getFile(new Path(ResourceConstants.WEB_POM_FILENAME));
        if (file.exists()) {
            file.delete(true, null);
        }
        try {
            String document =
                stream2string(is, context.getGroupId(),
                    context.getArtifactId(), context.getVersion(), context
                        .getWebPackageName());
            ByteArrayInputStream stream =
                new ByteArrayInputStream(document.getBytes());

            file.create(stream, true, null);
        } catch (IOException e) {
            DeviceAPIIdeLog.logError(e);
        }
    }

    /**
     * 컨텍스트 설정 후 파일 생성
     * @param project
     * @param is
     * @param fileName
     * @throws CoreException
     */
    protected void updateContextFile(IProject project, BufferedInputStream is,
            String fileName) throws CoreException {

        IFile file = project.getFile(new Path(fileName));
        if (file.exists()) {
            file.delete(true, null);
        }
        try {
            String document =
                stream2string(is, context.getGroupId(),
                    context.getArtifactId(), context.getVersion(), context
                        .getWebPackageName());
            ByteArrayInputStream stream =
                new ByteArrayInputStream(document.getBytes());

            file.create(stream, true, null);
        } catch (IOException e) {
            DeviceAPIIdeLog.logError(e);
        }
    }

    /**
     * 프로젝트 생성
     * @param monitor
     * @throws CoreException
     */
    protected void createProject(IProgressMonitor monitor) throws CoreException {
        IProject project = getWebProject();
        IProjectDescription desc = project.getWorkspace().newProjectDescription(project.getName());
        IPath locationPath = getWebLocationPath();
        if (Platform.getLocation().equals(locationPath))
            locationPath = null;
        desc.setLocation(locationPath);
        project.create(desc, Policy.subMonitorFor(monitor, 1));

        if (!project.isOpen())
            project.open(Policy.subMonitorFor(monitor, 1));
    }

    /**
     * 스프링 네이처 추가
     */
    protected void createSpringNature(IProgressMonitor monitor)
            throws CoreException {
        DeviceAPIIdeUtils.addNatureToProject(getWebProject(),
            "org.springframework.ide.eclipse.core.springnature", monitor);
    }

    /**
     * eGovFramework 네이처 추가
     * @param monitor
     * @throws CoreException
     */
    protected void createEgovNature(IProgressMonitor monitor, IProject project)
            throws CoreException {
    	DeviceAPIIdeUtils.addNatureToProject(project, EgovDeviceAPIIdePlugin.ID_NATURE,
            monitor);
    }
    
    /**
     * Maven 네이처 추가
     * @param monitor
     * @throws CoreException
     */
    protected void createMavenNature(IProgressMonitor monitor, IProject project) throws CoreException {
    	
    	ProvisioningUI provisioningUI = ProvisioningUI.getDefaultUI();
    	ProvisioningSession session = provisioningUI.getSession();
    	IProfileRegistry profileRegistry = (IProfileRegistry)session.getProvisioningAgent().getService(IProfileRegistry.SERVICE_NAME);
    	IProfile[] profiles = profileRegistry.getProfiles();
    	
    	for (int idx = 0; idx < profiles.length; idx++) {
            IQueryResult<IInstallableUnit> queryResult = profiles[idx].query(QueryUtil.createIUQuery(ProjectFacetConstants.MAVEN2_FEATURE_ID), null);
            if (!queryResult.isEmpty()){
            	DeviceAPIIdeUtils.addNatureToProject(project, ProjectFacetConstants.MAVEN2_NATURE_ID, monitor);
            } else {
            	IQueryResult<IInstallableUnit> queryResult2 = profiles[idx].query(QueryUtil.createIUQuery(ProjectFacetConstants.MAVEN3_FEATURE_ID), null);
            	if (!queryResult2.isEmpty()){
            		DeviceAPIIdeUtils.addNatureToProject(project, ProjectFacetConstants.MAVEN3_NATURE_ID, monitor);
            	}
            }
    	}
    }
    
    /**
     * Maven ContainerPath 설정
     * @throws CoreException
     */
    private IPath createMavenContainerPath() throws CoreException {
    	
    	ProvisioningUI provisioningUI = ProvisioningUI.getDefaultUI();
    	ProvisioningSession session = provisioningUI.getSession();
    	IProfileRegistry profileRegistry = (IProfileRegistry)session.getProvisioningAgent().getService(IProfileRegistry.SERVICE_NAME);
    	IProfile[] profiles = profileRegistry.getProfiles();
    	
    	for (int idx = 0; idx < profiles.length; idx++) {
    		IQueryResult<IInstallableUnit> queryResult = profiles[idx].query(QueryUtil.createIUQuery(ProjectFacetConstants.MAVEN2_FEATURE_ID), null);
    		if (!queryResult.isEmpty()){
    			return new Path(ProjectFacetConstants.MAVEN2_CLASSPATH_CONTAINER_ID);
    		} else {
    			IQueryResult<IInstallableUnit> queryResult2 = profiles[idx].query(QueryUtil.createIUQuery(ProjectFacetConstants.MAVEN3_FEATURE_ID), null);
    			if (!queryResult2.isEmpty()){
    				return new Path(ProjectFacetConstants.MAVEN3_CLASSPATH_CONTAINER_ID);
    			}
    		}
    	}
    	
    	return new Path(ProjectFacetConstants.MAVEN2_CLASSPATH_CONTAINER_ID);
    }

    /**
     * 메이븐 네이처 추가
     * @param monitor
     * @throws CoreException
     */
    protected void updateMavenNature(IProgressMonitor monitor)
            throws CoreException {

    	IPath containerPath = createMavenContainerPath();

        IClasspathEntry sdkEntry = null;

        if (this.context instanceof DeviceAPIContext) {
            IClasspathAttribute attribute =
                JavaCore.newClasspathAttribute(
                    MAVEN_CLASSPATHENTRY_ATTRIBUTE_NAME,
                    MAVEN_CLASSPATHENTRY_ATTRIBUTE_VALUE);
            sdkEntry =
                JavaCore.newContainerEntry(containerPath, new IAccessRule[0],
                    new IClasspathAttribute[] {attribute }, false);
        } else if (this.context instanceof DeviceAPIContext) {
            sdkEntry = JavaCore.newContainerEntry(containerPath);
        }

        DeviceAPIIdeUtils
            .assignClasspathEntryToJavaProject(getWebProject(), sdkEntry, true);
    }

    /**
     * 메이븐 POM파일 생성
     * @param project
     * @param monitor
     * @throws CoreException
     * @throws IOException 
     */
    protected void createPomFile(IProject project, IProgressMonitor monitor)
            throws CoreException {

    	InputStream inputStream = null;
    	ByteArrayInputStream stream = null;
        IFile file = project.getFile(new Path(ResourceConstants.WEB_POM_FILENAME));
        try {
        	inputStream = openPomContentStream();
            String document =
                stream2string(inputStream, context.getGroupId(),
                    context.getArtifactId(), context.getVersion(), context
                        .getWebPackageName());
            stream = new ByteArrayInputStream(document.getBytes());

            file.create(stream, true, monitor);
        } catch (IOException e) {
        	DeviceAPIIdeLog.logError(e);
        } finally {
        	if(inputStream != null) {
        		
        		try {
					inputStream.close();
        		} catch (IOException e) {
					
					DeviceAPIIdeLog.logError(e);
				}
        	}
        	if(stream != null) {
        		
        		try {
					stream.close();
        		} catch (IOException e) {
					
					DeviceAPIIdeLog.logError(e);
				}
        	}
        }
    }

    /**
     * POM 파일 스트림을 기본 데이터 적용하여 문자열로 변환
     * @param stream
     * @param groupId
     * @param artifactId
     * @param version
     * @param packageName
     * @return
     * @throws IOException
     */
    protected String stream2string(InputStream stream, String groupId,
            String artifactId, String version, String packageName)
            throws IOException {
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
            line = line.replace("###NAME###", artifactId); 
            line = line.replace("###URL###", "http://www.egovframe.go.kr");  //$NON-NLS-2$
            sb.append(line).append(lineSeparator);
        }
        br.close();
        return sb.toString();
    }

    /**
     * POM 파일에서 스트림으로 로딩
     * @return
     */
    protected InputStream openPomContentStream() {
        return getClass().getClassLoader().getResourceAsStream(
            ResourceConstants.WEB_POM_EXAMPLE_PATH + context.getWebPomFileName());
    }

    /**
     * 프로젝트 가져오기
     */
    protected IProject getWebProject() {
        return context.getWebProject();
    }

    /**
     * 위치경로 가져오기
     * @return
     */
    protected IPath getWebLocationPath() {
        return context.getWebProject().getLocation();
    }

    /**
     * 프로젝트명 가져오기
     * @return
     */
    protected String getWebProjectName() {
        return context.getWebProjectName();
    }

    /**
     * 프로젝트 위치
     * @return
     */
    protected String getWebProjectLocation() {
        return context.getWebLocationPath().toString();
    }

    /**
     * 아티팩트 아이디 가져오기
     * @return
     */
    protected String getArtifactId() {
        return context.getArtifactId();
    }

}
