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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.Text;

import egovframework.rte.rdt.pom.parser.PomParser;

/**
 * 프로젝트에서 사용되는 POM 파일을 모델링한 모델 클래스.
 */
@SuppressWarnings("all")
public class PomObject implements DetailPom {

	/**
	 * 원본 Pom 파일
	 */
	private File pomFile;
	/**
	 * 원본 JDOM XML Document 인스턴스
	 */
	private Document document;

	/**
	 * 디펜던시 엘레멘트 들의 루트 엘레멘트
	 */
	private Element dependencyRoot;
	/**
	 * 마지막 디펜던시 엘레멘트
	 */
	private Element lastDependency;
	/**
	 * 네임스페이스
	 */
	private Namespace namespace;
	/**
	 * 새롭게 추가되는 줄의 인덴테이션
	 */
	public static Text newlineIndent;
	/**
	 * 첫번째 줄의 인덴테이션
	 */
	public static Text firstIndent;
	/**
	 * 중간 줄의 인덴테이션
	 */
	public static Text middleIndent;
	/**
	 * 마지막 줄의 인덴테이션
	 */
	public static Text lastIndent;
	/**
	 * 디펜던시가 끝나는 부분의 인덴테이션
	 */
	public static Text dependencyEndIndent;

	/**
	 * 인덴테이션 배열
	 */
	public static Text[] indents = { newlineIndent, firstIndent, middleIndent, lastIndent, dependencyEndIndent };

	/**
	 * 디펜던시 맵
	 */
	private Map<String, Dependency> dependencies;
	/**
	 * 저장소 맵
	 */
	private Map<String, Repository> repositories;
	/**
	 * 프로퍼티 맵
	 */
	private PomMap properties;
	/**
	 * 모델 버전
	 */
	private Version modelVersion;
	/**
	 * 그룹ID
	 */
	private PomString groupId;
	/**
	 * 아티팩트ID
	 */
	private PomString artifactId;
	/**
	 * 패키징 타입
	 */
	private PomString packaging;
	/**
	 * 이름
	 */
	private PomString name;
	/**
	 * URL
	 */
	private PomString url;
	/**
	 * 버전
	 */
	private Version version;
	/**
	 * 빌드 정의
	 */
	private BuildDeclaration build;

	/**
	 * 정적 초기화 구문
	 */
	static {
		Text empyNewLine = new Text("\n  ");
		firstIndent = (Text) empyNewLine.clone();
		newlineIndent = (Text) empyNewLine.clone();
		middleIndent = (Text) empyNewLine.clone();
		lastIndent = (Text) empyNewLine.clone();
		dependencyEndIndent = (Text) empyNewLine.clone();
	}

	/**
	 * 새로운 Pom 객체 인스턴스를 생성한다.
	 */
	public PomObject() {
		dependencies = new HashMap<String, Dependency>();
		repositories = new HashMap<String, Repository>();
		namespace = Namespace.NO_NAMESPACE;

	}

	/**
	 * 프로퍼티를 가져온다.
	 * @return 프로퍼티 맵
	 */
	public PomMap getProperties() {
		return properties;
	}

	/**
	 * 프로퍼티를 설정한다.
	 * @param properties 프로퍼티 맵
	 */
	public void setProperties(PomMap properties) {
		this.properties = properties;
	}
	
	/**
	 * 특정 프로퍼티를 변경한다.
	 * @param key 프로퍼티 키 값
	 * @param version 변경할 버전
	 */
	public void changeProperty(String key, String version) {
		PomElement ps = properties.getValue(key);

		Element e = ps.getElement();
		e.setText(version);
		ps.setContent(e);
	}

	/**
	 * XML Document 인스턴스를 가져온다.
	 * @return XML Document 인스턴스
	 */
	public Document getDocument() {
		return document;
	}
	
	/**
	 * XML Document 인스턴스를 설정한다.
	 * @param document XML Document 인스턴스
	 */
	public void setDocument(Document document) {
		this.document = document;
	}

