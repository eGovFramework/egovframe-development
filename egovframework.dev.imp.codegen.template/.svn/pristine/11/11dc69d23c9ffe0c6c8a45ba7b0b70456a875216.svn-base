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

import net.sf.abstractplugin.core.EclipseProjectUtils;

import org.eclipse.core.resources.IProject;
import org.eclipse.eclipsework.core.wizard.EWWizardWrapper;
import org.eclipse.eclipsework.core.wizard.IEWWizard;
import org.eclipse.eclipsework.util.VelocityUtils;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

import egovframework.dev.imp.codegen.template.wizards.CodeGenWizard;

/**
 * 
 * 바법사 열기(실행) 액션 클래스
 * <p><b>NOTE:</b> 템플릿 뷰어에서 템플릿 항목을 더블클릭시 바법사를 띄우는 클래스
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
public class OpenWizardAction  extends Action{

    /** 마법사 모드 */
    private int mode = IEWWizard.DEFAULT_MODE;
    /** 템플릿 뷰 */
    private TemplatesView view;

    /**
     * 
     * 생성자
     *
     * @param view
     * @param mode
     */
    public OpenWizardAction(TemplatesView view,int mode) {
        this.mode = mode;

        this.view = view;
    }	

    /**
     * 
     * 생성자
     *
     * @param view
     */
    public OpenWizardAction(TemplatesView view) {
        this(view,IEWWizard.DEFAULT_MODE);
    }

    /**
     * 
     * 액션 실행
     *
     * @see org.eclipse.jface.action.Action#run()
     */
    @SuppressWarnings("unused")
	public void run()
    {
              
        
        ISelection selection = view.treeViewer.getSelection();
        Object obj = ((IStructuredSelection) selection).getFirstElement();

        IProject project = EclipseProjectUtils.getSelectedProject();
        if (project == null){
            return;
        }

        if (obj == null)
        {
            return;
        }


        ////////////////////////////////////////////////////////////////
        
        ////////////////////////////////////////////////////////////////


        if (obj instanceof WizardEntry) {

            String wizardName = ((WizardEntry)obj).getDescription();
            File f = new File(((WizardEntry)obj).getTemplateFile());
            if(f == null) {
                return;
            } else {

                VelocityUtils.setTemplatesFolder(f.getParent());
                //                EWWizardWrapper wizard =  new EWWizardWrapper(f, wizardName, new EclipseWorkWizard(),this.mode);
                EWWizardWrapper wizard =  new EWWizardWrapper(f, wizardName, new CodeGenWizard(),this.mode);
                wizard.open();
            }			
        }


    }
}
