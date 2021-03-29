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
package egovframework.hdev.imp.ide.project;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.actions.NewWizardShortcutAction;
import org.eclipse.ui.wizards.IWizardDescriptor;

import egovframework.hdev.imp.ide.EgovDeviceAPIIdePlugin;
import egovframework.hdev.imp.ide.common.DeviceAPIIdeUtils;
import egovframework.hdev.imp.ide.handlers.EgovDeviceAPIMenu;

/**
 * AddDeviceAPIHybridProject 생성 메뉴 클래스
 * 
 * @since 2012.07.24
 * @author 디바이스 API 개발환경 팀 조용현
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *수정일	  	수정자	  수정내용
 *-----------	------	----------------
 *2012.07.24	조용현	최초생성
 *
 * 
 * </pre>
 */

@SuppressWarnings("restriction")
public class AddDeviceAPIHybridProject extends AbstractHandler implements EgovDeviceAPIMenu {
	/**
	 * 기본 프로젝트 생성 마법사 실행
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {

		if (DeviceAPIIdeUtils.isAndroidDevelopmentTool() == false) {

			MessageDialog.openInformation(EgovDeviceAPIIdePlugin.getActiveWorkbenchWindow().getShell(), "Inform", "Selected function has not been installed. ADT is required.");
			return null;
		}

		IWizardDescriptor wizardDesc = WorkbenchPlugin.getDefault().getNewWizardRegistry().findWizard("egovframework.hdev.imp.ide.wizards.adddiviceapi");

		if (wizardDesc == null) {
			MessageDialog.openInformation(EgovDeviceAPIIdePlugin.getActiveWorkbenchWindow().getShell(), "Inform",
					"Selected function has not been installed. eGovFrmae IDE is required.");
		} else {
			IAction action = new NewWizardShortcutAction(EgovDeviceAPIIdePlugin.getActiveWorkbenchWindow(), wizardDesc);
			action.run();
		}

		return null;
	}

}
