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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.LiteralUnlimitedNatural;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Relationship;
import org.eclipse.uml2.uml.Type;

import egovframework.dev.imp.codegen.model.generator.CodeGenException;

/**
 * 모델 기반 코드 생성을 하기 위한 유틸
 * <p><b>NOTE:</b> 모델 기반 코드 생성 유틸 
 * @author 개발환경 개발팀 김형조
 * @since 2009.08.10
 * @version 1.0
 * @see
 *
 * <pre>
 *  == 개정이력(Modification Information) ==
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.08.10  김형조          최초 생성
 *
 * </pre>
 */
public class GenUtil {
	/** 개행문자 */
	public final static String NEW_LINE = "\n";
	/** 패키지 선언 문자 */
	public final static String PACKAGE = "package";
	/** 스테레오타입 목록 */
	private static Map<?, ?> stereoTypeList = null;
	
	/**
	 * 스테레오타입 목록을 세팅함. 
	 * 
	 * @param stereoTypeList
	 */
	public static void setStereooTypeList(Map<?, ?> stereoTypeList) {
		GenUtil.stereoTypeList = stereoTypeList;
	}

	/**
	 * 오프레이션의 파라미터열을  String  로 반환한다.
	 * 
	 * @param opr
	 * @return 파라미터열
	 * 
	 */
	@SuppressWarnings("unused")
	public static String getParameter(Operation opr) throws CodeGenException {
		StringBuffer rst = new StringBuffer();
		EList<Element> parms = opr.allOwnedElements();
		Parameter parm = null;
		boolean first = true;
		Object obj = null;
		
		try {
			for(Iterator<Element> ite = parms.iterator(); ite.hasNext();) {
				obj = ite.next();
				
				if (obj instanceof Parameter) {
					parm = (Parameter)obj;
	
					if ("return".equals(parm.getDirection().getName())) continue;
	
					if (first) {
						rst.append(parm.getType().getName()).append(" ").append(parm.getName());
						first = false;
					}
					else rst.append(", ").append(parm.getType().getName()).append(" ").append(parm.getName());
				} else if (obj instanceof LiteralUnlimitedNatural){
					LiteralUnlimitedNatural lit = (LiteralUnlimitedNatural)obj;
					String name = lit.getName();
					Type type = lit.getType();
				}
				
			}
		} catch (Exception e) {
			throw new CodeGenException(opr.getName() +" Operation's Parameter Extraction Error ["+e.getMessage()+"]");
		}

		return rst.toString();
	}

	/**
	 * 입력 받은 문자열의 첫자를 대문자로 변환.
	 * 
	 * 
	 */
	public static String firstCahrToUpperCase(String word){
		return "".equals(word) ? word : word.substring(0,1).toUpperCase() + word.substring(1);
	}

	/**
	 * 입력 받은 문자열의 첫자를 소문자로 변환.
	 * 
	 * 
	 */
	public static String firstCahrToLowerCase(String word){
		return "".equals(word) ? word : word.substring(0,1).toLowerCase() + word.substring(1);
	}

	/**
	 * Type 객체에서 패키지정보를 추출.(Model명 포함)
	 * 
	 * @param type
	 * @return 패키지정보(Model명 포함)
	 * @throws CodeGenException
	 * 
	 */
	public static String getPackage(Type type) throws CodeGenException {
		boolean first = true;
		Package pkg = type.getPackage();
		StringBuffer rslt = new StringBuffer();

		try {
			while (!(pkg instanceof Model)) {
				try {
					if (first) {
						rslt.append(pkg.getName());
						first = false;
					} else {
						rslt.insert(0, ".").insert(0, pkg.getName());
					}

					pkg = (Package)pkg.getOwner();
				} catch (Exception e) {
					pkg = type.getModel();
				}
			}
		} catch (Exception e) {
			throw new CodeGenException(type.getName() +"'s Package Extraction Error ["+e.getMessage()+"]");
		}

		return rslt.toString();
	}

