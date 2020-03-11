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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.m2e.core.MavenPlugin;

import egovframework.hdev.imp.ide.common.DeviceAPIIdeLog;
import egovframework.hdev.imp.ide.common.DeviceAPIIdeUtils;
import egovframework.hdev.imp.ide.model.DeviceAPIContext;

/**  
 * @Class Name : NewDeviceAPITemplateGenerateOperation
 * @Description : NewDeviceAPITemplateGenerateOperation Class
 * @Modification Information  
 * @
 * @  수정일			수정자		수정내용
 * @ ---------		---------	-------------------------------
 * @ 2012. 8. 24.		이율경		최초생성
 * 
 * @author 디바이스 API 개발환경 팀
 * @since 2012. 8. 24.
 * @version 1.0
 * @see
 * 
 */
public class NewDeviceAPITemplateGenerateOperation extends NewDeviceAPIHybridProjectCreationOperation {
	
	/*
	 * 생성자
	 */
	public NewDeviceAPITemplateGenerateOperation(DeviceAPIContext context) {
		super(context);
	}
	
	/**
	 * @see egovframework.hdev.imp.ide.wizards.operation.NewDeviceAPIHybridProjectCreationOperation#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void run(IProgressMonitor pmonitor) throws InvocationTargetException, InterruptedException {
		
		IProgressMonitor nullMointor = new NullProgressMonitor();
        pmonitor.beginTask("Create Project", 40); 

        try {
        	
        	Thread.sleep(20);
        	pmonitor.worked(2);
        	
        	pmonitor.subTask("create DeviceAPI project"); 
        	createDeviceapiProject(nullMointor);
        	pmonitor.worked(2);
        	Thread.sleep(20);
            
            pmonitor.subTask("create default resource"); 
            createAndroidDefaultResource(nullMointor);
            pmonitor.worked(2);
            
            pmonitor.subTask("create pom file"); 
            createDeviceapiPomFile(getDeviceapiProject(), nullMointor);
            pmonitor.worked(1);
            
            pmonitor.worked(1);
        	createMavenNature(nullMointor, getDeviceapiProject());
            
            pmonitor.subTask("create pre javanature"); 
            preAndroidJavaNature(nullMointor, getDeviceapiProject());
            pmonitor.worked(1);

            pmonitor.subTask("create java nature"); 
            JavaCore.create(getDeviceapiProject());
            pmonitor.worked(1);
            
            pmonitor.subTask("configure classpath"); 
            configureAndroidClasspath(nullMointor);
            pmonitor.worked(1);
            
            pmonitor.subTask("create android nature"); 
            updateAndroidNature(nullMointor);
            pmonitor.worked(1);
            
            pmonitor.subTask("create maven nature"); 
            updateMavenNature(nullMointor);
            pmonitor.worked(1);

            pmonitor.subTask("create egov nature"); 
            createEgovNature(nullMointor, getDeviceapiProject());
            pmonitor.worked(1);
            
            pmonitor.subTask("create DeviceAPI Example"); 
			createDeviceAPIExample();
			pmonitor.worked(1);
			
			pmonitor.subTask("refresh Project");
			getDeviceapiProject().refreshLocal(IResource.DEPTH_INFINITE, nullMointor);
			pmonitor.worked(1);
			Thread.sleep(1000);
			
			if(context.getIsWebContextUse()) {
				
				pmonitor.subTask("update Server Info"); 
				updateDeviceapiServerInfoFile(getDeviceapiProject(), nullMointor);
				pmonitor.worked(1);
			}
			
			removeJavaClasspath(nullMointor);
            
            DeviceAPIIdeUtils.sortClasspathEntry(getDeviceapiProject());
        	
    		if(context.getIsTemplate()) {
    			
	            pmonitor.subTask("create Web project"); 
	            createProject(nullMointor);
	
	            Thread.sleep(20);
	            pmonitor.worked(2);
	
	            pmonitor.subTask("create default resource"); 
	            createDefaultResource(nullMointor);
	            pmonitor.worked(2);
	
	            pmonitor.subTask("create pom file"); 
	            createPomFile(getWebProject(), nullMointor);
	            pmonitor.worked(1);
	            
	            pmonitor.worked(1);
	        	createMavenNature(nullMointor, getWebProject());
	
	            pmonitor.subTask("create pre javanature"); 
	            preJavaNature(nullMointor, getWebProject());
	            pmonitor.worked(1);
	
	            pmonitor.subTask("create java nature"); 
	            JavaCore.create(getWebProject());
	            pmonitor.worked(1);
	            
	            postJavaNature(nullMointor);
	            pmonitor.worked(1);
	
	            pmonitor.subTask("create spring nature"); 
	            createSpringNature(nullMointor);
	            pmonitor.worked(1);
	
	            pmonitor.subTask("create maven nature"); 
	            updateMavenNature(nullMointor);
	            pmonitor.worked(1);
	
	            pmonitor.subTask("create egov nature"); 
	            createEgovNature(nullMointor, getWebProject());
	            pmonitor.worked(1);
	            
	            pmonitor.subTask("configure classpath"); 
	            configureClasspath(nullMointor);
	            pmonitor.worked(1);
	            
	            pmonitor.subTask("generate sample"); 
	            createExample();
	            pmonitor.worked(1);
	            
	            DeviceAPIIdeUtils.sortClasspathEntry(getWebProject());
    		}
    		
    		try{
	        	if(MavenPlugin.getDefault() != null ) {
	              MavenPlugin.getProjectConfigurationManager().updateProjectConfiguration(this.getDeviceapiProject(), nullMointor);
	        	}
	        	

        	}catch(Exception e) {
        		
        	}


        } catch (CoreException e) {
            DeviceAPIIdeLog.logError(e);
        } catch (IOException e ) {
        	DeviceAPIIdeLog.logError(e);
        } finally {
            pmonitor.done();
        }
	}
}