	/**
	 * Pom 오브젝트가 인스턴스화한 원본 파일을 가져온다.
	 * @return 원본 파일
	 */
	public File getPomFile() {
		return pomFile;
	}
	/**
	 * 원본 파일을 설정한다.
	 * @param file 원본 파일
	 */
	public void setPomFile(File pomFile) {
		this.pomFile = pomFile;
	}
	/**
	 * 디펜던시 목록을 가져온다.
	 * @return 디펜던시 목록
	 */
	public List<Dependency> listDependencies() {
		return new ArrayList(dependencies.values());
	}
	/**
	 * 디펜던시를 새롭게 추가한다.
	 * @param dependency 추가할 디펜던시
	 */
	private void addDependency(Dependency dependency) {
		dependencies.put(dependency.getId(), dependency);
		lastDependency = dependency.getElement();
	}

	/**
	 * 디펜던시를 변경한다.
	 * @param dependency 변경할 디펜던시
	 */
	private void changeDependency(Dependency dependency) {
		dependencies.remove(dependency.getId());
		dependencies.put(dependency.getId(), dependency);
		lastDependency = dependency.getElement();
	}
	/**
	 * 디펜던시를 추가한다.
	 * @param dependency 추가할 디펜던시 인스턴스
	 */
	public void insertDependency(Dependency dependency) {
		Element element = new Element("dependency", namespace);
		Element groupId = new Element("groupId", namespace);
		Element artifactId = new Element("artifactId", namespace);
		Element scope = new Element("scope", namespace);
		Element version = new Element("version", namespace);
		groupId.setText(dependency.getGroupId());
		artifactId.setText(dependency.getArtifactId());
		scope.setText(dependency.getScope());
		version.setText(dependency.getVersion().getContent());
		element.addContent((Text) firstIndent.clone());
		element.addContent(groupId);
		element.addContent((Text) middleIndent.clone());
		element.addContent(artifactId);
		element.addContent((Text) middleIndent.clone());
		if (scope.getText() != null && scope.getText().trim().length() > 0) {
			element.addContent(scope);
			element.addContent((Text) middleIndent.clone());
		}
		element.addContent(version);
		element.addContent((Text) lastIndent.clone());

		//lastDependency 끝의 공백을 다지운다.
		for (int index = dependencyRoot.indexOf(lastDependency) + 1; index < dependencyRoot.getContentSize(); index++) {
			dependencyRoot.removeContent(index);
		}

		dependencyRoot.addContent((Text) newlineIndent.clone());
		dependencyRoot.addContent(element);
		dependencyRoot.addContent((Text) dependencyEndIndent.clone());

		dependency.setElement(element);
		addDependency(dependency);
	}
	
	/**
	 * 저장소 목록을 가져온다.
	 * @return 저장소 목록
	 */
	public List<Repository> listRepositories() {
		return new ArrayList(repositories.values());
	}

	/**
	 * 저장소를 추가한다.
	 * @param repository 추가할 저장소
	 */
	private void addRepository(Repository repository) {

	}
	/**
	 * 모델 버전을 가져온다.
	 * @return Pom 모델 버전
	 */
	public Version getModelVersion() {
		return modelVersion;
	}
	/**
	 * XML Document 인스턴스를 설정한다.
	 * @param element XML Document 루트 엘레멘트
	 */
	public void setModelVersion(Version modelVersion) {
		this.modelVersion = modelVersion;
	}

	/**
	 * 디펜던시 맵을 가져온다.
	 * @return 디펜던시 맵
	 */
	public Map<String, Dependency> getDependencies() {
		return dependencies;
	}

