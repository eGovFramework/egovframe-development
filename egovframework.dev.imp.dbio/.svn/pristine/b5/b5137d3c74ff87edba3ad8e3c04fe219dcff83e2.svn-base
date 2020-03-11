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
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

import egovframework.dev.imp.dbio.util.StringUtil;

/**
 * TextField
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
public class TextField extends FormField {

	private String labelText;
	private Text inputText;
	
	private String defaultValue;
	
	private String oldText;
	
	/**
	 * 생성자
	 * @param labelText
	 */
	public TextField(String labelText) {
		//this.labelText = labelText;
		this.setLabelText(labelText);
	}
	
	/**
	 * 라벨 설정
	 * 
	 * @param labelText
	 */
	private void setLabelText(String labelText) {
		this.labelText = labelText;
	}
	
	/**
	 * 필드 텍스트 정보 반환
	 * @return 필드 텍스트 정보
	 */
	public Text getTextField() {
		return inputText;
	}
	
	/**
	 * 입력정보 반환
	 * @return 입력 정보
	 */
	public String getText() {
		return inputText == null ? null : inputText.getText();
	}
	
	/**
	 * 정보 입력
	 * 
	 * @param value
	 */
	public void setText(String value) {
		if (inputText == null) {
			defaultValue = value;
		} else {
			inputText.setText(value);
		}
	}
	/**
	 * 화면 구성
	 * 
	 * @param toolkit
	 * @param parent
	 * @param cols
	 */
	public void create(FormToolkit toolkit, Composite parent, int cols) {
		Assert.isLegal(cols >= 2);
		createLabel(toolkit, parent, labelText);
		createInputText(toolkit, parent, cols-1);
	}

	/**
	 * 입력박스 생성
	 * 
	 * @param toolkit
	 * @param parent
	 * @param cols
	 */
	protected void createInputText(FormToolkit toolkit, Composite parent, int cols) {
		inputText = toolkit.createText(parent, StringUtil.nvl(defaultValue));
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		if (cols > 1) {
			gd.horizontalSpan = 2;
		}
		inputText.setLayoutData(gd);
		inputText.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				oldText = inputText.getText();
			}

			public void focusLost(FocusEvent e) {
				if (!inputText.getText().equals(oldText)) {
					fireFieldEvent(new FieldEvent(FieldEvent.Type.TextChanged, TextField.this));
				}
			}
		});
	}
	
	public void setEditable(boolean editable) {
		inputText.setEditable(editable);
	}
	
	public void setEnable(boolean enabled) {
		inputText.setEnabled(enabled);
	}
	
	public void setFocus() {
		inputText.setFocus();
	}
}
