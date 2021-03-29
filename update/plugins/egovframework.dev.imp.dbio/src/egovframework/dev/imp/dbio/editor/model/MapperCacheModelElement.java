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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * MapperCacheModelElement 모델
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
public class MapperCacheModelElement extends MapperElement {
//	public static final String TYPE_MEMORY = "MEMORY";
//	public static final String TYPE_LRU = "LRU";
//	public static final String TYPE_FIFO = "FIFO";
//	public static final String TYPE_OSCACHE = "OSCACHE";
	
	Element intervalElement, propertyElement;
	
	public MapperCacheModelElement(Element element, Object parent) {
		super(element, parent);

		NodeList nodeList = element.getElementsByTagName("flushInterval");
		if(nodeList.getLength() > 0) {
			intervalElement = (Element)nodeList.item(0);
		}
		
		nodeList = element.getElementsByTagName("property");
		if(nodeList.getLength() > 0) {
			propertyElement = (Element)nodeList.item(0);
		}
	}
	
	public void init() {
		this.setId("newCache");
		this.setType("LRU");
		this.setInterval("24");
		this.setProperty("size", "1000");
	}
	
	public boolean isPropertyNode() {
		return (propertyElement == null) ? false:true;
	}
	
	/**
	 * flushInterval 값을 반환한다.
	 * @return 시간
	 */
	public String getInterval() {
		NamedNodeMap namedNodeMap = intervalElement.getAttributes();
		Node node = namedNodeMap.getNamedItem("hours");

		return node.getNodeValue();
	}
	
	/**
	 * property 의 name 을 반환한다.
	 * @return
	 */
	public String getPropertyName() {
		NamedNodeMap namedNodeMap = propertyElement.getAttributes();
		Node node = namedNodeMap.getNamedItem("name");
		
		return node.getNodeValue();
	}
	
	/**
	 * property 의 value 를 반환한다.
	 * @return property 값
	 */
	public String getPropertyValue() {
		NamedNodeMap namedNodeMap = propertyElement.getAttributes();
		Node node = namedNodeMap.getNamedItem("value");
		
		return node.getNodeValue();
	}
	
	/**
	 * flushInterval 값을 설정
	 * @param value
	 */
	public void setInterval(String value) {
		if(intervalElement == null) createIntervalElement();
		intervalElement.setAttribute("hours",value);
	}
	
	private void createIntervalElement() {
		intervalElement = element.getOwnerDocument().createElement("flushInterval");
		element.appendChild(intervalElement);
	}
	
	/**
	 * property 의 value 를 설정
	 * @param value
	 */
	public void setProperty(String name, String value) {
		if(propertyElement == null) createProperty();
		
		propertyElement.setAttribute("name", name);
		propertyElement.setAttribute("value", value);
	}
	
	private void createProperty() {
		propertyElement = element.getOwnerDocument().createElement("property");
		element.appendChild(propertyElement);		
	}

	public void removeProperty() {
		element.removeChild(propertyElement);
	}
	/**
	 * 캐쉬모델 타입을 반환
	 * 
	 * @return 캐쉬모델타입 
	 */
	public String getType() {
		return element.getAttribute("type"); //$NON-NLS-1$
	}
	
	/**
	 * 캐쉬모델 타입 설정
	 * 
	 * @param modelType
	 */
	public void setType(String modelType) {
		element.setAttribute("type", modelType); //$NON-NLS-1$
	}
}