	/**
	 * Type 객체에서 basePackage(Model명)를 뺀 패키지명을 반환한다.
	 * 
	 * @param basePackage
	 * @param type
	 * @return 패키지명
	 * @throws CodeGenException
	 * 
	 */
	public static String getPurePackage(String basePackage, Type type) throws CodeGenException {
		String rslt = null;

		try {
			String pkg = getPackage(type);
			rslt = basePackage.length() > 0 ? pkg.substring(basePackage.length()+1) : pkg;
		} catch (Exception e) {
			throw new CodeGenException(basePackage + "."+type.getName()+"'s Package Extraction Error ["+e.getMessage()+"]");
		}
		return rslt;
	}

	/**
	 * 문자열 배열 정렬
	 * 
	 * @param strs
	 * @return
	 */
	public static String[] sort(String[] strs) {
		String comp = "";
		ArrayList<String> array = new ArrayList<String>();

		Arrays.sort(strs);

		for(int i = 0; i < strs.length; i++) {
			if (!comp.equals(strs[i]))	{
				if (strs[i].indexOf('.') < 0) {
					array.add(strs[i]);
					comp = strs[i];
				} else {
					array.add(comp.startsWith(strs[i].substring(0,strs[i].indexOf('.'))) ? strs[i] : NEW_LINE + strs[i]);
					comp = strs[i];
				}
			}
		}
		return (String[])array.toArray(new String[0]);
	}

	/**
	 * 클래스의 Extends 구문을 생성
	 * 
	 * @param cls
	 * @return Extends구문
	 * @throws CodeGenException
	 * 
	 */
	public static String getExtendsSyntax(Class cls) throws CodeGenException {
		StringBuffer rslt = new StringBuffer();
		boolean first = true;
		String name = null;

		try {
			EList<Class> supClss = cls.getSuperClasses();
			for(Iterator<Class> ite = supClss.iterator(); ite.hasNext();) {
				name = ((Class)ite.next()).getName();

				if (first) {
					rslt.append("extends ").append(name);
					first = false;
				} else {
					rslt.append(", ").append(name);
				}
			}
		} catch (Exception e) {
			throw new CodeGenException(cls.getName()+"'s ExtendsSyntax Extraction Error ["+e.getMessage()+"]");
		}
		return rslt.length() > 0 ? rslt.insert(0, " ").toString() : rslt.toString();
	}

	/**
	 * 인터페이스의 Extends 구문을 생성
	 * 
	 * @param itfc
	 * @return Extends구문
	 * @throws CodeGenException
	 * 
	 */
	public static String getExtendsSyntax(Interface itfc) throws CodeGenException {
		StringBuffer rslt = new StringBuffer();
		boolean first = true;
		String name = null;

		try {
			EList<Interface> supItfc = itfc.getAllUsedInterfaces();//SuperClasses();
			for(Iterator<Interface> ite = supItfc.iterator(); ite.hasNext();) {
				name = ((Interface)ite.next()).getName();

				if (first) {
					rslt.append("extends ").append(name);
					first = false;
				} else {
					rslt.append(", ").append(name);
				}
			}
		} catch (Exception e) {
			throw new CodeGenException("GenUtil.getExtendsSyntax("+itfc.getName()+")["+e.getMessage()+"]");
		}
		return rslt.length() > 0 ? rslt.insert(0, " ").toString() : rslt.toString();
	}

	/**
	 * 클래스의 implements 구문을 생성
	 * 
	 * @param cls
	 * @return implements 구문
	 * @throws CodeGenException
	 * 
	 */
	public static String getInterfaceSyntax(Class cls) throws CodeGenException {
		StringBuffer rslt = new StringBuffer();

		try {
			EList<Interface> itfs = cls.getImplementedInterfaces();
			Iterator<Interface> ite = itfs.iterator();

			if (ite.hasNext()) rslt.append("implements ").append(((Interface)ite.next()).getName());
			while( ite.hasNext()) {
				rslt.append(", ").append(((Interface)ite.next()).getName());
			}
		} catch (Exception e) {
			throw new CodeGenException("GenUtil.getInterfaceSyntax("+ cls.getName() +")["+e.getMessage()+"]");
		}
		return rslt.length() > 0 ? rslt.insert(0, " ").toString() : rslt.toString();

	}

