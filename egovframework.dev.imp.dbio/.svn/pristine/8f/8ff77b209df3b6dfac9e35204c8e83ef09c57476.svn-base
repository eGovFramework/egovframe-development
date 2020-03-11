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
 * SqlMapParameterMapElement 모델
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
public class SqlMapParameterMapElement extends SqlMapElement {

	public SqlMapParameterMapElement(Element element, Object parent) {
		super(element, parent);
	}
	
	/**
	 * 클래스명 반환
	 * 
	 * @return 클래스명 
	 */
	public String getClassName() {
		return element.getAttribute("class"); //$NON-NLS-1$
	}
	
	/**
	 * 클래스명 설정
	 * 
	 * @param value
	 */
	public void setClassName(String value) {
		element.setAttribute("class", value); //$NON-NLS-1$
	}
}
