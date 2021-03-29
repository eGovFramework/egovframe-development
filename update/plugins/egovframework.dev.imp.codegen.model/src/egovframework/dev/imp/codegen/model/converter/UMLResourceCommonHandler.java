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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Extension;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.util.UMLUtil;

import egovframework.dev.imp.codegen.model.util.ModelConvertUtil;

/**
 * UML Resource를 컨트롤하는 클래스  
 * <p><b>NOTE:</b> XMI 파일을 생성하거나 XMI 파일로부터 모델을 변환할 때 필요한 공통 클래스
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
public class UMLResourceCommonHandler {

	/** 로거 */
	protected final Logger log = Logger.getLogger(UMLResourceCommonHandler.class);

	/** UML 팩토리 인스턴스 */
	protected static UMLFactory factory = UMLFactory.eINSTANCE;
	/** 리소스셋 인스턴스 */
	protected static final ResourceSet RESOURCE_SET = new ResourceSetImpl();
	/** 패키지 맵 정보 */
	@SuppressWarnings("rawtypes")
	protected Map packageMap;
	/** 모델 맵 정보 */
	@SuppressWarnings("rawtypes")
	protected Map typeMap;

	/**
	 * 생성자 
	 * 
	 */
	public UMLResourceCommonHandler() {
		this.packageMap = new HashMap<String, Package>();
		this.typeMap = new HashMap<String, DataType>();
	}

	/** 모델 인스턴스 */
	protected Model egovModel;

	/** 모델 가져오기
	 * @return 모델
	 * 
	 */
	public Model getModel() {
		return egovModel;
	}

	/** 모델 설정하기
	 * @param model
	 * 
	 */
	public void setModel(Model model) {
		egovModel = model;
	}

	/** 모델 생성하기
	 * @param name
	 * @return
	 * 
	 */
	protected Model createModel(String name) {
		egovModel = factory.createModel();
		egovModel.setName(name);
		return egovModel;
	}

	/**
	 * 
	 * 리소스 팩토리 설정  
	 * 
	 * 
	 */
	protected static void registerResourceFactories() {
		EPackage.Registry.INSTANCE.put("http://www.eclipse.org/uml2/1.0.0/UML", UMLResource.Factory.INSTANCE);
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("uml2", UMLResource.Factory.INSTANCE);
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", UMLResource.Factory.INSTANCE);
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", UMLResource.Factory.INSTANCE);

		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);
	}

	/** 
	 * 지정된 URI 맵 정보 설정 
	 * 
	 * @param uri
	 * 
	 */
	protected static void registerPathmaps(URI uri) {
		URIConverter.URI_MAP.put(URI.createURI(UMLResource.LIBRARIES_PATHMAP), uri.appendSegment("libraries").appendSegment(""));

		URIConverter.URI_MAP.put(URI.createURI(UMLResource.METAMODELS_PATHMAP), uri.appendSegment("metamodels").appendSegment(""));

		URIConverter.URI_MAP.put(URI.createURI(UMLResource.PROFILES_PATHMAP), uri.appendSegment("profiles").appendSegment(""));
	}

	/** 
	 * 지정된 URI 에 모델 저장 
	 * 
	 * @param package_
	 * @param uri
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void save(org.eclipse.uml2.uml.Package package_, URI uri) {
		XMIResource resource = (XMIResource) RESOURCE_SET.createResource(uri);
		EList contents = resource.getContents();
		contents.add(package_);

		for (Iterator allContents = UMLUtil.getAllContents(package_, true, false); allContents.hasNext();) {

			EObject eObject = (EObject) allContents.next();

			if (eObject instanceof Element) {
				contents.addAll(((Element) eObject).getStereotypeApplications());
			}
		}
		try {
			Map<String, String> options = new HashMap<String, String>();
			options.put(XMIResource.OPTION_ENCODING, "UTF-8");
			resource.save(options);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * 지정된 URI 로부터 모델 가져오기
	 *  
	 * @param uri
	 * @return
	 * 
	 */
	protected static org.eclipse.uml2.uml.Package load(URI uri) {
		org.eclipse.uml2.uml.Package package_ = null;

		try {
			Resource resource = RESOURCE_SET.getResource(uri, true);

			package_ = (org.eclipse.uml2.uml.Package) EcoreUtil.getObjectByType(resource.getContents(), UMLPackage.Literals.PACKAGE);
		} catch (WrappedException we) {
			System.exit(1);
		}

		return package_;
	}

	/** 
	 * 패키지 가져오기 
	 * 
	 * @param fqcn
	 * @return
	 * 
	 */
	@SuppressWarnings("unchecked")
	protected Package getPackage(String fqcn) {
		String[] pkgs = fqcn.split("\\.");
		String pkgName = "";
		Package lastPackage = null;

		if (pkgs.length > 1) {
			for (int i = 0; i < pkgs.length - 1; i++) {
				String p = pkgs[i];
				pkgName += p + ".";
				if (packageMap.get(pkgName) == null) {
					Package pkg = factory.createPackage();
					pkg.setName(p);
					if (lastPackage != null) {
						if (!ModelConvertUtil.isContain(lastPackage, p)) {
							lastPackage.getPackagedElements().add(pkg);
						}
					} else {
						if (!ModelConvertUtil.isContain(egovModel, p)) {
							egovModel.getPackagedElements().add(pkg);
						}
					}
					packageMap.put(pkgName, pkg);
				}

				lastPackage = (Package) packageMap.get(pkgName);
			}
		}
		if (lastPackage != null) {
			return lastPackage;
		} else {
			return egovModel;
		}
	}

	/** 
	 * 스테레오타입 속성값 가져오기 
	 * 
	 * @param namedElement
	 * @param stereotype
	 * @param property
	 * @return
	 * 
	 */
	protected static Object getStereotypePropertyValue(NamedElement namedElement, Stereotype stereotype, Property property) {
		Object value = namedElement.getValue(stereotype, property.getName());

		return value;
	}

	/** 
	 * 스테레오타입 속성값 설정하기
	 *   
	 * @param namedElement
	 * @param stereotype
	 * @param property
	 * @param value
	 * 
	 */
	protected static void setStereotypePropertyValue(NamedElement namedElement, Stereotype stereotype, Property property, Object value) {
		namedElement.setValue(stereotype, property.getName(), value);
	}

	/**
	 *  generalization 관계를 설정함. 
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

	/**
	 *  속성을 생성함. 
	 * 
	 * @param class_
	 * @param name
	 * @param type
	 * @param lowerBound
	 * @param upperBound
	 * @return
	 * 
	 */
	protected static Property createAttribute(org.eclipse.uml2.uml.Class class_, String name, Type type, int lowerBound, int upperBound) {
		Property attribute = class_.createOwnedAttribute(name, type, lowerBound, upperBound);

		return attribute;
	}

	/**
	 *  Enumeration 을 생성함. 
	 * 
	 * @param package_
	 * @param name
	 * @return
	 * 
	 */
	protected static Enumeration createEnumeration(org.eclipse.uml2.uml.Package package_, String name) {
		Enumeration enumeration = (Enumeration) package_.createOwnedEnumeration(name);

		return enumeration;
	}

	/**
	 *  Enumeration Literal을 생성함. 
	 * 
	 * @param enumeration
	 * @param name
	 * @return
	 * 
	 */
	protected static EnumerationLiteral createEnumerationLiteral(Enumeration enumeration, String name) {
		EnumerationLiteral enumerationLiteral = enumeration.createOwnedLiteral(name);

		return enumerationLiteral;
	}

	/**
	 *  import 된 기본 데이타 타입을 가져옴. 
	 * 
	 * @param package_
	 * @param name
	 * @return
	 * 
	 */
	protected static PrimitiveType importPrimitiveType(org.eclipse.uml2.uml.Package package_, String name) {
		Model umlLibrary = (Model) load(URI.createURI(UMLResource.UML_PRIMITIVE_TYPES_LIBRARY_URI));

		PrimitiveType primitiveType = (PrimitiveType) umlLibrary.getOwnedType(name);

		package_.createElementImport(primitiveType);

		return primitiveType;
	}

	/**
	 *  Extension 을 생성함. 
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

	/**
	 *  프로필을 정의함. 
	 * 
	 * @param profile
	 * 
	 */
	protected static void defineProfile(Profile profile) {
		profile.define();
	}

	/**
	 *  프로필을 적용함. 
	 * 
	 * @param package_
	 * @param profile
	 * 
	 */
	protected static void applyProfile(org.eclipse.uml2.uml.Package package_, Profile profile) {
		package_.applyProfile(profile);

	}

	/**
	 *  스테레오 타입을 적용함. 
	 * 
	 * @param namedElement
	 * @param stereotype
	 * 
	 */
	protected static void applyStereotype(NamedElement namedElement, Stereotype stereotype) {
		for (int i = 0; i < namedElement.getAppliedStereotypes().size(); i++) {
			if (namedElement.getAppliedStereotypes().get(i).getName().equals(stereotype.getName())) {
				return;
			}
		}
		namedElement.applyStereotype(stereotype);
	}

	/**
	 *  Enumeration 을 생성함. 
	 * 
	 * @param profile
	 * @return
	 * 
	 */
	protected Profile createEnumeration(Profile profile) {

		Enumeration visibilityKindEnumeration = createEnumeration(profile, "VisibilityKind");
		Enumeration featureKindEnumeration = createEnumeration(profile, "FeatureKind");

		createEnumerationLiteral(visibilityKindEnumeration, "Unspecified");
		createEnumerationLiteral(visibilityKindEnumeration, "None");
		createEnumerationLiteral(visibilityKindEnumeration, "ReadOnly");
		createEnumerationLiteral(visibilityKindEnumeration, "ReadWrite");
		createEnumerationLiteral(visibilityKindEnumeration, "ReadOnlyUnsettable");
		createEnumerationLiteral(visibilityKindEnumeration, "ReadWriteUnsettable");

		createEnumerationLiteral(featureKindEnumeration, "Unspecified");
		createEnumerationLiteral(featureKindEnumeration, "Simple");
		createEnumerationLiteral(featureKindEnumeration, "Attribute");
		createEnumerationLiteral(featureKindEnumeration, "Element");
		createEnumerationLiteral(featureKindEnumeration, "AttributeWildcard");
		createEnumerationLiteral(featureKindEnumeration, "ElementWildcard");
		createEnumerationLiteral(featureKindEnumeration, "Group");

		return null;
	}

	/**
	 *  데이타타입을 생성함. 
	 * 
	 * @param typeName
	 * @return
	 * 
	 */
	@SuppressWarnings("unchecked")
	protected Type createDataType(String typeName) {
		String simpleName = ModelConvertUtil.getSimpleName(typeName);
		DataType type = factory.createDataType();
		type.setName(simpleName);
		Package package1 = getPackage(typeName);
		package1.getPackagedElements().add(type);
		typeMap.put(typeName, type);
		return type;
	}

	/**
	 *  모델에 프로필을 적용함. 
	 * 
	 * @param model
	 * @param profile
	 * 
	 */
	public void applyingProfile(Model model, Profile profile) {

		applyProfile(model, profile);
	}

	/**
	 *  속성을 추가함. 
	 * 
	 * @param properties
	 * @param property
	 * 
	 */
	protected void addAttributes(EList<Property> properties, Property property) {
		for (int i = 0; i < properties.size(); i++) {
			if (properties.get(i).getName().equals(property.getName())) {
				properties.get(i).setVisibility(property.getVisibility());
				properties.get(i).setIsStatic(property.isStatic());
				return;
			}
		}
		properties.add(property);
	}

	/**
	 *  프로필을 생성함. 
	 * 
	 * @param name
	 * @return
	 * 
	 */
	protected static Profile createProfile(String name) {
		Profile profile = factory.createProfile();
		profile.setName(name);
		return profile;
	}

	/**
	 *  스테레오타입을 생성함. 
	 * 
	 * @param profile
	 * @param name
	 * @param isAbstract
	 * @return
	 * 
	 */
	protected Stereotype createStereotype(Profile profile, String name, boolean isAbstract) {
		Stereotype stereotype = profile.createOwnedStereotype(name, isAbstract);
		return stereotype;
	}

	/**
	 *  참조 메타 클래스 정보를 설정함. 
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

}