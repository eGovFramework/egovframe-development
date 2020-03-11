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

import java.io.File;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * Pom 오브젝트에 대한 구체적인 조작을 가할 수 있도록 구성된 인터페이스. 디펜던시의 삽입 및 삭제를 넘어서 더 많은 조작이 필요할 때는 이 인터페이스로 Pom 오브젝트를 선언하여 사용한다. 모든 Pom 오브젝트는 DetailPom 이다.
 */
public interface DetailPom extends Pom {
	
	/**
	 * XML Document 인스턴스를 가져온다.
	 * @return XML Document 인스턴스
	 */
	public Document getDocument();
	/**
	 * 모델 버전을 가져온다.
	 * @return Pom 모델 버전
	 */
	public Version getModelVersion();
	/**
	 * Pom 오브젝트 전체의 그룹ID를 가져온다.
	 * @return 그룹ID
	 */
	public PomString getGroupId();
	/**
	 * Pom 오브젝트 전체의 아티팩트ID를 가져온다.
	 * @return 아티팩트ID
	 */
	public PomString getArtifactId();
	/**
	 * Pom 오브젝트의 패키징 타입을 가져온다.
	 * @return 패키징 타입
	 */
	public PomString getPackaging();
	/**
	 * Pom 오브젝트 전체의 버전을 가져온다.
	 * @return 버전
	 */
	public Version getVersion();
	/**
	 * Pom 오브젝트의 이름을 가져온다.
	 * @return Pom 이름
	 */
	public PomString getName();
	/**
	 * Pom 오브젝트의 URL을 가져온다.
	 * @return Pom URL
	 */
	public PomString getUrl();
	/**
	 * Pom 오브젝트의 빌드 정의를 가져온다.
	 * @return 빌드 정의
	 */
	public BuildDeclaration getBuild();
	/**
	 * Pom 오브젝트가 인스턴스화한 원본 파일을 가져온다.
	 * @return 원본 파일
	 */
	public File getPomFile();
	/**
	 * 프로퍼티를 가져온다.
	 * @return 프로퍼티
	 */
	public PomMap getProperties();
	/**
	 * XML 네임스페이스를 가져온다.
	 * @return 네임스페이스
	 */
	public Namespace getNamespace();

	/**
	 * XML Document 인스턴스를 설정한다.
	 * @param document XML Document 인스턴스
	 */
	public void setDocument(Document document);
	/**
	 * 모델 버전을 설정한다.
	 * @param modelVersion 모델 버전
	 */
	public void setModelVersion(Version modelVersion);
	/**
	 * 그룹ID를 설정한다.
	 * @param groupId 그룹ID
	 */
	public void setGroupId(PomString groupId);
	/**
	 * 아티팩트ID를 설정한다.
	 * @param artifactId 아티팩트ID
	 */
	public void setArtifactId(PomString artifactId);
	/**
	 * 패키징 타입을 설정한다.
	 * @param packaging 패키징 타입
	 */
	public void setPackaging(PomString packaging);
	/**
	 * 버전을 설정한다.
	 * @param version 버전
	 */
	public void setVersion(Version version);
	/**
	 * 이름을 설정한다.
	 * @param name 이름
	 */
	public void setName(PomString name);
	/**
	 * URL을 설정한다.
	 * @param url URL
	 */
	public void setUrl(PomString url);
	/**
	 * 빌드 정의를 설정한다.
	 * @param build 빌드 정의
	 */
	public void setBuild(BuildDeclaration build);
	/**
	 * 원본 파일을 설정한다.
	 * @param file 원본 파일
	 */
	public void setPomFile(File file);
	/**
	 * 프로퍼티를 설정한다.
	 * @param properties 프로퍼티
	 */
	public void setProperties(PomMap properties);
	/**
	 * 네임스페이스를 설정한다.
	 * @param ns 네임스페이스
	 */
	public void setNamespace(Namespace ns);
	
	/**
	 * 모델버전을 설정한다.
	 * @param element 모델 버전 엘레멘트
	 */
	public void setModelVersion(Element element);
	/**
	 * 그룹ID를 설정한다.
	 * @param element 그룹ID 엘레멘트
	 */
	public void setGroupId(Element element);
	/**
	 * 아티팩트ID를 설정한다.
	 * @param element 아티팩트 엘레멘트
	 */
	public void setArtifactId(Element element);
	/**
	 * 패키징 타입을 설정한다.
	 * @param element 패키징 타입 엘레멘트
	 */
	public void setPackaging(Element element);
	/**
	 * 버전을 설정한다.
	 * @param element 버전 엘레멘트
	 */
	public void setVersion(Element element);
	/**
	 * 이름을 설정한다.
	 * @param element 이름 엘레멘트
	 */
	public void setName(Element element);
	/**
	 * URL을 설정한다.
	 * @param element URL 엘레멘트
	 */
	public void setUrl(Element element);
	/**
	 * 빌드 정의를 설정한다.
	 * @param element 빌드 정의 엘레멘트
	 */
	public void setBuild(Element element);
	/**
	 * 프로퍼티를 설정한다.
	 * @param element 프로퍼티 엘레멘트
	 */
	public void setProperties(Element element);
	/**
	 * 저장소를 설정한다.
	 * @param element 저장소 루트 엘레멘트
	 */
	public void setRepositories(Element element);
	/**
	 * 디펜던시를 설정한다.
	 * @param element 디펜던시 루트 엘레멘트
	 */
	public void setDependencies(Element element);
}
