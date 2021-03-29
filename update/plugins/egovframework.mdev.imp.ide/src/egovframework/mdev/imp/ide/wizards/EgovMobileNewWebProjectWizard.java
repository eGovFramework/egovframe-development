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
package egovframework.mdev.imp.ide.wizards;

import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.IWizardPage;

import egovframework.mdev.imp.ide.EgovMobileIdePlugin;
import egovframework.mdev.imp.ide.common.MoblieIdeLog;
import egovframework.mdev.imp.ide.common.MoblieIdeMessages;
import egovframework.mdev.imp.ide.wizards.examples.MobileExampleInfo;
import egovframework.mdev.imp.ide.wizards.model.NewMobileWebProjectContext;
import egovframework.mdev.imp.ide.wizards.operation.MobileWebProjectCreationOperation;
import egovframework.mdev.imp.ide.wizards.pages.MobileWebProjectCreationPage;

/**
 * Moblie eGovFramework 웹 프로젝트 마법사 클래스
 * 
 * @author 이종대
 * @since 2011.07.13
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 * 수정일      	수정자           수정내용
 *  -------			--------    ---------------------------
 * 2011.07.13  	이종대          최초 생성
 * 
 * 
 * </pre>
 */
public class EgovMobileNewWebProjectWizard extends EgovMobileNewProjectWizard {

	/**
	 * 생성자
	 */
	public EgovMobileNewWebProjectWizard() {
		setContext(new NewMobileWebProjectContext());
		setWindowTitle(MoblieIdeMessages.wizardsEgovNewWebMProjectWizardTITLE);
		setDefaultPageImageDescriptor(EgovMobileIdePlugin.getDefault().getImageDescriptor(EgovMobileIdePlugin.IMG_WEB_PROJ_WIZ_BANNER));
	}

	/**
	 * 오퍼레이션 추가
	 */
	protected IRunnableWithProgress createOperation() {
		return new MobileWebProjectCreationOperation(getContext());
	}

	/**
	 * 마법사 페이지 추가
	 */
	public void addPages() {
		try {
			getContext().setDefaultExampleFile(MobileExampleInfo.defaultWebExample);

			IWizardPage firstPage = new MobileWebProjectCreationPage("firstPage", getContext());
			addPage(firstPage);

			super.addPages();

		} catch (Exception e) {
			MoblieIdeLog.logError(e);
		}
	}

}
