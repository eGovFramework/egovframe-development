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

import java.util.List;

import org.jdom.Element;

import egovframework.rte.rdt.pom.util.StringHelper;

/**
 * Pom 파일 내에 정의되어 있는 디펜던시 정의의 정보를 저장하는 클래스
 */
public class Dependency extends PomElement {
	/**
	 * 그룹 아이디
	 */
	private String groupId;
	/**
	 * 아티팩트 아이디
	 */
	private String artifactId;
	/**
	 * 스코프
	 */
	private String scope;
	/**
	 * 버전
	 */
	private Version version;
	/**
	 * 최신 버전
	 */
	private Version lastestVersion;
	/**
	 * 설치 여부
	 */
	private boolean isinstalled;

	/**
	 * 디펜던시 인스턴스를 생성한다.
	 */
	public Dependency() {
		
	}
	
	/**
	 * 디펜던시 인스턴스를 생성한다. 
	 * @param e JDOM 원소 인스턴스
	 */
	public Dependency(Element e) {
		super(e);
		setContent(e);
	}
	
	/**
	 * 디펜던시 인스턴스를 생성하면서, 프로퍼티를 참조하여 실제 버전을 알아낸다.
	 * @param e JDOM 원소 인스턴스
	 * @param properties 프로퍼티 맵
	 */
	public Dependency(Element e, PomMap properties) {
		super(e);
		setContent(e, properties);
	}
	
	/**
	 * 디펜던시 ID를 가져온다.
	 * @return 디펜던시 ID
	 */
	public String getId() {
		return StringHelper.concatNameWithDot(groupId, artifactId);
	}

	/**
	 * 그룹 ID를 가져온다.
	 * @return 그룹 ID
	 */
	public String getGroupId() {
		return groupId;
	}

	/**
	 * 그룹 ID를 설정한다.
	 * @param groupId 그룹 ID
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	/**
	 * 아티팩트 ID를 가져온다.
	 * @return 아티팩트 ID
	 */
	public String getArtifactId() {
		return artifactId;
	}

	/**
	 * 아티팩트 ID를 설정한다.
	 * @param artifactId 아티팩트 ID
	 */
	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	/**
	 * 스코프를 가져온다.
	 * @return 스코프
	 */
	public String getScope() {
		return scope;
	}

	/**
	 * 스코프를 설정한다. 
	 * @param scope 스코프
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}

	/**
	 * 버전을 가져온다.
	 * @return 버전
	 */
	public Version getVersion() {
		return version;
	}

	/**
	 * 버전을 설정한다.
	 * @param version 버전
	 */
	public void setVersion(Version version) {
		this.version = version;
	}
	
	/**
	 * 최신 버전을 가져온다.
	 * @return 최신 버전
	 */
	public Version getLastestVersion() {
		return lastestVersion;
	}
	
	/**
	 * 최신 버전을 설정한다. 
	 * @param lastestVersion 최신 버전
	 */
	public void setLastestVersion(Version lastestVersion) {
		this.lastestVersion = lastestVersion;
	}

	/**
	 * 디펜던시 내용을 설정한다.
	 * @param element JDOM 원소 인스턴스
	 * @param properties 프로퍼티 맵
	 */
	protected void setContent(Element element, PomMap properties) {
		List<?> childs = element.getChildren();
		for (Object o : childs) {
			if (o instanceof Element) {
				Element e = (Element) o;
				
				if (e.getName().equals("groupId")) {
					setGroupId(e.getText());
				}
				if (e.getName().equals("artifactId")) {
					setArtifactId(e.getText());
				}
				if (e.getName().equals("version")) {
					if (properties != null) {
						setVersion(new Version(e, properties));
					} else {
						setVersion(new Version(e));
					}
				}
				if (e.getName().equals("scope")) {
					setScope(e.getText());
				}
			}
		}
	}

	/**
	 * 디펜던시 내용을 설정한다.
	 * @param element JDOM 원소 인스턴스
	 */
	@Override
	protected void setContent(Element element) {
		setContent(element, null);
	}

	/**
	 * 디펜던시 정보를 가져온다.,
	 * @return 디펜던시 정보
	 */
	@Override
	public String toString() {
		return "Dependency [groupId=" + groupId + ", artifactId=" + artifactId
				+ ", scope=" + scope + ", version=" + version.realVersion + "]";
	}

	/**
	 * 설치 여부를 설정한다.
	 * @param isinstalled 설치여부
	 */
	public void setIsinstalled(boolean isinstalled) {
		this.isinstalled = isinstalled;
	}

	/**
	 * 설치 여부를 가져온다.
	 * @return 설치 여부
	 */
	public boolean isIsinstalled() {
		return isinstalled;
	}
	
}
