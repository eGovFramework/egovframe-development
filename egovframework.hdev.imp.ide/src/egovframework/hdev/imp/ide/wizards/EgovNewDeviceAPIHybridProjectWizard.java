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
package egovframework.hdev.imp.ide.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyDelegatingOperation;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

import egovframework.hdev.imp.ide.EgovDeviceAPIIdePlugin;
import egovframework.hdev.imp.ide.common.DeviceAPIIdeUtils;
import egovframework.hdev.imp.ide.common.DeviceAPIMessages;
import egovframework.hdev.imp.ide.model.DeviceAPIContext;
import egovframework.hdev.imp.ide.pages.CustomizeTableCreationPage;
import egovframework.hdev.imp.ide.pages.DeviceAPIProjectNewCreationPage;
import egovframework.hdev.imp.ide.pages.DeviceAPIWebProjectCreationPage;
import egovframework.hdev.imp.ide.pages.GenerateTemplatePage;
import egovframework.hdev.imp.ide.wizards.operation.NewDeviceAPITemplateGenerateOperation;

/**  
 * @Class Name : EgovNewDeviceAPIHybridProjectWizard
 * @Description : EgovNewDeviceAPIHybridProjectWizard Class
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
public class EgovNewDeviceAPIHybridProjectWizard extends Wizard implements INewWizard, IExecutableExtension {
	
	/** 프로젝트 기본 정보 입력 Page */
	DeviceAPIProjectNewCreationPage createPage = null;
	
	/** DB connection Test 및 Table create Page */
	CustomizeTableCreationPage customizeTablePage = null;
	
	/** Web 프로젝트 생성 페이지 */
	DeviceAPIWebProjectCreationPage createWebPJT = null;
	
	/** 템플릿 선택 페이지 */
	GenerateTemplatePage generateTemplatePage = null;
	
	/** EgovAddDeviceAPIHybridProjectWizard의 context */
	DeviceAPIContext context = null;
	
	/**
	 * 생성자
	 */
	public EgovNewDeviceAPIHybridProjectWizard() {
		
		
		
		setContext(new DeviceAPIContext());
		setWindowTitle(DeviceAPIMessages.WIZARD_NEW_PROJECT_TITLE);
		setDefaultPageImageDescriptor(EgovDeviceAPIIdePlugin.getDefault().getImageDescriptor(EgovDeviceAPIIdePlugin.IMG_CORE_PROJ_WIZ_BANNER));
	}
	
	protected IConfigurationElement fConfigElement;
	
	public DeviceAPIContext getContext() {
		return context;
	}

	public void setContext(DeviceAPIContext context) {
		this.context = context;
	}

	@Override
	public void init(IWorkbench arg0, IStructuredSelection arg1) {
		setNeedsProgressMonitor(true);
	}

	@Override
	public void setInitializationData(IConfigurationElement arg0, String arg1,
			Object arg2) throws CoreException {
		
	}
	
	
	
	/**
     * 오퍼레이션 추가
     */
    protected IRunnableWithProgress createOperation() {
    	
    	return new NewDeviceAPITemplateGenerateOperation(getContext());
    }
    
    @Override
	public void addPages() {    	
		createPage = new DeviceAPIProjectNewCreationPage("CreatePage", context);
		addPage(createPage);
		
//		Template generate Page
		generateTemplatePage = new GenerateTemplatePage("GenerateTemplatePage", context);
		addPage(generateTemplatePage);
		
		createWebPJT = new DeviceAPIWebProjectCreationPage("test", context);
		addPage(createWebPJT);
		
		customizeTablePage = new CustomizeTableCreationPage("CustomizePage", context);
		addPage(customizeTablePage);
	}
    
    @Override
    public IWizardPage getStartingPage() {
    	if(DeviceAPIIdeUtils.isAndroidDevelopmentTool() == false) {
			
			MessageDialog.openInformation(EgovDeviceAPIIdePlugin
					.getActiveWorkbenchWindow().getShell(), "Inform", "Selected function has not been installed. ADT is required.");
			return null;
			
		}
		
		if(DeviceAPIIdeUtils.isMaven3Version() == false) {
			
			MessageDialog.openInformation(EgovDeviceAPIIdePlugin
					.getActiveWorkbenchWindow().getShell(), "Inform", "Selected function has not been installed. Maven3(M2E) is required.");
			return null;
			
		}
		
		if(DeviceAPIIdeUtils.isM2EToAndroid() == false) {
			
			MessageDialog.openInformation(EgovDeviceAPIIdePlugin
					.getActiveWorkbenchWindow().getShell(), "Inform", "Selected function has not been installed. M2E Configure for Android is required.");
			return null;
			
		}else{
    		return super.getStartingPage();
    		
    	}
    }

    /**
     * 종료시 실행
     */
	@Override
	public boolean performFinish() {
		
		boolean result = true;
        IRunnableWithProgress runnable = createOperation();
        IRunnableWithProgress op = new WorkspaceModifyDelegatingOperation(runnable);
		
		try {
			
			getContainer().run(false, true, op);
            BasicNewProjectResourceWizard.updatePerspective(fConfigElement);
            BasicNewProjectResourceWizard.selectAndReveal(context.getDeviceapiProject(), EgovDeviceAPIIdePlugin.getActiveWorkbenchWindow());
			
		} catch (InvocationTargetException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
	        return result;
	}
	
	@Override
	public boolean canFinish() {
		IWizardPage currentPage = getContainer().getCurrentPage();
		
		if(currentPage.equals(createPage)){
			return false;
		}
		
		if(currentPage.equals(generateTemplatePage)){
			if(generateTemplatePage.isEnableFinishButton()){
				return true;
			}else{
				return false;
			}
		}
		
		if(currentPage.equals(createWebPJT)){
			if(createWebPJT.isEnableFinishButton()){
				return true;
			}else{
				return false;
			}
		}
		
		return super.canFinish();
	}
}