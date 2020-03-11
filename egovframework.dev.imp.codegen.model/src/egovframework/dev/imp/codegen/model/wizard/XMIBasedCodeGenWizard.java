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
package egovframework.dev.imp.codegen.model.wizard;

import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.uml2.uml.Model;

import egovframework.dev.imp.codegen.model.EgovModelCodeGenPlugin;
import egovframework.dev.imp.codegen.model.generator.StereoTypeWriter;
import egovframework.dev.imp.codegen.model.util.LogUtil;


/**
 * XMI 기반 코드 생성을 수행하는 위저드 클래스
 * <p><b>NOTE:</b> XMI 기반 코드 생성 처리. 
 * @author 개발환경 개발팀 윤수열
 * @since 2009.08.10
 * @version 1.0
 * @see
 *
 * <pre>
 *  == 개정이력(Modification Information) ==
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.08.10  윤수열          최초 생성
 *
 * </pre>
 */
public class XMIBasedCodeGenWizard extends Wizard implements UMLModelBasedCodeGeneration {

    /** 로거 */
    protected final Logger log = Logger.getLogger(XMIBasedCodeGenWizard.class);

    /** 타겟 폴더 선택 페이지 */
    private ModelBasedCodeGenWizardPage folderPage;
    /** 타겟 프로젝트 */
    private IJavaProject project;
    /** 소스 모델 */
    private Model egovModel;
    /** 코드 생성기 */
    private StereoTypeWriter typeWriter;
    /** XMI 리소스 선택 페이지 */
    private final XMIBasedCodeGenWizardPage resourcePage;
    /** 스테레오 타입 클래스 목록 */
    private Map<?, ?> stereotypeClassList;
	
    /**
     * 생성자
     * 
     */
    public XMIBasedCodeGenWizard(){
            setWindowTitle(EgovModelCodeGenPlugin.getDefault().getResourceString("wizard.xmicodegen.dialog.title"));
            setNeedsProgressMonitor(true);
    		resourcePage = new XMIBasedCodeGenWizardPage(EgovModelCodeGenPlugin.getDefault()
    				.getResourceString("wizard.resource.pagename"));
    }

    /* 
     * 페이지 추가
     * 
     * (non-Javadoc)
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     * 
     */
    public void addPages() {
		addPage(resourcePage);
        this.folderPage = new ModelBasedCodeGenWizardPage(EgovModelCodeGenPlugin.getDefault().getResourceString("wizard.model.description"));
        addPage(folderPage);
    }
    
    /* 
     * 위저드 종료 이벤트
     * 
     * (non-Javadoc)
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     * 
     */
    public boolean performFinish() {
    	egovModel = resourcePage.getModel();
		if (egovModel == null) {
			return false;
		}
		umlBasedCodeGen();
    	return true;
    }

	/* 
	 * 모델 기반 코드 생성 수행 
	 * 
	 * (non-Javadoc)
	 * @see egovframework.dev.imp.codegen.model.wizard.UMLModelBasedCodeGeneration#umlBasedCodeGen()
	 * 
	 * 
	 */
	public void umlBasedCodeGen() {
		doFinish();
	}
	
	/**
	 * 종료시 코드생성 수행 
	 * 
	 * 
	 */
	public void doFinish(){
        int totalCodeGenCount = 0;
        int successCodeGenCount = 0;
	   	LogUtil.consoleClear();
		log.info("====================================================================================");
        log.info("eGovFrame Code Generation ...");
		log.info("====================================================================================");
		
        String outputDir = folderPage.getTargetLocation().removeFirstSegments(1).toString();
        project = folderPage.getProject();
        if(outputDir.startsWith("/")){
                outputDir = outputDir.substring(1);
        }
        if(outputDir.endsWith("/")){
                outputDir = outputDir.substring(0, outputDir.length()-1);
        }
        
        String[] selection = folderPage.getGenerateClasses();
		if (selection != null && selection.length>0) {
	        String selected = null;
	        for (int i = 0; i < selection.length;i++) {
	        	selected = selection[i].replace('/', '.');
	        	selection[i] = selected.startsWith(".") ? selected.substring(1) : selected;
	        }
	        stereotypeClassList = folderPage.getStereotypeClassList();
	        
	        typeWriter = new StereoTypeWriter(egovModel,stereotypeClassList);
	
	        try {
	            typeWriter.setOutputFolder(outputDir);
	            typeWriter.setJavaProject(project);
	            typeWriter.setSelectedObjects(selection);
	            typeWriter.start();
	            totalCodeGenCount = typeWriter.getTotalCodeGenCount();
	            successCodeGenCount = typeWriter.getSuccessCodeGenCount();
	            
	        } catch (Exception e) {
	        	log.error(e, e);
	        	MessageDialog.openInformation(this.getShell(), "Source Code Generation", e.getMessage());
	        }
		}
	    log.info("====================================================================================");
		log.info("Successful Generated File Count : " + successCodeGenCount);
		log.info("Error File Count : " +(totalCodeGenCount - successCodeGenCount) );
		log.info("====================================================================================");
        
		log.info("====================================================================================");
        log.info("Code Generation has finished.");
        log.info("You need to Check above logs");
		log.info("====================================================================================");

	}
    
    

}
