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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.xerces.dom.DeferredElementNSImpl;
import org.apache.xerces.parsers.DOMParser;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.BehavioredClassifier;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.InterfaceRealization;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.VisibilityKind;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import egovframework.dev.imp.codegen.model.util.CommonUtil;
import egovframework.dev.imp.codegen.model.util.ModelConvertUtil;

/**
 * Eclipse UML2 모델을 사용하지 않는 XMI 파일을 변환하는 클래스.(ex. EA)
 * <p><b>NOTE:</b> XMI 내에 Extension 을 이용하여 모델 세부 정보를 얻어 낸다.  
 * @author 운영환경1 개발팀 김연수
 * @since 2010.06.10
 * @version 1.0
 * @see
 *
 * <pre>
 *  == 개정이력(Modification Information) ==
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2010.06.10  김연수          최초 생성
 *
 * </pre>
 */
@SuppressWarnings("unchecked")
public class UML2NonEclipseModelConverter  extends UMLModelConverter implements IConverter {

    /** 로거 */
    protected final Logger log = Logger.getLogger(UML2NonEclipseModelConverter.class);

	/**
	 * 
	 * 생성자 
	 * 
	 * 
	 */
	public UML2NonEclipseModelConverter() { 
		super();
		createModel("NonEclipseModel");
	}
	
	/** XMI Extension 에 표현된 요소 맵 정보	 * 
	 */
	private Map<String, Element> extensionMap;
	/** XMI ID와 객체간 맵 정보 */
	private Map<String, Classifier> xmiIdtoObjectMap;
	/** XMI ID와 객체간 맵 정보 */
	private Map<String, Dependency> xmiIdtoObjectMap2;
	/** association 관계로 설정된 속성 정보 */
	private Map<String, Element> associationAttMap;
	/** 객체명과 객체간 맵 정보 */
	private Map<String, Classifier> simpleNametoObjectMap;
    
	String isCompo = null;
	
	Classifier classifierAssociation = null; 
	/**
	 * 패키지 내에 있는 패키지, 클래스, 인터페이스 등의 정보를 변환한다. 
	 * 
	 * @param package_
	 * @param element
	 * 
	 */
	public void convertType(Package package_, Element element) {

		String xmitype = null;
		if (element.hasAttribute("xmi:type")){
			xmitype = element.getAttribute("xmi:type");

			if (xmitype.equals("uml:Class")){
				createClass(package_,element);
			}else if (xmitype.equals("uml:Package")){
				createPackage(package_,element);
				if (element.hasChildNodes()){
					for (int i=0;i< element.getChildNodes().getLength();i++){
						if (element.getChildNodes().item(i) instanceof Element)
							convertType((Package) packageMap.get(package_.getQualifiedName().replaceAll("::", ".")+"." + getName(element)), (Element) element.getChildNodes().item(i));
					}
				}
			}else if (xmitype.equals("uml:Interface")){
				createInterface(package_,element);
			}
		}
	}
    
	/**패키지 내의 클래스의 속성과 오퍼레이션을 변환한다. 
	 * @param package_
	 * @param element
	 * 
	 */
	public void convertStructure(Package package_, Element element) {
		String xmitype = null;
		if (element.hasAttribute("xmi:type")){
			xmitype = element.getAttribute("xmi:type");
			if (xmitype.equals("uml:Class")|| xmitype.equals("uml:Interface") ){
				String class_fullname = getQualifiedClassName(package_, element);  
				if (class_fullname!=null && !class_fullname.equals(""))
					convertClassStructure(class_fullname,element);
			}else if (xmitype.equals("uml:Package")){
				if (element.hasChildNodes()){
					for (int i=0;i< element.getChildNodes().getLength();i++){
						if (element.getChildNodes().item(i) instanceof Element)
							convertStructure((Package) packageMap.get(package_.getQualifiedName().replaceAll("::", ".")+"." + getName(element)), (Element) element.getChildNodes().item(i));
					}
				}
			}
		}
	}
	
	/**
	 * 지정된 요소의 경로명 가져오기 
	 * 
	 * @param package_
	 * @param element
	 * @return
	 * 
	 */
	private String getQualifiedClassName(Package package_, Element element) {
		String class_fullname = null;
		if (element.hasAttribute("name")){
			String name = element.getAttribute("name");
			class_fullname = package_.getQualifiedName().replaceAll("::", ".")+"." + name;
		}
		return class_fullname;
	}

	/**
	 * 특정 엘리먼트의 이름 가져오기 
	 * 
	 * @param element
	 * @return
	 * 
	 */
	private String getName(Element element){
		String name = null;
		if (element.hasAttribute("name")){
			name = element.getAttribute("name");
		}
		return name==null?"":name;
	}
	/**
	 * 클래스 생성
	 * 
	 * @param package_
	 * @param element
	 * 
	 */
	private void createClass(Package package_, Element element) {
		String name = null;
		String class_fullname = null; 
		if (element.hasAttribute("name")){
			name = element.getAttribute("name");
			Class clazz = factory.createClass();
			clazz.setName(name);
//			clazz.setIsAbstract(model.isAbstract());
			package_.getPackagedElements().add(clazz);
			class_fullname = package_.getQualifiedName().replaceAll("::", ".")+"." + clazz.getName();
			typeMap.put(class_fullname, clazz);
			simpleNametoObjectMap.put(name, clazz);
			if (element.hasAttribute("xmi:id"))
				xmiIdtoObjectMap.put(element.getAttribute("xmi:id"), clazz);
		}

	}
	
