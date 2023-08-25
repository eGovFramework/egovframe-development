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
package egovframework.dev.imp.codegen.model.templates;

import java.util.ArrayList;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Type;

import egovframework.dev.imp.codegen.model.generator.IGenerator;
import egovframework.dev.imp.codegen.model.util.GenUtil;

/**
 * eGovFramework 의 Dao 스테레오타입 클래스 모델 소스를 생성하는 자바 클래스
 * <p>
 * <b>NOTE:</b> Dao 스테레오타입 클래스 모델 템플릿.
 * 
 * @author 개발환경 개발팀 김형조
 * @since 2009.08.10
 * @version 1.0
 * @see <pre>
 *  == 개정이력(Modification Information) ==
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.08.10  김형조          최초 생성
 * 
 * </pre>
 */
public class TemplateDao implements IGenerator {
	/** 개행 문자 */
	protected static String nl;

	/**
	 * 템플릿 인스턴스 생성
	 * 
	 * @param lineSeparator
	 * @return
	 */
	public static TemplateDao create(String lineSeparator) {
		nl = lineSeparator;
		TemplateDao result = new TemplateDao();
		nl = null;
		return result;
	}
	
	/** 개행 문자 */
	public String NL = nl == null ? (System.getProperties()
			.getProperty("line.separator")) : nl;
	/** 템플릿 요소 1 */
	protected String TEXT_1 = NL + "/**" + NL + " * Description : " + NL
			+ " * @author" + NL + " * @since" + NL + " * @version" + NL
			+ " * @see" + NL + " *" + NL + " * <pre>" + NL
			+ " * << Modification Information >>" + NL + " *   " + NL
			+ " *   Date     Modifier               Expression" + NL
			+ " *  -------    --------    ---------------------------" + NL
			+ " *  " + NL + " *" + NL + " * " + NL + " * </pre>" + NL + " */"
			+ NL + "@Repository(\"";
	/** 템플릿 요소 2 */	
	protected String TEXT_2 = "\")";
	/** 템플릿 요소 3 */
	protected String TEXT_3 = NL;
	/** 템플릿 요소 4 */
	protected String TEXT_4 = " extends EgovAbstractDAO ";
	/** 템플릿 요소 5 */
	protected String TEXT_5 = NL + "{";
	/** 템플릿 요소 6 */
	protected String TEXT_6 = NL + "}";

	/* 
	 * 자바 코드 생성 
	 * 
	 * (non-Javadoc)
	 * @see egovframework.dev.imp.codegen.model.generator.IGenerator#generate(java.lang.Object)
	 */
	@SuppressWarnings("unused")
	public String generate(Object element) throws Exception {
		final StringBuffer stringBuffer = new StringBuffer();

		// (Object element)
		Class cls = (Class) element;

		// Package 구문 생성
		String basePkg = GenUtil.getPackage((Type) cls);
		String pkg = GenUtil.getPackageSyntax(basePkg);

		// Class 정의
		String clsDef = GenUtil.getClassDefineSyntax(cls);

		// 상속
		String extend = GenUtil.getExtendsSyntax(cls);

		// 인터페이스 상속
		String intfce = GenUtil.getInterfaceSyntax(cls);

		// 멤버변수 구문
		String member = GenUtil.getMemberSyntax(cls.getAllAttributes());

		// 메소드 구문
		String method = GenUtil.getOperationSyntax(cls.getAllOperations());

		// Import 구문 생성
		String ipt = null;
		ArrayList<String> iptList = new ArrayList<String>();
		GenUtil.addImportSyntaxForSuper(iptList, cls.getSuperClasses(), basePkg);
		GenUtil.addImportSyntaxForImpl(iptList, cls.getAllImplementedInterfaces(), basePkg);
		GenUtil.addImportSyntaxForAttributtes(iptList, cls.getAllAttributes(), basePkg);
		GenUtil.addImportSyntaxForOperations(iptList, basePkg, cls.getAllOperations());

		/**************************** 스테레오타입 로직 삽입구 시작 ***************************/
		GenUtil.addImportSyntax(iptList, "import org.egovframe.rte.psl.dataaccess.EgovAbstractDAO;");
		GenUtil.addImportSyntax(iptList, "import org.springframework.stereotype.Repository;");
		/**************************** 스테레오타입 로직 삽입구 끝 ***************************/

		ipt = GenUtil.getImportSyntax(iptList);

		stringBuffer.append(pkg);
		stringBuffer.append(ipt);
		stringBuffer.append(TEXT_1);
		stringBuffer.append(GenUtil.firstCahrToLowerCase(cls.getName()));
		stringBuffer.append(TEXT_2);
		stringBuffer.append(TEXT_3);
		stringBuffer.append(clsDef);
		stringBuffer.append(TEXT_4);
		stringBuffer.append(intfce);
		stringBuffer.append(TEXT_5);
		stringBuffer.append(member);
		stringBuffer.append(method);
		stringBuffer.append(TEXT_6);
		return stringBuffer.toString();
	}
}
