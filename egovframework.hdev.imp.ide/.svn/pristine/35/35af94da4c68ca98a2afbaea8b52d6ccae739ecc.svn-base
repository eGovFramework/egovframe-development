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
package egovframework.hdev.imp.ide.pages;

import org.eclipse.swt.graphics.Point;

import egovframework.hdev.imp.ide.model.DeviceAPIContext;
import egovframework.hdev.imp.ide.wizards.examples.DeviceAPITemplateInfo;

/**
 * eGovFramework 프로젝트 생성 마법사 페이지 클래스
 * @author 배치개발환경 개발팀
 * @since 2012.07.02
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *	수정일      	수정자           수정내용
 *  -------			--------    ---------------------------
 *
 * 
 * </pre>
 */
public class DeviceAPIProjectNewCreationPage extends DeviceAPIProjectCreationPage {
	/**
     * 생성자
     * @param pageName
     * @param context
     */
    public DeviceAPIProjectNewCreationPage(String pageName, DeviceAPIContext context) {
        super(pageName, context);
    }
    
    /**
     * 컨텍스트 정보 변경
     */
    protected void updateContext() {
        
    	 context.setDeviceapiProject(getDeviceapiProjectHandle());
         context.setDeviceapiProjectName(getDeviceapiProjectName());
         context.setGroupId(getGroupId());
         context.setArtifactId(getArtifactId());
         context.setVersion(getVersion());
         context.setDeviceapiLocationPath(getDeviceapiLocationPath());
         
         context.setDeviceapiExampleFile(DeviceAPITemplateInfo.deviceapiExampleFile);
         context.setDeviceapiPomFileName(DeviceAPITemplateInfo.webPomFile);
    }
    
    /**
     * 검증 페이지
     */
    @Override
    protected boolean validatePage() {
    	
    	if(!super.validatePage()) {
    		
    		return false;
    	}
    	
    	updateContext();
    	
    	return true;
    }
    
    /**
     * @param DeviceAPIProjectNewCreationPage
     * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean visible) {
    	if(visible){
    		getShell().setSize(new Point(533, 533));
    	}
    	super.setVisible(visible);
    }
}
