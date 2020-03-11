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
package egovframework.rte.rdt.service.unit;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

/**
 * 서비스들의 정보와 의존성을 표현한다.
 * @author 이영진
 */
public class Service {
	/** 서비스명 */
	private String name; 
	/** 서비스가 포함된 layer 이름 */
	private String layer; 
	/** 의존성이 걸린 갯수 */
	private int count; 
	/** 의존성 있는 라이브러리 리스트 */
	private ArrayList<String> dependency;
	/** 설치여부 */
	private boolean isInstalled = false;
	
	/**
	 * Service 기본 생성자로 dependency 리스트를 초기화 한다.
	 */
	public Service() {
		this.dependency = new ArrayList<String>();
	}
	
	/**
	 * Service 생성자로 dependency 리스트를 초기화 한다.
	 * @param dependencyName
	 */
	public Service(String... dependencyName) {
		this.dependency = new ArrayList<String>();
		setDependency(dependencyName);
	}
	/**
	 * 서비스명을 조회한다.
	 * @return 서비스명
	 */
	public String getName() {
		return name;
	}

	/**
	 * 서비스명을 등록한다.
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 서비스명을 등록한다.
	 * @param name
	 */
	public void setName(Element name) {
		this.name = name.getText();
	}
	
	/**
	 * 서비스가 포함된 layer 이름을 조회한다.
	 * @return
	 */
	public String getLayer() {
		return layer;
	}

	/**
	 * 서비스가 포함된 layer 이름을 등록한다.
	 * @param layer
	 */
	public void setLayer(String layer) {
		this.layer = layer;
	}

	/**
	 * 서비스가 포함된 layer 이름을 등록한다.
	 * @param layer
	 */
	public void setLayer(Element layer) {
		this.layer = layer.getText();
	}

	/**
	 * 의존성이 걸린 갯수를 조회한다.
	 * @return
	 */
	public int getCount() {
		return count;
	}

	/**
	 * 의존성이 걸린 갯수 등록한다.
	 * @param count
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * 의존성 있는 라이브러리들을 조회한다.
	 * @return ArrayList<String> 서비스 리스트
	 */
	public ArrayList<String> getDependency() {
		return dependency;
	}

	/**
	 * 의존성 있는 라이브러리들을 등록한다.
	 * @param dependency
	 */
	public void setDependency(ArrayList<String> dependency) {
		this.dependency = dependency;
	}

	/**
	 * 의존성 있는 라이브러리들을 등록한다.
	 * @param dependenciesName
	 */
	public void setDependency(String... dependenciesName) {
		for (String dependencyName : dependenciesName) {
			this.dependency.add(dependencyName.trim());
		}
		count = dependency.size();
	}
	
	/**
	 * 의존성 있는 라이브러리들을 등록한다.
	 * @param dependenciesName
	 */
	public void setDependency(Element element){
		List<?> childs = element.getChildren();
		for(Object o : childs){
			if(o instanceof Element){
				Element e = (Element)o;
				if("dependency".equals(e.getName())){
					setDependency(e.getValue());
				}
			}
		}
	}

	/**
	 * 설치여부를 조회한다.
	 * @return 설치여부
	 */
	public boolean isInstalled() {
		return isInstalled;
	}

	/**
	 * 설치여부를 등록한다.
	 * @param isInstalled
	 */
	public void setInstalled(boolean isInstalled) {
		this.isInstalled = isInstalled;
	}

	/**
	 * Service객체의 정보를 리턴한다.
	 * @return Service객체의 정보
	 */
	@Override
	public String toString() {
		return "Service [name=" + name + ", layer=" + layer + ", count=" + count + ", dependency="
				+ dependency + ", isInstalled=" + isInstalled + "]";
	}
	
}
