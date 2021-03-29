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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

import org.apache.xerces.parsers.DOMParser;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Extension;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.internal.impl.ClassImpl;
import org.eclipse.uml2.uml.internal.impl.DataTypeImpl;
import org.eclipse.uml2.uml.internal.impl.InterfaceImpl;
import org.eclipse.uml2.uml.internal.impl.PackageImpl;
import org.eclipse.uml2.uml.internal.impl.PrimitiveTypeImpl;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * UML 모델을 변환하기 위한 공통 사용 클래스
 * <p><b>NOTE:</b> UML 모델을 변환에 필요한 공통 메소드 및 변수를 정의함.
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
public class UMLModelConverter extends UMLResourceCommonHandler {

	/** 클래스 변환 목록	 * 
	 */
	@SuppressWarnings("rawtypes")
	protected final Map target = new TreeMap();

	/** XMI 파일 경로 */
	protected String xmiFilePath;
	/** UML 팩토리 인스턴스 */
	protected static UMLFactory factory = UMLFactory.eINSTANCE;

	/** XMI ID 속성명 */
	protected static final String XMI_ID = "xmi:id";
	/** XMI ID와 객체명 매핑정보 */
	protected final HashMap<String, String> IDsToObjectNames;
	/** 객체명과 객체 매핑정보 */
	protected final HashMap<?, ?> NamesToObjects;
	/** 스테레오타입 목록 */
	protected final HashMap<String, Object> StereotypeList;
	/** 스테레오타입이 적용된 클래스 목록 */
	@SuppressWarnings("rawtypes")
	protected final HashMap StereotypeClassesList;
	/** 사용된 스테레오타입명 목록 */
	protected final HashSet<?> usedStereotypeNameList;

	/** XMI ID와 객체 매핑정보 */
	protected Map<?, ?> IDsToObjects;
	/** 속성에 대한 메타 클래스 인스턴스*/
	private org.eclipse.uml2.uml.Class propertyMetaclass = null;

	/**
	 * 클래스 목록 정보를 반환.
	 *
	 * @return 클래스 목록 맵
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Map<?, Object> getTarget() {
		return target;
	}

	/**
	 * 스테레오타입이 적용된 클래스 목록 정보를 반환함. 
	 * 
	 * @return 스테레오타입이 적용된 클래스 목록
	 * 
	 */
	public Map<?, ?> getStereotypeClasses() {
		return StereotypeClassesList;
	}

	/**
	 * 생성자 
	 * 
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public UMLModelConverter() {
		registerResourceFactories();
		StereotypeList = new HashMap<String, Object>();
		IDsToObjectNames = new HashMap<String, String>();
		NamesToObjects = new HashMap();
		StereotypeClassesList = new HashMap();
		usedStereotypeNameList = new HashSet();
	}

	/**
	 * XMI 파일 경로를 설정함. 
	 * 
	 * @param xmiFilePath
	 * 
	 */
	public void setXmiFilePath(String xmiFilePath) {
		this.xmiFilePath = xmiFilePath;
	}

