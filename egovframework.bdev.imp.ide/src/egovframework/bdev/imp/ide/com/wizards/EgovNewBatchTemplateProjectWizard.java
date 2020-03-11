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
package egovframework.bdev.imp.ide.com.wizards;

import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.IWizardPage;

import egovframework.bdev.imp.ide.EgovBatchIdePlugin;
import egovframework.bdev.imp.ide.com.wizards.operation.BatchProjectCreationOperation;
import egovframework.bdev.imp.ide.com.wizards.pages.BatchTemplateProjectCreationPage;
import egovframework.bdev.imp.ide.com.wizards.pages.BatchTemplateProjectSelectCreateTypePage;
import egovframework.bdev.imp.ide.com.wizards.pages.BatchTemplateProjectSelectExecuteTypePage;
import egovframework.bdev.imp.ide.common.BatchIdeLog;
import egovframework.bdev.imp.ide.common.BatchIdeMessages;
import egovframework.bdev.imp.ide.scheduler.wizards.model.NewBatchWebProjectContext;

/**
 * 배치 템플릿 프로젝트 마법사 클래스
 * @author 조용현
 * @since 2012.07.24
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *수정일	  	수정자	  수정내용
 *-----------	------	----------------
 *2012.07.24	조용현	최초생성
 *
 * 
 * </pre>
 */
public class EgovNewBatchTemplateProjectWizard extends EgovBatchNewProjectWizard {
	
	private BatchProjectCreationOperation operation = null;
	
	/** Project 생성 Wizard Page */
	private IWizardPage projectCreationPage = null;

    /** EgovNewBatchTemplateProjectWizard 생성자 */
    public EgovNewBatchTemplateProjectWizard() {
        setContext(new NewBatchWebProjectContext());
        setWindowTitle(BatchIdeMessages.wizardsEgovNewTemplateBProjectWizardTITLE);
        setDefaultPageImageDescriptor(EgovBatchIdePlugin.getDefault().getImageDescriptor(EgovBatchIdePlugin.IMG_BATCH_TMP_PROJ_WIZ_BANNER)); // Template 배너로 교체해야함.    
    }
    
	public void setOperation(BatchProjectCreationOperation operation) {
		this.operation = operation;
	}

    @Override
    protected IRunnableWithProgress createOperation() {
    	return operation;
    }

    /** 마법사 페이지 추가 */
    public void addPages() {
        try {
        	getContext().setCreateExample(true);
        	
        	IWizardPage templateSelectCreateTypePage = new BatchTemplateProjectSelectCreateTypePage("FirstPage", getContext());
        	addPage(templateSelectCreateTypePage);
        	
        	IWizardPage templateSelectExecuteTypePage = new BatchTemplateProjectSelectExecuteTypePage("SecondPage", getContext());
        	addPage(templateSelectExecuteTypePage);
        	
            projectCreationPage = new BatchTemplateProjectCreationPage("ThirdPage", getContext()); 
            addPage(projectCreationPage);
            
        } catch (Exception e) {
            BatchIdeLog.logError(e);
        }
    }
    
    @Override
    public boolean canFinish() {
    	
    	if(super.canFinish() && getContainer().getCurrentPage().equals(projectCreationPage)){
    		return true;
    	}else{
    		return false;
    	}
    }

}
