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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.java.amateras.uml.classdiagram.model.AggregationModel;
import net.java.amateras.uml.classdiagram.model.Argument;
import net.java.amateras.uml.classdiagram.model.AssociationModel;
import net.java.amateras.uml.classdiagram.model.AttributeModel;
import net.java.amateras.uml.classdiagram.model.ClassModel;
import net.java.amateras.uml.classdiagram.model.CompositeModel;
import net.java.amateras.uml.classdiagram.model.DependencyModel;
import net.java.amateras.uml.classdiagram.model.GeneralizationModel;
import net.java.amateras.uml.classdiagram.model.InterfaceModel;
import net.java.amateras.uml.classdiagram.model.OperationModel;
import net.java.amateras.uml.classdiagram.model.RealizationModel;
import net.java.amateras.uml.model.AbstractUMLConnectionModel;
import net.java.amateras.uml.model.AbstractUMLEntityModel;
import net.java.amateras.uml.model.AbstractUMLModel;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
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
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;

import egovframework.dev.imp.codegen.model.util.CommonUtil;
import egovframework.dev.imp.codegen.model.util.ModelConvertUtil;

/**
 * XMI 파일로 EXPORT 하는 객체
 * <p>
 * <b>NOTE:</b> 구현도구에서 작성한 클래스 다이어그램을 XMI 파일로 EXPORT 하여 타 모델링 툴과 호환되도록 함.
 * 
 * @author 개발환경 개발팀 윤수열
 * @since 2009.08.10
 * @version 1.0
 * @see <pre>
 *  == 개정이력(Modification Information) ==
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.08.10  윤수열          최초 생성
 * 
 * </pre>
 */
public class XMIFileExporter extends UMLResourceCommonHandler {

	/**
	 * 
	 * 생성자
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public XMIFileExporter() {
		registerResourceFactories();
		egovModel = createModel("eGovFrameXMIOutput");

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

		typeMap.put("short", shortType);
		typeMap.put("int", intType);
		typeMap.put("long", longType);
		typeMap.put("float", floatType);
		typeMap.put("double", doubleType);
		typeMap.put("boolean", booleanType);
		typeMap.put("char", charType);
		typeMap.put("String", stringType);

		egovModel.getPackagedElements().add(booleanType);
		egovModel.getPackagedElements().add(intType);
		egovModel.getPackagedElements().add(shortType);
		egovModel.getPackagedElements().add(longType);
		egovModel.getPackagedElements().add(floatType);
		egovModel.getPackagedElements().add(doubleType);
		egovModel.getPackagedElements().add(charType);
		egovModel.getPackagedElements().add(stringType);
	}

	/**
	 * 타입 정보를 변환함.
	 * 
	 * @param model
	 * 
	 */
	public void convertType(AbstractUMLEntityModel model) {
		if (model instanceof ClassModel) {
			createClass((ClassModel) model);
		} else if (model instanceof InterfaceModel) {
			createInterface((InterfaceModel) model);
		}
	}

	/**
	 * 모델 내부 스트럭쳐를 변환함.
	 * 
	 * @param model
	 * 
	 */
	public void convertStructure(AbstractUMLEntityModel model) {
		addAttributes(model);
		addOperations(model);
	}

	/**
	 * 관계정보를 변환함.
	 * 
	 * @param model
	 * 
	 */
	public void convertLink(AbstractUMLEntityModel model) {
		createGeneralization(model);
		createRealization(model);
		createComposite(model);
		createAssociation(model);
		createDependency(model);
	}

	/**
	 * 스테레오타입을 프로필에 추가.
	 * 
	 * @param profile
	 * @param model
	 * 
	 */
	public void addStereotypeToProfile(Profile profile, AbstractUMLEntityModel model) {
		createClassStereoType(profile, model);
		createAssociationStereoType(profile, model);
	}

