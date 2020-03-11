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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.java.amateras.uml.model.AbstractUMLEntityModel;
import net.java.amateras.uml.model.RootModel;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.internal.impl.ClassImpl;
import org.eclipse.uml2.uml.internal.impl.DataTypeImpl;
import org.eclipse.uml2.uml.internal.impl.InterfaceImpl;
import org.eclipse.uml2.uml.internal.impl.PackageImpl;
import org.eclipse.uml2.uml.internal.impl.PrimitiveTypeImpl;
import org.eclipse.uml2.uml.util.UMLUtil;

import egovframework.dev.imp.codegen.model.EgovModelCodeGenPlugin;
import egovframework.dev.imp.codegen.model.converter.XMIFileExporter;
import egovframework.dev.imp.codegen.model.generator.StereoTypeWriter;
import egovframework.dev.imp.codegen.model.util.LogUtil;

/**
 * 모델 기반 코드 생성을 수행하는 위저드 클래스 
 * <p><b>NOTE:</b> 모델 기반 코드생성 처리  
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
@SuppressWarnings("restriction")
public class ModelBasedCodeGenWizard extends Wizard implements UMLModelBasedCodeGeneration {
    
    /** 로거 */
    protected final Logger log = Logger.getLogger(ModelBasedCodeGenWizard.class);

    /** 경로설정 페이지 인스턴스 */
    private ModelBasedCodeGenWizardPage folderPage;
    /** 프로젝트 인스턴스 */
    private IJavaProject project;
    /** 클래스 목록 */
    private final Map<Object, Object> target;
    
    /** 클래스다이어그램 모델 인스턴스 */
    private RootModel model;
    /** UML 모델 인스턴스 */
    private Model egovModel;	
    /** 스테레오 타입 변환 인스턴스 */
    private StereoTypeWriter typeWriter;
    /** 스테레오 타입 클래스 목록 */
    private Map<Object, String> stereotypeClassList;
	
	/** 모델을 설정함.
	 * @param model
	 * 
	 */
	public void setModel(RootModel model) {
		this.model = model;
	}
	
    /**
     * 생성자 
     * 
     */
    public ModelBasedCodeGenWizard(){ // IJavaProject project, RootModel root
//            this.project = project;
//            this.model = root;
            setWindowTitle(EgovModelCodeGenPlugin.getDefault().getResourceString("wizard.modelcodegen.dialog.title"));
            target = new TreeMap<Object, Object>();
            //setNeedsProgressMonitor(true);
    }
    /**
     * UML 모델 변환 
     * 
     */
    @SuppressWarnings("unused")
	public void setUmlModel(){
            //
    		XMIFileExporter exporter = new XMIFileExporter();
    		
    		List<?> children = model.getChildren();
    		for (Iterator<?> iter = children.iterator(); iter.hasNext();) {
    			AbstractUMLEntityModel element = (AbstractUMLEntityModel) iter.next();
    			exporter.convertType(element);
    		}
    		for (Iterator<?> iter = children.iterator(); iter.hasNext();) {
    			AbstractUMLEntityModel element = (AbstractUMLEntityModel) iter.next();
    			exporter.convertLink(element);
    		}
    		for (Iterator<?> iter = children.iterator(); iter.hasNext();) {
    			AbstractUMLEntityModel element = (AbstractUMLEntityModel) iter.next();
    			exporter.convertStructure(element);
    		}
    		Profile profile = exporter.makeBaseProfile(); 
     		
    		// make profile 
    		for (Iterator<?> iter = children.iterator(); iter.hasNext();) {
    			AbstractUMLEntityModel element = (AbstractUMLEntityModel) iter.next();
    			exporter.addStereotypeToProfile(profile, element);
    		}
    		// apply profile
     		profile.define();

    		exporter.applyingProfile(exporter.getModel(), profile);
    		// apply stereotype
    		for (Iterator<?> iter = children.iterator(); iter.hasNext();) {
    			AbstractUMLEntityModel element = (AbstractUMLEntityModel) iter.next();
    			exporter.applyClassStereoType(profile,element);
    			exporter.applyAssociationStereoType(profile,element);
    		}
    		
    		// for model 
    		egovModel = exporter.getModel();
    		
    		
    		// for stereoType;
    		TreeIterator<Object> tree = UMLUtil.getAllContents(exporter.getModel(), true,
                    false);
    		
    		getClasses("",egovModel);
    		stereotypeClassList = new HashMap<Object, String>();
    		getStereotypeClassList("",egovModel);
    		
    		typeWriter = new StereoTypeWriter(egovModel,stereotypeClassList);

    }
    
    /** 클래스 목록 생성 
     * 
     * @param parentPackageName
     * @param package_
     */
	public void getClasses(String parentPackageName, org.eclipse.uml2.uml.Package package_){
      String packageName = parentPackageName + "." + package_.getName();
      for(int i=0;i<package_.getPackagedElements().size();i++){
		Object child = package_.getPackagedElements().get(i);
		if(child instanceof ClassImpl ){
            this.target.put(packageName+"."+((org.eclipse.uml2.uml.Class)child).getName(), child);
		}
		if(child instanceof InterfaceImpl){
              this.target.put(packageName+"."+((org.eclipse.uml2.uml.Interface)child).getName(), child);
		}
		if(!(child instanceof PrimitiveTypeImpl) && child instanceof DataTypeImpl){
            this.target.put(packageName+"."+((org.eclipse.uml2.uml.DataType)child).getName(), child);
		}
		if (child instanceof PackageImpl){
			getClasses(packageName,(org.eclipse.uml2.uml.Package)child);
		}
	  }
    }

    /** 스테레오 타입 클래스 목록 변환 
     * 
     * @param parentPackageName
     * @param package_
     */
	public void getStereotypeClassList(String parentPackageName, org.eclipse.uml2.uml.Package package_){
        String packageName = parentPackageName + "." + package_.getName();
        for(int i=0;i<package_.getPackagedElements().size();i++){
	  		Object child = package_.getPackagedElements().get(i);
	  		if(child instanceof Classifier ){
				if (((Classifier) child).getAppliedStereotypes().size()>0){
					stereotypeClassList.put(child, ((Classifier) child).getAppliedStereotypes().get(0).getName());
				}
	  		}
	        else if (child instanceof org.eclipse.uml2.uml.Package){
	  			getStereotypeClassList(packageName,(org.eclipse.uml2.uml.Package)child);
	  		}
  	  	}
    }
    
    /* 페이지 추가
     * 
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    public void addPages() {
    		
            this.folderPage = new ModelBasedCodeGenWizardPage();
            
            addPage(folderPage); // project, target
            
            folderPage.setTarget(target);
            folderPage.setStereotypeClassList(stereotypeClassList);
    }

    
    /* 
     * 위저드 종료시 이벤트 
     * 
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     * 
     */
    public boolean performFinish() {
    	umlBasedCodeGen();
        return true;
    }

	/* 
	 * 
	 * 모델기반 코드생성 수행 
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
	 * 종료시 코드 생성 수행 
	 * 
	 * 
	 */
	public void doFinish(){
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
            
            String selected = null;
            for (int i = 0; i < selection.length;i++) {
            	selected = selection[i].replace('/', '.');
            	selection[i] = selected.startsWith(".") ? selected.substring(1) : selected;
            }
            int totalCodeGenCount = 0;
            int successCodeGenCount = 0;
            
            try {
                typeWriter.setOutputFolder(outputDir);
                typeWriter.setJavaProject(project);
                typeWriter.setSelectedObjects(selection);
                typeWriter.start();
                totalCodeGenCount = typeWriter.getTotalCodeGenCount();
                successCodeGenCount = typeWriter.getSuccessCodeGenCount();
            } catch (Exception e) {
            	log.error(e,e);
            	MessageDialog.openError(this.getShell(), "Source Code Generation", e.getMessage());
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
