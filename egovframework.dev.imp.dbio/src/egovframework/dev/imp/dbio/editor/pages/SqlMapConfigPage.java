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


import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.w3c.dom.Element;

import egovframework.dev.imp.dbio.editor.SqlMapConfigEditor;
import egovframework.dev.imp.dbio.editor.parts.SqlMapConfigSqlMapSection;
import egovframework.dev.imp.dbio.editor.parts.SqlMapConfigTransactionManagerSection;

/**
 * SqlMap Config Page 
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
@SuppressWarnings("restriction")
public class SqlMapConfigPage extends SimpleFormPage {

	private boolean active;
	private IStructuredModel model;
	private SqlMapConfigTransactionManagerSection tmSection;
	private SqlMapConfigSqlMapSection sqlMapSection;


	public SqlMapConfigPage(SqlMapConfigEditor editor) {
		super(editor, SqlMapConfigPage.class.getName(), "SQL Map Configuration");
	}

	/**
	 * Config 화면 그리기
	 */
	@Override
	protected void createContents(IManagedForm managedForm, Composite parent) {
		parent.setLayout(new GridLayout(1, true));

		tmSection = new SqlMapConfigTransactionManagerSection();
		Section section = tmSection.createContents(managedForm, parent);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.verticalAlignment = SWT.BEGINNING;
		section.setLayoutData(gd);

		sqlMapSection = new SqlMapConfigSqlMapSection(getEditor());
		section = sqlMapSection.createContents(managedForm, parent);
		gd = new GridData(GridData.FILL_BOTH);
		gd.verticalAlignment = SWT.BEGINNING;
		section.setLayoutData(gd);
	}

	/**
	 * 사용할 모델 객체 설정
	 * 
	 * @param model
	 */
	public void setModel(IStructuredModel model) {
		this.model = model;
		refresh();
	}

	private void refresh() {
		if (active) {
			if (model != null && (model instanceof IDOMModel)) {
				IDOMDocument domDoc = ((IDOMModel) model).getDocument();

				Element element = domDoc.getDocumentElement();
				if ("sqlMapConfig".equals(element.getTagName())) { //$NON-NLS-1$
					setRootElement(element);
				} else {
					setRootElement(null);
				}
			} else {
				setRootElement(null);
			}
		}
	}

	private void setRootElement(Element root) {
		if (tmSection != null) {
			tmSection.setElement(root);
		}
		if (sqlMapSection != null) {
			sqlMapSection.setElement(root);
		}
	}

	/**
	 * 활성화
	 */
	@Override
	public void setActive(boolean active) {
		this.active = active;
		refresh();
		super.setActive(active);
	}

}
