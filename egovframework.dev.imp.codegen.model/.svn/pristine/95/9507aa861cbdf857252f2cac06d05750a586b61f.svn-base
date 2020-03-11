package egovframework.dev.imp.codegen.model.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import net.java.amateras.uml.classdiagram.model.Visibility;
import net.java.amateras.uml.model.AbstractUMLEntityModel;

import org.apache.log4j.Logger;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.InterfaceRealization;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.VisibilityKind;

/**
 * 클래스다이어그램으로 Import 하는 객체 
 * <p><b>NOTE:</b> XMI 파일의 속성정보를 전자정부프레임워크의 클래스다이어그램 모델로 변환함.
 * @author 운영환경1팀 김연수
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
public class UML2XMIFileImporter extends UMLModelConverter{

    /* 로거 */
    protected final Logger log = Logger.getLogger(UML2XMIFileImporter.class);
    
    /* ecoreTypeMap */
	private Map<Element, AbstractUMLEntityModel> ecoreTypeMap = new HashMap<Element, AbstractUMLEntityModel>();
	
	/* TreeMap */
//	private TreeMap<?, ?> classes;
	
	/* 위치 y */
	private int x;
	/* 위치 x */
	private int y;

	/* AbstractUMLEntityModel */
	private List<AbstractUMLEntityModel> amaterasModels;
	
	/**
	 *  생성자. 
	 * 
	 * 
	 */
	public UML2XMIFileImporter() {
		amaterasModels = new ArrayList<AbstractUMLEntityModel>();
		
	}
	
	/**
	 *  클래스 정보를 변환함. 
	 * 
	 * @param element, stName
	 * 
	 */
	public void convertNodes(Element element, String stName) {

		if (element instanceof Class) {

			ClassModel classModel = createClassModel(element);

			String name = createFullyQualifiedName((Classifier) element);

			classModel.setName(name);

			classModel.setStereoType(stName);

			layoutModel(classModel);

			ecoreTypeMap.put(element, classModel);
			amaterasModels.add(classModel);

		} else if (element instanceof Interface) {
			InterfaceModel interfaceModel = createInterfaceModel((Interface) element);
			String name = createFullyQualifiedName((Classifier) element);
			interfaceModel.setName(name);
			interfaceModel.setStereoType(stName);
			layoutModel(interfaceModel);
			ecoreTypeMap.put(element, interfaceModel);
			amaterasModels.add(interfaceModel);

		} //else if (element instanceof DataType && !(element instanceof PrimitiveType)) {

			// 데이타 타입은 클래스다이어그램에서는 표현하지 않기로 함.
			
			/*DataType type = (DataType) element;
			ClassModel classModel = new ClassModel();
			classModel.setName(type.getName());
			classModel.setStereoType("DataType");
			layoutModel(classModel);
			ecoreTypeMap.put(type, classModel);
			amaterasModels.add(classModel);
			log.info("=================ecoreTypeMap========================="+ecoreTypeMap.toString());	*/		
		//}
	}
	
	/**
	 *  관계정보를 변환함. 
	 * 
	 * @param element
	 * 
	 */
	public void convertLinks(Element element, String stName) {

		if (element instanceof Generalization) {

			createGeneralization((Generalization) element);
		} else if (element instanceof InterfaceRealization) {

			createRealization(element);
		} else if (element instanceof Association) {

			createAssociation(element, stName);
		} else if (element instanceof Dependency) {

			createDependency(element, stName);
		} 
	}	

	/**
	 *  layout을 지정함 
	 * 
	 * @param model
	 * 
	 */
	private void layoutModel(AbstractUMLEntityModel model) {
		model.setConstraint(new Rectangle(x, y, -1, -1));
		x += 400;
		if (x > 2000) {
			x = 0;
			y += 800;
		}		
	}
	
	/**
	 *  amaterasModel로 변환함 
	 * 
	 * @param 
	 * 
	 */
	public Collection<AbstractUMLEntityModel> getConvertedModel() {
		return amaterasModels;
	}

	/**
	 *  Classifier의 이름을 가져옴. 
	 * 
	 * @param classifier
	 * 
	 */
	private String createFullyQualifiedName(Classifier classifier) {
		if (classifier.eContainer() instanceof Package && !(classifier.eContainer() instanceof Model)) {
			//return getPackageName((Package) classifier.eContainer()) + "." + classifier.getName();
			return classifier.getName();
		} else {
			return classifier.getName();
		}
	}
	
	/**
	 *  패키지 정보를 가져옴. 
	 * 
	 * @param pkg
	 * 
	 */