	/**
	 * 스테레오 타입을 클래스에 적용함.
	 * 
	 * @param profile
	 * @param model
	 * 
	 */
	public void applyClassStereoType(Profile profile, AbstractUMLEntityModel model) {
		Classifier classifier = (Classifier) typeMap.get(ModelConvertUtil.getName(model));

		String stereotypeName = "";
		if (model instanceof ClassModel) {
			stereotypeName = ((ClassModel) model).getStereoType();
		} else if (model instanceof InterfaceModel) {
			stereotypeName = ((InterfaceModel) model).getStereoType();
		}
		if (stereotypeName != null && !stereotypeName.equals("")) {
			applyStereotype(profile, classifier, stereotypeName, "class");
		}
	}

	/**
	 * 클래스에 대한 스테레오타입을 생성함.
	 * 
	 * @param profile
	 * @param model
	 * 
	 */
	@SuppressWarnings("unused")
	private void createClassStereoType(Profile profile, AbstractUMLEntityModel model) {
		Classifier classifier = (Classifier) typeMap.get(ModelConvertUtil.getName(model));
		String stereotypeName = "";
		if (model instanceof ClassModel) {
			stereotypeName = ((ClassModel) model).getStereoType();
		} else if (model instanceof InterfaceModel) {
			stereotypeName = ((InterfaceModel) model).getStereoType();
		}
		if (stereotypeName != null && !stereotypeName.equals("")) {
			addStereotype(profile, profile.getOwnedStereotypes(), stereotypeName, "class");
		}
	}

	/**
	 * Association 에 대한 스테레오 타입을 생성함.
	 * 
	 * @param profile
	 * @param model
	 * 
	 */
	@SuppressWarnings("unused")
	private void createAssociationStereoType(Profile profile, AbstractUMLEntityModel model) {
		Classifier source = (Classifier) typeMap.get(ModelConvertUtil.getName(model));
		List<?> connections = model.getModelSourceConnections();
		String stereotypeName = "";
		for (Iterator<?> iter = connections.iterator(); iter.hasNext();) {
			AbstractUMLConnectionModel element = (AbstractUMLConnectionModel) iter.next();
			if (element instanceof AssociationModel) {
				stereotypeName = ((AssociationModel) element).getStereoType();
				if (stereotypeName != null && !stereotypeName.equals(""))
					addStereotype(profile, profile.getOwnedStereotypes(), stereotypeName, "class");
			} else if (element instanceof CompositeModel) {
				stereotypeName = ((CompositeModel) element).getStereoType();
				if (stereotypeName != null && !stereotypeName.equals(""))
					addStereotype(profile, profile.getOwnedStereotypes(), stereotypeName, "class");
			} else if (element instanceof AggregationModel) {
				stereotypeName = ((AggregationModel) element).getStereoType();
				if (stereotypeName != null && !stereotypeName.equals(""))
					addStereotype(profile, profile.getOwnedStereotypes(), stereotypeName, "class");
			}
		}
	}

