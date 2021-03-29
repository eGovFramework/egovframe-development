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

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.eclipsework.core.jdom.element.category.CategoryElement;
import org.eclipse.eclipsework.core.jdom.parser.JDomCategoryReader;
import org.eclipse.eclipsework.core.util.KeyValuePair;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import egovframework.dev.imp.codegen.template.CodeGenLog;

/**
 * 
 * 컨텐트 제공자 클래스
 * <p><b>NOTE:</b> Viewer에 표기될 컨텐트 제공자 클래스 
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
class ViewContentProvider implements IStructuredContentProvider
                                        , ITreeContentProvider {
    
    /** 트리구조의 루트처리를 위한 TreeParent 객체 */	
    private WizardCategory invisibleRootCategory;
    
    /** 뷰의 카테고리 리스트 */
    private List<CategoryElement> categories = null;
    
    /** 템플릿뷰 데이터 리더 */
    private final TemplatesViewReader reader;	

    /** 로거 */
    Logger log = Logger.getLogger(ViewContentProvider.class);	

    /**
     * 
     * 생성자
     *
     */
    public ViewContentProvider(){
        this.reader = new TemplatesViewReader(this);
    }

    /**
     * 입력값이 변경되었을 때 처리 
     * inputChanged
     *
     * @param v
     * @param oldInput
     * @param newInput
     */
    public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        //
    }

    /**
     * 
     * dispose 되었을 때 처리 
     *
     * @see org.eclipse.jface.viewers.IContentProvider#dispose()
     */
    public void dispose() {
        //
    }

    /**
     * 요소 정보를 가져옴. 
     * getElements
     *
     * @param parent
     * @return
     */
    public Object[] getElements(Object parent) {

        if(parent.equals(ResourcesPlugin.getWorkspace())) {
            //if(invisibleRoot == null)
            //reader.initialize(getWizardConfigFile());
            if (invisibleRootCategory == null){
                invisibleRootCategory = new WizardCategory(null, "");
                reader.initialize();
            }
            return getChildren(invisibleRootCategory);
        }
        return getChildren(parent);
    }

    /**
     * 부모 개체를 가져옴
     * getParent
     *
     * @param child
     * @return
     */
    public Object getParent(Object child) {
        if(child instanceof WizardElement) {
            return ((WizardElement) child).getParent();
        }
        return null;
    }

    /**
     * 자식 개체를 가져옴
     * getChildren
     *
     * @param parent
     * @return
     */
    public Object[] getChildren(Object parent) {
        if(parent instanceof WizardElement) {
            return ((WizardElement) parent).getChildren();
        }
        return new Object[0];
    }

    /**
     * 자식 개체가 있는 지 확인
     * hasChildren
     *
     * @param parent
     * @return
     */
    public boolean hasChildren(Object parent) {
        if(parent instanceof WizardElement)
            return ((WizardElement) parent).hasChildren();
        return false;
    }


    /**
     * 
     * 마법사 목록 보여주기
     *
     * @param rootName
     * @param in
     * @throws Exception
     */
    public void showWizards(String rootName, InputStream in) throws Exception {
        showWizards(rootName, in,null);
    }

    /**
     * 
     * 마법사 목록 보여주기
     *
     * @param rootName
     * @param in
     * @param file
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	void showWizards(String rootName, InputStream in, File file) throws Exception {
        categories = JDomCategoryReader.getCategories(file,in);

        if(categories != null) {
            CodeGenLog.logInfo("Loading " + categories + " Wizard");
        }
        else {
            String msg = file.getName() + " not found";
            log.error(msg);
            createErrorTree(msg);
            return;
        }


        WizardCategory rootCategory = new WizardCategory(invisibleRootCategory, rootName);
        invisibleRootCategory.addChid(rootCategory);

        for(CategoryElement category : categories){
            addCategory(rootCategory, category, file);
        }

        log.info("done.");
    }

    /**
     * 에러트리 보여주기
     */
    void createErrorTree(String msg) {
        WizardCategory rootCategory = new WizardCategory(invisibleRootCategory, msg);
        invisibleRootCategory = new WizardCategory(null, "");
        invisibleRootCategory.addChid(rootCategory);
    }

    /**
     * 
     * 카테고리 추가
     *
     * @param parent
     * @param category
     * @param file
     */
    private void addCategory(WizardCategory parent, CategoryElement category, File file){
        WizardCategory wizardCatetory = new WizardCategory(parent, category.getCategoryName());

        log.debug("category: " + category.getCategoryName());

        List<?> wizards = category.getWizards();
        Path path = new Path(file.getParent());		
        for (int i = 0, n = wizards.size(); i < n ; i++ ){
            KeyValuePair keyValue = (KeyValuePair) wizards.get(i);
            WizardEntry wizardEntry = new WizardEntry(wizardCatetory
                , keyValue.getKey()
                , keyValue.getValue()
                , path.append(keyValue.getValue()).toString()
            );
            wizardCatetory.addChid(wizardEntry);

            log.debug("     -> wizard: " + keyValue.getKey());
        }

        parent.addChid(wizardCatetory);

        List<?> subcategories = category.getCategories();
        for (int i = 0, n = subcategories.size(); i < n; i++) {
            // recursion
            addCategory(wizardCatetory, (CategoryElement) subcategories.get(i), file);
        }

    }

}