	/**
	 * package 구문을 생성
	 * 
	 * @param pkg
	 * @return package 구문
	 * 
	 */
	public static String getPackageSyntax(String pkg) {
		StringBuffer rslt = new StringBuffer();		
		if (!"".equals(pkg)) rslt.append("\npackage ").append(pkg).append(";");
		return rslt.toString();		
	}

	/**
	 * 목록에 import 구문을 추가한다.
	 * 
	 * @param emptyList
	 * @param imptSyntax ex) "import egov.test.TestObject;"
	 * 
	 */
	public static void addImportSyntax(List<String> emptyList, String imptSyntax) {
		emptyList.add(imptSyntax + NEW_LINE);
	}

	/**
	 * 비어있는 List에 import 구문을 추가
	 * 
	 * @param emptyList
	 * @param type
	 * @param basePkg
	 * @throws CodeGenException
	 * 
	 */
	public static void addImportSyntax(List<String> emptyList, Type type, String basePkg) throws CodeGenException {
		StringBuffer iptSyntax = new StringBuffer();
		String pkg = null;

		try {
			if (type != null && !(type instanceof PrimitiveType)) {
				pkg = getPackage(type);
				//pkg = getPurePackage(basePkg, type);
	
				if (!"".equals(pkg) && !pkg.equals(basePkg)) {
					iptSyntax.append("import ").append(pkg).append(".").append(type.getName()).append(";\n");
					emptyList.add(iptSyntax.toString());
				}
			}
		} catch (Exception e) {
			throw new CodeGenException("GenUtil.addImportSyntax(emptyList, "+ type.getName()+", " + basePkg +")["+e.getMessage()+"]");
		}
	}
	
	/**
	 * Impl 객체를 위한 Import 구문 생성
	 * 
	 * @param emptyList
	 * @param itfces
	 * @param basePkg
	 * @throws CodeGenException
	 * 
	 */
	public static void addImportSyntaxForImpl(List<String> emptyList, EList<Interface> itfces, String basePkg) throws CodeGenException {
		try {
			Type itfc = null;
			for(Iterator<Interface> ite = itfces.iterator(); ite.hasNext();) {
				itfc = (Type) ite.next();
				addImportSyntax(emptyList, itfc, basePkg);
			}
		} catch (Exception e) {
			throw new CodeGenException("GenUtil.addImportSyntaxForRealization(emptyList, EList, " + basePkg +")["+e.getMessage()+"]");
		}
	}

	/**
	 * Super 클래스를 위한 Import 구문 생성
	 * 
	 * @param emptyList
	 * @param clses
	 * @param basePkg
	 * @throws CodeGenException
	 * 
	 */
	public static void addImportSyntaxForSuper(List<String> emptyList, EList<Class> clses, String basePkg) throws CodeGenException {
		try {
			Type cls = null;
			for(Iterator<Class> ite = clses.iterator(); ite.hasNext();) {
				cls = (Type) ite.next();
				addImportSyntax(emptyList, cls, basePkg);
			}
		} catch (Exception e) {
			throw new CodeGenException("GenUtil.addImportSyntaxForSuper(emptyList, EList, " + basePkg +")["+e.getMessage()+"]");
		}
	}
	/**
	 * EList<Property> 에서 import 구문 추출(멤버변수용)
	 * 
	 * @param emptyList
	 * @param prpts
	 * @param basePkg
	 * @throws CodeGenException
	 * 
	 */
	public static void addImportSyntaxForAttributtes(List<String> emptyList, EList<Property> prpts, String basePkg) throws CodeGenException {
		try {
			Property prpt = null;

			for(Iterator<Property> ite = prpts.iterator(); ite.hasNext();) {
				prpt = (Property)ite.next();
				addImportSyntax(emptyList, prpt.getType(), basePkg);
			}
		} catch (Exception e) {
			throw new CodeGenException("GenUtil.addImportSyntaxForAttributtes(emptyList, EList, " + basePkg +")["+e.getMessage()+"]");
		}
	}

