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
 * MapperSelectElement 모델
 * @author 개발환경 개발팀 김효수
 * @since 2019.02.25
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2019.02.25  김효수          MyBatis DBIO  
 *
 * 
 * </pre>
 */
public class MapperSelectElement extends MapperCRUDElement {

	/**
	 * 생성자
	 * 
	 * @param element
	 * @param parent
	 */
	public MapperSelectElement(Element element, Object parent) {
		super(element, parent);
	}

	/**
	 * ResultMap명 반환
	 * @return
	 */
	public String getResultMap() {
		return element.getAttribute("resultMap"); //$NON-NLS-1$
	}
	
	/**
	 * ResultMap 설정
	 * @param value
	 */
	public void setResultMap(String value) {
		if(value == null || value.equals(""))
			element.removeAttribute("resultMap");
		else
			element.setAttribute("resultMap", value); //$NON-NLS-1$
	}
	
	/**
	 * ResultClass명 반환
	 * @return
	 */
	public String getResultClass() {
		return element.getAttribute("resultType"); //$NON-NLS-1$
	}
	
	/**
	 * ResultClass명 설정
	 * @param value
	 */
	public void setResultClass(String value) {
		if(value == null || value.equals(""))
			element.removeAttribute("resultType");
		else
			element.setAttribute("resultType", value); //$NON-NLS-1$
	}

	public String getResultXml() {
		return element.getAttribute("xmlResultName");
	}
	
	public void setResultXml(String xmlName) {
		if (xmlName == null || "".equals(xmlName)) {
			element.removeAttribute("resultType");
			element.removeAttribute("xmlResultName");
		} else {
			element.setAttribute("resultType", "xml");
			element.setAttribute("xmlResultName", xmlName);
		}
	}
	/**
	 * CacheModel 명을 반환
	 * @return CacheModel 명
	 */
	public String getResultCacheModel() {
		return element.getAttribute("cacheModel"); //$NON-NLS-1$
	}
	
	/**
	 * CacheModel 명을 설정
	 * @param value 
	 */
	public void setResultCacheModel(String value) {
		if(value == null || value.equals(""))
			element.removeAttribute("cacheModel");
		else
			element.setAttribute("cacheModel", value); //$NON-NLS-1$
	}
}
