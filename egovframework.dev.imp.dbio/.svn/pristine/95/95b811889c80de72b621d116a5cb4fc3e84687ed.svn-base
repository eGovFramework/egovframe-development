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
package egovframework.dev.imp.dbio.editor.pages;


import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import egovframework.dev.imp.dbio.util.ImageUtil;

/**
 * 에디터의 추상 페이지 객체
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
 *   2009.02.20  김형조          최초 생성
 *
 * 
 * </pre>
 */
public class SimpleFormPage extends FormPage {

	private ResourceManager resourceManager;

	/**
	 * 생성자
	 * 
	 * @param editor
	 * @param id
	 * @param title
	 */
	public SimpleFormPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
		this.setResourceManager();
	}
	
	private void setResourceManager() {
		resourceManager = JFaceResources.getResources();
	}

	/**
	 * 화면 생성
	 */
	@Override
	protected void createFormContent(IManagedForm managedForm) {
		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		form.setText(getTitle());
		toolkit.decorateFormHeading(form.getForm());
		//form.setBackgroundImage(resourceManager.createImage(ImageUtil.FORM_BANNER));
		
		createContents(managedForm, form.getBody());
		managedForm.getToolkit().paintBordersFor(form.getBody());
	}
	
	protected void createContents(IManagedForm managedForm, Composite parent) {
		// DbioLog.logInfo("sham");
	}
	
	/**
	 * 폼 닫기 처리
	 */
	@Override
	public void dispose() {
		resourceManager.destroyImage(ImageUtil.FORM_BANNER);
		super.dispose();
	}
}
