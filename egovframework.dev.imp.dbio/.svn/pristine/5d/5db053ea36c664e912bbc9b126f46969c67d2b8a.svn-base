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
package egovframework.dev.imp.dbio.editor.model;

import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * SqlBindingParm 모델
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
public class SqlBindingParm {
	public final static String PARAMETER = "Parameter";
	public final static String VALUE = "Value";
	public final static String DATA_TYPE = "Data Type";
	public String parm = "new row";
	public String value = "";
	public ComboBoxCellEditor dataTypeComboBox = null;
	public static final String[] dataTypes = new String[] {"String", "Byte", "Integer", 
													"Long", "Float", "Double", "BigDecimal"};
	
	/**
	 * 생성자
	 */
	public SqlBindingParm(){}
	
	/**
	 * 생성자
	 * 
	 * @param parm
	 * @param value
	 */
	public SqlBindingParm(String parm, String value, Composite parent) {
		this.parm = parm;
		this.value = value;
		//this.dataTypeComboBox = new TableComboBoxCellEditor(parent, 2);
		this.dataTypeComboBox = new ComboBoxCellEditor(parent, dataTypes, SWT.READ_ONLY);
		this.dataTypeComboBox.setItems(dataTypes);
	}
}
