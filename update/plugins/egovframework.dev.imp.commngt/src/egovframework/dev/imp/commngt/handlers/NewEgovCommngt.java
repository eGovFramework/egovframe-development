package egovframework.dev.imp.commngt.handlers;

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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.actions.NewWizardShortcutAction;
import org.eclipse.ui.wizards.IWizardDescriptor;

import egovframework.dev.imp.commngt.EgovCommngtPlugin;
import egovframework.dev.imp.ide.EgovIdePlugin;

/**
 * 공통컴포넌트 생성 조립 도구 메뉴 클래스
 * 
 * @author 개발환경 개발팀 박수림
 * @since 2011.06.13
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2011.06.13  박수림          최초 생성
 * 
 * 
 * </pre>
 */
@SuppressWarnings("restriction")
public class NewEgovCommngt extends AbstractHandler {
	/**
	 * 공통컴포넌트 생성 조립 도구 마법사 실행
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWizardDescriptor wizardDesc = WorkbenchPlugin.getDefault().getNewWizardRegistry().findWizard("egovframework.dev.imp.commngt.wizards.newEgovCommngt");

		if (wizardDesc == null) {
			MessageDialog.openInformation(EgovIdePlugin.getActiveWorkbenchWindow().getShell(), "inform", "Selected function has not been installed.");
		} else {
			IAction action = new NewWizardShortcutAction(EgovCommngtPlugin.getActiveWorkbenchWindow(), wizardDesc);
			action.run();
		}
		return null;
	}

}