	/**
	 * EList<Operation> 에서 import 구문 추출(메소드의 파라미터용)
	 * 
	 * @param emptyList
	 * @param basePkg
	 * @param oprs
	 * @throws CodeGenException
	 * 
	 */
	public static void addImportSyntaxForOperations(List<String> emptyList, String basePkg, EList<Operation> oprs) throws CodeGenException {
		try {
			EList<Element> parms = null;
			Parameter parm = null;
			Operation opr = null;
			Object obj = null;

			for(Iterator<Operation> ite = oprs.iterator(); ite.hasNext();) {
				opr = (Operation) ite.next();
				parms = opr.allOwnedElements();

				for(Iterator<Element> ite2 = parms.iterator(); ite2.hasNext();) {
					obj = ite2.next();
					
					if (obj instanceof Parameter) {
						parm = (Parameter)obj;
						addImportSyntax(emptyList, parm.getType(), basePkg);
					}
				}
			}
		} catch (Exception e) {
			throw new CodeGenException("GenUtil.addImportSyntaxForOperations(emptyList, " + basePkg + ", EList)["+e.getMessage()+"]");
		}
	}

	/**
	 * List에서 import 구문을 뽑아 하나의 구문으로 조합
	 * 
	 * @param iptList
	 * @return import 구문
	 * @throws CodeGenException
	 * 
	 */
	public static String getImportSyntax(List<String> iptList) throws CodeGenException {
		StringBuffer rslt = new StringBuffer();
		try {
			String[] ipts = iptList.toArray(new String[0]);
			ipts = GenUtil.sort(ipts);

			for (int i = 0; i < ipts.length; i++) {
				rslt.append(ipts[i]);
			}
		} catch (Exception e) {
			throw new CodeGenException("GenUtil.getImportSyntax(iptList)["+e.getMessage()+"]");
		}
		return rslt.length() > 0 ? rslt.insert(0, NEW_LINE).toString() : rslt.toString();
	}

	/**
	 * 클래스의 헤드 정의 구문 생성
	 * 
	 * @param cls
	 * @return 클래스의 헤드정의구문 
	 * 
	 */
	public static String getClassDefineSyntax(Class cls) {
		StringBuffer rslt = new StringBuffer();

		rslt.append(cls.getVisibility().getName())
		.append(" class ").append(cls.getName());

		return rslt.toString();
	}

	/**
	 * 인터페이스의 헤드 정의 구문 생성
	 * 
	 * @param itfc
	 * @return 인터페이스의 헤드정의구문
	 * 
	 */
	public static String getInterfaceDefineSyntax(Interface itfc) {
		StringBuffer rslt = new StringBuffer();

		rslt.append(itfc.getVisibility().getName())
		.append(" interface ").append(itfc.getName());

		return rslt.toString();
	}

	/**
	 * 목록에서 객체명이 존재하는지 판별
	 * 
	 * @param chkList
	 * @param objName
	 * @return
	 * 
	 */
	public static boolean checkExistence(String[] chkList, String objName) {
		boolean rslt = false;

		for(int i = 0; i < chkList.length; i ++) {
			if (chkList[i].equals(objName)) {
				rslt = true;
				break;
			}
		}
		return rslt;
	}

	/**
	 * 멤버변수 구문 생성(Set, Get 메소드 포함)
	 * 
	 * @param pros
	 * @return 멤버변수 구문 생성
	 * @throws CodeGenException
	 * 
	 */
	public static String getMemberSyntax(EList<Property> pros) throws CodeGenException {
		StringBuffer rslt = new StringBuffer();
		StringBuffer method = new StringBuffer();
		Property pro = null;

		try {
			for (Iterator<Property> ite = pros.iterator(); ite.hasNext();) {
				pro = (Property) ite.next();

				rslt.append("\t")
				.append(PACKAGE.equals(pro.getVisibility().toString()) ? "" : pro.getVisibility()+" ")
				.append(pro.getType().getName()).append(" ")
				.append(pro.getName() == null ? GenUtil.firstCahrToLowerCase(pro.getType().getName()):pro.getName())
				.append(";\n");
//				(Set, Get 메소드 제외)
//				method	//set method
//				.append("\tpublic void set")
//				.append(pro.getName() == null ? pro.getType().getName():GenUtil.firstCahrToUpperCase(pro.getName()))
//				.append("(").append(pro.getType().getName()).append(" ").append(pro.getName()).append(")")
//				.append("{\n")
//				.append("\t\tthis.").append(pro.getName()).append(" = ").append(pro.getName()).append(";\n")
//				.append("\t}\n\n");
//
//				method	//get method
//				.append("\tpublic ").append(pro.getType().getName()).append(" get")
//				.append(pro.getName() == null ? pro.getType().getName():GenUtil.firstCahrToUpperCase(pro.getName())).append("() {\n")
//				.append("\t\t return this.").append(pro.getName()).append(";\n")
//				.append("\t}\n\n");
			}

		} catch (Exception e) {
			throw new CodeGenException("Making Member Code Error!\nGenUtil.getMemberSyntax("+pro.getName()+") [" + e.getMessage() + "]");
		}

		if (rslt.length() > 0 ) {
			rslt.insert(0, NEW_LINE).append(NEW_LINE).append(method);
		}
		return rslt.toString();
	}
	