//	private String getPackageName(Package pkg) {
//		if (pkg.eContainer() instanceof Package && !(pkg.eContainer() instanceof Model)) {
//			return getPackageName((Package) pkg.eContainer()) + "." + pkg.getName();
//		} else {
//			return pkg.getName() != null ? pkg.getName() : "";
//		}
//	}
	
	/**
	 *  createAssociation 정보를 생성함. 
	 * 
	 * @param element
	 * 
	 */
	private void createAssociation(Element element, String stName) {
		
		Association association = (Association) element;
		EList<?> ends = association.getMemberEnds();
		AbstractUMLEntityModel sourceModel = null;
		AbstractUMLEntityModel targetModel = null;
		AssociationModel model = new AssociationModel();
		AggregationModel aggModel = new AggregationModel();
		CompositeModel comModel = new CompositeModel();

		if (ends.size() == 2) {
			// 김연수 수정
			int i = 1;
			boolean isCompo = false;
			boolean isAggre = false;
			for (Iterator<?> iterator = ends.iterator(); iterator.hasNext();) {
				Property p = (Property) iterator.next();

				if("composite".equals(p.getAggregation().toString())){
					isCompo = true;
				}
				if("shared".equals(p.getAggregation().toString())){
					isAggre = true;
				}
				
				if(isCompo){
					if(i==1){
						targetModel = ecoreTypeMap.get(p
								.getType());
						int upper = p.getUpper();
						if(1 == upper){
							comModel.setToMultiplicity(upper == -1 ? "*" : "");
						}else{
							comModel.setToMultiplicity(upper == -1 ? "*" : String.valueOf(upper));							
						}

					} else {
						sourceModel = ecoreTypeMap.get(p
								.getType());
						int upper = p.getUpper();
						if(1 == upper){
							comModel.setToMultiplicity(upper == -1 ? "*" : "");
						}else{
							comModel.setToMultiplicity(upper == -1 ? "*" : String.valueOf(upper));							
						}
						
						if (sourceModel != null && targetModel != null) {
							comModel.setSource(sourceModel);
							comModel.setTarget(targetModel);
							comModel.attachSource();
							comModel.attachTarget();
						}
					}
					i++;
				}else if(isAggre){
					if(i==1){
						targetModel = ecoreTypeMap.get(p
								.getType());
						int upper = p.getUpper();
						if(1 == upper){
							aggModel.setFromMultiplicity(upper == -1 ? "*" : "");
						}else{
							aggModel.setFromMultiplicity(upper == -1 ? "*" : String.valueOf(upper));								
						}
					} else {
						sourceModel = ecoreTypeMap.get(p
								.getType());
						int upper = p.getUpper();
						if(1 == upper){
							aggModel.setFromMultiplicity(upper == -1 ? "*" : "");
						}else{
							aggModel.setFromMultiplicity(upper == -1 ? "*" : String.valueOf(upper));								
						}
						
						if (sourceModel != null && targetModel != null) {
							aggModel.setSource(sourceModel);
							aggModel.setTarget(targetModel);
							aggModel.attachSource();
							aggModel.attachTarget();
						}
						
					}
					i++;
				}else{
					if(i==1){
						targetModel = ecoreTypeMap.get(p
								.getType());
						int upper = p.getUpper();
						if(1 == upper){
							model.setToMultiplicity(upper == -1 ? "*" : "");
						}else{
							model.setToMultiplicity(upper == -1 ? "*" : String.valueOf(upper));								
						}

					} else {
						sourceModel = ecoreTypeMap.get(p
								.getType());
						int upper = p.getUpper();
						if(1 == upper){
							model.setToMultiplicity(upper == -1 ? "*" : "");
						}else{
							model.setToMultiplicity(upper == -1 ? "*" : String.valueOf(upper));								
						}
						
						if (sourceModel != null && targetModel != null) {
							model.setSource(sourceModel);
							model.setTarget(targetModel);
							model.attachSource();
							model.attachTarget();
							if(null != stName && "" != stName){
								model.setStereoType(stName);
							}
						}
						
					}
					
					i++;
				}
			}
			
		}
		
		/*if (ends.size() == 2) {
			// 김연수 수정
			int i = 1;
			boolean isCompo = false;
			boolean isAggre = false;
			for (Iterator iterator = ends.iterator(); iterator.hasNext();) {
				Property p = (Property) iterator.next();

				if("composite".equals(p.getAggregation().toString())){
					isCompo = true;
				}
				if("shared".equals(p.getAggregation().toString())){
					isAggre = true;
				}
				
				if(isCompo){
					if(i==1){
						targetModel = ecoreTypeMap.get(p
								.getType());
						int upper = p.getUpper();
						if(1 == upper){
							comModel.setToMultiplicity(upper == -1 ? "*" : "");
						}else{
							comModel.setToMultiplicity(upper == -1 ? "*" : String.valueOf(upper));							
						}

					} else {
						sourceModel = ecoreTypeMap.get(p
								.getType());
						int upper = p.getUpper();
						if(1 == upper){
							comModel.setToMultiplicity(upper == -1 ? "*" : "");
						}else{
							comModel.setToMultiplicity(upper == -1 ? "*" : String.valueOf(upper));							
						}							
						
						if (sourceModel != null && targetModel != null) {
							comModel.setSource(sourceModel);
							comModel.setTarget(targetModel);
							comModel.attachSource();
							comModel.attachTarget();
						}
					}
					i++;
				}else if(isAggre){
					if(i==1){
						targetModel = ecoreTypeMap.get(p
								.getType());
						int upper = p.getUpper();
						if(1 == upper){
							aggModel.setFromMultiplicity(upper == -1 ? "*" : "");
						}else{
							aggModel.setFromMultiplicity(upper == -1 ? "*" : String.valueOf(upper));								
						}
					} else {
						sourceModel = ecoreTypeMap.get(p
								.getType());
						int upper = p.getUpper();
						if(1 == upper){
							aggModel.setFromMultiplicity(upper == -1 ? "*" : "");
						}else{
							aggModel.setFromMultiplicity(upper == -1 ? "*" : String.valueOf(upper));								
						}
						
						if (sourceModel != null && targetModel != null) {
							aggModel.setSource(sourceModel);
							aggModel.setTarget(targetModel);
							aggModel.attachSource();
							aggModel.attachTarget();
						}
						
					}
					i++;
				}else{
					if(i==1){
						targetModel = ecoreTypeMap.get(p
								.getType());
						int upper = p.getUpper();
						if(1 == upper){
							model.setToMultiplicity(upper == -1 ? "*" : "");
						}else{
							model.setToMultiplicity(upper == -1 ? "*" : String.valueOf(upper));								
						}

					} else {
						sourceModel = ecoreTypeMap.get(p
								.getType());
						int upper = p.getUpper();
						if(1 == upper){
							model.setToMultiplicity(upper == -1 ? "*" : "");
						}else{
							model.setToMultiplicity(upper == -1 ? "*" : String.valueOf(upper));								
						}
						
						if (sourceModel != null && targetModel != null) {
							model.setSource(sourceModel);
							model.setTarget(targetModel);
							model.attachSource();
							model.attachTarget();
						}
						
					}
					i++;
				}
			}
			
		}*/

	}

	/**
	 *  createRealization 정보를 생성함. 
	 * 
	 * @param element
	 * 
	 */
	private void createRealization(Element element) {

		InterfaceRealization realization = (InterfaceRealization) element;
		RealizationModel realizationModel = new RealizationModel();
		AbstractUMLEntityModel source = ecoreTypeMap
				.get(realization.getImplementingClassifier());
		AbstractUMLEntityModel target = ecoreTypeMap
				.get(realization.getContract());
		
		if (source != null && target != null) {
			realizationModel.setSource(source);
			realizationModel.setTarget(target);
			realizationModel.attachSource();
			realizationModel.attachTarget();
		}

	}
	
	/**
	 *  createGeneralization 정보를 생성함. 
	 * 
	 * @param element
	 * 
	 */
	private void createGeneralization(Element element) {

		Generalization generalization = (Generalization) element;
		GeneralizationModel generalizationModel = new GeneralizationModel();
		AbstractUMLEntityModel source = ecoreTypeMap
				.get(generalization.getSpecific());
		AbstractUMLEntityModel target = ecoreTypeMap
				.get(generalization.getGeneral());
		if (source != null && target != null) {
			generalizationModel.setSource(source);
			generalizationModel.setTarget(target);
			generalizationModel.attachSource();
			generalizationModel.attachTarget();
		}

	}

	/**
	 *  createDependency 정보를 생성함. 
	 * 
	 * @param element
	 * 
	 */
	private void createDependency(Element element, String stName) {

		Dependency dependency = (Dependency) element;
		
		DependencyModel dependencyModel = new DependencyModel();
		AbstractUMLEntityModel source = (AbstractUMLEntityModel) ecoreTypeMap.get(dependency.getRelatedElements().get(1));
		AbstractUMLEntityModel target = (AbstractUMLEntityModel) ecoreTypeMap.get(dependency.getRelatedElements().get(0));

		if (source != null && target != null) {
			dependencyModel.setSource(source);
			dependencyModel.setTarget(target);
			dependencyModel.attachSource();
			dependencyModel.attachTarget();
			
			if(null != stName && "" != stName){
				dependencyModel.setStereoType(stName);
			}
		}

	}

	/**
	 *  createClassModel 정보를 생성함. 
	 * 
	 * @param ele
	 * 
	 */
	private ClassModel createClassModel(Element ele) {

		Class c = (Class) ele;

		ClassModel cm = new ClassModel();

		if(null != c.getName()){
			cm.setName(c.getName());
		}
		if(null != cm.getStereoType()){
			cm.setStereoType(cm.getStereoType());
		}
		if(null != c.getPackage()){
			cm.setName(c.getPackage().getName());
		}
		EList<?> attributes = c.getAttributes();
		for (Iterator<?> iterator = attributes.iterator(); iterator.hasNext();) {
			Property prop = (Property) iterator.next();
			AttributeModel am = new AttributeModel();

			am.setStatic(prop.isStatic());
			am.setVisibility(getVisibility(prop.getVisibility()));
			am.setName(prop.getName());
			am.setType(prop.getType().getName());
			cm.addChild(am);
		}
		EList<?> operations = c.getOperations();
		for (Iterator<?> iterator = operations.iterator(); iterator.hasNext();) {
			Operation ope = (Operation) iterator.next();
			OperationModel model = createOperationModel(ope);
			cm.addChild(model);
		}

		return cm;
	}

	/**
	 *  createInterfaceModel 정보를 생성함. 
	 * 
	 * @param interface1
	 * 
	 */
	private InterfaceModel createInterfaceModel(Interface interface1) {
		InterfaceModel model = new InterfaceModel();

		model.setName(interface1.getName());
		EList<?> operations = interface1.getOperations();
		for (Iterator<?> iterator = operations.iterator(); iterator.hasNext();) {
			Operation ope = (Operation) iterator.next();
			OperationModel operationModel = createOperationModel(ope);
			model.addChild(operationModel);
		}
		return model;
	}

	/**
	 *  operation 정보를 생성함. 
	 * 
	 * @param ope
	 * 
	 */
	private OperationModel createOperationModel(Operation ope) {
		OperationModel model = null;

		model = new OperationModel();
		model.setAbstract(ope.isAbstract());
		model.setStatic(ope.isStatic());
		model.setName(ope.getName());
		model.setVisibility(getVisibility(ope.getVisibility()));
		try {
			if (ope.getReturnResult() != null) {
				model.setType(ope.getReturnResult().getType().getName());
			}
			
		}catch(Exception e){
			// 리턴타입이 아무값도 없는 경우(null도 아닌 경우)에는 그냥 스킵하도록 함.
			e.printStackTrace();
		}
		
		EList<?> parameters = ope.getOwnedParameters();
		List<Argument> params = new ArrayList<Argument>();
		for (Iterator<?> ite = parameters.iterator(); ite.hasNext();) {
			Parameter param = (Parameter) ite.next();
			if (!param.equals(ope.getReturnResult())) {
				Argument argument = new Argument();
				argument.setName(param.getName());
				argument.setType(param.getType().getName());
				params.add(argument);
			}
		}
		model.setParams(params);

		return model;
	}

	/**
	 *  Visibility 정보를 생성함. 
	 * 
	 * @param kind
	 * 
	 */
	private Visibility getVisibility(VisibilityKind kind) {
		int value = kind.getValue();
		switch (value) {
		case VisibilityKind.PACKAGE:
			return Visibility.PACKAGE;
		case VisibilityKind.PRIVATE:
			return Visibility.PRIVATE;
		case VisibilityKind.PROTECTED:
			return Visibility.PROTECTED;
		case VisibilityKind.PUBLIC:
			return Visibility.PUBLIC;
		default:
			return Visibility.PACKAGE;
		}
	}
}
