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
 * 폼 내부에서 사용되는 문자열 객체. 폼 엘레멘트의 일부로 사용되기 위하여 작성되었다.
 */
public class PomString extends PomElement {

	/**
	 * 문자열 내용
	 */
	String content;

	/**
	 * 문자열 인스턴스를 생성한다.
	 */
	public PomString() {
		
	}
	
	/**
	 * 문자열 인스턴스를 생성하고 내용을 설정한다.
	 * @param string 설정할 문자열
	 */
	public PomString(String string) {
		setContent(string);
	}
	
	/**
	 * 문자열 인스턴스를 생성하고 내용을 설정한다.
	 * @param element 설정할 문자열을 담고 있는 엘레멘트
	 */
	public PomString(Element element) {
		super(element);
	}

	/**
	 * 문자열 내용을 가져온다.
	 * @return 문자열 내용
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 문자열 내용을 설정한다.
	 * @param content 문자열 내용
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 문자열 내용을 설정한다.
	 * @element 문자열 내용을 가지고 있는 JDOM 엘레멘트
	 */
	@Override
	protected void setContent(Element element) {
		setContent(element.getText());
	}
	
	/**
	 * 문자열 내용을 가져온다.
	 * @return 문자열 내용
	 */
	@Override
	public String toString() {
		return getContent();
	}

}
