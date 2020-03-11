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
package egovframework.dev.imp.codegen.template.views;

/**
 * 
 * 템플릿 요소 모델 클래스
 * <p><b>NOTE:</b> 템플릿 요소 정보를 담고 있는 모델 클래스
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
public abstract class WizardElement {
    /** 구성요소 목록 */
    public static final WizardElement[] NO_CHILDREN = {};
    /** 상위 요소 */
    private final WizardElement parent;
    /** 라벨 */
    protected String label;

    
    /**
     * 
     * 생성자
     *
     * @param parent
     */
    public WizardElement(WizardElement parent) {
        this.parent = parent;
    }

    /**
     * 부모 개체를 가져옴
     * 
     * @return
     */
    public WizardElement getParent(){
        return this.parent;
    }

    /**
     * 자식개체 요소를 가져오는 추상 메소드
     * 
     * @return
     */
    public abstract WizardElement[] getChildren();

    /**
     * 자식 개체의 존재여부를 가져오는 추상 메소드
     * 
     * @return
     */
    public abstract boolean hasChildren();

    /**
     * Label 정보를 가져옴
     * 
     * @return
     */
    public String getLabel(){
        return this.label;
    }
}
