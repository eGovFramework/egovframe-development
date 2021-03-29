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
package egovframework.dev.imp.codegen.model.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.internal.WorkbenchPlugin;

import egovframework.dev.imp.codegen.model.actions.XMIBasedCodeGenAction;

/**
 * XMI 모델 기반 코드 생성 핸들러 
 * <p><b>NOTE:</b> 메뉴로부터 연계되어 실행된다.  
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
@SuppressWarnings("restriction")
public class XMIBasedCodeGenHandler  extends AbstractHandler {

	/* 
	 * 실행하기
	 * 
	 * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 * 
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		WorkbenchPlugin.getDefault()
		.getNewWizardRegistry().findWizard("egovframework.dev.imp.codegen.wizard.XMIBasedCodeGenWizard");

		new XMIBasedCodeGenAction().run();

		return null;
	}

}
