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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jst.common.project.facet.core.JavaFacetInstallConfig;
import org.eclipse.wst.common.project.facet.core.IActionDefinition;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IFacetedProject.Action;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;

import egovframework.hdev.imp.ide.EgovDeviceAPIIdePlugin;
import egovframework.hdev.imp.ide.common.DeviceAPIIdeLog;
import egovframework.hdev.imp.ide.common.DeviceAPIIdeUtils;
import egovframework.hdev.imp.ide.common.Policy;
import egovframework.hdev.imp.ide.common.ProjectFacetConstants;
import egovframework.hdev.imp.ide.common.ResourceConstants;
import egovframework.hdev.imp.ide.common.ResourceUtils;
import egovframework.hdev.imp.ide.model.DeviceAPIContext;

/**  
 * @Class Name : DeviceAPIWebProjectCreationOperation
 * @Description : DeviceAPIWebProjectCreationOperation Class
 * @Modification Information  
 * @
 * @    수정일			 수정자		수정내용
 * @ -------------		---------	-------------------------------
 * @ 2012. 08. 22.		 이율경		최초생성
 * @ 2013. 10. 11.		 이기하		classpathentry 설정변경(exported true)
 * 
 * @author 디바이스 API 개발환경 팀
 * @since 2012. 8. 22.
 * @version 1.0
 * @see
 * 
 */
public abstract class NewDeviceAPIHybridProjectCreationOperation extends NewDeviceAPIWebProjectCreationOperation {

	/** 실행 */
	public abstract void run(IProgressMonitor pmonitor) throws InvocationTargetException, InterruptedException;
	
    /**
     * 생성자
     * @param context
     */
    public NewDeviceAPIHybridProjectCreationOperation(DeviceAPIContext context) {
        super(context);
    }
    
    /**
     * 프로젝트 생성
     * @param monitor
     * @throws CoreException
     */
    protected void createDeviceapiProject(IProgressMonitor monitor) throws CoreException {
        IProject project = getDeviceapiProject();
        IProjectDescription desc = project.getWorkspace().newProjectDescription(project.getName());
        IPath locationPath = getDeviceapiLocationPath();
        if (Platform.getLocation().equals(locationPath))
            locationPath = null;
        desc.setLocation(locationPath);
        project.create(desc, Policy.subMonitorFor(monitor, 1));

        if (!project.isOpen())
            project.open(Policy.subMonitorFor(monitor, 1));
    }
	
	/**
     * Android 네이처 추가 이전 작업
     */
    protected void updateAndroidNature(IProgressMonitor monitor) throws CoreException {
		
    	DeviceAPIIdeUtils.addNatureToProject(getDeviceapiProject(), ProjectFacetConstants.ANDROID_NATURE_ID,
                monitor);
	}
    
    /**
     * 기본 리소스 생성
     */
    protected void createAndroidDefaultResource(IProgressMonitor monitor)
            throws CoreException {

        for (int i = 0; i < ResourceConstants.ANDROID_SYSTEM_FOLDERS.length; i++) {
            org.eclipse.core.resources.IFolder folder =
                getDeviceapiProject().getFolder(ResourceConstants.ANDROID_SYSTEM_FOLDERS[i]);
            ResourceUtils.ensureFolderExists(folder, Policy.subMonitorFor(
                monitor, 1));
        }
    }
    
    /**
     * 자바 네이처 추가 이전 작업
     */
    protected void preAndroidJavaNature(IProgressMonitor monitor, IProject project) throws CoreException {
    	
    	addAndroidProjectFacets(monitor, project);
    }
    
    /**
     * 프로젝트 패싯 추가
     * @param monitor
     * @throws CoreException
     */
    @SuppressWarnings( {"deprecation" })
    protected void addAndroidProjectFacets(IProgressMonitor monitor, IProject project)
            throws CoreException {
        
        IFacetedProject facetedProject = ProjectFacetsManager.create(project.getProject(), true, null);
        IProjectFacet javaFacet = ProjectFacetsManager.getProjectFacet(ProjectFacetConstants.JAVA_FACET_ID);
        IProjectFacetVersion javaFacetVersion = javaFacet.getVersion(ProjectFacetConstants.ANDROID_JAVA_VERSION);
        Set<Action> facetActions = new HashSet<Action>(2);
        for (IActionDefinition def : javaFacetVersion.getActionDefinitions(IFacetedProject.Action.Type.INSTALL)) {
            Object object = def.createConfigObject(javaFacetVersion, project.getProject().getName());
            JavaFacetInstallConfig config = (JavaFacetInstallConfig) object;
            config.setSourceFolder(ResourceConstants.ANDROID_SOURCE_FOLDER);
            config.setDefaultOutputFolder(ResourceConstants.ANDROID_DEFAULT_OUTPUT_FOLDER);

            facetActions.add(new Action(Action.Type.INSTALL, javaFacetVersion, config));
        }

        facetedProject.modify(facetActions, Policy.subMonitorFor(monitor, 1));
    }
    
