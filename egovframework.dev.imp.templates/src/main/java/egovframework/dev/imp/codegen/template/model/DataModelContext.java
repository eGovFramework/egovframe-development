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

import java.util.List;
/**
 * 
 * 데이터베이스 Context 클래스
 * <p><b>NOTE:</b> 데이터베이스 정보 및 테이블, 컬럼 정보를 담기위한 클래스
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
public class DataModelContext {

    /** 데이터베이스 벤더 */
    private String vender;
    /** 데이터베이스 제품 명 */
    private String databaseProductName;
    /** 엔티티(테이블) 정보 */
    private Entity entity;
    /** 어트리뷰트(컬럼) 정보 */
    private List<Attribute> attributes;
    /** 기본키 컬럼 정보 */
    private List<Attribute> primaryKeys;    
    
    /**
     * 데이타베이스 제품명
     * @return
     */
    public String getDatabaseProductName() {
        return databaseProductName;
    }
    
    /**
     * 데이타베이스 제품명 세팅하기
     * @param databaseProductName
     */
    public void setDatabaseProductName(String databaseProductName) {
        this.databaseProductName = databaseProductName;
    }
    
    /**
     * 벤더 정보 가져오기
     * @return
     */
    public String getVender() {
        return vender;
    }
    
    /**
     * 벤더 정보 세팅하기
     * @param vender
     */
    public void setVender(String vender) {
        this.vender = vender;
    }


    /**
     * 기본키 목록 가져오기
     * 
     * @return
     */
    public List<Attribute> getPrimaryKeys() {
        return primaryKeys;
    }
    
    /**
     * 기본키 목록 세팅하기
     * @param primaryKeys
     */
    public void setPrimaryKeys(List<Attribute> primaryKeys) {
        this.primaryKeys = primaryKeys;
    }
    
    /**
     * 엔티티 가져오기
     * @return
     */
    public Entity getEntity() {
        return entity;
    }
    
    /**
     * 엔티티 세팅하기
     * 
     * @param entity
     */
    public void setEntity(Entity entity) {
        this.entity = entity;
    }
    
    /**
     * 속성 목록 가져오기
     * 
     * @return
     */
    public List<Attribute> getAttributes() {
        return attributes;
    }
    
    /**
     * 속성 목록 세팅하기
     * 
     * @param attributes
     */
    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }
}
