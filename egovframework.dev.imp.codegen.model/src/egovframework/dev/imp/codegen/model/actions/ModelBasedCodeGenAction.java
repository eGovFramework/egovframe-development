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
package egovframework.dev.imp.codegen.model.actions;

import net.java.amateras.uml.classdiagram.ClassDiagramEditor;
import net.java.amateras.uml.model.RootModel;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

import egovframework.dev.imp.codegen.model.wizard.ModelBasedCodeGenWizard;

/**
 * 모델 기반 코드젠을 수행하는 액션 클래스
 * <p><b>NOTE:</b> 클래스 다이어그램 에디터의 컨텍스트 메뉴에서 실행된다. 
 * @author 개발환경 개발팀 윤수열
 * @since 2009.08.10
 * @version 1.0
 * @see
 *
 * <pre>
 *  == 개정이력(Modification Information) ==
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.08.10  윤수열          최초 생성
 *
 * </pre>
 */
public class ModelBasedCodeGenAction implements UMLModelBasedCodeGenAction, IEditorActionDelegate {
	
	/** 클래스 다이어그램 편집기 인스턴스 */
	private ClassDiagramEditor editor;
	
	/* 
	 * 활성화된 에디터를 설정
	 * 
	 * @see org.eclipse.ui.IEditorActionDelegate#setActiveEditor(org.eclipse.jface.action.IAction, org.eclipse.ui.IEditorPart)
	 * 
	 */
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		this.editor = (ClassDiagramEditor)targetEditor;
	}

	/* 
	 * 액션을 실행
	 * 
	 * @see egovframework.dev.imp.codegen.model.actions.UMLModelBasedCodeGenAction#run(org.eclipse.jface.action.IAction)
	 * 
	 */
	public void run(IAction action) {
		RootModel root = (RootModel)this.editor.getAdapter(RootModel.class);
		
		ModelBasedCodeGenWizard wizard = new ModelBasedCodeGenWizard();
		wizard.setModel(root);
		try{
			wizard.setUmlModel();
		}catch(Exception e){
			MessageDialog.openError(null, "Class Diagram Model Error", "Class Diagram Model has an Error or More. \n You need to check Diagram.");
		}
		WizardDialog dialog = new WizardDialog(null,wizard);
		dialog.open();
	}

	/* 
	 * 선택이 변경되었을 때 처리 
	 * 
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
	 * 
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		// selectionChanged 에 대한 별다른 작업 요구되지 않음.  
	}

}