	/**인터페이스 생성
	 * @param package_
	 * @param element
	 * 
	 */
	private void createInterface(Package package_, Element element) {
		String name = null;
		if (element.hasAttribute("name")){
			name = element.getAttribute("name");
			Interface interface_ = factory.createInterface();
			interface_.setName(name);
			package_.getPackagedElements().add(interface_);
			typeMap.put(package_.getQualifiedName().replaceAll("::", ".")+"." + interface_.getName(), interface_);
			simpleNametoObjectMap.put(name, interface_);
			if (element.hasAttribute("xmi:id"))
				xmiIdtoObjectMap.put(element.getAttribute("xmi:id"), interface_);
		}
	}
	
	/**
	 * 패키지 생성
	 * 
	 * @param package_
	 * @param element
	 * 
	 */
	private void createPackage(Package package_, Element element) {
		String name = null;
		if (element.hasAttribute("name")){
			name = element.getAttribute("name");
			Package pkg = factory.createPackage();
			pkg.setName(name);
			package_.getPackagedElements().add(pkg);
			packageMap.put(package_.getQualifiedName().replaceAll("::", ".")+"." + name, pkg);
		}
	}
	
	/**
	 * 클래스의 구조를 변환함
	 *
	 * @param class_fullname
	 * @param element
	 * 
	 */
	public void convertClassStructure(String class_fullname, Element element) {
		convertAttributes(class_fullname, element);
		convertOperations(class_fullname, element);
	}
	
	/**
	 * 오퍼레이션을 변환함
	 *
	 * @param class_fullname
	 * @param classElement
	 * 
	 */
	private void convertOperations(String class_fullname, Element classElement) {
		Classifier clazz = (Classifier) typeMap.get(class_fullname);
		List<Element> opes = getOperations(classElement);
		Element operationElement = null;
		for (Iterator<Element> iter = opes.iterator(); iter.hasNext();) {
			operationElement = (Element) iter.next();
			addOperation(clazz, operationElement);
		}
		
		// Inner Class가 있는 경우 로그메세지 출력함.
		List<Element> nestedClassifier = getSpecificChildList(classElement,"nestedClassifier");
		Element nestedElement = null;
		for (Iterator<Element> iter = nestedClassifier.iterator(); iter.hasNext();) {
			nestedElement = (Element) iter.next();
			log.info(clazz.getName()+" 클래스에 [" +nestedElement.getAttribute("name") + "] Inner Class가 존재하나 AmaterasUml에서는 표현할 수 없어 생략합니다.");
		}
		
	}

	/**
	 * 오퍼레이션 목록을 가져옴. 
	 *
	 * @param element
	 * @return
	 * 
	 */
	private List<Element> getOperations(Element element) {

		return getSpecificChildList(element,"ownedOperation");
	}
	
	/**특정 이름을 가진 하위개체목록을 가져옴. 
	 * @param element
	 * @param strName
	 * @return
	 * 
	 */
	private List<Element> getSpecificChildList(Element element, String strName) {
		NodeList list = element.getChildNodes();
		List<Element> rv = new ArrayList<Element>();
		for (int i=0;i<list.getLength();i++) {
			if (list.item(i) instanceof Element
					&& list.item(i).getNodeName().equals(strName)){
				rv.add((Element) list.item(i));
			}
		}
		return rv;
	}