	/**
	 * 인터페이스의 멤버변수 구문 생성(Set, Get 메소드 포함)
	 * 
	 * @param pros
	 * @return 멤버변수 구문 생성
	 * @throws CodeGenException
	 * 
	 */
	public static String getMemberSyntaxForInterface(EList<Property> pros) throws CodeGenException {
		StringBuffer rslt = new StringBuffer();
		StringBuffer method = new StringBuffer();
		Property pro = null;

		try {
			for (Iterator<Property> ite = pros.iterator(); ite.hasNext();) {
				pro = (Property) ite.next();

				rslt.append("\t")
				.append(PACKAGE.equals(pro.getVisibility().toString()) ? "" : pro.getVisibility()+" ")
				.append(pro.getType().getName()).append(" ")
				//.append(pro.getName())
				.append(pro.getName() == null ? GenUtil.firstCahrToLowerCase(pro.getType().getName()):pro.getName())
				.append(";\n");

//				method	//set method
//				.append("\tpublic void set")
//				//.append(GenUtil.firstCahrToUpperCase(pro.getName()))
//				.append(pro.getName() == null ? pro.getType().getName():GenUtil.firstCahrToUpperCase(pro.getName()))
//				.append("(").append(pro.getType().getName()).append(" ").append(pro.getName()).append(");\n\n");
////				.append("(").append(pro.getType().getName()).append(" ").append(pro.getName()).append(")")
////				.append("{\n")
////				.append("\t\tthis.").append(pro.getName()).append(" = ").append(pro.getName()).append(";\n")
////				.append("\t}\n\n");
//				
//				method	//get method
//				.append("\tpublic ").append(pro.getType().getName()).append(" get")
//				//.append(GenUtil.firstCahrToUpperCase(pro.getName())).append("();\n\n");
//				.append(pro.getName() == null ? pro.getType().getName():GenUtil.firstCahrToUpperCase(pro.getName())).append("();\n\n");
////				.append(GenUtil.firstCahrToUpperCase(pro.getName())).append("() {\n")
////				.append("\t\t return this.").append(pro.getName()).append(";\n")
////				.append("\t}\n\n");
			}

		} catch (Exception e) {
			throw new CodeGenException("Making Member Code Error!\nGenUtil.getMemberSyntax("+pro.getName()+") [" + e.getMessage() + "]");
		}

		if (rslt.length() > 0 ) {
			rslt.insert(0, NEW_LINE).append(NEW_LINE).append(method);
		}
		return rslt.toString();
	}

