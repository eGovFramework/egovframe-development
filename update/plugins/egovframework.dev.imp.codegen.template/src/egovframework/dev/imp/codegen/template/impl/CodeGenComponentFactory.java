/*
 * Copyright 2008-2009 MOSPA(Ministry of Security and Public Administration).
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
package egovframework.dev.imp.codegen.template.impl;

import org.eclipse.eclipsework.core.impl.ComponentFactory;
import org.eclipse.eclipsework.core.impl.ui.Container;
import org.eclipse.eclipsework.core.impl.ui.Group;
import org.eclipse.eclipsework.core.wizard.ui.ICheckbox;
import org.eclipse.eclipsework.core.wizard.ui.IContainer;
import org.eclipse.eclipsework.core.wizard.ui.IGroup;
import org.eclipse.eclipsework.core.wizard.ui.ITextField;

/**
 * 
 * 컴포넌트 팩토리 클래스
 * <p>
 * <b>NOTE:</b> 컴포넌트페이지에 정의된 컴포넌트 인스턴스를 생성하는 팩토리 클래스
 * 
 * @author 개발환경 개발팀 이흥주
 * @since 2009.08.03
 * @version 1.0
 * @see <pre>
 *  == 개정이력(Modification Information) ==
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.08.03  이흥주          최초 생성
 * 
 * </pre>
 */
public class CodeGenComponentFactory extends ComponentFactory {

	/**
	 * 
	 * 체크박스 생성
	 * 
	 * @param name
	 * @param label
	 * @return
	 * @see org.eclipse.eclipsework.core.impl.ComponentFactory#getCheckBox(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public ICheckbox getCheckBox(String name, String label) {
		ICheckbox checkbox = new CodeGenCheckbox(name, label);
		return checkbox;
	}

	/**
	 * 
	 * 텍스트필드 생성
	 * 
	 * @param name
	 * @param label
	 * @return
	 * @see org.eclipse.eclipsework.core.impl.ComponentFactory#getTextField(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public ITextField getTextField(String name, String label) {
		ITextField textField = null;
		if (label.indexOf("[[CHECK]") > 0)
			textField = new CodeGenCheckTextField(name, label);
		else
			textField = super.getTextField(name, label);
		return textField;
	}

	/* 
	 * Container 생성
	 * 
	 * (non-Javadoc)
	 * @see org.eclipse.eclipsework.core.impl.ComponentFactory#getContainer(java.lang.String, java.lang.String)
	 */
	@Override
	public IContainer getContainer(String name, String label) {
		return new Container(name, label);
//		return new CodeGenContainer(name, label);
	}
	
	/**
	 * 그룹 생성
	 * 
	 * @see org.eclipse.eclipsework.core.interfaces.IComponentFactory#getGroup(java.lang.String, java.lang.String)
	 */
	@Override
	public IGroup getGroup(String name, String label) {
		return new Group(name,label);
//		return new CodeGenGroup(name,label);
	}
}