	/**
	 * 오퍼레이션을 추가함. 
	 *
	 * @param classifier
	 * @param operationElement
	 * 
	 */
	@SuppressWarnings("unused")
	private void addOperation(Classifier classifier, Element operationElement) {
		String name = operationElement.getAttribute("name");
		String visibility = operationElement.getAttribute("visibility");
		Type propertyType = null;
		String typeName = null;
		Element operationInfoElement = getOperationInfoFromExtension(operationElement);
		Operation operation = factory.createOperation();
		operation.setName(name);
		
		

		if (classifier instanceof Class) {
			Class className = (Class) classifier;
		} else if (classifier instanceof Interface) {
			Interface interfaceName = (Interface) classifier;
		}

		operation.setVisibility(ModelConvertUtil.getVisibility(visibility));
		operation.setIsStatic(operationInfoElement.getAttribute("static")!=null
								&& operationInfoElement.getAttribute("static").equals("true")?
										true:false);
		operation.setIsAbstract(operationInfoElement.getAttribute("isAbstract")!=null
				&& operationInfoElement.getAttribute("isAbstract").equals("true")?
						true:false);
		if (operationInfoElement.getAttribute("type") != null 
				&& !"void".equals(operationInfoElement.getAttribute("type"))) {
			Parameter parameter = factory.createParameter();
			typeName = operationInfoElement.getAttribute("type");
			parameter.setName("return");
			if (typeName!=null && !typeName.equals("")){	
				propertyType = (Type) typeMap.get(typeName);
				if (propertyType==null){
					propertyType = (Type) typeMap.get(
							classifier.getPackage().getQualifiedName()
								.replace("::",".")+"."+typeName);
				}
				if (propertyType == null) {
					if (operationElement.hasAttribute("type")){
						propertyType = (Type) xmiIdtoObjectMap.get(operationElement.getAttribute("type"));	
					}
				}
	
				if (propertyType == null) {
					propertyType = createDataType(typeName);
				}
				parameter.setType(propertyType);
			}
			parameter.setDirection(ParameterDirectionKind.get(ParameterDirectionKind.RETURN));
			operation.getOwnedParameters().add(parameter);
		}
		
		List<?> params = getParameter(operationElement);
		Element parameterElement = null;
		for (Iterator<?> iterator = params.iterator(); iterator.hasNext();) {

			parameterElement = (Element) iterator.next();
			if (parameterElement.getAttribute("direction")==null 
					|| parameterElement.getAttribute("direction").indexOf("return")<0
					)
				addParameter(classifier,operation, parameterElement);

		}
		
		if (classifier instanceof Class) {
			Class class1 = (Class) classifier;
			class1.getOwnedOperations().add(operation);
		} else if (classifier instanceof Interface) {
			Interface interface1 = (Interface) classifier;
			interface1.getOwnedOperations().add(operation);
		}
	}
		
		
	/**
	 * 오퍼레이션에 패러미터를 추가함. 
	 * 
	 * @param classifier
	 * @param operation
	 * @param parameterElement
	 * 
	 */
	private void addParameter(Classifier classifier, Operation operation, Element parameterElement) {
		Parameter parameter = factory.createParameter();
		parameter.setName(parameterElement.getAttribute("name"));
		String typeName = null;
		
		Element propertyInfoElement = getPropertyInfoFromExtension(parameterElement);
		if (propertyInfoElement!=null)
			typeName = propertyInfoElement.getAttribute("type");
		if (typeName!=null && !typeName.equals("")){		
			Type propertyType = (Type) typeMap.get(typeName);
			
			if (propertyType==null){
				propertyType = (Type) typeMap.get(
						classifier.getPackage().getQualifiedName()
							.replace("::",".")+"."+typeName);
			}
			if (propertyType == null) {
				if (propertyInfoElement.hasAttribute("classifier")){
					propertyType = (Type) xmiIdtoObjectMap.get(
										propertyInfoElement.getAttribute("classifier"));	
				}
			}
			if (propertyType == null) {
				propertyType = (Type) simpleNametoObjectMap.get(typeName);
			}		
			if (propertyType==null){
				propertyType = createDataType(typeName);
			}		
			parameter.setType(propertyType);

		}
		operation.getOwnedParameters().add(parameter);
	}

	/**
	 * 패러미터 목록을 가져옴. 
	 *
	 * @param operationElement
	 * @return
	 * 
	 */
	private List<?> getParameter(Element operationElement) {
		return getSpecificChildList(operationElement,"ownedParameter");
	}

	/**
	 * 속성을 변환함.
	 *
	 * @param class_fullname
	 * @param element
	 * 
	 */
	private void convertAttributes(String class_fullname, Element element) {

		Classifier clazz = (Classifier) typeMap.get(class_fullname);
		if (element.hasChildNodes()){
			for (int i=0;i< element.getChildNodes().getLength();i++){
				if (element.getChildNodes().item(i) instanceof Element
						&& element.getChildNodes().item(i).getNodeName().equals("ownedAttribute")){
					// 어트리뷰타가 어소시에이션인 경우 생략함.
					List<?> compolList = getSpecificChildList(element, "ownedAttribute");
					Element ownedEnd1 = (Element) compolList.get(0);
					String iisCompo = ownedEnd1.getAttribute("association");
						addAttribute(clazz,(Element) element.getChildNodes().item(i), iisCompo);
				}
					//
			}
		}
	}
	
	/**
	 * 클래스에 속성을 추가함. 
	 * 
	 * @param classifier
	 * @param attributeElement
	 * 
	 */
	@SuppressWarnings("unused")
	private void addAttribute(Classifier classifier, Element attributeElement, String iisCompo) {		

		String temp = iisCompo;
		
		if (attributeElement.hasAttribute("association") && attributeElement.hasAttribute("xmi:id")){
			//associationAttMap.put(attributeElement.getAttribute("xmi:id"), attributeElement);
			
			associationAttMap.put(attributeElement.getAttribute("xmi:id"), attributeElement);
			associationAttMap.put(attributeElement.getAttribute("association"), attributeElement);
			return;
		}
		
		String name = attributeElement.getAttribute("name");

		if (name==null || name.equals(""))
			return;
		String visibility = attributeElement.getAttribute("visibility");
		Element propertyInfo = getPropertyInfoFromExtension(attributeElement);
		Type propertyType = null;
		String typeName = null;
		Property property = factory.createProperty();
		boolean isStatic = false;
		boolean isAbstract = false;
		if (propertyInfo!=null){
			typeName = propertyInfo.getAttribute("type");
			isStatic = propertyInfo.getAttribute("static")!=null 
							&& propertyInfo.getAttribute("static").equals("true")?
								true:false;
			isAbstract = propertyInfo.getAttribute("isAbstract")!=null 
			&& propertyInfo.getAttribute("isAbstract").equals("true")?
				true:false;
		}
		if (typeName == null) {
			typeName="String";
		}
		// 타입명이 없을 경우 타입을 설정하지 않는다. 
		if (!typeName.equals("")){
			propertyType = (Type) typeMap.get(typeName);
			// primitive type 또는 기존에 생성된 data type 이 아닐 경우 
			// 먼저 현재 폴더에서 찾는다. 
			if (propertyType==null){
				propertyType = (Type) typeMap.get(
							classifier.getPackage().getQualifiedName()
								.replace("::",".")+"."+typeName);
			}
			// 그것도 아닌 경우 특정 클래스일 경우가 있다. 
			if (propertyType==null){
				if (attributeElement.hasChildNodes()){
					List<?> typeInfoList = getSpecificChildList(attributeElement,"type");
					if (typeInfoList!=null && typeInfoList.size()>0 && ((Element) typeInfoList.get(0)).hasAttribute("xmi:idref")){
						String xmiTypeId = ((Element) typeInfoList.get(0)).getAttribute("xmi:idref");
						propertyType = (Type) xmiIdtoObjectMap.get(xmiTypeId);
					}
				}
			}
	
			if (propertyType==null){
				propertyType = createDataType(typeName);
			}
			property.setType(propertyType);
		}
		property.setName(name);
		property.setVisibility(VisibilityKind.getByName(visibility));
		property.setIsStatic(isStatic);

		if (classifier instanceof Class) {
			Class clazz = (Class) classifier;
			addAttributes(clazz.getOwnedAttributes(), property);
		} else if (classifier instanceof Interface) {
			Interface interface1 = (Interface) classifier;
			addAttributes(interface1.getOwnedAttributes(),property);
		}

	}
	
