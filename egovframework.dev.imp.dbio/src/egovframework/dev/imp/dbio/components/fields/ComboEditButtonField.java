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
package egovframework.dev.imp.dbio.components.fields;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * ComboEditButtonField
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
 * </pre>
 */
public class ComboEditButtonField extends ComboEditField {

	private String buttonText;
	private Button button;

	/**
	 * 생성자
	 * @param labelText
	 * @param buttonText
	 */
	public ComboEditButtonField(String labelText, String buttonText) {
		super(labelText);
		//this.buttonText = buttonText;
		this.setButtonText(buttonText);
	}
	
	/**
	 * 버튼명 설정
	 * @param buttonText
	 */
	private void setButtonText(String buttonText) {
		this.buttonText = buttonText;
	}
	/**
	 * 버튼 생성
	 */
	@Override
	public void create(FormToolkit toolkit, Composite parent, int cols) {
		Assert.isLegal(cols >= 3);
		super.create(toolkit, parent, cols-1);
		createButton(toolkit, parent, buttonText);
	}
	/**
	 * 버튼 생성
	 * @param toolkit
	 * @param parent
	 * @param text
	 */
	private void createButton(FormToolkit toolkit, Composite parent,
			String text) {
		button = toolkit.createButton(parent, text, SWT.NONE);
		button.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fireFieldEvent(new FieldEvent(FieldEvent.Type.ButtonSelected, ComboEditButtonField.this));
			}
		});
	}
	
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		button.setEnabled(enabled);
	}
}
