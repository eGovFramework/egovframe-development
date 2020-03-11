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

import java.util.Iterator;
import java.util.List;

import net.java.amateras.uml.model.AbstractUMLEntityModel;
import net.java.amateras.uml.model.RootModel;

import org.apache.log4j.Logger;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.uml2.uml.Profile;

import egovframework.dev.imp.codegen.model.EgovModelCodeGenPlugin;
import egovframework.dev.imp.codegen.model.converter.XMIFileExporter;
import egovframework.dev.imp.codegen.model.util.LogUtil;

/**
 *  XMI 파일을 EXPORT 하는 위저드
 * <p><b>NOTE:</b> XMI 파일 EXPORT  처리. 
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
public class XMIExportWizard extends Wizard {

    /** 로거 */
    protected final Logger log = Logger.getLogger(XMIExportWizard.class);

    /** XMI 파일을 EXPORT 하는 페이지 */
    private final XMIExportWizardPage creationPage; // WizardNewFileCreationPage creationPage;
	
    /** 소스 모델 */
    private RootModel model;
	
	/**
	 * 생성자
	 * 
	 * 
	 */
	public XMIExportWizard() {
		creationPage = new XMIExportWizardPage(EgovModelCodeGenPlugin.getDefault().getResourceString("export.wizard.name"));
		creationPage.setTitle(EgovModelCodeGenPlugin.getDefault().getResourceString("export.wizard.name"));
		creationPage.setDescription(EgovModelCodeGenPlugin.getDefault().getResourceString("export.wizard.description"));
	}
	
	/**
	 * 모델 세팅하기
	 * 
	 * @param model
	 * 
	 */
	public void setModel(RootModel model) {
		this.model = model;
	}
	
	/* 
	 * 페이지 추가하기
	 * 
	 * (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 * 
	 */
	public void addPages() {
		super.addPages();
		addPage(creationPage);
	}
	
	/* 
	 * 위저드 종료 이벤트 처리시 EXPORT 수행
	 * 
	 * (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 * 
	 */
	public boolean performFinish() {
		String xmiFilePath = null;
		String xmiProfileFilePath = null;		
		
		LogUtil.consoleClear();
    	log.info("====================================================================================");
        log.info("eGovFrame Model Export ...");
		log.info("====================================================================================");

		xmiFilePath = creationPage.getExportPath().toOSString();
		if (xmiFilePath.indexOf('.')>=0)
			xmiProfileFilePath = xmiFilePath.substring(0,xmiFilePath.lastIndexOf("."))+ ".profile.xmi";
		else 
			xmiProfileFilePath = xmiFilePath + ".profile.xmi";

		//		if (xmiFileName.indexOf(".")<0 ){
//			xmiFileName += ".xmi";
//		}
//		xmiFilePath = creationPage.getContainerFullPath().toOSString() + "/" +  xmiFileName;
//		xmiProfileFileName = xmiFileName.substring(0,xmiFileName.lastIndexOf("."))+ ".profile.xmi";
//		xmiProfileFilePath = creationPage.getContainerFullPath().toOSString() + "/" + xmiProfileFileName;
		
		
		XMIFileExporter exporter = new XMIFileExporter();
		
		List<?> children = model.getChildren();
		AbstractUMLEntityModel element = null;
		for (Iterator<?> iter = children.iterator(); iter.hasNext();) {
			element = (AbstractUMLEntityModel) iter.next();
			exporter.convertType(element);
		}
		for (Iterator<?> iter = children.iterator(); iter.hasNext();) {
			element = (AbstractUMLEntityModel) iter.next();
			exporter.convertLink(element);
		}
		for (Iterator<?> iter = children.iterator(); iter.hasNext();) {
			element = (AbstractUMLEntityModel) iter.next();
			exporter.convertStructure(element);
		}
		Profile profile = exporter.makeBaseProfile(); 
 		
		// make profile 
		for (Iterator<?> iter = children.iterator(); iter.hasNext();) {
			element = (AbstractUMLEntityModel) iter.next();
			exporter.addStereotypeToProfile(profile, element);
		}
		
		// apply profile
 		profile.define();

 		// 		EgovModelCodeGenPlugin.getDefault().getStateLocation().toString();

 		exporter.applyingProfile(exporter.getModel(), profile);

 		// apply stereotype
		for (Iterator<?> iter = children.iterator(); iter.hasNext();) {
			element = (AbstractUMLEntityModel) iter.next();
			exporter.applyClassStereoType(profile,element);
			exporter.applyAssociationStereoType(profile,element);
		}
		
		exporter.save(profile, xmiProfileFilePath);

		exporter.save(exporter.getModel(), xmiFilePath);
		
		log.info("====================================================================================");
        log.info("XMI File Name : " + xmiFilePath);
        log.info("XMI Profile File Name : " + xmiProfileFilePath);
		log.info("====================================================================================");

		log.info("====================================================================================");
        log.info("Export Job has finished.");
		log.info("====================================================================================");

		return true;
	}

}
