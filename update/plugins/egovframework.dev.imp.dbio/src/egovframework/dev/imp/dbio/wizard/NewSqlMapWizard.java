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
 * NewSqlMap Wizard
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
public class NewSqlMapWizard extends BasicNewResourceWizard implements CreateSqlMap{

	private NewSqlMapWizardPage mainPage;
	private IFile createdFile;
	private boolean openEditorOnFinish;
	public NewSqlMapWizard() {
		setWindowTitle("New SQL Map file");
		setDefaultPageImageDescriptor(DBIOPlugin.getDefault().getImageDescriptor(DBIOPlugin.IMG_SQL_MAP_WIZ_BANNER));
		setOpenEditorOnFinish(true);
	}
	
	/**
	 * 생성자
	 * 
	 * @param openEditorOnFinish
	 */
	public NewSqlMapWizard(boolean openEditorOnFinish) {
		this();
		this.openEditorOnFinish = openEditorOnFinish;
	}

	private void setOpenEditorOnFinish(boolean bln) {
		this.openEditorOnFinish = bln;
	}
	@Override
	public void addPages() {
		super.addPages();
		mainPage = new NewSqlMapWizardPage(getSelection());
		addPage(mainPage);
	}

	/**
	 * 종료 처리
	 */
	@Override
	public boolean performFinish() {
		IFile file = mainPage.createNewFile();
		this.createdFile = file;
		selectAndReveal(file);
		try {
			if (openEditorOnFinish) {
				IDE.openEditor(getWorkbench().getActiveWorkbenchWindow().getActivePage(), file);
			}
		} catch (PartInitException e) {
			DBIOPlugin.getDefault().getLog().log(e.getStatus());
		}
		return true;
	}

	public IFile getCreatedFile(){
		return this.createdFile;
	}
	
	public void run() {
		addPages();
	}
}
