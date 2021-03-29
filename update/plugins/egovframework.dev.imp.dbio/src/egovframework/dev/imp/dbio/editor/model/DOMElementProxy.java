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


import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.wst.xml.core.internal.document.ElementImpl;
import org.eclipse.wst.xml.core.internal.document.NodeImpl;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import egovframework.dev.imp.dbio.DBIOPlugin;

/**
 * DOMElementProxy 모델 객체
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
@SuppressWarnings("restriction")
public class DOMElementProxy {

	protected Element element;
	private Object parent;

	/**
	 * 생성자
	 * 
	 * @param element
	 */
	public DOMElementProxy(Element element) {
		this(element, null);
	}
	
	/**
	 * 생성자
	 * 
	 * @param element
	 * @param parent
	 */
	public DOMElementProxy(Element element, Object parent) {
		this.parent = parent;
		this.element = element;
	}

	/**
	 * 부모요소 반환
	 * 
	 * @return 부모요소
	 */
	public Object getParent() {
		return parent;
	}
	
	/**
	 * 객체가 가지고 있는 정보 반환
	 * @return 요소 정보
	 */
	public Element getDOMElement() {
		return element;
	}
	
	/**
	 * 정보 비교
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DOMElementProxy)) return false;
		
		return ((DOMElementProxy) obj).getDOMElement().equals(getDOMElement());
	}
	
	protected static String getTextContent(Element element) {
		StringBuffer buf = new StringBuffer();
		NodeList children = element.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			NodeImpl child = (NodeImpl) children.item(i);
			buf.append(child.getSource());
		}
		return buf.toString();
	}
	
	protected static void setTextContent(Element element, String value) {
		String tmpValue = value;
		
		String lp = System.getProperty("line.separator"); //$NON-NLS-1$
		tmpValue = lp + tmpValue + lp;
		ElementImpl impl = (ElementImpl) element;
		int length = impl.getEndStartOffset() - impl.getStartEndOffset();
		try {
			impl.getStructuredDocument().replace(impl.getStartEndOffset(), length, tmpValue);
		} catch (BadLocationException e) {
			DBIOPlugin.getDefault().getLog().log(new Status(IStatus.ERROR,DBIOPlugin.PLUGIN_ID, e.getMessage(), e));
		}
	}
}