	/**
	 * 저장소 맵을 가져온다.
	 * @return 저장소 맵
	 */
	public Map<String, Repository> getRepositories() {
		return repositories;
	}
	/**
	 * Pom 오브젝트 전체의 그룹ID를 가져온다.
	 * @return 그룹ID
	 */
	public PomString getGroupId() {
		return groupId;
	}
	/**
	 * 그룹ID를 설정한다.
	 * @param groupId 그룹ID
	 */
	public void setGroupId(PomString groupId) {
		this.groupId = groupId;
	}
	/**
	 * Pom 오브젝트 전체의 아티팩트ID를 가져온다.
	 * @return 아티팩트ID
	 */
	public PomString getArtifactId() {
		return artifactId;
	}
	/**
	 * 아티팩트ID를 설정한다.
	 * @param artifactId 아티팩트ID
	 */
	public void setArtifactId(PomString artifactId) {
		this.artifactId = artifactId;
	}
	/**
	 * Pom 오브젝트의 패키징 타입을 가져온다.
	 * @return 패키징 타입
	 */
	public PomString getPackaging() {
		return packaging;
	}
	/**
	 * 패키징 타입을 설정한다.
	 * @param packaging 패키징 타입
	 */
	public void setPackaging(PomString packaging) {
		this.packaging = packaging;
	}
	/**
	 * Pom 오브젝트의 이름을 가져온다.
	 * @return Pom 이름
	 */
	public PomString getName() {
		return name;
	}
	/**
	 * 이름을 설정한다.
	 * @param element 이름 엘레멘트
	 */
	public void setName(PomString name) {
		this.name = name;
	}
	/**
	 * Pom 오브젝트의 URL을 가져온다.
	 * @return Pom URL
	 */
	public PomString getUrl() {
		return url;
	}
	/**
	 * URL을 설정한다.
	 * @param url URL
	 */
	public void setUrl(PomString url) {
		this.url = url;
	}
	/**
	 * Pom 오브젝트 전체의 버전을 가져온다.
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
	 * Pom 오브젝트의 빌드 정의를 가져온다.
	 * @return 빌드 정의
	 */
	public BuildDeclaration getBuild() {
		return build;
	}
	/**
	 * 빌드 정의를 설정한다.
	 * @param build 빌드 정의
	 */
	public void setBuild(BuildDeclaration build) {
		this.build = build;
	}
	/**
	 * 아티팩트ID를 설정한다.
	 * @param artifactId 아티팩트ID
	 */
	public void setArtifactId(Element element) {
		this.artifactId = new PomString(element);
	}
	/**
	 * 빌드 정의를 설정한다.
	 * @param element 빌드 정의 엘레멘트
	 */
	public void setBuild(Element element) {
		this.build = new BuildDeclaration(element);
	}
	/**
	 * 그룹ID를 설정한다.
	 * @param element 그룹ID 엘레멘트
	 */
	public void setGroupId(Element element) {
		this.groupId = new PomString(element);
	}
	/**
	 * 모델버전을 설정한다.
	 * @param element 모델 버전 엘레멘트
	 */
	public void setModelVersion(Element element) {
		this.modelVersion = new Version(element);
	}
	/**
	 * 이름을 설정한다.
	 * @param element 이름 엘레멘트
	 */
	public void setName(Element element) {
		this.name = new PomString(element);
	}
	/**
	 * 패키징 타입을 설정한다.
	 * @param element 패키징 타입 엘레멘트
	 */
	public void setPackaging(Element element) {
		this.packaging = new PomString(element);
	}
	/**
	 * URL을 설정한다.
	 * @param element URL 엘레멘트
	 */
	public void setUrl(Element element) {
		this.url = new PomString(element);
	}
	/**
	 * 버전을 설정한다.
	 * @param element 버전 엘레멘트
	 */
	public void setVersion(Element element) {
		if (properties != null) {
			this.version = new Version(element, properties);
		} else {
			this.version = new Version(element);
		}
	}
	/**
	 * 프로퍼티를 설정한다.
	 * @param element 프로퍼티 엘레멘트
	 */
	public void setProperties(Element element) {
		this.properties = new PomMap(element);
	}
	/**
	 * 저장소를 설정한다.
	 * @param element 저장소 루트 엘레멘트
	 */
	public void setRepositories(Element element) {

	}
	/**
	 * 디펜던시를 설정한다.
	 * @param element 디펜던시 루트 엘레멘트
	 */
	public void setDependencies(Element element) {
		List childs = element.getChildren();
		dependencyRoot = element;
		for (Object o : childs) {
			if (o instanceof Element) {
				Element e = (Element) o;
				if (e.getName().equals("dependency")) {
					if (properties != null) {
						addDependency(new Dependency(e, properties));
					} else {
						addDependency(new Dependency(e));
					}
				}
			}
		}

		assayDependency();
	}