	/**
	 * 스테레오 타입을 추가
	 * 
	 * @param profile
	 * @param stereotypes
	 * @param name
	 * @param strKind
	 * 
	 */
	private void addStereotype(Profile profile, EList<Stereotype> stereotypes, String name, String strKind) {
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
	 * 스테레오 타입을 적용함.
	 * 
	 * @param profile
	 * @param classifier
	 * @param name
	 * @param strKind
	 * 
	 */
	private void applyStereotype(Profile profile, Classifier classifier, String name, String strKind) {
		Stereotype stereotype = null;
		if (strKind.equals("class")) {

			stereotype = profile.getOwnedStereotype(name);
			applyStereotype(classifier, stereotype);
		}
	}

	/**
	 * 클래스를 생성함.
	 * 
	 * @param model
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void createClass(ClassModel model) {
		Class clazz = factory.createClass();
		clazz.setName(ModelConvertUtil.getSimpleName(model.getName()));
		clazz.setIsAbstract(model.isAbstract());
		Package pkg = getPackage(model.getName());
		pkg.getPackagedElements().add(clazz);
		typeMap.put(model.getName(), clazz);
	}

	/**
	 * 클래스를 생성함.
	 * 
	 * @param package_
	 * @param name
	 * @param isAbstract
	 * @return
	 */
	protected static org.eclipse.uml2.uml.Class createClass(org.eclipse.uml2.uml.Package package_, String name, boolean isAbstract) {
		org.eclipse.uml2.uml.Class class_ = package_.createOwnedClass(name, isAbstract);

		return class_;
	}

	/**
	 * 인터페이스를 생성함.
	 * 
	 * @param model
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void createInterface(InterfaceModel model) {
		Interface interface1 = factory.createInterface();
		interface1.setName(ModelConvertUtil.getSimpleName(model.getName()));
		Package pkg = getPackage(model.getName());
		pkg.getPackagedElements().add(interface1);
		typeMap.put(model.getName(), interface1);
	}

	/**
	 * 속성을 추가함.
	 * 
	 * @param model
	 * 
	 */
	private void addAttributes(AbstractUMLEntityModel model) {

		List<AbstractUMLModel> attrs = getAttributes(model);
		Classifier classifier = (Classifier) typeMap.get(ModelConvertUtil.getName(model));

		for (Iterator<AbstractUMLModel> iter = attrs.iterator(); iter.hasNext();) {
			AttributeModel element = (AttributeModel) iter.next();
			Property property = factory.createProperty();

			property.setName(element.getName());
			if (typeMap.get(element.getType()) == null) {
				createDataType(element.getType());
			}
			Type t = (Type) typeMap.get(element.getType());
			property.setType(t);
			property.setVisibility(ModelConvertUtil.getVisibility(element.getVisibility()));
			property.setIsStatic(element.isStatic());

			if (classifier instanceof Class) {
				Class clazz = (Class) classifier;
				addAttributes(clazz.getOwnedAttributes(), property);
			} else if (classifier instanceof Interface) {
				Interface interface1 = (Interface) classifier;
				addAttributes(interface1.getOwnedAttributes(), property);
			}
		}
	}

	/**
	 * 오퍼레이션을 추가.
	 * 
	 * @param model
	 * 
	 */
	private void addOperations(AbstractUMLEntityModel model) {
		List<AbstractUMLModel> opes = getOperations(model);
		Classifier classifier = (Classifier) typeMap.get(ModelConvertUtil.getName(model));

		for (Iterator<AbstractUMLModel> iter = opes.iterator(); iter.hasNext();) {
			OperationModel element = (OperationModel) iter.next();
			Operation operation = factory.createOperation();
			operation.setName(element.getName());
			operation.setVisibility(ModelConvertUtil.getVisibility(element.getVisibility()));
			operation.setIsStatic(element.isStatic());
			operation.setIsAbstract(element.isAbstract());
			if (element.getType() != null && !"void".equals(element.getType())) {
				Parameter parameter = factory.createParameter();
				parameter.setName("return");
				Type t = (Type) typeMap.get(element.getType());
				if (t == null) {
					t = createDataType(element.getType());
				}
				parameter.setType(t);
				parameter.setDirection(ParameterDirectionKind.get(ParameterDirectionKind.RETURN));
				operation.getOwnedParameters().add(parameter);
			}

			List<?> params = element.getParams();
			for (Iterator<?> iterator = params.iterator(); iterator.hasNext();) {
				Argument arg = (Argument) iterator.next();
				Parameter parameter = factory.createParameter();
				parameter.setName(arg.getName());
				Type t = (Type) typeMap.get(arg.getType());
				if (t == null) {
					t = createDataType(arg.getType());
				}
				parameter.setType(t);
				operation.getOwnedParameters().add(parameter);
			}

			if (classifier instanceof Class) {
				Class class1 = (Class) classifier;
				class1.getOwnedOperations().add(operation);
			} else if (classifier instanceof Interface) {
				Interface interface1 = (Interface) classifier;
				interface1.getOwnedOperations().add(operation);
			}
		}
	}

	/**
	 * 속성 정보 목록을 가져옴.
	 * 
	 * @param model
	 * @return
	 * 
	 */
	private List<AbstractUMLModel> getAttributes(AbstractUMLEntityModel model) {
		List<?> list = model.getChildren();
		List<AbstractUMLModel> rv = new ArrayList<AbstractUMLModel>();
		for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
			AbstractUMLModel element = (AbstractUMLModel) iter.next();
			if (element instanceof AttributeModel) {
				rv.add(element);
			}
		}
		return rv;
	}

