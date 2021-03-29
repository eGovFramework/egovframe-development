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
	 * 버전 인스턴스를 생성한다.
	 * @param version 버전 문자열
	 * @param properties 프로퍼티 맵
	 */
	public Version(String version, PomMap properties) {
		this(version);
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
	 * 버전간의 비교를 수행한다.
	 * @param o 비교할 버전
	 * @return 비교 결과
	 */
	public int compareTo(Version o) {
		return this.getContent().compareTo(o.getContent());
	}

	/**
	 * 버전 내용을 설정한다. 프로퍼티 맵이 있을 경우 실제 버전도 같이 설정된다.
	 * @param element 버전 엘레멘트
	 */
	@Override
	protected void setContent(Element element) {
		super.setContent(element);
		setPropertyVersion(false);
		realVersion = getContent();
		if (properties != null && StringHelper.isPropertyString(getContent())) {
			String version = StringHelper.getProperty(getContent());
			if (version != null && version.length()>0) {
				if (properties.getValue(version) != null)
					setPropertyVersion(true);
					realVersion = properties.getValue(version).toString();
			}
		}
	}
}