    /**
     * 메이븐 POM파일 생성
     * @param project
     * @param monitor
     * @throws CoreException
     * @throws IOException 
     */
    protected void createDeviceapiPomFile(IProject project, IProgressMonitor monitor)
            throws CoreException {
    	
    	InputStream inputStream = null;
    	ByteArrayInputStream stream = null;
        IFile file = project.getFile(new Path(ResourceConstants.ANDROID_POM_FILENAME));
        try {
        	inputStream = openAndroidPomContentStream();
            String document =
                stream2string(inputStream, context.getGroupId(),
                    context.getArtifactId(), context.getVersion(), context
                        .getDeviceapiPackageName());
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
     * SERVER INFO 파일 생성
     * @param project
     * @param monitor
     * @throws CoreException
     * @throws IOException 
     */
    protected void updateDeviceapiServerInfoFile(IProject project, IProgressMonitor monitor)
            throws CoreException, IOException {

    	InputStream inputStream = null;
    	ByteArrayInputStream stream = null;
        IFile file = project.getFile(ResourceConstants.DEVICEAPI_SERVER_INFO_PATH.append(ResourceConstants.DEVICEAPI_SERVER_INFO_FILENAME));
        if (file.exists()) {
            file.delete(true, null);
        }
        
        try {
        	inputStream = openDeviceapiServerInfoContentStream();
            String document =
            	stream2ServerInfoString(inputStream, context.getServerURL());
            stream =
                new ByteArrayInputStream(document.getBytes());

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
     * POM 파일에서 스트림으로 로딩
     * @return
     */
    protected InputStream openAndroidPomContentStream() {
        return getClass().getClassLoader().getResourceAsStream(
            ResourceConstants.ANDROID_POM_EXAMPLE_PATH + context.getDeviceapiPomFileName());
    }
    
    /**
     * SERVER INFO 파일에서 스트림으로 로딩
     * @return
     */
    protected InputStream openDeviceapiServerInfoContentStream() {
        return getClass().getClassLoader().getResourceAsStream(
            ResourceConstants.DEVICEAPI_SERVER_INFO_EXAMPLE_PATH + ResourceConstants.DEVICEAPI_SERVER_INFO_FILENAME);
    }
    
    /**
     * SERVER INFO 파일 스트림을 기본 데이터 적용하여 문자열로 변환
     * @param stream
     * @param serverURL
     * @return
     * @throws IOException
     */
    protected String stream2ServerInfoString(InputStream stream, String serverURL)
            throws IOException {
        String lineSeparator = System.getProperty("line.separator"); 
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        StringBuffer sb = new StringBuffer();
        for (;;) {
            String line = br.readLine();
            if (line == null)
                break;
            line = line.replace("##SERVER_URL##", serverURL); 
            sb.append(line).append(lineSeparator);
        }
        br.close();
        return sb.toString();
    }
    
    /**
     * 클래스 패스 설정
     */
    protected void configureAndroidClasspath(IProgressMonitor monitor) throws CoreException {
    	
        List<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();
        
        entries.add(JavaCore.newContainerEntry(ResourceConstants.ANDROID_FRAMEWORK, true));
        entries.add(JavaCore.newContainerEntry(ResourceConstants.ANDROID_LIBRARIES, true));
        
        IClasspathEntry[] classpathEntrys = (IClasspathEntry[])entries.toArray(new IClasspathEntry[entries.size()]);
        
        DeviceAPIIdeUtils.assignClasspathEntryToJavaProject(getDeviceapiProject(), classpathEntrys, true);
        
    }
    
    /**
     * 자바 1.6 클래스 패스삭제
     */
    protected void removeJavaClasspath(IProgressMonitor monitor)
            throws CoreException {
    	
        IClasspathEntry classpathEntry = JavaCore.newContainerEntry(ResourceConstants.JAVA_CLASS_PATH_ENTRY);
        
        
        DeviceAPIIdeUtils.removeClasspathEntry(getDeviceapiProject(), classpathEntry);
    }
    
    /**
     * 프로젝트 내, 파일 삭제
     */
    protected void deleteFileExists(IProject project, final IProgressMonitor monitor) throws CoreException {
    	
    	project.accept(new IResourceVisitor(){
    	                                public boolean visit(IResource resource)   throws CoreException {
    	                                    
    	                                	if(resource.getType() == IResource.FOLDER) {
    	                                		
    	                                		resource.delete(true, monitor);
    	                                	}
    	                                	
    	                                    return true;
    	                                }
    	                     });
    }
    
    
    /**
     * DeviceAPI 예제 템플릿 생성
     * @throws IOException 
     * @throws CoreException
     */
    @SuppressWarnings("unchecked")
	protected void createDeviceAPIExample() {

    	BufferedInputStream is = null;
    	ZipFile zipFile = null;
        try {
            Path path = new Path(EgovDeviceAPIIdePlugin.getDefault().getInstalledPath());
            String zipFileName = path.append("examples/hyb/").append(context.getDeviceapiExampleFile()).toOSString();
            zipFile = new ZipFile(zipFileName, "UTF-8");
            Enumeration<? extends ZipEntry> enumeration = zipFile.getEntries();
            IPath targetPath = getDeviceapiLocationPath().append(getDeviceapiProjectName());

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

                ResourceUtils.ensureFolderExists(getDeviceapiProject(), entry.getName());

                FileOutputStream fos =
                    new FileOutputStream(targetPath.append(entry.getName())
                        .toOSString());

                dest = new BufferedOutputStream(fos, BUFFER);
                while ((count = is.read(data, 0, BUFFER)) != -1) {
                    dest.write(data, 0, count);
                }
                dest.flush();
                dest.close();
                
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
        }
    }
    

    /**
     * Device API 프로젝트 가져오기
     */
    protected IProject getDeviceapiProject() {
        return context.getDeviceapiProject();
    }

    /**
     * Device API 위치경로 가져오기
     * @return
     */
    protected IPath getDeviceapiLocationPath() {
        return context.getDeviceapiLocationPath();
    }

    /**
     * Device API 프로젝트명 가져오기
     * @return
     */
    protected String getDeviceapiProjectName() {
        return context.getDeviceapiProjectName();
    }

    /**
     * Device API 프로젝트 위치
     * @return
     */
    protected String getDeviceapiProjectLocation() {
        return context.getDeviceapiLocationPath().toString();
    }
}