	/**
	 * 오퍼레이션 정보 목록을 가져옴.
	 * 
	 * @param model
	 * @return
	 * 
	 */
	private List<AbstractUMLModel> getOperations(AbstractUMLEntityModel model) {
		List<?> list = model.getChildren();
		List<AbstractUMLModel> rv = new ArrayList<AbstractUMLModel>();
		for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
			AbstractUMLModel element = (AbstractUMLModel) iter.next();
			if (element instanceof OperationModel) {
				rv.add(element);
			}
		}
		return rv;
	}

	/**
	 * Generalization 관계를 생성함.
	 * 
	 * @param model
	 * 
	 */
	private void createGeneralization(AbstractUMLEntityModel model) {
		Classifier source = (Classifier) typeMap.get(ModelConvertUtil.getName(model));
		List<?> connections = model.getModelSourceConnections();
		for (Iterator<?> iter = connections.iterator(); iter.hasNext();) {
			AbstractUMLConnectionModel element = (AbstractUMLConnectionModel) iter.next();
			if (element instanceof GeneralizationModel) {
				Generalization association = factory.createGeneralization();
				association.setSpecific(source);
				Classifier target = (Classifier) typeMap.get(ModelConvertUtil.getName(element.getTarget()));
				association.setGeneral(target);
			}
		}
	}

	/**
	 * Realization 관계를 생성함.
	 * 
	 * @param model
	 * 
	 */
	private void createRealization(AbstractUMLEntityModel model) {
		Classifier source = (Classifier) typeMap.get(ModelConvertUtil.getName(model));
		// if (!(source instanceof Interface)) {
		// return;
		// }
		List<?> connections = model.getModelTargetConnections();
		for (Iterator<?> iter = connections.iterator(); iter.hasNext();) {
			AbstractUMLConnectionModel element = (AbstractUMLConnectionModel) iter.next();
			if (element instanceof RealizationModel) {
				InterfaceRealization realization = factory.createInterfaceRealization();
				realization.setContract((Interface) source);
				Classifier target = (Classifier) typeMap.get(ModelConvertUtil.getName(element.getSource()));
				if (target instanceof BehavioredClassifier) {
					realization.setImplementingClassifier((BehavioredClassifier) target);
				}
			}
		}
	}

	/**
	 * Composition 관계를 생성함.
	 * 
	 * @param model
	 * 
	 */
	@SuppressWarnings("unused")
	private void createComposite(AbstractUMLEntityModel model) {
		Classifier source = (Classifier) typeMap.get(ModelConvertUtil.getName(model));
		List<?> connections = model.getModelSourceConnections();
		for (Iterator<?> iter = connections.iterator(); iter.hasNext();) {
			AbstractUMLConnectionModel element = (AbstractUMLConnectionModel) iter.next();
			AggregationKind aggregationKind = null;
			if (element instanceof CompositeModel || element instanceof AggregationModel) {
				if (element instanceof CompositeModel) {
					aggregationKind = AggregationKind.COMPOSITE_LITERAL;
				} else if (element instanceof AggregationModel) {
					aggregationKind = AggregationKind.SHARED_LITERAL;
				}
				int[] fromMultiplicity = ModelConvertUtil.getMultiplicity(((AssociationModel) element).getFromMultiplicity());
				int[] toMultiplicity = ModelConvertUtil.getMultiplicity(((AssociationModel) element).getToMultiplicity());

				Classifier classifier = (Classifier) typeMap.get(ModelConvertUtil.getName(element.getTarget()));
				List<?> attrs = getAttributes(element.getTarget());
				for (Iterator<?> iter2 = attrs.iterator(); iter2.hasNext();) {
					AttributeModel attribute = (AttributeModel) iter2.next();
					if (attribute.getType().equals(ModelConvertUtil.getName(element.getSource()))) {
						Association association = classifier.createAssociation(true, aggregationKind, attribute.getName(), fromMultiplicity[0], fromMultiplicity[1],
								(Classifier) typeMap.get(ModelConvertUtil.getName(model)), false, AggregationKind.NONE_LITERAL, "", toMultiplicity[0], toMultiplicity[1]);
						association.setName("A_" + ModelConvertUtil.getSimpleName(classifier.getName()) + "_" + ModelConvertUtil.getSimpleName(ModelConvertUtil.getName(model)));
					}

				}
			}

		}
	}

	/**
	 * Association 관계를 생성함.
	 * 
	 * @param model
	 * 
	 */
	@SuppressWarnings("unused")
	private void createAssociation(AbstractUMLEntityModel model) {
		Classifier source = (Classifier) typeMap.get(ModelConvertUtil.getName(model));

		List<?> connections = model.getModelSourceConnections();
		for (Iterator<?> iter = connections.iterator(); iter.hasNext();) {
			AbstractUMLConnectionModel element = (AbstractUMLConnectionModel) iter.next();
			AggregationKind aggregationKind = null;
			if (element instanceof AssociationModel && !(element instanceof CompositeModel || element instanceof AggregationModel)) {
				aggregationKind = AggregationKind.NONE_LITERAL;
				int[] fromMultiplicity = ModelConvertUtil.getMultiplicity(((AssociationModel) element).getFromMultiplicity());
				int[] toMultiplicity = ModelConvertUtil.getMultiplicity(((AssociationModel) element).getToMultiplicity());

				Classifier classifier = (Classifier) typeMap.get(ModelConvertUtil.getName(element.getTarget()));

				List<?> attrs = getAttributes(element.getTarget());

				String AttributeName1 = "";
				String AttributeName2 = "";

				for (Iterator<?> iter2 = attrs.iterator(); iter2.hasNext();) {
					AttributeModel attribute = (AttributeModel) iter2.next();
					if (attribute.getType().equals(ModelConvertUtil.getName(element.getSource()))) {
						AttributeName1 = attribute.getName();
						break;
					}
				}
				List<?> attrs2 = getAttributes(element.getSource());
				for (Iterator<?> iter3 = attrs2.iterator(); iter3.hasNext();) {
					AttributeModel attribute = (AttributeModel) iter3.next();
					if (attribute.getType().equals(ModelConvertUtil.getName(element.getTarget()))) {
						AttributeName2 = attribute.getName();
						break;
					}
				}
				if (AttributeName1.equals(""))
					AttributeName1 = CommonUtil.firstCharToLowerCase(ModelConvertUtil.getSimpleName(ModelConvertUtil.getName(element.getSource())));
				if (AttributeName2.equals(""))
					AttributeName2 = CommonUtil.firstCharToLowerCase(ModelConvertUtil.getSimpleName(ModelConvertUtil.getName(element.getTarget())));

				Association association = classifier.createAssociation(true, AggregationKind.NONE_LITERAL, AttributeName1, fromMultiplicity[0], fromMultiplicity[1],
						(Classifier) typeMap.get(ModelConvertUtil.getName(model)), true, AggregationKind.NONE_LITERAL, AttributeName2, toMultiplicity[0], toMultiplicity[1]);
				association.setName("A_" + ModelConvertUtil.getSimpleName(classifier.getName()) + "_" + ModelConvertUtil.getSimpleName(ModelConvertUtil.getName(model)));
			}
		}
	}

	/**
	 * Assocication 관계정보를 검색하여 가져오기
	 * 
	 * @param classifier
	 * @param name
	 * @return
	 * 
	 */
	@SuppressWarnings("null")
	private Association findAssociation(Classifier classifier, String name) {
		for (int i = 0; i < classifier.getRelationships().size(); i++) {
			if (classifier.getRelationships().get(i) instanceof Association) {
				if (name != null || !name.equals("") || ((Association) classifier.getRelationships().get(i)).getName().equals(name))
					return (Association) classifier.getRelationships().get(i);
			}
		}
		return null;
	}

	/**
	 * Assocication에 스테레오 타입 적용하기
	 * 
	 * @param profile
	 * @param model
	 * 
	 */
	public void applyAssociationStereoType(Profile profile, AbstractUMLEntityModel model) {
		Classifier source = (Classifier) typeMap.get(ModelConvertUtil.getName(model));

		List<?> connections = model.getModelSourceConnections();
		String stereotypeName = "";
		for (Iterator<?> iter = connections.iterator(); iter.hasNext();) {
			AbstractUMLConnectionModel element = (AbstractUMLConnectionModel) iter.next();
			if (element instanceof AssociationModel && !(element instanceof CompositeModel || element instanceof AggregationModel)) {
				stereotypeName = ((AssociationModel) element).getStereoType();
				if (stereotypeName != null && !stereotypeName.equals("")) {
					Stereotype stereotype = profile.getOwnedStereotype(stereotypeName);
					applyStereotype(findAssociation(source, "A_" + ModelConvertUtil.getName(element.getTarget()) + "_" + source.getName()), stereotype);
				}
			} else if (element instanceof CompositeModel) {
				stereotypeName = ((CompositeModel) element).getStereoType();
				if (stereotypeName != null && !stereotypeName.equals("")) {
					Stereotype stereotype = profile.getOwnedStereotype(stereotypeName);
					for (int i = 0; i < source.getRelationships().size(); i++) {
						if (source.getRelationships().get(i) instanceof Association) {
							if (((Association) source.getRelationships().get(i)).getName().equals("A_" + ModelConvertUtil.getName(element.getTarget()) + "_" + source.getName()))
								applyStereotype((Association) source.getRelationships().get(i), stereotype);
						}
					}
				}
			} else if (element instanceof AggregationModel) {
				stereotypeName = ((AggregationModel) element).getStereoType();
				if (stereotypeName != null && !stereotypeName.equals("")) {
					Stereotype stereotype = profile.getOwnedStereotype(stereotypeName);
					for (int i = 0; i < source.getRelationships().size(); i++) {
						if (source.getRelationships().get(i) instanceof Association) {
							if (((Association) source.getRelationships().get(i)).getName().equals("A_" + ModelConvertUtil.getName(element.getTarget()) + "_" + source.getName()))
								applyStereotype((Association) source.getRelationships().get(i), stereotype);
						}
					}
				}
			}
		}
	}

	/**
	 * Dependency 관계를 생성함.
	 * 
	 * @param model
	 * 
	 */
	@SuppressWarnings("unused")
	private void createDependency(AbstractUMLEntityModel model) {
		Classifier source = (Classifier) typeMap.get(ModelConvertUtil.getName(model));
		List<?> connections = model.getModelSourceConnections();
		for (Iterator<?> iter = connections.iterator(); iter.hasNext();) {
			AbstractUMLConnectionModel element = (AbstractUMLConnectionModel) iter.next();
			if (element instanceof DependencyModel) {
				Classifier classifier = (Classifier) typeMap.get(ModelConvertUtil.getName(element.getTarget()));
				Dependency dependency = classifier.createDependency((Classifier) typeMap.get(ModelConvertUtil.getName(model)));
				dependency.setName("A_" + ModelConvertUtil.getSimpleName(classifier.getName()) + "_" + ModelConvertUtil.getSimpleName(ModelConvertUtil.getName(model)));
			}
		}
	}

	/**
	 * 저장
	 * 
	 * @param package_
	 * @param pathName
	 */
	public void save(Package package_, String pathName) {
		save(package_, URI.createFileURI(pathName));
	}

	/**
	 * 기초 프로필을 생성함.
	 * 
	 * @return
	 * 
	 */
	@SuppressWarnings("unused")
	public Profile makeBaseProfile() {

		registerResourceFactories();

		Profile egovProfile = createProfile("egov");

		PrimitiveType booleanPrimitiveType = importPrimitiveType(egovProfile, "Boolean");
		PrimitiveType stringPrimitiveType = importPrimitiveType(egovProfile, "String");

		createEnumeration(egovProfile);

		propertyMetaclass = referenceMetaclass(egovProfile, UMLPackage.Literals.PROPERTY.getName());

		return egovProfile;
	}

	/** 속성 메타클래스 인스턴스 */
	private org.eclipse.uml2.uml.Class propertyMetaclass = null;

}
