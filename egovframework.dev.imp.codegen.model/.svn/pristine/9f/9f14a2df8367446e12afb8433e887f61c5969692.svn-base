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
package egovframework.dev.imp.codegen.model.util;

import java.util.Iterator;

import net.java.amateras.uml.classdiagram.model.ClassModel;
import net.java.amateras.uml.classdiagram.model.InterfaceModel;
import net.java.amateras.uml.classdiagram.model.Visibility;
import net.java.amateras.uml.model.AbstractUMLEntityModel;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.LiteralUnlimitedNatural;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.VisibilityKind;

/**
 * 모델 변환 유틸 클래스 
 * <p><b>NOTE:</b> 
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
public class ModelConvertUtil {

	/** 간단 모델명 가져오기 
	 * 
	 * @param fqcn
	 * @return
	 * 
	 */
	public static String getSimpleName(String fqcn) {
		String[] pkgs = fqcn.split("\\.");
		return pkgs[pkgs.length - 1];
	}
	
	/** Visibility 정보 가져오기 
	 * 
	 * @param kind
	 * @return
	 * 
	 */
	public static VisibilityKind getVisibility(Visibility kind) {
		if (kind.equals(Visibility.PACKAGE)) {
			return VisibilityKind.get(VisibilityKind.PACKAGE);
		} else if (kind.equals(Visibility.PRIVATE)) {
			return VisibilityKind.get(VisibilityKind.PRIVATE);
		} else if (kind.equals(Visibility.PROTECTED)) {
			return VisibilityKind.get(VisibilityKind.PROTECTED);
		} else {
			return VisibilityKind.get(VisibilityKind.PUBLIC);
		}
	}


	/** Visibility 정보 가져오기 
	 * 
	 * @param visibility
	 * @return
	 * 
	 */
	public static VisibilityKind getVisibility(String visibility) {
		if (visibility.equals("package")) {
			return VisibilityKind.get(VisibilityKind.PACKAGE);
		} else if (visibility.equals("private")) {
			return VisibilityKind.get(VisibilityKind.PRIVATE);
		} else if (visibility.equals("protected")) {
			return VisibilityKind.get(VisibilityKind.PROTECTED);
		} else {
			return VisibilityKind.get(VisibilityKind.PUBLIC);
		}
	}
	
	/** 모델명 가져오기 
	 * 
	 * @param model
	 * @return
	 * 
	 */
	public static String getName(AbstractUMLEntityModel model) {
		if (model instanceof ClassModel) {
			ClassModel classModel = (ClassModel) model;
			return classModel.getName(); //ModelConvertUtil.getSimpleName(classModel.getName());
		} else if (model instanceof InterfaceModel) {
			InterfaceModel interfaceModel = (InterfaceModel) model;
			return interfaceModel.getName(); // ModelConvertUtil.getSimpleName(interfaceModel.getName());
		}
		return "";
	}
	
	/** 해당요소 포함여부 확인 
	 * 
	 * @param pkg
	 * @param name
	 * @return
	 * 
	 */
	public static boolean isContain(Package pkg, String name) {
		EList<?> ownedMembers = pkg.getOwnedMembers();
		Element element = null;
		Package p = null;
		for (Iterator<?> iter = ownedMembers.iterator(); iter.hasNext();) {
			element = (Element) iter.next();
			if (element instanceof Package) {
				p = (Package) element;
				if (p.getName().equals(name)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/** Multiplicity 정보를 가져옴. 
	 * 
	 * @param str
	 * @return
	 * 
	 */
	public static int[] getMultiplicity(String str){
		int[] result = {1,1};
		String min = null;
		String max = null;
		
		if (str!=null && str.length()>0){
			if (str.indexOf("..")>0){ 
				min = str.substring(0, str.indexOf(".."));
				max = str.substring(str.indexOf("..")+2);
				if (max.equals("*")){
					result[0] = 0;
					result[1] = LiteralUnlimitedNatural.UNLIMITED;
				}else if (CommonUtil.isNumber(max)){
					result[0] = 1;
					result[1] = Integer.parseInt(max);
				}
				if (CommonUtil.isNumber(min)){
					result[0] = Integer.parseInt(min);
				}
			}else {
				if (str.equals("*")){
					result[0] = 0;
					result[1] = LiteralUnlimitedNatural.UNLIMITED;
				}else if (CommonUtil.isNumber(str)){
					result[0] = Integer.parseInt(str);
					result[1] = Integer.parseInt(str);
				}
			}
		}
		return result;
	}
	
	/** 패키지 정보를 가져옴 
	 * 
	 * @param obj
	 * @return
	 * 
	 */
	public static String getPackage(Object obj) {
		org.eclipse.uml2.uml.Package package_ = null;
		String packageName = null;
		if (obj instanceof org.eclipse.uml2.uml.Class) {
			package_ = ((org.eclipse.uml2.uml.Class) obj).getPackage();
		} else if (obj instanceof org.eclipse.uml2.uml.Interface) {
			package_ = ((org.eclipse.uml2.uml.Interface) obj).getPackage();
		} else if (obj instanceof org.eclipse.uml2.uml.DataType) {
			package_ = ((org.eclipse.uml2.uml.DataType) obj).getPackage();
		}
		packageName = package_.getQualifiedName();

		if (packageName == null || packageName.indexOf("::") < 0)
			packageName = "";
		if (packageName.indexOf("::") > 0)
			packageName = packageName.substring(packageName.indexOf("::") + 2)
					.replaceAll("::", ".");
		if (packageName.indexOf('.') == 0)
			packageName = packageName.substring(1);
		if (package_.getName().equals(""))
			packageName = "";
		return packageName;
	}

}
