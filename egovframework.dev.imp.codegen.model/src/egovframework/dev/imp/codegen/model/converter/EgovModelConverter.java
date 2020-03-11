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
package egovframework.dev.imp.codegen.model.converter;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * 구현도구에서 생성한 XMI 파일을 변환하는 클래스.
 * <p><b>NOTE:</b> 구현도구에서 export 한 파일로부터 UML2 모델을 읽는다.
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
@SuppressWarnings("unchecked")
public class EgovModelConverter extends UMLModelConverter implements
		IConverter {
	/** XMI 리소스 인스턴스 */
	private XMIResource resource = null;
	
	/* 
	 * 모델 변환 
	 * 
	 * @see egovframework.dev.imp.codegen.model.converter.IConverter#convertModel()
	 * 
	 */
	public void convertModel() {
		URI uri = null;
		ResourceSet RESOURCE_SET = null;
		registerResourceFactories();

		uri = URI.createFileURI(xmiFilePath);

		RESOURCE_SET = new ResourceSetImpl();
		resource = (XMIResource) RESOURCE_SET.getResource(uri, true);
		org.eclipse.uml2.uml.Package package_ = null;
		Map<String, String> options = new HashMap<String, String>();
		options.put(XMIResource.OPTION_ENCODING, "UTF-8");
		package_ = (org.eclipse.uml2.uml.Package) EcoreUtil.getObjectByType(
				resource.getContents(), UMLPackage.Literals.PACKAGE);
		egovModel = package_.getModel();

		// 스테레오 타입 목록화함  
		TreeIterator<EObject>  tree = resource.getAllContents();
		while(tree.hasNext()){
			Object obj = tree.next();
			
			if (obj instanceof Classifier){
				if (((Classifier) obj).getAppliedStereotypes().size()>0)
					StereotypeClassesList.put(obj, ((Classifier) obj).getAppliedStereotypes().get(0).getName());
			}
		}
		
	}

	/* 
	 * XMI 파일 경로 설정 
	 * 
	 * @see egovframework.dev.imp.codegen.model.converter.IConverter#setXMIFilePath(java.lang.String)
	 * 
	 */
	public void setXMIFilePath(String xmiFilePath) {
		super.setXmiFilePath(xmiFilePath);
	}
	
}
