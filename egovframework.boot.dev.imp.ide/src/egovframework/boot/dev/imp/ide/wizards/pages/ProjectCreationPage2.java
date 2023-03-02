/*
 * Copyright 2008-2009 the original author or authors.
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
package egovframework.boot.dev.imp.ide.wizards.pages;

import java.util.regex.Pattern;

import org.eclipse.osgi.util.TextProcessor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import egovframework.boot.dev.imp.ide.common.BootIdeMessages;
import egovframework.boot.dev.imp.ide.wizards.model.NewProjectContext;
import egovframework.dev.imp.core.utils.IdeUIUtil;


/**
 * 프로젝트 생성 마법사 페이지 클래스 for Boot Template Project
 * @author 개발환경 개발팀 이흥주
 * @since 2009.06.01
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.02.20  이흥주          최초 생성
 * 
 * 
 * </pre>
 */
public class ProjectCreationPage2 extends BaseProjectCreationPage {

    /** 위젯 */
    protected Text packageText;
    protected NewProjectContext context;

    /**
     * 생성자
     * @param pageName
     * @param context
     */
    protected ProjectCreationPage2(String pageName, NewProjectContext context) {
        super(pageName);

        this.context = context;
    }

    /**
     * 프로젝트명 변경 이벤트 리스너
     */
    private final Listener projectNameModifyListener = new Listener() {
        public void handleEvent(Event e) {
            boolean valid = validatePage();
            setPageComplete(valid);
        }
    };


    /**
     * 위젯 생성
     */
    @Override
    protected void createControls(Composite parent) {


        addProjectNameListener(projectNameModifyListener);
    }

    @Override
    protected boolean validatePage() {
    	
        if ("".equals(getProjectName())) {  //$NON-NLS-1$
        	setErrorMessage(BootIdeMessages.projectCreationPage7);
        	return false;
        }
    	
        if (!super.validatePage()) {
            return false;
        }
        
        updateContext();
        return true;
    }

    // 컨텍스트 정보 변경
    protected void updateContext() {
        context.setProject(getProjectHandle());
        context.setProjectName(getProjectName());
        context.setPackageName(getPackage());
        context.setLocationPath(getLocationPath());

    }

    //getter
    public String getPackage() {
        if (packageText == null) {
            return "";  //$NON-NLS-1$
        }
        return packageText.getText().trim();
    }

    @Override
    public void createControl(Composite parent) {
        super.createControl(parent);
    }

}
