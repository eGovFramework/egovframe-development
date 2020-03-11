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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jdom.Element;

/**
 * POM에서 사용되는 원소들을 key와 value 쌍으로 저장하는 맵으로 특정 루트 엘레멘트를 전달하면 해당 루트 엘레멘트의 하위 엘레멘트들을 모두 일괄적으로 맵에 등록할 수 있다.
 */
public class PomMap extends PomElement {

	/**
	 * POM 객체들을 저장하고 있는 맵
	 */
	private Map<String, PomElement> map;

	/**
	 * 새로운 Pom 맵을 생성한다.
	 */
	public PomMap() {
		map = new HashMap<String, PomElement>();
	}

	/**
	 * 새로운 Pom 맵을 생성한다. 이 때 전달된 엘레멘트의 하위 엘레멘트를 모두 등록하면서 생성한다. 
	 * @param element 등록할 엘레멘트의 루트
	 */
	public PomMap(Element element) {
		this();
		setContent(element);
	}
	
	/**
	 * 특정 POM 엘레멘트를 가져온다.
	 * @param key 엘레멘트의 키 값
	 * @return POM 엘레멘트
	 */
	public PomElement getValue(String key) {
		return map.get(key);
	}
	
	/**
	 * 주어진 원소의 하위 엘레멘트를 모두 맵에 등록한다. 단, 하위 엘레멘트의 하위 엘레멘트는 대상이 되지 않는다.
	 * @param element 등록할 엘레멘트의 루트
	 */
	@Override
	protected void setContent(Element element) {
		List<?> childs = element.getChildren();
		for (Object o : childs) {
			if (o instanceof Element) {
				Element e = (Element) o;
				map.put(e.getName(), new PomString(e));
			}
		}
	}

	/**
	 * Map에 등록된 키 값 및 원소들의 정보를 가져온다.
	 * @return Pom map 정보
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (Entry<String, PomElement> e : map.entrySet()) {
			sb.append(e.getKey() + " : " + e.getValue() + " \n");
		}

		return sb.toString();
	}

}
