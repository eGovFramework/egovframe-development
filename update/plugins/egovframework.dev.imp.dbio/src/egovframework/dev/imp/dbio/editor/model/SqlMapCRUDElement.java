/*
 * Copyright 2008-2009 the original author or authors.
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
package egovframework.dev.imp.dbio.editor.model;

import org.w3c.dom.Element;

/**
 * SqlMap File의 모델 객체
 * @author 개발환경 개발팀 김형조
 * @since 2009.02.20
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.02.20  김형조          최초 생성
 *
 * 
 * </pre>
 */
public class SqlMapCRUDElement extends SqlMapElement {

	/**
	 * 생성자
	 * 
	 * @param element
	 * @param parent
	 */
	public SqlMapCRUDElement(Element element, Object parent) {
		super(element, parent);
	}
	
	/**
	 * SQL 문 반환
	 * @return SQL 문
	 */
	public String getSQLStatement() {
		return getTextContent(element);
	}

	/**
	 * SQL 문 설정
	 * 
	 * @param value
	 */
	public void setSQLStatement(String value) {
		setTextContent(element, value);
	}

	/**
	 * ParameterMap 정보 반환
	 * 
	 * @return ParameterMap명
	 */
	public String getParameterMap() {
		return element.getAttribute("parameterMap"); //$NON-NLS-1$
	}
	
	/**
	 * ParameterMap 정보 설정
	 * 
	 * @param value
	 */
	public void setParameterMap(String value) {
		if(value == null || value.equals(""))
			element.removeAttribute("parameterMap");
		else
			element.setAttribute("parameterMap", value); //$NON-NLS-1$
	}
	
	/**
	 * ParameterClass 정보 반환
	 * 
	 * @return ParameterClass명
	 */
	public String getParameterClass() {
		return element.getAttribute("parameterClass"); //$NON-NLS-1$
	}
	
	/**
	 * ParameterClass 정보 설정
	 * 
	 * @param value
	 */
	public void setParameterClass(String value) {
		if(value == null || value.equals(""))
			element.removeAttribute("parameterClass");
		else
			element.setAttribute("parameterClass", value); //$NON-NLS-1$
	}
	
	public String getTimeout() {
		return element.getAttribute("timeout"); //$NON-NLS-1$
	}
	
	public void setTimeout(String value) {
		element.setAttribute("timeout", value); //$NON-NLS-1$
	}
	
	/**
	 * 선택된 항목의 TagName 을 반환한다.
	 * @return TagName
	 */
	public String getTagName() {
		return element.getTagName();
	}

}
