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
package egovframework.dev.imp.dbio.wizard;


import org.eclipse.core.resources.IFile;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

import egovframework.dev.imp.dbio.DBIOPlugin;

/**
 * NewSqlMapConfig Wizard
 * @author 개발환경 개발팀 김형조
 * @since 2009.02.20
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.02.20    김형조      최초 생성
 *
 * 
 * </pre>
 */
public class NewSqlMapConfigWizard extends BasicNewResourceWizard implements CreateSqlMapConfig{

	private NewSqlMapConfigWizardPage mainPage;
	
	/**
	 * 생성자
	 */
	public NewSqlMapConfigWizard() {
		setWindowTitle("New SQL Map Configuration file");
		setDefaultPageImageDescriptor(DBIOPlugin.getDefault().getImageDescriptor(DBIOPlugin.IMG_SQL_MAP_CONFIG_WIZ_BANNER));
	}

	@Override
	public void addPages() {
		super.addPages();
		mainPage = new NewSqlMapConfigWizardPage(getSelection());
		addPage(mainPage);
	}
	
	@Override
	public boolean performFinish() {
		IFile file = mainPage.createNewFile();
		selectAndReveal(file);
		try {
			IDE.openEditor(getWorkbench().getActiveWorkbenchWindow().getActivePage(), file);
		} catch (PartInitException e) {
			DBIOPlugin.getDefault().getLog().log(e.getStatus());
		}
		return true;
	}
	
	public void run() {
		this.addPages();
	}


}
