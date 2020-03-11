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

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 카테고리 모델 클래스
 * <p><b>NOTE:</b> 카테고리 정보를 담고있는 모델 클래스
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
public class WizardCategory extends WizardElement {

    /** 이름 */
    private final String name;
    /** 하위 목록 */
    private final List<WizardElement> children;
    
    /**
     * 이름 정보를 가져옴
     * 
     * @return
     */
    public String getName() {
        return name;
    }


    /**
     * 
     * 생성자
     *
     * @param parent
     * @param name
     */
    public WizardCategory(WizardElement parent, String name) {
        super(parent);
        this.name = name;
        this.label = name;
        children = new ArrayList<WizardElement>();
    }

    /**
     * 
     * 하위 목록 가져오기
     *
     * @return
     */
    @Override
    public WizardElement[] getChildren() {
        return (WizardElement[])children.toArray(new WizardElement[children.size()]);		
    }

    /**
     * 
     * 하위 목록 추가
     *
     * @param child
     */
    public void addChid(WizardElement child){
        children.add(child);		
    }

    /**
     * 
     * 하위 목록 존재 여부
     *
     * @return
     */
    @Override
    public boolean hasChildren() {
        return children.size() > 0;
    }

}