	/**
	 * Extension 으로부터 속성 정보 얻기 
	 * 
	 * @param element
	 * @return
	 * 
	 */
	private Element getPropertyInfoFromExtension(Element element) {

		Element propertyInfo = null;
		Element extensionElement = null;
		/*log.info("=위치찾기1="+extensionMap.toString());
		log.info("=위치찾기2="+extensionMap.get(element.getAttribute("xmi:id")));*/
		if (element.hasAttribute("xmi:id") && extensionMap.containsKey(element.getAttribute("xmi:id"))){
			extensionElement = (Element) extensionMap.get(element.getAttribute("xmi:id"));
		}
		if (extensionElement!=null && extensionElement.hasChildNodes()){
			NodeList properties = extensionElement.getElementsByTagName("properties");
			if (properties.getLength()>0)
				propertyInfo = (Element) extensionElement.getElementsByTagName("properties").item(0);
		}
		return propertyInfo;
	}
	
	/**
	 * Extension 으로부터 오퍼레이션의 정보 얻기 
	 * 
	 * @param element
	 * @return
	 * 
	 */
	private Element getOperationInfoFromExtension(Element element) {

		Element operationInfo = null;
		Element extensionElement = null;

		if (element.hasAttribute("xmi:id") && extensionMap.containsKey(element.getAttribute("xmi:id"))){
			extensionElement = (Element) extensionMap.get(element.getAttribute("xmi:id"));
		}
		if (extensionElement!=null && extensionElement.hasChildNodes()){
			NodeList typeInfo = extensionElement.getElementsByTagName("type");
			if (typeInfo.getLength()>0)
				operationInfo = (Element) extensionElement.getElementsByTagName("type").item(0);
		}
		return operationInfo;
	}	
	
