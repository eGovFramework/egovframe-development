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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import egovframework.dev.imp.dbio.DBIOPlugin;
import egovframework.dev.imp.dbio.common.DbioMessages;
import egovframework.dev.imp.dbio.components.TableComboBoxCellEditor;
import egovframework.dev.imp.dbio.components.TableTextCellEditor;
import egovframework.dev.imp.dbio.components.fields.ComboField;
import egovframework.dev.imp.dbio.components.fields.FieldEvent;
import egovframework.dev.imp.dbio.components.fields.IFieldListener;
import egovframework.dev.imp.dbio.util.StringUtil;


/**
 * SqlMapConfig 에디터의 TransactionManagerSection
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
public class SqlMapConfigTransactionManagerSection extends SectionFactory {

	private ComboField typeCombo;
	private ComboField dataSourceTypeCombo;
	private TableViewer propertyViewer;
	private TableComboBoxCellEditor propertyNameEditor;
	private Element root;
	private Element tmElement;
	private Element dataSourceElement;
	private Button addPropertyButton;
	private Button removePropertyButton;
	private Button clearPropertyButton;
	private Form form;
	
	private static final String[] JDBC_SIMPLE_PROPERTIES = new String[] {
		"JDBC.Driver", "JDBC.ConnectionURL", "JDBC.Usename", "JDBC.Password"
	};
	private static final String[] JDBC_SIMPLE_COMBO_PROPERTIES = new String[] {
		"JDBC.Driver", "JDBC.ConnectionURL", "JDBC.Usename", "JDBC.Password"
		,"JDBC.DefaultAutoCommit"
		,"Pool.MaximumActiveConnections"
		,"Pool.MaximumIdleConnections"
		,"Pool.MaximumCheckoutTime"
		,"Pool.TimeToWait"
		,"Pool.PingQuery"
		,"Pool.PingEnabled"
		,"Pool.PingConnectionsOlderThan"
		,"Pool.PingConnectionsNotUsedFor"
	};
	private static final String[] JDBC_JNDI_PROPERTIES = new String[] {
		"DataSource"
	};
	private static final String[] JTA_JNDI_PROPERTIES = new String[] {
		"DataSource", "UserTransaction"
	};

	public SqlMapConfigTransactionManagerSection() {
//		super("Transaction Manager", Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
		super("Transaction Manager", Section.TITLE_BAR | Section.TWISTIE);
	}

	/**
	 * TransactionManagerSection 화면 구성
	 */
	@Override
	protected void createContents(Composite parent, FormToolkit toolkit) {
		
		parent.setLayout(new GridLayout(3, false));
		
		typeCombo = new ComboField("Type:", SWT.READ_ONLY);
		typeCombo.create(toolkit, parent, 3);
		typeCombo.setItems(new String[] {"JDBC", "JTA"});
		typeCombo.addFieldListener(new IFieldListener() {
			public void eventOccured(FieldEvent event) {
				if (event.getType() == FieldEvent.Type.TextChanged) {
					typeChanged();
				}
			}
		});
		dataSourceTypeCombo = new ComboField("DataSource Type:", SWT.READ_ONLY);
		dataSourceTypeCombo.create(toolkit, parent, 3);
		dataSourceTypeCombo.addFieldListener(new IFieldListener() {
			public void eventOccured(FieldEvent event) {
				if (event.getType() == FieldEvent.Type.TextChanged) {
					dataSourceTypeChanged();
				}
			}
		});
		
		final Label label = new Label(parent, SWT.NONE);
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1));
		toolkit.adapt(label, true, true);
		label.setText("Properties:");
				
		Table table = toolkit.createTable(parent, SWT.MULTI | SWT.FULL_SELECTION);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		gd.heightHint = 103;
		table.setLayoutData(gd);
		toolkit.adapt(table, true, true);
		
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		//TableLayout tl = new TableLayout();
		//table.setLayout(tl);
		
		//tl.addColumnData(new ColumnWeightData(150, true));
		TableColumn column;
		
		column = new TableColumn(table, SWT.LEFT, 0);
		column.setText("");
		column.setWidth(20);
		
		column = new TableColumn(table, SWT.LEFT, 1);
		column.setText("Name");
		column.setWidth(150);
		
		//tl.addColumnData(new ColumnWeightData(150, true));
		column = new TableColumn(table, SWT.LEFT, 2);
		column.setText("Value");
		column.setWidth(150);
		
		propertyViewer = new TableViewer(table);
		propertyViewer.setColumnProperties(new String[] {"", "name", "value"});
		propertyViewer.setContentProvider(new ArrayContentProvider());
		propertyViewer.setLabelProvider(new TransactionManagerPropertyLabelProvider(propertyViewer));
		propertyNameEditor = new TableComboBoxCellEditor(propertyViewer, 1);
		propertyViewer.setCellEditors(new CellEditor[] {
				null,
				propertyNameEditor,
				new TableTextCellEditor(propertyViewer, 2)
		});	
		propertyViewer.setCellModifier(new ICellModifier() {
			public boolean canModify(Object element, String property) {
				return (element instanceof Element) && "property".equals(((Element) element).getTagName());
			}

			public Object getValue(Object element, String property) {
				if (element instanceof Element) {
					return StringUtil.nvl(((Element) element).getAttribute(property));
				} else {
					return null;
				}
			}

			public void modify(Object element, String property, Object value) {
				if (element instanceof Element) {
					((Element) element).setAttribute(property, (String) value);
					propertyViewer.refresh(element, true);
					chkValidation();
				}
			}
		});
		propertyViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				//updateWidgetStatus();
			}
		});

		//버튼 컴포지트
		Composite buttonComposite = toolkit.createComposite(parent, SWT.NONE);
		gd = new GridData(SWT.LEFT, SWT.TOP, false, false);
		buttonComposite.setLayoutData(gd);		
		GridLayout blayout = new GridLayout();
		blayout.marginHeight = 0;
		blayout.horizontalSpacing = 0;
		blayout.marginWidth = 0;		
		buttonComposite.setLayout(blayout);
		toolkit.adapt(buttonComposite);
		
		addPropertyButton = toolkit.createButton(buttonComposite, "Add", SWT.NONE);
		addPropertyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addNewProperty();
				chkValidation();				
			}
		});
		removePropertyButton = toolkit.createButton(buttonComposite, "Remove", SWT.NONE);
		removePropertyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				removeProperties();
				chkValidation();				
			}
		});
		
		clearPropertyButton = toolkit.createButton(buttonComposite, "Init", SWT.NONE);
		clearPropertyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clearProperties();
				initProperties();
				chkValidation();				
			}
		});
		
		
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.verticalAlignment = SWT.BEGINNING;
		addPropertyButton.setLayoutData(gd);
		
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.verticalAlignment = SWT.BEGINNING;
		removePropertyButton.setLayoutData(gd);
		
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.verticalAlignment = SWT.BEGINNING;
		clearPropertyButton.setLayoutData(gd);
	}
	
	protected void addNewProperty() {
		Element element =  getDataSourceElement();
		Element newProperty = element.getOwnerDocument().createElement("property");
		newProperty.setAttribute("name", "");
		newProperty.setAttribute("value", "");
		element.appendChild(newProperty);
		propertyViewer.setInput(getProperties());
		propertyViewer.setSelection(new StructuredSelection(newProperty));
	}
	protected void addNewProperty(String name) {
		Element element =  getDataSourceElement();
		Element newProperty = element.getOwnerDocument().createElement("property");
		newProperty.setAttribute("name", name);
		newProperty.setAttribute("value", "");
		element.appendChild(newProperty);
		propertyViewer.setInput(getProperties());
		propertyViewer.setSelection(new StructuredSelection(newProperty));
	}	
	
	protected void removeProperties() {
		IStructuredSelection selection = (IStructuredSelection) propertyViewer.getSelection();
		Iterator<?> iterator = selection.iterator();
		while (iterator.hasNext()) {
			Element element = (Element) iterator.next();
			element.getParentNode().removeChild(element);
		}
		propertyViewer.setInput(getProperties());
	}
	
	protected void clearProperties() {
		
		Iterator<?> iterator = getProperties().iterator();
		while (iterator.hasNext()) {
			Element element = (Element) iterator.next();
			element.getParentNode().removeChild(element);
		}
		propertyViewer.setInput(getProperties());
	}
	
	protected void dataSourceTypeChanged() {
		getDataSourceElement().setAttribute("type", dataSourceTypeCombo.getText());
		updateWidgetStatus();
		
		initProperties();
	}
	
	private void initProperties(){
		//속성 초기화
		if (propertyViewer.getSelection().isEmpty()){
			String type = typeCombo.getText();
			String dataSourceType = dataSourceTypeCombo.getText();
			if ((!"".equals(StringUtil.nvl(type)))
					&& (!"".equals(StringUtil.nvl(dataSourceType)))
					) {
				String[] properties = new String[0];
				if ("JDBC".equals(type)) {
					if ("SIMPLE".equals(dataSourceType)){
						properties = JDBC_SIMPLE_PROPERTIES;
					}
					else if ("JNDI".equals(dataSourceType)){
						properties = JDBC_JNDI_PROPERTIES;
					}
				}
				else if ("JTA".equals(type)) {
					if ("JNDI".equals(dataSourceType)){
						properties = JTA_JNDI_PROPERTIES;
					}
				}
				
				for (int i = 0; i < properties.length; i++){
					addNewProperty(properties[i]);
				}
			}
			
		}
	}

	protected void typeChanged() {
		if (getTmElement() == null) createTmElement();
		getTmElement().setAttribute("type", typeCombo.getText());
		updateWidgetStatus();
	}
	
	private void updateWidgetStatus() {
		String type = typeCombo.getText();
		String old = dataSourceTypeCombo.getText();
		if ("JDBC".equals(type)) {
			dataSourceTypeCombo.setItems(new String[] {"SIMPLE", "JNDI"});
			dataSourceTypeCombo.setText(old);
			dataSourceTypeCombo.setEnabled(true);
		} else if ("JTA".equals(type)) {
			dataSourceTypeCombo.setItems(new String[] {"JNDI"});
			dataSourceTypeCombo.setText(old);
			dataSourceTypeCombo.setEnabled(true);
		} else {
			dataSourceTypeCombo.setItems(new String[0]);
			dataSourceTypeCombo.setEnabled(false);
		}
		
		String dsType = dataSourceTypeCombo.getText();
		if ("SIMPLE".equals(dsType)) {
			propertyNameEditor.setItems(JDBC_SIMPLE_COMBO_PROPERTIES);
		} else if ("JNDI".equals(dsType)) {
			if ("JTA".equals(type)) {
				propertyNameEditor.setItems(JTA_JNDI_PROPERTIES);
			} else {
				propertyNameEditor.setItems(JDBC_JNDI_PROPERTIES);
			}
		} else {
			propertyNameEditor.setItems(new String[0]);
		}
		
		addPropertyButton.setEnabled(dataSourceElement != null);
		removePropertyButton.setEnabled(!propertyViewer.getSelection().isEmpty());
	}
	
	private Element getDataSourceElement() {
		if (dataSourceElement == null) {
			Element element = getTmElement();
			dataSourceElement = element.getOwnerDocument().createElement("dataSource"); //$NON-NLS-1$
			element.appendChild(dataSourceElement);
			element.appendChild(element.getOwnerDocument().createTextNode("\n"));
		}
		return dataSourceElement;
	}

	private Element getTmElement() {
//		if (tmElement == null) {
//			tmElement = root.getOwnerDocument().createElement("transactionManager"); //$NON-NLS-1$
//			Element sibling = null;
//			
//			NodeList children = root.getChildNodes();
//			for (int i = 0; i < children.getLength(); i++) {
//				Node child = children.item(i);
//				if (child instanceof Element
//						&& "sqlMap".equals(((Element) child).getTagName())) { //$NON-NLS-1$
//					sibling = (Element) child;
//					break;
//				}
//			}
//			root.insertBefore(tmElement, sibling);
//			root.insertBefore(root.getOwnerDocument().createTextNode("\n"), sibling);
//		}
		return tmElement;
	}

	private void createTmElement() {
		if (tmElement == null) {
			tmElement = root.getOwnerDocument().createElement("transactionManager"); //$NON-NLS-1$
			Element sibling = null;

			NodeList children = root.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				if (child instanceof Element
						&& "sqlMap".equals(((Element) child).getTagName())) { //$NON-NLS-1$
					sibling = (Element) child;
					break;
				}
			}
			root.insertBefore(tmElement, sibling);
			root.insertBefore(root.getOwnerDocument().createTextNode("\n"), sibling);
		}
	}
	private List<Element> getProperties() {
		List<Element> ret = new LinkedList<Element>();
		if (dataSourceElement != null) {
			NodeList children = dataSourceElement.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				if (child instanceof Element
						&& "property".equals(((Element) child).getTagName())) {
					ret.add((Element) child);
				}
			}
		}
		return ret;
	}
	
	public void setElement(Element root) {
		this.root = root;
		this.tmElement = null;
		this.dataSourceElement = null;
		
		if (root != null) {
			NodeList children = root.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				if (child instanceof Element
						&& "transactionManager".equals(((Element) child).getTagName())) { //$NON-NLS-1$
					this.tmElement = (Element) child;
				}
			}
		}
		if (tmElement != null) {
			NodeList children = tmElement.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				if (child instanceof Element
						&& "dataSource".equals(((Element) child).getTagName())) { //$NON-NLS-1$
					this.dataSourceElement = (Element) child;
				}
			}
		}
		setInitialData();
	}

	private void setInitialData() {
		if (tmElement != null) {
			typeCombo.setText(StringUtil.nvl(tmElement.getAttribute("type"))); //$NON-NLS-1$
		} else {
			typeCombo.setText("");//$NON-NLS-1$
		}
		if (dataSourceElement != null) {
			dataSourceTypeCombo.setText(StringUtil.nvl(dataSourceElement.getAttribute("type"))); //$NON-NLS-1$
		} else {
			dataSourceTypeCombo.setText(""); //$NON-NLS-1$
		}
		propertyViewer.setInput(getProperties());
		updateWidgetStatus();
		chkValidation();
	}

	private static class TransactionManagerPropertyLabelProvider extends LabelProvider
	implements ITableLabelProvider {

		private TableViewer viewer;

		public TransactionManagerPropertyLabelProvider(
				TableViewer viewer) {
			//this.viewer = viewer;
			this.setViewer(viewer);
		}
		
		/**
		 * viewer 설정
		 * 
		 * @param viewer
		 */
		private void setViewer(TableViewer viewer) {
			this.viewer = viewer;
		}

		public Image getColumnImage(Object element, int columnIndex) {
			if (columnIndex == 0){
				return DBIOPlugin.getDefault().getImageDescriptor(DBIOPlugin.IMG_PROPERTY).createImage();
			}
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof Element
					&& "property".equals(((Element) element).getTagName())) { //$NON-NLS-1$
				return ((Element) element).getAttribute((String) viewer.getColumnProperties()[columnIndex]);
			}
			return null;
		}

	}

	/**
	 * 화면 구성
	 * 
	 * @param managedForm
	 * @param parent
	 * @return
	 */
	public Section createContents(IManagedForm managedForm, Composite parent) {

		Form form = managedForm.getForm().getForm();
		this.form = form;
		initErrorMessage(form);
		return super.createContents(managedForm,parent);
	}
	
	/**
	 * TmElement 에 대한 validation 체크 
	 * @param element
	 * @return
	 */	
	public void chkValidation(){ 
		initErrorMessage(form);
		if (getTmElement() != null && getTmElement().hasChildNodes()){
			if (chkPropertyNameValidation( getTmElement(),"property"))
				displayErrorMessage(form,DbioMessages.sqlmapconfig_err_PropertyName_invalid);
			if (chkPropertyValueEmpty(getTmElement(),"property"))
				displayErrorMessage(form,DbioMessages.sqlmapconfig_err_PropertyValue_empty);
			if (chkPropertyDuplicate(getTmElement(),"property"))
				displayErrorMessage(form,DbioMessages.sqlmapconfig_err_PropertyName_duplication);
		}
	}

	/**
	 * element tag 에 대한 name validation 체크.. 현재는 공란만 체크함.   
	 * @param element
	 * @param TagName
	 * @return
	 */
	public boolean chkPropertyNameValidation(Element element, String TagName){ 
		NodeList temp = element.getElementsByTagName(TagName);
		for (int i=0;i<temp.getLength();i++){
			if (temp.item(i).getAttributes().getNamedItem("name").getNodeValue().equals("")){
				return true;
			}
		}
		return false;		
	}
	/**
	 * Property Value Empty 체크
	 * @param element
	 * @param TagName
	 * @return
	 */
	public boolean chkPropertyValueEmpty(Element element, String TagName){ 
		NodeList temp = element.getElementsByTagName(TagName);
		for (int i=0;i<temp.getLength();i++){
			if (temp.item(i).getAttributes().getNamedItem("value").getNodeValue().equals("")){
				return true;
			}
		}
		return false;		
	}	
	
	/**
	 * Property 에 대한 name 중복 체크함 
	 * @param element
	 * @param TagName
	 * @return
	 */
	public boolean chkPropertyDuplicate(Element element, String TagName){ 
		NodeList temp = element.getElementsByTagName(TagName);
		for (int i=0;i<temp.getLength();i++){
			for (int j=i;j<temp.getLength();j++){
				if (temp.item(i).getAttributes().getNamedItem("name").getNodeValue().equals(
				temp.item(j).getAttributes().getNamedItem("name").getNodeValue())&& i!=j
				&& !temp.item(i).getAttributes().getNamedItem("name").getNodeValue().equals("")
				){
					return true;
				}
			}
		}
		return false;		
	}
	
	/**
	 * displayErrorMessage 중복에러메시지를 초기화함. 
	 * @param managedForm
	 * @param parent
	 */
	public void initErrorMessage(Form form) { 
		form.getToolBarManager().update(true);
		String message = null;
		message = form.getMessage();
		if (message!=null){
			message = message.replaceAll(DbioMessages.sqlmapconfig_err_PropertyName_duplication,"");
			message = message.replaceAll(DbioMessages.sqlmapconfig_err_PropertyName_invalid,"");
			message = message.replaceAll(DbioMessages.sqlmapconfig_err_PropertyValue_empty,"");
			if (message.length()==0)
				form.setMessage(message, IMessageProvider.NONE);
			else
				form.setMessage(message,IMessageProvider.ERROR);	// 기존에 에러메시지가 표시되어 있으면 삭제한다.  
		}
	}

	/**
	 * displayErrorMessage 에러메시지를 타이틀에 표시함. 
	 * @param managedForm
	 * @param parent
	 */
	public void displayErrorMessage(Form form, String message) { 
		form.getToolBarManager().update(true);
		
		if (form.getMessage()==null)
			form.setMessage(message,IMessageProvider.ERROR);	// NEW LINE
		else
			if (form.getMessage().indexOf(message)<0)
				form.setMessage(form.getMessage()+message,IMessageProvider.ERROR);	// 기존에 에러메시지가 표시되어 있지 않으면 표시한다. 
	}

}
