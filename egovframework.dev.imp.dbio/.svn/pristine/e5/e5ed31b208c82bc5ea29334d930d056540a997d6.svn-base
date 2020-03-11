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
package egovframework.dev.imp.dbio.editor.parts;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 * SqlMapConfig 에디터 화면의 SectionFactory 객체
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
public abstract class SectionFactory {

	private String title;
	private int style;

	/**
	 * 생성자
	 * 
	 * @param title
	 * @param style
	 */
	public SectionFactory(String title, int style) {
		//this.title = title;
		//this.style = style;
		
		this.setTitle(title);
		this.setStyle(style);
	}
	
	/*
	 * 타이틀 설정
	 */
	private void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * 스타일 설정
	 * 
	 * @param style
	 */
	private void setStyle(int style) {
		this.style = style;
	}
	
	/**
	 * 화면 구성
	 * 
	 * @param managedForm
	 * @param parent
	 * @return
	 */
	public Section createContents(IManagedForm managedForm, Composite parent) {
		FormToolkit toolkit = managedForm.getToolkit();
		Section section = toolkit.createSection(parent, style);
		if (title != null) {
			section.setText(title);
		}

		Composite client = toolkit.createComposite(section);
		
		createContents(client, toolkit);
		
		section.setClient(client);
		toolkit.paintBordersFor(client);
		
		managedForm.addPart(new SectionPart(section));
		return section;
	}

	protected abstract void createContents(Composite parent, FormToolkit toolkit);
}
