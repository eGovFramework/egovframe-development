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
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * 콤보 필드 객체
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
public class ComboField extends FormField {

	private Label lavel;
	private String labelText;
	private CCombo combo;
	private String oldText;
	private String defaultText;
	private int style;
	
	/**
	 * 생성자
	 * @param labelText
	 */
	public ComboField(String labelText) {
		this(labelText, SWT.NONE);
	}
	
	/**
	 * 생성자
	 * @param labelText
	 * @param style
	 */
	public ComboField(String labelText, int style) {
		this.labelText = labelText;
		this.style = style;
	}
	
	/**
	 * 콤보박스의 스타일을 설정
	 * @param style
	 */
	public void setComboStyle(int style) {
		this.style = style;
	}
	
	/**
	 * 콤보 필드 그리기
	 * @param toolkit
	 * @param parent
	 * @param cols
	 */
	public void create(FormToolkit toolkit, Composite parent, int cols) {
		Assert.isLegal(cols >= 2);
		lavel = createLabel(toolkit, parent, labelText);
		createCombo(toolkit, parent, cols-1);
	}
	
	/**
	 * 콤보 생성
	 * @param toolkit
	 * @param parent
	 * @param cols
	 */
	protected void createCombo(FormToolkit toolkit, Composite parent, int cols) {
		combo = new CCombo(parent, style | SWT.FLAT | SWT.READ_ONLY);
		toolkit.adapt(combo, true, true);
		
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		if (cols > 1) {
			gd.horizontalSpan = 2;
		}
		combo.setLayoutData(gd);
		combo.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				oldText = combo.getText();
			}

			public void focusLost(FocusEvent e) {
				if (!combo.getText().equals(oldText)) {
					fireFieldEvent(new FieldEvent(FieldEvent.Type.TextChanged, ComboField.this));
				}
			}
		});
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!combo.getText().equals(oldText)) {
					oldText = combo.getText();
					fireFieldEvent(new FieldEvent(FieldEvent.Type.TextChanged, ComboField.this));
				}
			}
		});
		if (defaultText != null) {
			combo.setText(defaultText);
		}
		combo.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
		toolkit.paintBordersFor(combo);
	}

	/**
	 * 콤보의 텍스트를 설정한다.
	 * @param text
	 */
	public void setText(String text) {
		if (combo == null) {
			defaultText = text;
		} else {
			combo.setText(text);
		}
	}
	
	/**
	 * 콤보의 텍스트를 반환한다.
	 * @return 콤보의 텍스트 값
	 */
	public String getText() {
		return combo == null ? null : combo.getText();
	}
	
	/**
	 * 콤보에 아이템을 설정한다.
	 * @param items
	 */
	public void setItems(String[] items) {
		combo.setItems(items);
	}
	
	/**
	 * 콤보의 활성여부를 설정
	 * @param enabled
	 */
	public void setEnabled(boolean enabled) {
		combo.setEnabled(enabled);
	}
	
	public void setVisible(boolean value) {
		if (lavel != null) lavel.setVisible(value);
		if (combo != null) combo.setVisible(value);
	}
	
	public void setFocus() {
		combo.setFocus();
	}
}
