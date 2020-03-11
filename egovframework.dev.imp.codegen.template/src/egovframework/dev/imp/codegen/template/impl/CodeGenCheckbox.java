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
package egovframework.dev.imp.codegen.template.impl;
import java.lang.reflect.Field;
import java.util.List;

import org.eclipse.eclipsework.core.impl.ui.Checkbox;
import org.eclipse.eclipsework.wizard.EclipseWorkComponentsPage;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import egovframework.dev.imp.codegen.template.CodeGenLog;

/**
 * 
 * 체크 박스 컴포넌트 클래스
 * <p><b>NOTE:</b> 체크 박스 컴포넌트 클래스로써 check 이벤트 발생시 componentsPage.setPageComplete값 설정되도록 함. 
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
public class CodeGenCheckbox extends Checkbox {
    
    /** 컴포넌트 페이지 인스턴스 */
    private EclipseWorkComponentsPage componentsPage;

    /** 셀렉션 리스너 */
    SelectionListener listener = new SelectionListener() {

        //Enable/Disable
        public void widgetSelected(SelectionEvent e) {
            //updateStateAllComponents(component);
            componentsPage.setPageComplete(componentsPage.isPageComplete());
            
        }
        public void widgetDefaultSelected(SelectionEvent e) {
            //            
        }
    };
    
    /**
     * 
     * 생성자
     *
     * @param name
     * @param label
     */
    public CodeGenCheckbox(String name, String label) {
        super(name, label);
    }
    
    /**
     * 
     * 컨트롤 생성
     * 체크버튼 컴포넌트에 리스너 추가
     * @param composite
     * @param componentsList
     * @param componentsPage
     * @see org.eclipse.eclipsework.core.impl.ui.Checkbox#createControl(org.eclipse.swt.widgets.Composite, java.util.List, org.eclipse.eclipsework.wizard.EclipseWorkComponentsPage)
     */
    @SuppressWarnings("rawtypes")
	@Override
    public void createControl(Composite composite, List componentsList, final EclipseWorkComponentsPage componentsPage) {
        this.componentsPage = componentsPage;
        super.createControl(composite, componentsList, componentsPage);

        try {
            
            Field privateCheckButtonField = Checkbox.class.getDeclaredField("checkButton");
            
            privateCheckButtonField.setAccessible(true);
            Button checkButton = (Button) privateCheckButtonField.get(this);
            checkButton.addSelectionListener(listener);
            
        } catch (Exception e) {
            CodeGenLog.logError(e);
        }

        
        
    }
}
