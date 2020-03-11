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
package egovframework.dev.imp.ide.wizards;

import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.IWizardPage;

import egovframework.dev.imp.ide.EgovIdePlugin;
import egovframework.dev.imp.ide.common.IdeLog;
import egovframework.dev.imp.ide.common.IdeMessages;
import egovframework.dev.imp.ide.wizards.model.NewWebProjectContext;
import egovframework.dev.imp.ide.wizards.operation.WebProjectCreationOperation;
import egovframework.dev.imp.ide.wizards.pages.WebProjectCreationPage;

/**
 * 웹 프프로젝트 마법사 클래스
 * @author 개발환경 개발팀 이흥주
 * @since 2009.06.01
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.02.20  이흥주          최초 생성
 * 
 * 
 * </pre>
 */
public class EgovNewWebProjectWizard extends EgovNewProjectWizard {

    /**
     * 생성자
     */
    public EgovNewWebProjectWizard() {
        setContext(new NewWebProjectContext());
        setWindowTitle(IdeMessages.wizardsEgovNewWebProjectWizardTITLE);
        setDefaultPageImageDescriptor(EgovIdePlugin.getDefault()
            .getImageDescriptor(EgovIdePlugin.IMG_WEB_PROJ_WIZ_BANNER));
    }

    /**
     * 오퍼레이션 추가
     */
    protected IRunnableWithProgress createOperation() {
        return new WebProjectCreationOperation(getContext());
    }

    /**
     * 마법사 페이지 추가
     */
    public void addPages() {
        try {

            IWizardPage firstPage =
                new WebProjectCreationPage("firstPage", getContext()); 
            addPage(firstPage);

            super.addPages();

        } catch (Exception e) {
            IdeLog.logError(e);
        }
    }

}
