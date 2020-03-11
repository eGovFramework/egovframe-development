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

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

import egovframework.dev.imp.dbio.DBIOPlugin;
import egovframework.dev.imp.dbio.common.DbioMessages;

/**
 * NewMapperConfiguration WizardPage
 * @author 개발환경 개발팀 김효수
 * @since 2019.02.25
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2019.02.25  김효수          MyBatis DBIO  
 *
 * 
 * </pre>
 */
public class NewMapperConfigurationWizardPage extends WizardNewFileCreationPage {

	/**
	 * 생성자
	 * @param selection
	 */
	public NewMapperConfigurationWizardPage(IStructuredSelection selection) {
		super(NewMapperConfigurationWizardPage.class.getName(), selection);
		setAllowExistingResources(false);
		setFileExtension("xml"); //$NON-NLS-1$
		
		setTitle(DbioMessages.NewMapperConfigWizardPage_1);
		setDescription(DbioMessages.NewMapperConfigWizardPage_2);
		
		setImageDescriptor(DBIOPlugin.getDefault().getImageDescriptor(DBIOPlugin.IMG_MAPPER_CONFIGURATION_WIZ_BANNER));
	}

	@Override
	protected InputStream getInitialContents() {
		String data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + //$NON-NLS-1$
		"<!DOCTYPE configuration PUBLIC \"-//mybatis.org//DTD Config 3.0//EN\" " + //$NON-NLS-1$
		"\"http://mybatis.org/dtd/mybatis-3-config.dtd\" >\n" + //$NON-NLS-1$
		"<configuration>\n</configuration>"; //$NON-NLS-1$
		return new ByteArrayInputStream(data.getBytes());
	}

}
