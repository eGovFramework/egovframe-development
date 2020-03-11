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
package egovframework.mdev.imp.ide.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyDelegatingOperation;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

import egovframework.mdev.imp.ide.EgovMobileIdePlugin;
import egovframework.mdev.imp.ide.common.MoblieIdeLog;
import egovframework.mdev.imp.ide.wizards.model.NewMobileProjectContext;
import egovframework.mdev.imp.ide.wizards.pages.SelectMobileExamplePage;

/**
 * Moblie eGovFramework 프로젝트 마법사 추상 클래스
 * @author 이종대
 * @since 2011.07.13
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *	수정일      	수정자           수정내용
 *  -------			--------    ---------------------------
 *	2011.07.13  	이종대          최초 생성
 *
 * 
 * </pre>
 */
public abstract class EgovMobileNewProjectWizard extends Wizard implements INewWizard, IExecutableExtension {

    /** Context */
    private NewMobileProjectContext context;
    /** 워크벤치 */
    protected IWorkbench workbench;
    protected IStructuredSelection selection;
    protected IConfigurationElement fConfigElement;

    /**
     * 생성자
     */
    public EgovMobileNewProjectWizard() {
        setNeedsProgressMonitor(true);
    }

    /**
     * 오퍼레이션 추가 추상 메소드
     * @return
     */
    protected abstract IRunnableWithProgress createOperation();

    /**
     * 컨택스트 설정
     * @param context
     */
    protected void setContext(NewMobileProjectContext context) {
        this.context = context;
    }

    /**
     * 컨텍스트 가져오기
     * @return
     */
    protected NewMobileProjectContext getContext() {
        return this.context;
    }

    /**
     * 초기화
     */
    public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
        this.workbench = workbench;
        this.selection = currentSelection;
    }

    /**
     * 초기데이터 설정
     */
    public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException {
        fConfigElement = config;
    }

    /**
     * 마법사 페이지 추가
     */
    @Override
    public void addPages() {
        IWizardPage secondPage = new SelectMobileExamplePage("secondPage", context); 
        addPage(secondPage);
    }

    /**
     * 종료시 실행
     */
    public boolean performFinish() {
        boolean result = true;
        IRunnableWithProgress runnable = createOperation();
        IRunnableWithProgress op = new WorkspaceModifyDelegatingOperation(runnable);
        try {
            getContainer().run(false, true, op);
            BasicNewProjectResourceWizard.updatePerspective(fConfigElement);
            // BasicNewResourceWizard.selectAndReveal(context.getProject(),
            // EgovImpToolPlugin.getActiveWorkbenchWindow());
            BasicNewProjectResourceWizard.selectAndReveal(context.getProject(), EgovMobileIdePlugin.getActiveWorkbenchWindow());
        } catch (InvocationTargetException e) {
            MoblieIdeLog.logError(e);
            result = false;
        } catch (InterruptedException e) {
            MoblieIdeLog.logError(e);
            result = false;
        }
        return result;
    }
}
