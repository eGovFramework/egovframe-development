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

import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Type;

import egovframework.dev.imp.codegen.model.generator.IGenerator;
import egovframework.dev.imp.codegen.model.util.GenUtil;

/**
 * eGovFramework 의 DataType 스테레오타입 클래스 모델 소스를 생성하는 자바 클래스
 * <p>
 * <b>NOTE:</b> DataType 클래스 모델 템플릿.
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
public class TemplateDataType implements IGenerator {
	/** 개행 문자 */
	protected static String nl;

	/**
	 * 
	 * 템플릿 인스턴스 생성
	 * 
	 * @param lineSeparator
	 * @return
	 */
	public static TemplateDataType create(String lineSeparator) {
		nl = lineSeparator;
		TemplateDataType result = new TemplateDataType();
		nl = null;
		return result;
	}

	/** 개행 문자 */
	public String NL = nl == null ? (System.getProperties()
			.getProperty("line.separator")) : nl;
	/** 템플릿 요소 1 */
	protected String TEXT_1 = NL + " " + NL + "/**" + NL + " * Description : "
			+ NL + " * @author" + NL + " * @since" + NL + " * @version" + NL
			+ " * @see" + NL + " *" + NL + " * <pre>" + NL
			+ " * << Modification Information >>" + NL + " *   " + NL
			+ " *   Date     Modifier               Expression" + NL
			+ " *  -------    --------    ---------------------------" + NL
			+ " *  " + NL + " *" + NL + " * " + NL + " * </pre>" + NL + " */";
	/** 템플릿 요소 2 */
	protected String TEXT_2 = NL;
	/** 템플릿 요소 3 */
	protected String TEXT_3 = " {}";

	/* 
	 * 자바 코드 생성 
	 * 
	 * (non-Javadoc)
	 * @see egovframework.dev.imp.codegen.model.generator.IGenerator#generate(java.lang.Object)
	 */
	public String generate(Object element) throws Exception {
		final StringBuffer stringBuffer = new StringBuffer();

		// (Object element)
		DataType dataType = (DataType) element;

		// Package 구문 생성
		String basePkg = GenUtil.getPackage((Type) dataType);
		String pkg = GenUtil.getPackageSyntax(basePkg);

		// Class 정의
		String dataTypeDef = "public class " + dataType.getName();

		stringBuffer.append(pkg);
		stringBuffer.append(TEXT_1);
		stringBuffer.append(TEXT_2);
		stringBuffer.append(dataTypeDef);
		stringBuffer.append(TEXT_3);
		return stringBuffer.toString();
	}
}
