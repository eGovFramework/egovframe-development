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

import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * SqlMapParameterMapGroupElement 모델
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
public class SqlMapParameterMapGroupElement extends SqlMapGroupElement {

	public SqlMapParameterMapGroupElement(Element element) {
		super(element, "ParameterMap");
	}

	/**
	 * 구성원 생성
	 */
	@Override
	protected Object[] createChildren() {
		List<SqlMapParameterMapElement> ret = new LinkedList<SqlMapParameterMapElement>();
		NodeList children = element.getChildNodes();
		for (int i = 0; i< children.getLength(); i++) {
			Node child = children.item(i);
			if (child instanceof Element) {
				Element childElement = (Element) child;
				String name = childElement.getTagName();
				if ("parameterMap".equals(name)) { //$NON-NLS-1$
					ret.add(new SqlMapParameterMapElement(childElement, this));
				}
			}
		}
		return ret.toArray();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof SqlMapParameterMapGroupElement)) return false;
		
		return getDOMElement().equals(((SqlMapGroupElement) obj).getDOMElement());
	}

}
