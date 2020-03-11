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
 * 저장소를 모델링한 클래스
 */
@SuppressWarnings("unused")
public class Repository extends PomElement {
	/**
	 * 저장소 ID
	 */
	private PomString id;
	/**
	 * 저장소 명
	 */
	private PomString name;
	/**
	 * 저장소 URL
	 */
	private PomString url;
	
	/**
	 * 저장소를 설정한다.
	 * @param element 저장소 엘레멘트
	 */
	@Override
	protected void setContent(Element element) {
		
	}
}