	/**
	 * 디펜던시의 인덴테이션을 측정하여 저장한다. 저장된 인덴테이션은 새로운 디펜던시를 추가할 때 사용된다.
	 */
	private void assayDependency() {
		if (dependencyRoot != null && lastDependency != null) {
			newlineIndent = new Text(dependencyRoot.getContent(dependencyRoot.indexOf(lastDependency) - 1).getValue());
			//dependencyEndIndent = new Text(dependencyRoot.getContent(dependencyRoot.indexOf(lastDependency)+1).getValue());
			lastIndent = new Text(lastDependency.getContent(lastDependency.getContentSize() - 1).getValue());
			firstIndent = new Text(lastDependency.getContent(0).getValue());
			middleIndent = new Text(lastDependency.getContent(2).getValue());
		}
	}
	/**
	 * 디펜던시를 삭제한다.
	 * @param dependencyId 삭제할 디펜던시ID
	 */
	public void removeDependency(String dependencyId) {
		Dependency dependencyToRemove = dependencies.get(dependencyId);
		Element elementToRemove = dependencyToRemove.getElement();
		int elementIndexToRemove = dependencyRoot.indexOf(elementToRemove);

		dependencyRoot.removeContent(elementIndexToRemove); // Content삭제
		dependencyRoot.removeContent(elementIndexToRemove); // Content자리의 빈공간 삭제

		dependencies.remove(dependencyId);
	}
	/**
	 * 저장소를 삭제한다.
	 * @param repositoryId 삭제할 저장소ID
	 */
	public void removeRepository(String repositoryId) {

	}
	/**
	 * 디펜던시 버전을 변경한다.
	 * @param dependencyId 변경할 디펜던시ID
	 * @param version 변경할 버전
	 */
	public void changeVersion(String dependencyId, Version version) {
		Dependency dependencyTochange = dependencies.get(dependencyId);
		dependencyTochange.getElement().getChild("version", getNamespace()).setText(version.toString());
		//changeDependency(dependencyTochange);
	}
	/**
	 * Pom 인스턴스의 변경사항을 반영한다.
	 */
	public void commit() {
		PomParser.writeDocument(this);
	}

	public void commit(File file) {

		OutputStream os = null;
		try {
			os = new FileOutputStream(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		PomParser.writeDocument(this, os);
	}
	/**
	 * Pom 인스턴스의 변경사항을 파일로 작성한다.
	 * @param file 파일 인스턴스
	 */
	public void commit(File file, File backupFile) {

	}
	/**
	 * 네임스페이스를 설정한다.
	 * @param ns 네임스페이스
	 */
	public void setNamespace(Namespace ns) {
		this.namespace = ns;
	}
	/**
	 * XML 네임스페이스를 가져온다.
	 * @return 네임스페이스
	 */
	public Namespace getNamespace() {
		return namespace;
	}
	/**
	 * Pom 인스턴스의 정보를 가져온다.
	 * @return Pom 인스턴스 정보
	 */
	@Override
	public String toString() {
		return "PomObject [pomFile=" + pomFile + ", dependencies=" + dependencies + ", repositories=" + repositories + ", properties=" + properties
				+ ", modelVersion=" + modelVersion + ", groupId=" + groupId + ", artifactId=" + artifactId + ", packaging=" + packaging + ", name=" + name
				+ ", url=" + url + ", version=" + version.getRealVersion() + ", build=" + build + "]";
	}

}
