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
package egovframework.boot.dev.imp.ide.handlers.project;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.actions.NewWizardShortcutAction;
import org.eclipse.ui.wizards.IWizardDescriptor;

import egovframework.boot.dev.imp.ide.EgovBootIdePlugin;
import egovframework.boot.dev.imp.ide.handlers.EgovBootMenu;

/**
 * 기본프로젝트 생성 메뉴 클래스
 * 
 * @author 개발환경 개발팀 이흥주
 * @since 2009.06.01
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.03.20  이흥주          최초 생성
 * 
 * 
 * </pre>
 */
@SuppressWarnings("restriction")
public class NewBootWebProject extends AbstractHandler implements EgovBootMenu {
	/**
	 * 기본 프로젝트 생성 마법사 실행
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWizardDescriptor wizardDesc = WorkbenchPlugin.getDefault().getNewWizardRegistry().findWizard("egovframework.boot.dev.imp.ide.wizards.egovbootwebprojectwizard");
		if (wizardDesc == null) {
			MessageDialog.openInformation(EgovBootIdePlugin.getActiveWorkbenchWindow().getShell(), "Inform", "Selected function has not been installed. eGovFrmae BOOT IDE is required.");
		} else {
			IAction action = new NewWizardShortcutAction(EgovBootIdePlugin.getActiveWorkbenchWindow(), wizardDesc);
			action.run();
		}

		return null;
	}

}
