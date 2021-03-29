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
 * SqlMapElement 모델
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
public class SqlMapElement extends DOMElementProxy {

	/**
	 * 생성자
	 * 
	 * @param element
	 * @param parent
	 */
	public SqlMapElement(Element element, Object parent) {
		super(element, parent);
	}

	/**
	 * id 정보 반환
	 * 
	 * @return id 정보
	 */
	public String getId() {
		return element.getAttribute("id"); //$NON-NLS-1$
	}
	
	/**
	 * id 정보 설정
	 * 
	 * @param value
	 */
	public void setId(String value) {
		element.setAttribute("id", value); //$NON-NLS-1$
	}
	
	/**
	 * typeAlias 태그의 alias 반환한다.
	 * @return alias
	 */
	public String getAlias() {
		return element.getAttribute("alias");
	}
	
	/**
	 * typeAlias 태그의 alias 의 값을 설정한다.
	 * @param value
	 */
	public void setAlias(String value) {
		element.setAttribute("alias", value);
	}
}
