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

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;

import egovframework.dev.imp.codegen.model.wizard.XMIBasedCodeGenWizard;

/**
 * XMI 기반 코드젠을 수행하는 액션 클래스
 * <p><b>NOTE:</b> 플러그인 로딩, 종료 이벤트 처리. 
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
public class XMIBasedCodeGenAction implements UMLModelBasedCodeGenAction {

	/* 
	 * 액션을 수행
	 * 
	 * @see egovframework.dev.imp.codegen.model.actions.UMLModelBasedCodeGenAction#run(org.eclipse.jface.action.IAction)
	 * 
	 */
	public void run(IAction action) {
		run();
	}
	/**
	 * 
	 * XMI 기반 소스생성 위저드를 실행
	 *  
	 * 
	 */
	public void run() {
		
		XMIBasedCodeGenWizard wizard = new XMIBasedCodeGenWizard();
		WizardDialog dialog = new WizardDialog(null,wizard);
		dialog.open();
	}

	/**
	 * 선택이 변경되었을 때 처리 
	 * 
	 * @param action
	 * @param selection
	 * 
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		// 해당 처리가 필요하지 않음.
	}

}