	/**
	 * XMI 객체 목록을 생성함. 
	 *  
	 */
	public void makeXMIObjectClassList() {
		FileReader reader = null;
		try {
			reader = new FileReader(new File(xmiFilePath));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		InputSource inputSource = new InputSource(reader);
		inputSource.setEncoding("UTF-8");
		DOMParser parser = new DOMParser();
		try {
			parser.parse(inputSource);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Document d = parser.getDocument();
		if (d.getDocumentElement() != null && d.getDocumentElement().hasChildNodes()) {
			setIDsToObjectNames(d.getDocumentElement().getChildNodes());
		}
	}

	/**
	 * 객체ID와 객체명 매핑 정보를 생성함. 
	 * 
	 * @param nodelist
	 */
	public void setIDsToObjectNames(NodeList nodelist) {
		String xmi_id = "";
		String name = "";

		if (nodelist != null)
			for (int i = 0; i < nodelist.getLength(); i++) {
				if (nodelist.item(i).hasAttributes() && nodelist.item(i).getAttributes().getNamedItem(XMI_ID) != null)
					xmi_id = nodelist.item(i).getAttributes().getNamedItem(XMI_ID).getNodeValue();
				if (nodelist.item(i).hasAttributes() && nodelist.item(i).getAttributes().getNamedItem("name") != null)
					name = nodelist.item(i).getAttributes().getNamedItem("name").getNodeValue();
				if (xmi_id != null && !xmi_id.equals("") && name != null && !name.equals(""))
					IDsToObjectNames.put(xmi_id, name);
				if (nodelist.item(i).hasChildNodes()) {
					setIDsToObjectNames(nodelist.item(i).getChildNodes());
				}
			}
	}

	/* 
	 * 모델을 설정함. 
	 * 
	 * @see egovframework.dev.imp.codegen.model.converter.UMLResourceCommonHandler#setModel(org.eclipse.uml2.uml.Model)
	 */
	public void setModel(Model model) {
		egovModel = model;
	}

	/**
	 * 기본 데이타타입을 변환함. 
	 * 
	 * @param package_
	 * @param name
	 * @return
	 * 
	 */
	protected static org.eclipse.uml2.uml.PrimitiveType createPrimitiveType(org.eclipse.uml2.uml.Package package_, String name) {
		org.eclipse.uml2.uml.PrimitiveType primitiveType = (org.eclipse.uml2.uml.PrimitiveType) package_.createOwnedPrimitiveType(name);

		return primitiveType;
	}

	/** 
	 * 
	 * 경로맵 정보를 레지스트리에 등록함. 
	 * 
	 * @param uri
	 */
	protected static void registerPathmaps(URI uri) {
		URIConverter.URI_MAP.put(URI.createURI(UMLResource.LIBRARIES_PATHMAP), uri.appendSegment("libraries").appendSegment(""));

		URIConverter.URI_MAP.put(URI.createURI(UMLResource.METAMODELS_PATHMAP), uri.appendSegment("metamodels").appendSegment(""));

		URIConverter.URI_MAP.put(URI.createURI(UMLResource.PROFILES_PATHMAP), uri.appendSegment("profiles").appendSegment(""));
	}

	/** 
	 * 
	 * Generation관계를 생성함. 
	 * 
	 * @param specificClassifier
	 * @param generalClassifier
	 * @return
	 * 
	 */
	protected static Generalization createGeneralization(Classifier specificClassifier, Classifier generalClassifier) {
		Generalization generalization = specificClassifier.createGeneralization(generalClassifier);

		return generalization;
	}

	/** 스테레오타입명에 해당하는 스테레오타입 모델을 가져옴.  
	 * @param stereotype_name
	 * @return
	 */
	protected Stereotype getStereoType(String stereotype_name) {
		Stereotype stereoType = (Stereotype) StereotypeList.get(stereotype_name);
		if (stereoType == null) {
			Profile profile = egovModel.getAppliedProfiles().get(0);
			stereoType = createStereotype(profile, stereotype_name, true);
		}
		return stereoType;
	}

	/** 프로필 인스턴스 */
	protected Profile egovProfile = null;

	/**
	 *  변환할 클래스 목록을 생성함. 
	 * 
	 * @param parentPackageName
	 * @param package_
	 */
	@SuppressWarnings("unchecked")
	public void getClasses(String parentPackageName, org.eclipse.uml2.uml.Package package_) {
		String packageName = parentPackageName + "." + package_.getName();
		for (int i = 0; i < package_.getPackagedElements().size(); i++) {
			Object child = package_.getPackagedElements().get(i);
			if (child instanceof ClassImpl) {
				this.target.put(packageName + "." + ((org.eclipse.uml2.uml.Class) child).getName(), child);
			}
			if (child instanceof InterfaceImpl) {
				this.target.put(packageName + "." + ((org.eclipse.uml2.uml.Interface) child).getName(), child);
			}
			if (!(child instanceof PrimitiveTypeImpl) && child instanceof DataTypeImpl) {
				this.target.put(packageName + "." + ((org.eclipse.uml2.uml.DataType) child).getName(), child);
			}
			if (child instanceof PackageImpl) {
				getClasses(packageName, (org.eclipse.uml2.uml.Package) child);
			}
		}
	}

	/**
	 * 패키지에 속한 스테레오 타입 정보를 가져옴. 
	 * 
	 * @param package_
	 */
	public void getStereoTypes(org.eclipse.uml2.uml.Package package_) {
		for (int i = 0; i < package_.getPackagedElements().size(); i++) {
			Object child = package_.getPackagedElements().get(i);
			if (child instanceof Stereotype) {
				this.StereotypeList.put(((org.eclipse.uml2.uml.Stereotype) child).getName(), child);
			}
		}
	}

	/**
	 * 
	 * 기초 프로파일을 생성함.  
	 * 
	 * 
	 */
	@SuppressWarnings("unused")
	public void makeBaseProfile() {

		registerResourceFactories();

		egovProfile = createProfile("egov");

		PrimitiveType booleanPrimitiveType = importPrimitiveType(egovProfile, "Boolean");
		PrimitiveType stringPrimitiveType = importPrimitiveType(egovProfile, "String");

		createEnumeration(egovProfile);

		propertyMetaclass = referenceMetaclass(egovProfile, UMLPackage.Literals.PROPERTY.getName());
	}

	/**
	 * 참조할 메타 클래스 정보를 생성함.  
	 * 
	 * @param profile
	 * @param name
	 * @return
	 * 
	 */
	protected static org.eclipse.uml2.uml.Class referenceMetaclass(Profile profile, String name) {
		Model umlMetamodel = (Model) load(URI.createURI(UMLResource.UML_METAMODEL_URI));

		org.eclipse.uml2.uml.Class metaclass = (org.eclipse.uml2.uml.Class) umlMetamodel.getOwnedType(name);

		profile.createMetaclassReference(metaclass);

		return metaclass;
	}

	/** 
	 * 스테레오 타입을 추가함. 
	 *
	 * @param profile
	 * @param stereotypes
	 * @param name
	 * @param strKind
	 * 
	 */
	protected void addStereotype(Profile profile, EList<Stereotype> stereotypes, String name, String strKind) {
		for (int i = 0; i < stereotypes.size(); i++) {
			if (stereotypes.get(i).getName().equals(name)) {
				return;
			}
		}
		Stereotype stereotype = createStereotype(profile, name, false);
		if (strKind.equals("class")) {
			Profile EcoreProfile = (Profile) load(URI.createURI(UMLResource.ECORE_PROFILE_NS_URI));

			Stereotype EcoreClassStereotype = (Stereotype) EcoreProfile.getOwnedStereotype("EClass");

			createGeneralization(stereotype, EcoreClassStereotype);
			createExtension(propertyMetaclass, stereotype, false);

		}
	}

	/**
	 *  Extension 정보를 생성함. 
	 * 
	 * @param metaclass
	 * @param stereotype
	 * @param required
	 * @return
	 * 
	 */
	protected static Extension createExtension(org.eclipse.uml2.uml.Class metaclass, Stereotype stereotype, boolean required) {
		Extension extension = stereotype.createExtension(metaclass, required);

		return extension;
	}
}
