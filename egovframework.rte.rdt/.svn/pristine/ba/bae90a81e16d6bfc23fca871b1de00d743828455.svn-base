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
import java.util.List;

/**
 * 일반적인 저장소 및 디펜던스의 추가 삭제만을 담당하는 간편화된 인터페이스
 */
public interface Pom {
	/**
	 * 디펜던시 목록을 가져온다.
	 * @return 디펜던시 목록
	 */
	public List<Dependency> listDependencies();
	
	/**
	 * 저장소 목록을 가져온다.
	 * @return 저장소 목록
	 */
	public List<Repository> listRepositories();

	/**
	 * 디펜던시를 삭제한다.
	 * @param dependencyId 삭제할 디펜던시ID
	 */
	public void removeDependency(String dependencyId);
	
	/**
	 * 저장소를 삭제한다.
	 * @param repositoryId 삭제할 저장소ID
	 */
	public void removeRepository(String repositoryId);
	
	/**
	 * 디펜던시를 추가한다.
	 * @param dependency 추가할 디펜던시 인스턴스
	 */
	public void insertDependency(Dependency dependency);
	
	/**
	 * 디펜던시 버전을 변경한다.
	 * @param dependencyId 변경할 디펜던시ID
	 * @param version 변경할 버전
	 */
	public void changeVersion(String dependencyId, Version version);
	
	/**
	 * Pom 인스턴스의 변경사항을 반영한다.
	 */
	public void commit();
	
	/**
	 * Pom 인스턴스의 변경사항을 파일로 작성한다.
	 * @param file 파일 인스턴스
	 */
	public void commit(File file);
	
}
