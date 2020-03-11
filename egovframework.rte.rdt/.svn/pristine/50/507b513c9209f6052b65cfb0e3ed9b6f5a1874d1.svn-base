/*
 * Copyright 2011 MOSPA(Ministry of Security and Public Administration).
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
package egovframework.rte.rdt.pom.unit;

import org.jdom.Element;

/**
 * 모든 Pom 객체에 대한 추상 상위 클래스. 모든 Pom 객체는 JDOM 엘레멘트로 부터 생성되기 때문에 폼 엘레멘트는 setContent 메소드를 
 */
public abstract class PomElement {
	/**
	 * 원본 JDOM 엘레멘트
	 */
	protected Element originalElement;
	
	/**
	 * Pom 엘레멘트의 인스턴스를 생성한다.
	 */
	public PomElement() {

	}
	
	/**
	 * 원본 JDOM 엘레멘트를 이용하여 Pom 엘레멘트 인스턴스를 생성한다.
	 * @param originalElement 원본 엘레멘트
	 */
	public PomElement(Element originalElement) {
		setElement(originalElement);
	}

	/**
	 * 원본 JDOM 엘레멘트를 이용하여 Pom 엘레멘트를 설정한다. 만약 null 엘레멘트가 전달될 경우 아무것도 하지 않는다.
	 * @param elementToSet 설정할 JDOM 엘레멘트
	 */
	public void setElement(Element elementToSet) {
		originalElement = elementToSet;
		if (elementToSet != null) {
			setContent(elementToSet);
		}
	}
	
	/**
	 * 원본 JDOM 엘레멘트 인스턴스를 가져온다.
	 * @return JDOM 인스턴스
	 */
	public Element getElement() {
		return originalElement;
	}

	/**
	 * JDOM 엘레멘트를 이용하여 POM 객체 인스턴스를 생성해낸다. 각 POM 객체에 따라 설정 방법이 모두 다를 수 있다.
	 * @param element JDOM 엘레멘트
	 */
	protected abstract void setContent(Element element);
	
}
