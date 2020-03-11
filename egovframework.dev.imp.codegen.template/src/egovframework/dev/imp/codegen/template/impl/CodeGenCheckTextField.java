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
import java.util.Map;

import org.eclipse.eclipsework.core.impl.ui.TextField;
import org.eclipse.eclipsework.core.wizard.ui.IComponent;
import org.eclipse.eclipsework.wizard.EclipseWorkComponentsPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import egovframework.dev.imp.codegen.template.CodeGenLog;

/**
 * 
 * 텍스트 필드 컴포넌트 클래스 
 * <p><b>NOTE:</b> 텍스트 필드에 체크버튼를 추가하여 체크버튼 선택 여부에 따라 활성/비활성되는 컴포넌트 
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
public class CodeGenCheckTextField extends TextField{

    /** 체크버튼 */
    private Button checkButton;
    /** 텍스트 컴포넌트 */
    private Text superText;
    /** 자신 컴포넌트 */
    private IComponent thisComponent;
    /** 체크옵션명 */
    private String checkName = "";
    /** 체크 기본 값 */
    private boolean checkDefault = true;

    /**
     * 
     * 생성자
     *
     * @param name
     * @param label
     */
    public CodeGenCheckTextField(String name, String label) {
        this(name, label, false);
    }
    
    /**
     * 
     * 생성자
     *
     * @param name
     * @param label
     * @param required
     */
    public CodeGenCheckTextField(String name, String label, boolean required) {
        super(name, label.substring(0, label.indexOf("[[CHECK]")));
        this.required = required;
        
        int pos = label.indexOf(":",label.indexOf("[[CHECK]"));
        
        checkName = label.substring(label.indexOf("[[CHECK]") + "[[CHECK]".length(), pos);
        checkDefault = Boolean.parseBoolean(label.substring(pos+1, label.indexOf("]", pos + 1)));

    }    

    /**
     * 
     * 컨트롤 생성
     *
     * @param composite
     * @param componentsList
     * @param componentsPage
     * @see org.eclipse.eclipsework.core.impl.ui.TextField#createControl(org.eclipse.swt.widgets.Composite, java.util.List, org.eclipse.eclipsework.wizard.EclipseWorkComponentsPage)
     */
    @SuppressWarnings("rawtypes")
	@Override
    public void createControl(Composite composite, List componentsList, final EclipseWorkComponentsPage componentsPage) {
        super.createControl(composite, componentsList, componentsPage);


        try {

            Field privateTextField = TextField.class.getDeclaredField("text");

            privateTextField.setAccessible(true);
            superText = (Text) privateTextField.get(this);

            GridData data = new GridData(GridData.FILL_HORIZONTAL);
            data.horizontalSpan = 1;
            superText.setLayoutData(data);            

        } catch (Exception e) {
            CodeGenLog.logError(e);
        }   

        thisComponent = (IComponent)this;

        SelectionListener listener = new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                if (checkButton.getSelection() != superText.getEnabled()) {
                    thisComponent.updateEnabled();
                }
            }
            public void widgetDefaultSelected(SelectionEvent e) {
                //
            }
        };

        checkButton = new Button(composite, SWT.CHECK);
        checkButton.addSelectionListener(listener);
        checkButton.setSelection(checkDefault);
        
        if (checkButton.getSelection() != superText.getEnabled()) {
            thisComponent.updateEnabled();
        }
    }
    
    /**
     * 
     * VelocityContext에 값 설정
     *
     * @param map
     * @see org.eclipse.eclipsework.core.impl.ui.TextField#putValuesToVelocityContext(java.util.Map)
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public void putValuesToVelocityContext(Map map) {
        super.putValuesToVelocityContext(map);
        map.put(checkName, new Boolean(checkButton.getSelection()));
    }
}