	/* 
	 * 모델을 변환함. 
	 * 
	 * @see egovframework.dev.imp.codegen.model.converter.IConverter#convertModel()
	 * 
	 */
	public void convertModel() { 

		FileReader reader= null;
		try {
			reader = new FileReader(new File(xmiFilePath));

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} 
		InputSource inputSource = new InputSource(reader);
		inputSource.setEncoding("UTF-8");
		
		DOMParser parser = new DOMParser();

		Document document = null; 

		try {

			parser.parse(inputSource);
			document = parser.getDocument();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Element de = document.getDocumentElement();
		NodeList nl = de.getChildNodes();
		Element umlModel = null; 
		Element xmiExtension = null; 
		for (int i=0;i<nl.getLength();i++){
			if (nl.item(i) instanceof DeferredElementNSImpl){
				if (nl.item(i).getNodeName().equals("uml:Model"))
					umlModel = (Element) nl.item(i);
				if (nl.item(i).getNodeName().equals("xmi:Extension"))
					xmiExtension = (Element) nl.item(i);
			}
		}
		
		// 초기화 
		packageMap = new HashMap<Object, Package>();
		typeMap = new HashMap<String, Classifier>();
		extensionMap = new HashMap<String, Element>();
		xmiIdtoObjectMap = new HashMap<String, Classifier>();
		xmiIdtoObjectMap2 = new HashMap<String, Dependency>();
		associationAttMap = new HashMap<String, Element>();
		simpleNametoObjectMap = new HashMap<String, Classifier>();		
		
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
		typeMap = new HashMap<String, Classifier>();	
		typeMap.put("short", shortType);
		typeMap.put("int", intType);
		typeMap.put("long", longType);
		typeMap.put("float", floatType);
		typeMap.put("double", doubleType);
		typeMap.put("boolean", booleanType);
		typeMap.put("char", charType);
		typeMap.put("String", stringType);
		
		getModel().getPackagedElements().add(booleanType);
		getModel().getPackagedElements().add(intType);
		getModel().getPackagedElements().add(shortType);
		getModel().getPackagedElements().add(longType);
		getModel().getPackagedElements().add(floatType);
		getModel().getPackagedElements().add(doubleType);
		getModel().getPackagedElements().add(charType);
		getModel().getPackagedElements().add(stringType);	   
		
		removeError(umlModel);
		makeExtensionMap(xmiExtension);

		String stereotype_name = null;
		Classifier classifier = null;
		Element subElement = null;
		
		if (umlModel.hasChildNodes()){
			// convertType
			for (int i=0;i< umlModel.getChildNodes().getLength();i++){
				if (umlModel.getChildNodes().item(i) instanceof Element)
					convertType(getModel(), (Element) umlModel.getChildNodes().item(i));
			}
			// convertStructure
			for (int i=0;i< umlModel.getChildNodes().getLength();i++){
				if (umlModel.getChildNodes().item(i) instanceof Element)
					convertStructure(getModel(), (Element) umlModel.getChildNodes().item(i));
			}			
			// convertLink
			for (int i=0;i< umlModel.getChildNodes().getLength();i++){

				if (umlModel.getChildNodes().item(i) instanceof Element){

					convertLink(getModel(), (Element) umlModel.getChildNodes().item(i));
				}
			}

			for (int i=0;i< umlModel.getChildNodes().getLength();i++){
				if (umlModel.getChildNodes().item(i) instanceof Element){
					subElement = (Element) umlModel.getChildNodes().item(i);
					if (subElement.hasAttribute("base_Class")){
						stereotype_name = subElement.getLocalName();
						classifier = (Classifier) xmiIdtoObjectMap.get(subElement.getAttribute("base_Class"));
						StereotypeClassesList.put(classifier, stereotype_name);	
					}else if (subElement.hasAttribute("base_Interface")){
						stereotype_name = subElement.getLocalName();
						classifier = (Classifier) xmiIdtoObjectMap.get(subElement.getAttribute("base_Interface"));
						StereotypeClassesList.put(classifier, stereotype_name);	
					}else if (subElement.hasAttribute("base_Association")){
						stereotype_name = subElement.getLocalName();
						classifier = (Classifier) xmiIdtoObjectMap.get(subElement.getAttribute("base_Association"));
						StereotypeClassesList.put(classifier, stereotype_name);	
					}else if (subElement.hasAttribute("base_Dependency")){
						stereotype_name = subElement.getLocalName();
						Dependency depModel = xmiIdtoObjectMap2.get(subElement.getAttribute("base_Dependency"));
						StereotypeClassesList.put(depModel, stereotype_name);	
					}
				}
			}
			
			
		}
	}		
	
	/** 
	 * 클래스 모델간의 관계를 변환함. 
	 * 
	 * @param package_
	 * @param element
	 * 
	 */
	private void convertLink(Package package_, Element element) {

		String xmitype = null;
		if (element.hasAttribute("xmi:type")){
			xmitype = element.getAttribute("xmi:type");

			String ownedGen11 = element.getAttribute("xmi:id");
			
			/*//위치 테스트
			log.info("위치찾기:"+extensionMap.get(ownedGen11).getAttribute("Left"));
			log.info("위치찾기:"+(extensionMap.get(ownedGen11).getElementsByTagName("Left")).item(0));*/
			if (xmitype.equals("uml:Association")){
				
				// 어소시에이션 찾아서 이놈이 어그리게이션이면 스킵
				Element isAssociate = (Element) extensionMap.get(ownedGen11);

				List<?> aggregationList = getSpecificChildList(isAssociate, "properties");

				if(aggregationList.size() > 0){
					Element ownedEnd1 = (Element) aggregationList.get(0);

					String isCompoYB = ownedEnd1.getAttribute("ea_type");

					if(!"Aggregation".equals(isCompoYB)){
						convertAssociation(element, null);
					}
				}
				
				//convertAssociation(element, null);
			}else if (xmitype.equals("uml:Dependency")){
				convertDependency(element);
			}else if (xmitype.equals("uml:Realization")){

				convertRealization(element);
			}else if (xmitype.equals("uml:Class") || xmitype.equals("uml:Interface")){

				// composition, aggregation 도 포함 
				// Association Aggregation Composite 구분
				
				// 소스 커넥션 찾기
				//log.info("소스 커넥션 찾기=="+ownedGen11);
				// 타겟커넥션 찾기
				isCompo = null;
				List<?> compolList = getSpecificChildList(element, "ownedAttribute");
				if(compolList.size() > 0){
					for(int i=0;i<compolList.size();i++){

						Element ownedEnd1 = (Element) compolList.get(i);
						isCompo = ownedEnd1.getAttribute("aggregation");
						
						if("shared".equals(isCompo) ||"composite".equals(isCompo)){

							convertAggregation(ownedEnd1, ownedGen11);
						}
					}
				}
				
				// Generalization 시작
				convertGeneralization(element);

			}else if (xmitype.equals("uml:Package")){
				if (element.hasChildNodes()){
					for (int i=0;i< element.getChildNodes().getLength();i++){
						if (element.getChildNodes().item(i) instanceof Element)
							convertLink((Package) packageMap.get(package_.getQualifiedName().replaceAll("::", ".")+"." + getName(element)), (Element) element.getChildNodes().item(i));
					}
				}
			}
		}
	}

	/** 
	 * Generation 관계를 변환
	 * 
	 * @param package_
	 * @param element
	 * 
	 */
	private void convertGeneralization(Element element) {

		List<?> generalList = getSpecificChildList(element, "generalization");
		
		Classifier classifier = (Classifier) xmiIdtoObjectMap.get(element.getAttribute("xmi:id"));

		Classifier classifier2 = null;
		Generalization generalization = null;
		
		String attributeName1 = null;
				
		for (int i=0;i<generalList.size();i++){

			Element ownedGen1 = (Element) generalList.get(i);
			if (ownedGen1.hasAttribute("general"))
				attributeName1 = ownedGen1.getAttribute("general");
			if (attributeName1==null || attributeName1.equals(""))
				attributeName1 =  CommonUtil.firstCharToLowerCase(classifier2.getName());
			classifier2 = (Classifier) xmiIdtoObjectMap.get(attributeName1);
			//classifier2 = (Classifier) xmiIdtoObjectMap.get(((Element) generalList.get(i)).getAttribute("xmi:id"));
			if (classifier2!=null){
				generalization = factory.createGeneralization();
				generalization.setSpecific(classifier);
				generalization.setGeneral(classifier2);
			}else{
				log.info(classifier.getName()+" 의 Gneralization의 중 하나가 타겟 클래스가 존재하지 않아 해당 Gneralization의 생략합니다.");
			}
		}

	}

	
	/** 
	 * Realization 관계를 변환함. 
	 * 
	 * @param element
	 * 
	 */
	private void convertRealization(Element element) {
		String supplierXmiId = element.getAttribute("supplier");
		String clientXmiId = element.getAttribute("client"); 
		Classifier supplier = (Classifier) xmiIdtoObjectMap.get(supplierXmiId);
		Classifier client = (Classifier) xmiIdtoObjectMap.get(clientXmiId);
		InterfaceRealization realization = factory.createInterfaceRealization();
		try{
			realization.setContract((Interface) supplier);
		}catch(Exception e){
			// realization이 interface <- class 의 순서로 되어 있어야만 통과하므로 
			// 이 부분을 스킵처리하여 realization이 반대로 정의된 경우는 그냥 통과토록 함.
			log.info("realization 오류가 발생하여 [" + supplier.getName() + " --> " +client.getName()+ "]을 생략합니다.");
			log.info("realization 오류:::::::::"+e.toString());
		}
		
		if (client instanceof BehavioredClassifier) {
			realization.setImplementingClassifier((BehavioredClassifier) client);
		}
	}

	/**
	 *  Dependency 관계를 변환함.
	 * 
	 * @param element
	 * 
	 */
	private void convertDependency(Element element) {
		String supplierXmiId = element.getAttribute("supplier");
		String clientXmiId = element.getAttribute("client"); 	
		// 김연수 수정 소스와 타겟 위치가 바뀌어서 조정함.
		/*Classifier supplier = (Classifier) xmiIdtoObjectMap.get(supplierXmiId);
		Classifier client = (Classifier) xmiIdtoObjectMap.get(clientXmiId);*/
		Classifier supplier = (Classifier) xmiIdtoObjectMap.get(clientXmiId);
		Classifier client = (Classifier) xmiIdtoObjectMap.get(supplierXmiId);
		if(null == supplier){
			// 김연수 수정 : EA에서 export할 때 상속 받은 클래스가 다른 패키지에 있는경우를 대비하여 
			// supplier/client 가 있을 경우만 dependency 처리
			log.info(client.getName()+" 의 Dependency의 중 하나가 타겟 클래스가 존재하지 않아 해당 Dependency의 생략합니다.");
		}else if(null == client){
			// 김연수 수정 : EA에서 export할 때 상속 받은 클래스가 다른 패키지에 있는경우를 대비하여 
			// supplier/client 가 있을 경우만 dependency 처리
			log.info(supplier.getName()+" 의 Dependency의 중 하나가 타겟 클래스가 존재하지 않아 해당 Dependency의 생략합니다.");
		}else{
			client.createDependency(supplier);

			if(null != client.createDependency(supplier)){

				xmiIdtoObjectMap2.put(element.getAttribute("xmi:id"), client.createDependency(supplier));
			}
			
		}
		
	}

	/**
	 *  Aggregation관계를 변환함. 
	 *
	 * @param element
	 * 
	 */
	private void convertAggregation(Element aggregationElement, String classifierXmiIdforcompo) {
		// 김연수 수정

		String attributeName1 = null;
		String attributeName2 = null;
		Classifier classifier1 = (Classifier) xmiIdtoObjectMap.get(classifierXmiIdforcompo);
		Classifier classifier2 = null;
		if (attributeName1==null || attributeName1.equals(""))
			attributeName1 =  CommonUtil.firstCharToLowerCase(classifier1.getName());

			String classifierXmiId = ((Element) aggregationElement.getElementsByTagName("type").item(0)).getAttribute("xmi:idref");
			classifier2 = (Classifier) xmiIdtoObjectMap.get(classifierXmiId);
			
			if (attributeName2==null || attributeName2.equals(""))
				attributeName2 =  CommonUtil.firstCharToLowerCase(classifier2.getName()); 

			if("composite".equals(isCompo)){

				if (classifier1!=null && attributeName1!=null){
					classifier1.createAssociation(true, 
							AggregationKind.COMPOSITE_LITERAL, attributeName1,
							1, 1, 
							classifier2, 
						   false, AggregationKind.NONE_LITERAL, attributeName2, 1, 1);
				}
			}else if("shared".equals(isCompo)){

				if (classifier1!=null && attributeName1!=null){
					classifier1.createAssociation(true, 
							AggregationKind.SHARED_LITERAL, attributeName1,
							1, 1, 
							classifier2, 
						   false, AggregationKind.NONE_LITERAL, attributeName2, 1, 1);
				}

				if (classifier1!=null && attributeName1!=null){
					classifier1.createAssociation(true, 
							AggregationKind.NONE_LITERAL, attributeName1,
							1, 1, 
							classifier2, 
						   false, AggregationKind.NONE_LITERAL, attributeName2, 1, 1);
				}
			}
	}
	
	/**
	 *  Association관계를 변환함. 
	 *
	 * @param element
	 * 
	 */
	@SuppressWarnings({ "unused", "null" })
	private void convertAssociation(Element associaionElement, String classifierXmiIdforcompo) {
		// 김연수 수정
		
		String temp = classifierXmiIdforcompo;
		
		List<?> owendEndList = getSpecificChildList(associaionElement,"ownedEnd");
		List<Element> attributeElements = new ArrayList<Element>();
		String attributeName1 = null;
		String attributeName2 = null;
		Classifier classifier1 = null;
		Classifier classifier2 = null;

		//log.info("owendEndList.size()=="+owendEndList.size());
		// unspecified or bi-directed
		if (owendEndList.size()==2){
			Element ownedEnd1 = (Element) owendEndList.get(0);
			Element ownedEnd2 = (Element) owendEndList.get(1);
			if (ownedEnd1.hasAttribute("name"))
				attributeName1 = ownedEnd1.getAttribute("name");

			if (ownedEnd2.hasAttribute("name"))
				attributeName2 = ownedEnd1.getAttribute("name");

			if (ownedEnd1.hasChildNodes()){
				String classifierXmiId = ((Element) ownedEnd1.getElementsByTagName("type").item(0)).getAttribute("xmi:idref");
				classifier1 = (Classifier) xmiIdtoObjectMap.get(classifierXmiId);
			}
			if (ownedEnd2.hasChildNodes()){
				String classifierXmiId = ((Element) ownedEnd2.getElementsByTagName("type").item(0)).getAttribute("xmi:idref");
				classifier2 = (Classifier) xmiIdtoObjectMap.get(classifierXmiId);
			}
			
			if (attributeName1==null || attributeName1.equals(""))
				// association에서 classifier1이  없을 경우 생략
				if(null == classifier1){
					log.info(classifier1.getName()+" 의 Association 중 하나가 타겟 클래스가 존재하지 않아 해당 Association을 생략합니다.");
				}else{
					attributeName1 =  CommonUtil.firstCharToLowerCase(classifier1.getName()); 
				}
				
			if (attributeName2==null || attributeName2.equals(""))
				attributeName2 =  CommonUtil.firstCharToLowerCase(classifier2.getName()); 

			if (classifier1!=null && attributeName1!=null){
				classifier1.createAssociation(true, 
						AggregationKind.NONE_LITERAL, attributeName1,
						1, 1, 
						classifier2, 
					   false, AggregationKind.NONE_LITERAL, attributeName2, 1, 1);
				
				classifierAssociation = classifier1.createAssociation(true, 
						AggregationKind.NONE_LITERAL, attributeName1,
						1, 1, 
						classifier2, 
					   false, AggregationKind.NONE_LITERAL, attributeName2, 1, 1);
				xmiIdtoObjectMap.put(associaionElement.getAttribute("xmi:id"), classifierAssociation);
			}
		}
		
		// source -> direction or direction -> source
		else if (owendEndList.size()==1){

			Element ownedEnd1 = (Element) owendEndList.get(0);
			if (ownedEnd1.hasAttribute("name"))
				attributeName1 = ownedEnd1.getAttribute("name");
			if (ownedEnd1.hasChildNodes()){
				String classifierXmiId = ((Element) ownedEnd1.getElementsByTagName("type").item(0)).getAttribute("xmi:idref");

				classifier1 = (Classifier) xmiIdtoObjectMap.get(classifierXmiId); //service
			}
			if ((attributeName1==null || attributeName1.equals("")) && classifier1!=null)
				// association에서 classifier1이  없을 경우 생략
				log.info(classifier1.getName()+" 의 Association 중 하나가 타겟 클래스가 존재하지 않아 해당 Association을 생략합니다.");
				if(null != classifier1){
					attributeName1 =  CommonUtil.firstCharToLowerCase(classifier1.getName()); 
				}
		
			List<?> memberEndList = getSpecificChildList(associaionElement,"memberEnd");
			Element memberEnd2 = (Element) memberEndList.get(0);

			if (memberEnd2.hasAttribute("xmi:idref")){
				Element attElement2 = (Element) associationAttMap.get(memberEnd2.getAttribute("xmi:idref"));

				// 김연수 수정
				if(attElement2 != null){
					if (attElement2.hasAttribute("name")){
						attributeName2 = attElement2.getAttribute("name");
					}
					if (attElement2.hasChildNodes()){
						String classifierXmiId = ((Element) attElement2.getElementsByTagName("type").item(0)).getAttribute("xmi:idref");
						classifier2 = (Classifier) xmiIdtoObjectMap.get(classifierXmiId);

					}
					if (attributeName2==null || attributeName2.equals(""))
						attributeName2 =  CommonUtil.firstCharToLowerCase(classifier2.getName()); 
				}
			}
			
			if (null != classifier2 && null != attributeName2){
				
				if (classifier1!=null && attributeName1!=null){
					classifier1.createAssociation(true, 
							AggregationKind.NONE_LITERAL, attributeName1,
							1, 1, 
							classifier2, 
						   false, AggregationKind.NONE_LITERAL, attributeName2, 1, 1);
				}
				
				classifierAssociation = classifier1.createAssociation(true, 
						AggregationKind.NONE_LITERAL, attributeName1,
						1, 1, 
						classifier2, 
					   false, AggregationKind.NONE_LITERAL, attributeName2, 1, 1);
				xmiIdtoObjectMap.put(associaionElement.getAttribute("xmi:id"), classifierAssociation);
			}
		 }	
		else if (owendEndList.size()==0){

			List<?> memberEndList = getSpecificChildList(associaionElement,"memberEnd");

			if (memberEndList.size()==2){
				Element memberEnd1 = (Element) memberEndList.get(0);
				Element memberEnd2 = (Element) memberEndList.get(1);
				if (memberEnd1.hasAttribute("xmi:idref")){
					Element attElement1 = (Element) associationAttMap.get(memberEnd1.getAttribute("xmi:idref"));
					if (attElement1.hasAttribute("name")){
						attributeName1 = attElement1.getAttribute("name");
					}
					if (attElement1.hasChildNodes()){
						String classifierXmiId = ((Element) attElement1.getElementsByTagName("type").item(0)).getAttribute("xmi:idref");
						classifier1 = (Classifier) xmiIdtoObjectMap.get(classifierXmiId);
					}
					if (attributeName1==null || attributeName1.equals(""))
						// association에서 classifier1이  없을 경우 생략
						log.info(classifier1.getName()+" 의 Association 중 하나가 타겟 클래스가 존재하지 않아 해당 Association을 생략합니다.");
						if(null != classifier1){
							attributeName1 =  CommonUtil.firstCharToLowerCase(classifier1.getName()); 
						}
				}
				if (memberEnd2.hasAttribute("xmi:idref")){
					Element attElement2 = (Element) associationAttMap.get(memberEnd2.getAttribute("xmi:idref"));
					if (attElement2.hasAttribute("name")){
						attributeName2 = attElement2.getAttribute("name");
					}
					if (attElement2.hasChildNodes()){
						String classifierXmiId = ((Element) attElement2.getElementsByTagName("type").item(0)).getAttribute("xmi:idref");
						classifier2 = (Classifier) xmiIdtoObjectMap.get(classifierXmiId);
					}
					if (attributeName2==null || attributeName2.equals(""))
						attributeName2 =  CommonUtil.firstCharToLowerCase(classifier2.getName()); 
				}
				
			}

			if (classifier1!=null && attributeName1!=null){
				classifier1.createAssociation(true, 
						AggregationKind.NONE_LITERAL, attributeName1,
						1, 1, 
						classifier2, 
					   false, AggregationKind.NONE_LITERAL, attributeName2, 1, 1);
				
				classifierAssociation = classifier1.createAssociation(true, 
						AggregationKind.NONE_LITERAL, attributeName1,
						1, 1, 
						classifier2, 
					   false, AggregationKind.NONE_LITERAL, attributeName2, 1, 1);
				
				xmiIdtoObjectMap.put(associaionElement.getAttribute("xmi:id"), classifierAssociation);
			}
		}
	}
	
	/** 
	 * Extension 맵 관계를 변환함. 
	 * 
	 * @param element
	 * 
	 */
	private void makeExtensionMap(Element element) {
		if (element.hasAttribute("xmi:idref")){
			extensionMap.put(element.getAttribute("xmi:idref"),element); 
		}
		if (element.hasChildNodes()){
			for (int i=0;i< element.getChildNodes().getLength();i++){
				if (element.getChildNodes().item(i) instanceof Element)
					makeExtensionMap((Element) element.getChildNodes().item(i)); 
			}
		}
		
		if (element.hasAttribute("geometry")){
			extensionMap.put(element.getAttribute("geometry"),element);
		}
	}

	/** 
	 * XMI문서 표준과 상이하여 발생하는 오류항목 제거  
	 * 
	 * @param umlModel
	 * 
	 */
	private void removeError(Element element) {
		// direction
		if (element.hasAttribute("direction")){
			if (element.getAttribute("direction").equals("pk_return"))
				element.setAttribute("direction","return");
			else if (element.getAttribute("direction").equals("pk_in"))
				element.removeAttribute("direction");
		}
		if (element.hasChildNodes()){
			for (int i=0;i< element.getChildNodes().getLength();i++){
				if (element.getChildNodes().item(i) instanceof Element)
					removeError((Element) element.getChildNodes().item(i));
			}
		}
	}

	/* 
	 * XMI 파일 경로를 설정한다.
	 * 
	 * @see egovframework.dev.imp.codegen.model.converter.IConverter#setXMIFilePath(java.lang.String)
	 * 
	 */
	public void setXMIFilePath(String xmiFilePath) {
		super.setXmiFilePath(xmiFilePath);
	}

}

