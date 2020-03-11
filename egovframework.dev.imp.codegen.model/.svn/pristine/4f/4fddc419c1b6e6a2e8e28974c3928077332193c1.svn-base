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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.DynamicEObjectImpl;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xml.type.impl.AnyTypeImpl;
import org.eclipse.uml2.uml.BehavioredClassifier;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.InterfaceRealization;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Realization;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;

import egovframework.dev.imp.codegen.model.util.CommonUtil;

/**
 * Eclipse UML2 모델을 사용하는 XMI 파일을 변환하는 클래스.
 * <p><b>NOTE:</b> Eclipse UML2 모델 XMI를 지원하는 툴은 Rose, Together 등이 있다.   
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
public class EclipseModelConverter extends UMLModelConverter implements IConverter {

	/** 리소스 인스턴스 */
	private XMIResource resource = null;

	/**
	 * 생성자 
	 * 
	 */
	public EclipseModelConverter() {
		super();
	}

	/* 
	 * 모델 변환 
	 * 
	 * @see egovframework.dev.imp.codegen.model.converter.IConverter#convertModel()
	 * 
	 */
	@SuppressWarnings({ "deprecation", "unused", "rawtypes" })
	public void convertModel() {
		URI uri = null;
		ResourceSet RESOURCE_SET = null;

		TreeIterator<EObject> tree = null;

		registerResourceFactories();

		uri = URI.createFileURI(xmiFilePath);

		RESOURCE_SET = new ResourceSetImpl();
		resource = (XMIResource) RESOURCE_SET.getResource(uri, true);
		org.eclipse.uml2.uml.Package package_ = null;
		// xmi list 목록 만듬. 

		Map<String, String> options = new HashMap<String, String>();
		options.put(XMIResource.OPTION_ENCODING, "UTF-8");
		try {
			resource.load(options);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		IDsToObjects = resource.getIDToEObjectMap();
		package_ = (org.eclipse.uml2.uml.Package) EcoreUtil.getObjectByType(resource.getContents(), UMLPackage.Literals.PACKAGE);
		egovModel = package_.getModel();
		if (egovModel == null) {
			egovModel = createModel("eGovFrameXMIOutput");
			egovModel.getPackagedElements().add(package_);
		}

		ArrayList<?> PrimitiveTypeAddList = new ArrayList();

		// 필수 프로퍼티 추가 항목 
		PrimitiveType intType = factory.createPrimitiveType();
		intType.setName("int");
		PrimitiveType booleanType = factory.createPrimitiveType();
		booleanType.setName("boolean");
		PrimitiveType shortType = factory.createPrimitiveType();
		shortType.setName("short");
		PrimitiveType longType = factory.createPrimitiveType();
		longType.setName("long");
		PrimitiveType floatType = factory.createPrimitiveType();
		floatType.setName("float");
		PrimitiveType doubleType = factory.createPrimitiveType();
		doubleType.setName("double");
		PrimitiveType charType = factory.createPrimitiveType();
		charType.setName("char");
		PrimitiveType stringType = factory.createPrimitiveType();
		stringType.setName("String");
		typeMap = new HashMap<String, PrimitiveType>();
		typeMap.put("short", shortType);
		typeMap.put("int", intType);
		typeMap.put("long", longType);
		typeMap.put("float", floatType);
		typeMap.put("double", doubleType);
		typeMap.put("boolean", booleanType);
		typeMap.put("char", charType);
		typeMap.put("String", stringType);

		// 
		tree = resource.getAllContents();
		makeStereotypeClassList(tree);
		tree = resource.getAllContents();
		HashMap<Type, String> tempPropertytypeList = new HashMap<Type, String>();
		while (tree.hasNext()) {
			Object obj = tree.next();
			if (obj instanceof Classifier) {
				if (((Classifier) obj).getAppliedStereotypes().size() > 0)
					StereotypeClassesList.put(((Classifier) obj), ((Classifier) obj).getAppliedStereotypes().get(0).getName());
			}
			if (obj instanceof org.eclipse.uml2.uml.Realization) {
				Realization real = (Realization) obj;
				if (real.getSuppliers().size() > 0 && real.getClients().size() > 0) {
					InterfaceRealization realization = factory.createInterfaceRealization();
					if (real.getSuppliers().get(0) instanceof Interface) {
						realization.setContract((Interface) real.getSuppliers().get(0));
						realization.setImplementingClassifier((BehavioredClassifier) real.getClients().get(0));
					}
				}
			}
			// 프로퍼티 타입인데 타입을 알수 없는 경우 프로퍼티 타입을 정의함.   
			if (obj instanceof org.eclipse.uml2.uml.Property) {

				//				속도 문제로 주석처리 					
				if (((Property) obj).getType().getName() == null) {
					Property property = (Property) obj;
					if (property.getName() != null && property.getType() instanceof org.eclipse.uml2.uml.PrimitiveType) {
						if (tempPropertytypeList.get(property.getType()) != null) {
							property.getType().setName((String) tempPropertytypeList.get(property.getType()));
						} else {
							String primitiveTypeName = ((org.eclipse.emf.ecore.InternalEObject) property.getType()).eProxyURI().fragment();
							tempPropertytypeList.put(property.getType(), primitiveTypeName);
							property.getType().setName(primitiveTypeName);
						}
					}
					//					property.setType((org.eclipse.uml2.uml.PrimitiveType) typeMap.get(primitiveTypeName));
				}
				if (((Property) obj).getName() == null) {
					Property property = (Property) obj;
					if (property.getType().getName() != null && property.getType().getName().length() > 0)
						property.setName(CommonUtil.firstCharToLowerCase(property.getType().getName()) + "1");
					//					property.setType((org.eclipse.uml2.uml.PrimitiveType) typeMap.get(primitiveTypeName));
				}

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

	/** 
	 * 스테레오타입 클래스 목록 생성 
	 * 
	 * @param tree
	 * 
	 */
	public void makeStereotypeClassList(TreeIterator<EObject> tree) {
		while (tree.hasNext()) {
			Object obj = tree.next();

			if (obj instanceof AnyTypeImpl) {
				String base_xmi_id = (String) ((AnyTypeImpl) obj).getAnyAttribute().getValue(0);
				String stereotype_name = ((AnyTypeImpl) obj).eClass().getName();
				Object targetObj = IDsToObjects.get(base_xmi_id); // NamesToObjects.get(IDsToObjectNames.get(base_xmi_id));
				if (targetObj instanceof Classifier)
					StereotypeClassesList.put(targetObj, stereotype_name);
			} else if (obj instanceof DynamicEObjectImpl) {
				String stereotype_name = ((DynamicEObjectImpl) obj).eClass().getName();
				Object targetObj = ((org.eclipse.emf.ecore.impl.EObjectImpl) obj).eCrossReferences().get(0);
				if (targetObj instanceof Classifier)
					StereotypeClassesList.put(targetObj, stereotype_name);
			}
		}
	}
}
