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

import egovframework.rte.rdt.pom.util.StringHelper;

/**
 * Pom 파일에서 사용되는 모든 버전 표현을 담당하고 있는 클래스
 */
public class Version extends PomString implements Comparable<Version> {
	/**
	 * 버전 정보를 담고 있는 프로퍼티 맵
	 */
	protected PomMap properties;
	/**
	 * 실제 버전
	 */
	protected String realVersion;
	/**
	 * 프로퍼티에 지정된 버전인지 여부
	 */
	protected boolean propertyVersion;
	/**
	 * 프로퍼티가 다른 프로퍼티를 참조하는 연쇄를 따라가는 최대 단계. 순환 참조에서 무한 반복을 막는다.
	 */
	private static final int MAX_PROPERTY_DEPTH = 10;
	
	/**
	 * 버전 인스턴스를 생성한다.
	 * @param version 버전 문자열
	 */
	public Version(String version) {
		super(version);
	}
	
	/**
	 * 버전 인스턴스를 생성한다.
	 * @param e 버전 엘레멘트
	 */
	public Version(Element e) {
		super(e);
	}
	
	/**
	 * 버전 인스턴스를 생성한다.
	 * @param e 버전 엘레멘트
	 * @param properties 프로퍼티 맵
	 */
	public Version(Element e, PomMap properties) {
		this(e);
		setProperties(properties, e);
	}
	
	/**
	 * 버전 인스턴스를 생성한다. 버전 문자열이 프로퍼티 참조이면 프로퍼티 맵에서 실제 버전을 찾아 설정한다.
	 * @param version 버전 문자열
	 * @param properties 프로퍼티 맵
	 */
	public Version(String version, PomMap properties) {
		super();
		this.properties = properties;
		setContent(version);
	}
	
	/**
	 * 프로퍼티에 기록된 실제 버전을 가져온다. 
	 * @return 실제 버전
	 */
	public String getRealVersion() {
		return realVersion;
	}

	/**
	 * 프로퍼티를 설정한다.
	 * @param properties 프로퍼티 맵
	 * @param e 버전 엘레멘트
	 */
	public void setProperties(PomMap properties, Element e) {
		this.properties = properties;
		setContent(e);
	}
	/**
	 * 프로퍼티 버전인지 여부를 가져온다.
	 * @return 프로퍼티 버전 여부
	 */
	public boolean isPropertyVersion() {
		return propertyVersion;
	}
	/**
	 * 프로퍼티 버전 여부를 설정한다.
	 * @param propertyVersion 프로퍼티 버전
	 */
	public void setPropertyVersion(boolean propertyVersion) {
		this.propertyVersion = propertyVersion;
	}

	/**
	 * 프로퍼티 참조(${...}) 형태이지만 프로퍼티 맵에서 값을 찾지 못해 실제 버전을 알 수 없는지 여부를 가져온다.
	 * @return 프로퍼티를 해석할 수 없으면 true
	 */
	public boolean isUnresolvedProperty() {
		String content = getContent();
		return content != null && StringHelper.isPropertyString(content) && !propertyVersion;
	}

	/**
	 * 이 버전이 주어진 버전보다 오래되었다고 확정할 수 있는지 여부를 가져온다.
	 * 어느 한쪽이라도 실제 버전을 알 수 없으면(버전이 없거나 프로퍼티를 해석할 수 없으면) 판단을 유보하고 false 를 반환한다.
	 * @param other 비교할 버전
	 * @return 실제 버전 기준으로 이 버전이 더 오래되었으면 true
	 */
	public boolean isOlderThan(Version other) {
		if (realVersion == null || other == null || other.realVersion == null) {
			return false;
		}
		if (isUnresolvedProperty() || other.isUnresolvedProperty()) {
			return false;
		}
		return compareTo(other) < 0;
	}

	/**
	 * 버전간의 비교를 수행한다. 프로퍼티 참조가 아닌 실제 버전을 비교한다.
	 * @param o 비교할 버전
	 * @return 비교 결과
	 */
	public int compareTo(Version o) {
		return this.realVersion.compareTo(o.realVersion);
	}

	/**
	 * 버전 내용을 설정한다. 프로퍼티 맵이 있고 내용이 프로퍼티 참조이면 프로퍼티가 다른 프로퍼티를 가리키는 연쇄까지 따라가
	 * 실제 버전을 찾아 설정하고, 그렇지 않으면 내용을 그대로 실제 버전으로 삼는다.
	 * 연쇄 도중 프로퍼티를 찾을 수 없거나 순환 참조이면 해석하지 못한 것으로 두어 isUnresolvedProperty 가 true 가 된다.
	 * @param content 버전 문자열
	 */
	@Override
	public void setContent(String content) {
		super.setContent(content);
		setPropertyVersion(false);
		realVersion = content;
		if (properties == null || content == null) {
			return;
		}
		String resolved = content;
		int depth = 0;
		while (StringHelper.isPropertyString(resolved) && depth < MAX_PROPERTY_DEPTH) {
			PomElement value = properties.getValue(StringHelper.getProperty(resolved));
			if (value == null) {
				return;
			}
			resolved = value.toString();
			depth++;
		}
		if (depth > 0 && !StringHelper.isPropertyString(resolved)) {
			setPropertyVersion(true);
			realVersion = resolved;
		}
	}

	/**
	 * 버전 내용을 설정한다. 엘레멘트가 null 이면(pom 에 version 이 없으면) 아무것도 하지 않는다.
	 * @param element 버전 엘레멘트
	 */
	@Override
	protected void setContent(Element element) {
		if (element == null) {
			return;
		}
		super.setContent(element);
	}
}
