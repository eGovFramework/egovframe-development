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

/**
 * 
 * 어트리뷰트 정보 클래스 
 * <p><b>NOTE:</b> 어트리뷰트(데이터베이스 특정 테이블의 컬럼) 정보를 담기위한 모델 클래스 
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
public class Attribute extends DbModelElement {
    
    /**
     * 
     * 생성자
     *
     * @param name
     */
    public Attribute(String name) {
        super(name);
    }

    /** 데이터 타입 */
    private String type;
    
    /** 자바 타입 */
    private String javaType;
    
    /** 프라이머리 키 여부 */
    private boolean isPrimaryKey;

    /**
     * 필수 키 여부 가져오기
     * @return
     */
    public boolean getIsPrimaryKey() {
        return isPrimaryKey;
    }
    
    /**
     * 필수 키 여부 가져오기
     * @return
     */
    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }    

    /**
     * 필수키 여부 세팅하기
     * @param isPrimaryKey
     */
    public void setPrimaryKey(boolean isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
    }

    /**
     * 자바 타입 가져오기
     * @return
     */
    public String getJavaType() {
        return javaType;
    }

    /**
     * 자바 타입 세팅하기
     * @param javaType
     */
    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    /**
     * 타입 가져오기
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * 타입 세팅하기
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }
}