	/**
	 * 객체의 메소드 구문을 생성 
	 * 
	 * @param opes
	 * @return 메소드 구문
	 * @throws CodeGenException
	 * 
	 */
	public static String getOperationSyntax(EList<Operation> opes) throws CodeGenException {
		StringBuffer rslt = new StringBuffer();
		Operation ope = null;
		String rtn = null;
		Parameter rtnObj = null;

		try {
			for (Iterator<Operation> ite = opes.iterator() ; ite.hasNext();) {
				ope = (Operation)ite.next();
				rtnObj = ope.getReturnResult();
				rtn = (rtnObj == null ? "void" : rtnObj.getType().getName());  
				// rtn = (rtnObj == null ? "" : rtnObj.getType().getName());  
				String parm = GenUtil.getParameter(ope);
				//String ex	= GenUtil.getExceptions(ope);

				rslt
				.append("\t").append(PACKAGE.equals(ope.getVisibility().toString()) ? "" : ope.getVisibility() + " ");
				
				//메소드의 Return Type 명시
				//if (rtnObj != null) 
				rslt.append(rtn).append(" ");
				rslt
				.append(ope.getName()).append("(").append(parm).append(") ")
				.append(" ").append("{\n")
				.append((rtn != null && ( "void".equals(rtn) || "".equals(rtn))) ? NEW_LINE : "\t\treturn " + GenUtil.getReturnValue(rtn)+ ";\n");
				
				rslt.append("\t}\n\n");

			}			
		} catch (Exception e) {
			throw new CodeGenException("Makeing Operation Code Error!\nGenUtil.getOperationSyntax(" + ope.getName() + ") [" + e.getMessage() + "]");
		}

		if (rslt.length() > 0 ) rslt.insert(0, NEW_LINE);
		return rslt.toString();
	}

	/**
	 * 메소드의 Return Type(rtnType)에 맞는 기본값을 반환
	 * 
	 * @param rtnType
	 * @return
	 * 
	 */
	public static String getReturnValue(String rtnType) {
		String rslt = "null";
		
		if ("int".equals(rtnType)) rslt = "0";
		else if ("char".equals(rtnType)) rslt = "''";
		else if ("long".equals(rtnType)) rslt = "0";
		else if ("boolean".equals(rtnType)) rslt = "false";
		return rslt;
	}
	/**
	 * 인터페이스의 메소드 구문을 생성 
	 * 
	 * @param opes
	 * @return 메소드 구문
	 * @throws CodeGenException
	 * 
	 */
	public static String getOperationSyntaxForInterface(EList<Operation> opes) throws CodeGenException {
		StringBuffer rslt = new StringBuffer();
		Operation ope = null;
		String rtn = null;
		Parameter rtnObj = null;

		try {
			for (Iterator<Operation> ite = opes.iterator() ; ite.hasNext();) {
				ope = (Operation)ite.next();
				rtnObj = ope.getReturnResult();
				rtn = (rtnObj == null ? "void" : rtnObj.getType().getName());  
				String parm = GenUtil.getParameter(ope);
				//String ex	= GenUtil.getExceptions(ope);

				rslt
				.append("\t").append(PACKAGE.equals(ope.getVisibility().toString()) ? "" : ope.getVisibility() + " ")
				.append(rtn).append(" ")
				.append(ope.getName()).append("(").append(parm).append(");\n\n");
			}			
		} catch (Exception e) {
			throw new CodeGenException("Makeing Operation Code Error!\nGenUtil.getOperationSyntax(" + ope.getName() + ") [" + e.getMessage() + "]");
		}

		if (rslt.length() > 0 ) rslt.insert(0, NEW_LINE);
		return rslt.toString();
	}

	/**
	 * 스테레오 타입명을 가져오기 
	 * 
	 * @param type
	 * @return
	 */
	public static String getStereoType(Type type) {
		return (String)stereoTypeList.get(type);
	}
	
	/**
	 * 주어진 type 과 관계(Relation)된 객체중 stereoName를 가지는 객체를 찾아 반환
	 * 
	 * @param type
	 * @param stereoName
	 * @return
	 * @throws Exception
	 * 
	 */
	public static Type getTypeOfRelatedStereotype(Type type, String stereoName) throws Exception {
		Type rslt = null;
		Object stereoType = null;

		EList<Relationship> relList = type.getRelationships();
		for(Iterator<Relationship> relIt = relList.iterator(); relIt.hasNext(); ) {
			Relationship rel = (Relationship)relIt.next();
			EList<Element> elList = rel.getRelatedElements();

			for (Iterator<Element> elIte = elList.iterator(); elIte.hasNext();) {
				Element el = (Element)elIte.next();
				
				stereoType = stereoTypeList.get(el);				
				if (stereoType != null && stereoName.equals((String)stereoType)) return (Type)el;				
			}
		}
		return rslt;
	}
}
