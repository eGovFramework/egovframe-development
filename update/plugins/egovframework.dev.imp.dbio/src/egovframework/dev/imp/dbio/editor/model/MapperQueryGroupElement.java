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
 * MapperQueryGroupElement 모델
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
public class MapperQueryGroupElement extends MapperGroupElement {

	public MapperQueryGroupElement(Element element) {
		super(element, "Query");
	}

	/* (non-Javadoc)
	 * @see kr.go.MOSPA.ibatis.editor.model.MapperGroupElement#createChildren()
	 */
	
	/**
	 * 쿼리그룹의 구성원을 생성한다.
	 */
	@Override
	protected Object[] createChildren() {
		List<MapperCRUDElement> ret = new LinkedList<MapperCRUDElement>();
		NodeList children = element.getChildNodes();
		for (int i = 0; i< children.getLength(); i++) {
			Node child = children.item(i);
			if (child instanceof Element) {
				Element childElement = (Element) child;
				String name = childElement.getTagName();
				if ("select".equals(name)
						|| "statement".equals(name)) { //$NON-NLS-1$
					ret.add(new MapperSelectElement(childElement, this));
				} else if ("insert".equals(name) //$NON-NLS-1$
						|| "update".equals(name) //$NON-NLS-1$
						|| "delete".equals(name)
						|| "procedure".equals(name)) { //$NON-NLS-1$
					ret.add(new MapperCRUDElement(childElement, this));
				}
			}
		}
		return ret.toArray();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof MapperQueryGroupElement)) return false;
		
		return getDOMElement().equals(((MapperQueryGroupElement) obj).getDOMElement());
	}
}
