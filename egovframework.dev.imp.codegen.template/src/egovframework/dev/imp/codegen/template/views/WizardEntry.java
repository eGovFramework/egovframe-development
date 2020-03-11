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
 * 템플릿 뷰어의 트리 요소 모델 클래스
 * <p><b>NOTE:</b> 템플릿 뷰어의 트리에 보여질 각 요소들의 정보를 담고있는 모델 클래스
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
public class WizardEntry extends WizardElement {

    /** 설명 */
    String description;
    /** 템플릿 */
    String template;
    /** 템플릿 파일 */
    String templateFile;

    /**
     * 
     * 생성자
     *
     * @param parent
     * @param description
     * @param template
     * @param templateFile
     */
    public WizardEntry(WizardElement parent
            , String description
            , String template
            , String templateFile) {
        super(parent);
        this.description = description;
        this.label = description;
        this.template = template;
        this.templateFile = templateFile;
    }


    /**
     * Description 정보를 가져옴. 
     * 
     * @return
     */
    public String getDescription() {
        return description;
    }


    /**
     * Template 정보를 가져옴
     * 
     * @return
     */
    public String getTemplate() {
        return template;
    }

    /**
     * Template 파일 정보를 가져옴
     * 
     * @return
     */
    public String getTemplateFile() {
        return templateFile;
    }


    /* 
     * 자식개체를 가져옴
     * 
     * (non-Javadoc)
     * @see egovframework.dev.imp.codegen.template.views.WizardElement#getChildren()
     */
    @Override
    public WizardElement[] getChildren() {
        return NO_CHILDREN;
    }


    /* 
     * 자식 개체의 존재여부를 가져옴
     * 
     * (non-Javadoc)
     * @see egovframework.dev.imp.codegen.template.views.WizardElement#hasChildren()
     */
    @Override
    public boolean hasChildren() {
        return false;
    }

}
