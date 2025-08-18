/*
 * Copyright 2008-2009 MOSPA(Ministry of Security and Public Administration).
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
package egovframework.dev.imp.codegen.template.model;

import egovframework.dev.imp.codegen.template.util.NamingUtils;

/**
 * 
 * 데이터베이스 모델 요소의 상위 클래스
 * <p><b>NOTE:</b> 데이터베이스 모델 요소(테이블, 컬럼)의 최상위 클래스로서 
 * DB명을 세팅하면 Java 코드젠에서 사용되어지는 여러 케이싱 명칭을 반환할 수 있다.
 * 
 * @author 개발환경 개발팀 이흥주
 * @since 2009.08.03
 * @version 1.0
 * @see
 *
 * <pre>
 *  == 개정이력(Modification Information) ==
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.08.03  이흥주          최초 생성
 *
 * </pre>
 */
public class DbModelElement {
        /** 이름 */
	private String name;
	/** Upper Case 명 */
	private String ucName;
	/** Lower Case 명 */
	private String lcName;
	/** Camel Case 명 */
	private String ccName;
	/** Pascal Case 명 */
	private String pcName;

	/**
	 * 
	 * 생성자
	 *
	 * @param name 이름
	 */
	public DbModelElement(String name){
		setName(name);
	}
	
	/**
	 * 이름 가져오기
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 이름 세팅하기
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
		setUcName(name.toUpperCase());
		setLcName(name.toLowerCase());
		setCcName(NamingUtils.convertUnderscoreNameToCamelcase(name));
		setPcName(NamingUtils.convertCamelcaseToPascalcase(getCcName()));
	}
	
	/**
	 * 대문자 이름 반환
	 * 
	 * @return
	 */
	public String getUcName() {
		return ucName;
	}
	
	
	/**
	 * 대문자 이름 세팅 
	 * 
	 * @param uppercaseName
	 */
	public void setUcName(String uppercaseName) {
		this.ucName = uppercaseName;
	}
	
	/**
	 * 소문자 이름 반환
	 * 
	 * @return
	 */
	public String getLcName() {
		return lcName;
	}
	
	/**
	 * 소문자 이름 세팅
	 * 
	 * @param lowercaseName
	 */
	public void setLcName(String lowercaseName) {
		this.lcName = lowercaseName;
	}
	
	/**
	 * 카멜 케이스 이름 반환 
	 * 
	 * @return
	 */
	public String getCcName() {
		return ccName;
	}
	
	/**
	 * 카멜 케이스 이름 세팅
	 * 
	 * @param camelcaseName
	 */
	public void setCcName(String camelcaseName) {
		this.ccName = camelcaseName;
	}
	
	/**
	 * 파스칼 케이스 이름 반환
	 * @return
	 */
	public String getPcName() {
		return pcName;
	}
	
	/**
	 * 파스칼 케이스 이름 세팅
	 * @param pascalcaseName
	 */
	public void setPcName(String pascalcaseName) {
		this.pcName = pascalcaseName;
	}


}
