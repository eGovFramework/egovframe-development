/*
 * Copyright 2008-2009 MOSPA(Ministry of Security and Public Administration).
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
package egovframework.dev.imp.codegen.template.wizards;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import net.sf.abstractplugin.core.EclipseProjectUtils;
import net.sf.abstractplugin.util.ThreadUtils;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.eclipsework.core.EclipseWorkFactoryManager;
import org.eclipse.eclipsework.core.interfaces.IEWUtils;
import org.eclipse.eclipsework.core.jdom.element.wizard.WizardModelElement;
import org.eclipse.eclipsework.core.wizard.IEWWizard;
import org.eclipse.eclipsework.core.wizard.IEWWizardPage;
import org.eclipse.eclipsework.core.wizard.WizardHelper;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;

import egovframework.dev.imp.codegen.template.CodeGenLog;
import egovframework.dev.imp.codegen.template.util.LogUtil;
import egovframework.dev.imp.codegen.template.util.TemplateUtil;

/**
 * 
 * 코드 생성 마법사 클래스
 * <p><b>NOTE:</b> 코드 생성을 위한 데이터를 사용자로부터 입력 받기 위한 마법사 클래스
 * 
 * @author 개발환경 개발팀 이흥주
 * @since 2009.08.03
 * @version 1.0
 * @see
 *
 * <pre>
 *  == 개정이력(Modification Information) ==
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.08.03  이흥주          최초 생성
 *
 * </pre>
 */
@SuppressWarnings("restriction")
public class CodeGenWizard extends Wizard implements IEWWizard
                                                       , egovframework.dev.imp.codegen.template.wizards.Wizard
                                                       , BusinessLayerSkeletonGeneration
                                                       , PresentationLayerSkeletonGeneration{

    /** 로거 */
    protected final Logger log = Logger.getLogger(CodeGenWizard.class);

    /**
     * 
     * 생성자
     *
     */
    public CodeGenWizard(){
        setDialogSettings(JavaPlugin.getDefault().getDialogSettings());
    }

    /**
     * 
     * 페이지 추가
     *
     * @param iewwizardpage
     */
    public void addPage(IEWWizardPage iewwizardpage) {
        addPage((IWizardPage) iewwizardpage);
    }

    /**
     * 
     * 페이지 추가
     *
     */
    public void addPages() {
        if (WizardHelper.invalidXMLWizard) {
            return;
        }

        super.addPages();

        WizardModelElement xmlModelPage = WizardHelper.wizards.getWizardModelPage();
        if (xmlModelPage != null) {
            addWizardModelPage(xmlModelPage, WizardHelper.mode);
        }

        WizardHelper.addComponentPages();

    }

    /**
     * 
     * 마법사 페이지 가져오기
     *
     * @return
     */
    public IEWWizardPage[] getWizardPages() {
        IWizardPage[] eclipsePages = getPages();
        IEWWizardPage[] pages = new IEWWizardPage[getPageCount()];
        for (int i = 0; i < pages.length; i++) {
            pages[i] = (IEWWizardPage) eclipsePages[i];
        }
        return pages;
    }

    /**
     * 
     * VelocityContext 값 넣기
     *
     * @return
     */
    @SuppressWarnings("unchecked")
	public Map<String, TemplateUtil> putValuesToVelocityContext() {
        Map<String, TemplateUtil> map = WizardHelper.putVariablesToVelocityContext(); 
        IProject project = EclipseProjectUtils.getSelectedProject();
        TemplateUtil templateUtil = new TemplateUtil(project);
        map.put("templateUtil", templateUtil);

        return map; 
    }


    /**
     * 
     * 시작
     *
     */
    public void start() {
        WizardDialog dialog = new WizardDialog(getShell(), this);
        dialog.create();
        dialog.open();
    }

    /**
     * 
     * 페이지 검증
     *
     * @return
     */
    public boolean validatePages() {
        return WizardHelper.validatePages();
    }

    /**
     * 
     * 다음 페이지 가져오기
     *
     * @param page
     * @return
     */
    public IWizardPage getNextPage(IWizardPage page) {
        return super.getNextPage(page);
    }    

    /**
     * 
     * 마법사 종료
     *
     * @return
     */
    @Override
    public boolean performFinish() {
        try {
        	LogUtil.consoleClear();
    		log.info("====================================================================================");
            log.info("eGovFrame Code Generation ...");
    		log.info("====================================================================================");
            doFinish();
    		log.info("====================================================================================");
            log.info("Code Generation has finished.");
            log.info("You need to Check above logs");
    		log.info("====================================================================================");
            return true;
        } catch (Exception e) {
            log.error(e, e);
            EclipseWorkFactoryManager.getUtils().logMessage(this, IEWUtils.ERROR_MESSAGE);
            log.error("Some Error Occured in Code Generation.");
            return false;
        } finally {

            // Always clear the temporary Map
            WizardHelper.getAuxiliarMap().clear();
        }
    }

    /**
     * 
     * 마법사 종료시 실행
     *
     */
    private void doFinish() {

        try {
            IRunnableWithProgress op = new IRunnableWithProgress() {
                public void run(final IProgressMonitor monitor)
                throws InvocationTargetException, InterruptedException {
                    monitor.beginTask("eGovFrame code generation. ", 100);

                    ThreadUtils.start(getShell(), new Runnable() {
                        public void run() {
                            WizardHelper.finish(null);

                        }
                    });

                    monitor.done();
                }
            };

            new ProgressMonitorDialog(getShell()).run(true, true, op);
//            log.info("Code Generation has Finished.");
        } catch (Exception e) {
            CodeGenLog.logError(e);
        }
    }

    /**
     * 
     * 모델 마법사 페이지 추가(테이블 선택 페이지)
     *
     * @param xmlModelPage
     * @param mode
     */
    private void addWizardModelPage(WizardModelElement xmlModelPage, int mode) {
        CodeGenTableWizardPage tableWizardPage = new CodeGenTableWizardPage(xmlModelPage.getDescription()
            , xmlModelPage.isRequired()
            , xmlModelPage.getImage());

        if (mode >= 0) {
            addPage((IWizardPage)tableWizardPage);
        }

    }

    /* 
     * 비즈니스 로직 코드 생성
     * 
     * (non-Javadoc)
     * @see egovframework.dev.imp.codegen.template.wizards.BusinessLayerSkeletonGeneration#businessCodeGen()
     */
    public void businessCodeGen() {
        doFinish();
        
    }

    /* 
     * 프리젠테이션 로직 코드 생성 
     * 
     * (non-Javadoc)
     * @see egovframework.dev.imp.codegen.template.wizards.PresentationLayerSkeletonGeneration#presentationCodeGen()
     */
    public void presentationCodeGen() {
        doFinish();
        
    }

}
