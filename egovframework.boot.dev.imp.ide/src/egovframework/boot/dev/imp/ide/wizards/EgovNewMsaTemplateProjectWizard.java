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
package egovframework.boot.dev.imp.ide.wizards;

import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.IWizardPage;

import egovframework.boot.dev.imp.ide.EgovBootIdePlugin;
import egovframework.boot.dev.imp.ide.common.BootIdeLog;
import egovframework.boot.dev.imp.ide.common.BootIdeMessages;
import egovframework.boot.dev.imp.ide.wizards.model.NewProjectContext;
import egovframework.boot.dev.imp.ide.wizards.operation.BootWebProjectCreationOperation;
import egovframework.boot.dev.imp.ide.wizards.pages.TemplateProjectCreationPage;
import egovframework.boot.dev.imp.ide.wizards.pages.TemplateProjectSelectPage2;

/**
 * 템플릿 프로젝트 마법사 클래스
 * @author 개발환경 개발팀 이종대
 * @since 2011.06.10
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *	수정일		수정자		수정내용
 *  ----------	-------		---------------------------
 *	2011.06.10	이종대		최초 생성
 * 
 * 
 * </pre>
 */
public class EgovNewMsaTemplateProjectWizard extends EgovNewProjectWizard {

    /**
     * 생성자
     */
    public EgovNewMsaTemplateProjectWizard() {
        setContext(new NewProjectContext());
        setWindowTitle(BootIdeMessages.wizardsEgovNewMsaTemplateProjectWizardTITLE);
        setDefaultPageImageDescriptor(EgovBootIdePlugin.getDefault().getImageDescriptor(EgovBootIdePlugin.IMG_BOOT_TMP_MSA_WIZ_BANNER));
    }

    /**
     * 오퍼레이션 추가
     */
    protected IRunnableWithProgress createOperation() {
        return new BootWebProjectCreationOperation(getContext());
    }

    /**
     * 마법사 페이지 추가
     */
    public void addPages() {
        try {
        	
        	IWizardPage templateSelectPage = new TemplateProjectSelectPage2("basePage", getContext());
        	addPage(templateSelectPage);
        	
            IWizardPage firstPage = new TemplateProjectCreationPage("firstPage", getContext()); 
            addPage(firstPage);

//            super.addPages();

        } catch (Exception e) {
        	BootIdeLog.logError(e);
        }
    }

}
